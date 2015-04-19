package soundlogic.silva.common.crafting.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class RecipeElvenInstantDrop extends RecipeElvenTrade {

	ItemStack autodrop;
	ItemStack autodropSearch;
	
	public RecipeElvenInstantDrop(ItemStack autodrop, ItemStack autoDropSearch) {
		super(autodrop, autoDropSearch);
		this.autodrop=autodrop;
		this.autodropSearch=autoDropSearch;
	}
	
	public boolean matches(List<ItemStack> stacks, boolean remove) {
		ItemStack toRemove = null;
		for(ItemStack stack : stacks) {
			if (stack == null)
				continue;
			if(ItemStack.areItemStacksEqual(stack, autodropSearch) && ItemStack.areItemStackTagsEqual(stack, autodropSearch)) {
				toRemove = stack;
				break;
			}
		}
		if(toRemove != null) {
			if(remove)
				stacks.remove(toRemove);
			return true;
		}
		return false;
	}
	
}
