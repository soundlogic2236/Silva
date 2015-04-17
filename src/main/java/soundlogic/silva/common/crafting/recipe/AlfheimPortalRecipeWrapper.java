package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.tile.TilePortalCore;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class AlfheimPortalRecipeWrapper implements IPortalRecipe {

	@Override
	public IPortalRecipeTransaction getTransaction(
			ArrayList<ItemStack> inventory, TilePortalCore tilePortalCore) {
		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			if(recipe.matches(inventory, false)) {
				return new AlfheimTransactionWrapper(recipe);
			}
		}
		return null;
	}

	@Override
	public List<ItemStack> getOutput() {
		return null;
	}

	@Override
	public List<Object> getInputs() {
		return null;
	}

}
