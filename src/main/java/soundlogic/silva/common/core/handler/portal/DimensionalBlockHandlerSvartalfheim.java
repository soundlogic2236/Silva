package soundlogic.silva.common.core.handler.portal;

import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;

public class DimensionalBlockHandlerSvartalfheim implements IDimensionalBlockHandler{

	@Override
	public void init(Dimension dim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int frequencyForSearch(TilePortalCore core) {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public boolean shouldTryApply(TilePortalCore core) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBlocksPerTick(TilePortalCore core) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int triesPerBlock(TilePortalCore core) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean tryApplyToBlock(TilePortalCore core, World world, int[] coords) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generalTick(TilePortalCore core) {
		// TODO Auto-generated method stub
		
	}

}
