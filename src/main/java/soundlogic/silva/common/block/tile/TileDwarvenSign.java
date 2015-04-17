package soundlogic.silva.common.block.tile;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;

public class TileDwarvenSign extends TileMod{
	
	public TilePortalCore core;
	int ticks=0;
	boolean activated;
	
	@Override
	public void updateEntity() {
		ticks++;
		checkValid();
		if(core!=null) {
			
		}
	}
	
	private void checkValid() {
		if(core!=null)
			if(core.isInvalid())
				core=null;
		if(core!=null)
			if(core.getDimension()!=Dimension.NIDAVELLIR)
				core=null;
		if(core==null)
			activated=false;
	}

	public void activate() {
		if(activated) {
			activated=false;
			return;
		}
		if(core==null)
			return;
		TileDwarvenSign[] signs=core.getDwarvenSigns();
		for(TileDwarvenSign sign : signs)
			if(sign!=null)
				sign.activated=false;
		this.activated=true;
	}
	
	public boolean isActivated() {
		return activated;
	}
}
