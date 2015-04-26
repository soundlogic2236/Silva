package soundlogic.silva.common.core.handler.portal;

import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;

public interface IDimensionalBlockHandler {

	public void init(Dimension dim);
	public boolean shouldTryApply(TilePortalCore core);
	public int getBlocksPerTick(TilePortalCore core);
	public int triesPerBlock(TilePortalCore core);
	public boolean canApplyToBlock(TilePortalCore core, World world, int[] coords);
	public void applyToBlock(TilePortalCore core, World world, int[] coords);
}
