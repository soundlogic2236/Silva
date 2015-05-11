package soundlogic.silva.common.crafting.recipe;

import java.util.List;

import soundlogic.silva.common.block.tile.TilePortalCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDarkElfAct {

	boolean tryApplyToBlock(World world, int x, int y, int z, TilePortalCore core);
	List<ItemStack> getDisplayInputs();
	List<ItemStack> getDisplayOutputs();
}
