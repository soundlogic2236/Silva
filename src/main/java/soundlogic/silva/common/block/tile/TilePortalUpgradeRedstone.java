package soundlogic.silva.common.block.tile;

import net.minecraft.item.ItemStack;

public class TilePortalUpgradeRedstone extends TileMod implements IPortalUpgrade{

	public TilePortalCore core;
	
	@Override
	public boolean isPortalUpgrade() {
		return true;
	}

	@Override
	public boolean permitBlockExposureTicks() {
		return true;
	}

	@Override
	public boolean permitEntityExposureTicks() {
		return true;
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
		if(core.getDimension()==null) {
			if(worldObj.isBlockIndirectlyGettingPowered(xCoord,yCoord,zCoord)) {
				core.tryOpenPortal();
			}
		}
		else if(!worldObj.isBlockIndirectlyGettingPowered(xCoord,yCoord,zCoord)) {
			core.closePortal();
		}
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
