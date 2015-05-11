package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActNoteBlock implements IDarkElfAct{

	public DarkElfActNoteBlock() {

	}
	
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		if(block != Blocks.noteblock)
			return false;
		TileEntityNote tile = (TileEntityNote) world.getTileEntity(x, y, z);
		tile.changePitch();
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
