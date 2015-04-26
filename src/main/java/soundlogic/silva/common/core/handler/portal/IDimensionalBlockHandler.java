package soundlogic.silva.common.core.handler.portal;

import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;

public interface IDimensionalBlockHandler {

	public void init(Dimension dim);
	public int frequencyForSearch(TilePortalCore core);
	public boolean shouldTryApply(TilePortalCore core);
	public int getBlocksPerTick(TilePortalCore core);
	public int triesPerBlock(TilePortalCore core);
	public boolean tryApplyToBlock(TilePortalCore core, World world, int[] coords);
}
