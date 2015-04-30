package soundlogic.silva.common.block.tile;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import net.minecraft.item.ItemStack;

public class TilePortalUpgradeInhibit extends TileMod implements IPortalUpgrade{

	public TilePortalCore core;
	
	@Override
	public boolean isPortalUpgrade() {
		return true;
	}

	@Override
	public boolean permitBlockExposureTicks() {
		return core.getDimension()==Dimension.SVARTALFHEIM;
	}

	@Override
	public boolean permitEntityExposureTicks() {
		return false;
	}

	@Override
	public boolean permitTrades() {
		return true;
	}

	@Override
	public boolean permitItemsThroughPortal() {
		return true;
	}

	@Override
	public void onTick() {
		// NO OP
	}

	@Override
	public void onItemThroughPortal(ItemStack stack) {
		// NO OP
	}

	@Override
	public void setPortalCore(TilePortalCore core) {
		this.core=core;
	}

}
