package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class PortalRecipeSimple implements IPortalRecipe {

	List<ItemStack> outputs;
	List<Object> inputs;
	
	public PortalRecipeSimple(List<ItemStack> outputs, Object... inputs) {
		this.outputs = outputs;

		List<Object> inputsToSet = new ArrayList();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet;
	}

	public PortalRecipeSimple(ItemStack output, Object... inputs) {
		this(Arrays.asList(new ItemStack[]{output}),inputs);
	}
	
	@Override
	public IPortalRecipeTransaction getTransaction(
			ArrayList<ItemStack> inventory, TilePortalCore tilePortalCore) {
		ArrayList<Object> inputsMissing = new ArrayList();
		for(Object o : inputs) {
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
		
		if(inputsMissing.isEmpty())
			return new PortalRecipeTransaction(this.getOutput(),stacksToRemove);
		
		return null;
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

}
