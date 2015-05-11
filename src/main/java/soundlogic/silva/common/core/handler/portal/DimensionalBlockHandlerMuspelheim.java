package soundlogic.silva.common.core.handler.portal;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;

public class DimensionalBlockHandlerMuspelheim implements IDimensionalBlockHandler{

	@Override
	public void init(Dimension dim) {
		// NO OP
	}

	@Override
	public int frequencyForSearch(TilePortalCore core) {
		return 30;
	}

	@Override
	public boolean shouldTryApply(TilePortalCore core) {
		return true;
	}

	@Override
	public int getBlocksPerTick(TilePortalCore core) {
		return 1;
	}

	@Override
	public int triesPerBlock(TilePortalCore core) {
		return 4;
	}

	@Override
	public boolean tryApplyToBlock(TilePortalCore core, World world, int[] coords) {
		Block block = world.getBlock(coords[0], coords[1]-1, coords[2]);
		if(block.isFlammable(world, coords[0], coords[1]-1, coords[2], ForgeDirection.UP) && world.isAirBlock(coords[0],coords[1],coords[2])) {
			world.setBlock(coords[0], coords[1], coords[2], Blocks.fire,0,3);
			return true;
		}
		return false;
	}

	@Override
	public void generalTick(TilePortalCore core) {
		// NO OP
	}

	@Override
	public AxisAlignedBB modifyBoundingBox(TilePortalCore core,
			AxisAlignedBB aabb) {
		return aabb;
	}

}
