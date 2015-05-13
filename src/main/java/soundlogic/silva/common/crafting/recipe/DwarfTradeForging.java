package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.DwarfForgedHandler;
import soundlogic.silva.common.item.ItemProxy;
import soundlogic.silva.common.item.ModItems;

public class DwarfTradeForging extends DwarfTrade{

	private final String TAG_EXTRA_COST_SIZE = "extraCostSize";
	private final String TAG_EXTRA_COST = "extraCost";
	private final String TAG_EXTRA_COST_STRING = "extraCostString";
	
	int cost;
	ItemStack result;
	List<Object> extraCost;
	List<Object> inputs;
	int requiredRep;
	int repBoost;
	int maxRep;
	
	public DwarfTradeForging(ItemStack result, int cost, int requiredRep, int repBoost, int maxRep, Object... inputs) {
		this.cost = cost;
		this.result=result;

		List<Object> inputsToSet = new ArrayList();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}
		this.inputs=inputsToSet;

		this.requiredRep=requiredRep;
		this.repBoost=repBoost;
		this.maxRep=maxRep;
	}

	@Override
	public IPortalRecipeTransaction getTransaction(
			ArrayList<ItemStack> inventory, TilePortalCore tilePortalCore) {
		if(!this.canApply(tilePortalCore))
			return null;
		ArrayList<Object> inputsCompressed = new ArrayList(extraCost);
		inputsCompressed.addAll(inputs);
		ArrayList<Object> inputsMissing = new ArrayList();
		for(Object o : inputsCompressed) {
			if(o instanceof String)
				inputsMissing.add(o);
			else {
				ItemStack stack=((ItemStack) o).copy();
				int size=stack.stackSize;
				stack.stackSize=1;
				for(int i=0;i<size;i++) {
					inputsMissing.add(stack);
				}
			}
		}
		ArrayList<ItemStack> stacksToRemove = new ArrayList();
		
		for(ItemStack stack : inventory) {
			if(stack == null) {
				continue;
			}
			if(inputsMissing.isEmpty())
				break;
			int i=0;
			boolean match=false;
			while(i < inputsMissing.size()) {
				if(stackMatches(stack,inputsMissing.get(i)))
				{
					stacksToRemove.add(stack);
					match=true;
					break;
				}
				i++;
			}
			if(match)
				inputsMissing.remove(i);
		}
		
		ArrayList<ItemStack> result=new ArrayList<ItemStack>();
		result.add(DwarfForgedHandler.dwarfForgeStack(this.result.copy()));
		
		if(inputsMissing.isEmpty())
			return new DwarvenTradeTransaction(result,stacksToRemove,this.getRepBoost(),this.getMaxRep());
		
		return null;
	}
	

	protected int getMaxRep() {
		return this.maxRep;
	}

	protected int getRepBoost() {
		return this.repBoost;
	}

	private boolean stackMatches(ItemStack stack, Object searchTarget) {
		if(searchTarget instanceof String) {
			List<ItemStack> validStacks = OreDictionary.getOres((String) searchTarget);
			for(ItemStack test : validStacks) {
				ItemStack temp=test.copy();
				if(temp.getItemDamage() == Short.MAX_VALUE)
					temp.setItemDamage(stack.getItemDamage());
				if(stack.isItemEqual(temp) && stack.getItemDamage() == temp.getItemDamage()) {
					return true;
				}
			}
		}
		if(searchTarget instanceof ItemStack) {
			return stack.getItem() == ((ItemStack) searchTarget).getItem() && stack.getItemDamage() == ((ItemStack) searchTarget).getItemDamage();
		}
		return false;
	}

	@Override
	public List<ItemStack> getOutput() {
		ArrayList<ItemStack> result=new ArrayList<ItemStack>();
		result.add(DwarfForgedHandler.dwarfForgeStack(this.result.copy()));
		return result;
	}

	@Override
	public List<Object> getInputs() {
		ArrayList<Object> result=new ArrayList<Object>(inputs);
		if(extraCost!=null)
			result.addAll(extraCost);
		return result;
	}

	@Override
	public boolean isReputationSufficent(int reputation) {
		return reputation>=this.requiredRep;
	}

	@Override
	public void prepRecipe(int reputation) {
		extraCost=getExtraCosts(cost);
	}

	@Override
	public void writeNBTData(NBTTagCompound cmp) {
		cmp.setInteger(TAG_EXTRA_COST_SIZE, extraCost.size());
		for(int i=0;i<extraCost.size();i++) {
			Object o = extraCost.get(i);
			if(o instanceof String) {
				cmp.setBoolean(TAG_EXTRA_COST_STRING+i, true);
				cmp.setString(TAG_EXTRA_COST+i, (String)o);
			}
			else {
				cmp.setBoolean(TAG_EXTRA_COST_STRING+i, false);
				NBTTagCompound stack=new NBTTagCompound();
				((ItemStack)o).writeToNBT(stack);
				cmp.setTag(TAG_EXTRA_COST+i, stack);
			}
		}
	}

	@Override
	public void readNBTData (NBTTagCompound cmp) {
		extraCost=new ArrayList<Object>();
		int size=cmp.getInteger(TAG_EXTRA_COST_SIZE);
		for(int i=0;i<size;i++) {
			boolean str=cmp.getBoolean(TAG_EXTRA_COST_STRING+i);
			if(str)
				extraCost.add(cmp.getString(TAG_EXTRA_COST+i));
			else {
				ItemStack stack=ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_EXTRA_COST+i));
				extraCost.add(stack);
			}
		}
	}

	@Override
	public boolean doesStackMatchSlotForDisplay(ItemStack stack, int slot) {
		Object target = getInputs().get(slot);
		return stackMatches(stack, target);
	}

}
