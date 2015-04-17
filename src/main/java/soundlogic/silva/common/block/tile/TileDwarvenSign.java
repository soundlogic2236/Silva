package soundlogic.silva.common.block.tile;

public class TileDwarvenSign extends TileMod{
	
	public TilePortalCore core;
	int ticks=0;
	boolean activated;
	
	@Override
	public void updateEntity() {
		ticks++;
		if(core!=null) {
			if(core.isInvalid()) {
				core=null;
			}
		}
		if(core!=null) {
			
		}
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
