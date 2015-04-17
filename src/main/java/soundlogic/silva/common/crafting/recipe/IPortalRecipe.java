package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.TilePortalCore;
import net.minecraft.item.ItemStack;

public interface IPortalRecipe {

	IPortalRecipeTransaction getTransaction(ArrayList<ItemStack> inventory,
			TilePortalCore tilePortalCore);

	List<ItemStack> getOutput();

	List<Object> getInputs();

}
