package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.tile.TilePortalCore;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class AlfheimTransactionWrapper implements IPortalRecipeTransaction{

	RecipeElvenTrade recipe;
	
	public AlfheimTransactionWrapper(RecipeElvenTrade recipe) {
		this.recipe=recipe;
	}
	
	public void removeItems(ArrayList<ItemStack> inventory) {
		recipe.matches(inventory, true);
	}
	public void doTransaction(TilePortalCore tilePortalCore) {
		// NO-OP
	}
	public List<ItemStack> getOutput() {
		ArrayList<ItemStack> output= new ArrayList<ItemStack>();
		output.add(recipe.getOutput().copy());
		return output;
	}
}
