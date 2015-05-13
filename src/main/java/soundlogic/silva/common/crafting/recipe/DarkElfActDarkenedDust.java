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
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.DustHandler;

public class DarkElfActDarkenedDust implements IDarkElfAct{

	public DarkElfActDarkenedDust() {

	}
	
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		if(DustHandler.isMagicDust(world, x, y, z)) {
			world.setBlock(x,y,z, ModBlocks.darkenedDust, 0, 3);
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
