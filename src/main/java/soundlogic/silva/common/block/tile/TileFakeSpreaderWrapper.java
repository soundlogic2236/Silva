package soundlogic.silva.common.block.tile;

import net.minecraft.world.World;
import vazkii.botania.common.block.tile.mana.TileSpreader;

public class TileFakeSpreaderWrapper extends TileSpreader{

	TileManaEater eater;
	
	public TileFakeSpreaderWrapper(TileManaEater eater) {
		this.eater = eater;
		this.xCoord = eater.xCoord;
		this.yCoord = eater.yCoord;
		this.zCoord = eater.zCoord;
		this.rotationX = eater.rotationX;
		this.rotationY = eater.rotationY;
	}
	
	@Override
	public World getWorldObj() {
		return eater.getWorldObj();
	}
	
}
