package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DwarfTradeSimple extends DwarfTrade{

	List<ItemStack> outputs;
	List<Object> inputs;
	int requiredRep;
	int repBoost;
	int maxRep;
	
	public DwarfTradeSimple(List<ItemStack> outputs, int requiredRep, int repBoost, int maxRep , Object... inputs) {
		this.outputs = outputs;

		List<Object> inputsToSet = new ArrayList();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet;
		this.requiredRep=requiredRep;
		this.repBoost=repBoost;
		this.maxRep=maxRep;
	}

	public DwarfTradeSimple(ItemStack output, int requiredRep, int repBoost, int maxRep, Object... inputs) {
		this(Arrays.asList(new ItemStack[]{output}),requiredRep,repBoost,maxRep,inputs);
	}
	
	@Override
	public IPortalRecipeTransaction getTransaction(
			ArrayList<ItemStack> inventory, TilePortalCore tilePortalCore) {
		if(!this.canApply(tilePortalCore))
			return null;
		ArrayList<Object> inputsMissing = new ArrayList(inputs);
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
		
		if(inputsMissing.isEmpty())
			return new DwarvenTradeTransaction(this.getOutput(),stacksToRemove,this.getRepBoost(),this.getMaxRep());
		
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
		return outputs;
	}

	@Override
	public List<Object> getInputs() {
		return inputs;
	}

	@Override
	public boolean isReputationSufficent(int reputation) {
		return reputation>=this.requiredRep;
	}

}
