package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActRedstoneControl implements IDarkElfAct{

	public DarkElfActRedstoneControl() {

	}
	
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.redstone_torch) {
			world.setBlock(x, y, z, Blocks.unlit_redstone_torch, 0, 3);
			return true;
		}
		if(block == Blocks.powered_comparator || block == Blocks.unpowered_comparator) {
	        block.onBlockActivated(world, x, y, z, null, 0, 0, 0, 0);
			return true;
		}
		if(block == Blocks.powered_repeater || block == Blocks.unpowered_repeater) {
	        block.onBlockActivated(world, x, y, z, null, 0, 0, 0, 0);
			return true;
		}
		if(block == Blocks.lever) {
	        block.onBlockActivated(world, x, y, z, null, 0, 0, 0, 0);
			return true;
		}
		return false;
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
