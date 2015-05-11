package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActFurnace implements IDarkElfAct{

	public DarkElfActFurnace() {

	}
	
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		if(block != Blocks.furnace & block != Blocks.lit_furnace)
			return false;
		TileEntityFurnace tile = (TileEntityFurnace) world.getTileEntity(x, y, z);
		ItemStack stack = tile.getStackInSlot(1);
		tile.setInventorySlotContents(1, tile.getStackInSlot(2));
		tile.setInventorySlotContents(2, stack);
		tile.markDirty();
		return true;
	}

	@Override
	public List<ItemStack> getDisplayInputs() {
		return null;
	}

	@Override
	public List<ItemStack> getDisplayOutputs() {
		return null;
	}

}
