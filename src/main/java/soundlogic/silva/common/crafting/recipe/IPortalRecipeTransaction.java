package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.tile.TilePortalCore;

public interface IPortalRecipeTransaction {

	public void removeItems(ArrayList<ItemStack> inventory);
	public void doTransaction(TilePortalCore tilePortalCore);
	public List<ItemStack> getOutput();

}
