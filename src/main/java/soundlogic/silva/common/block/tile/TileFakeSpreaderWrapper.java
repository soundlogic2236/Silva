package soundlogic.silva.common.block.tile;

import net.minecraft.world.World;
import vazkii.botania.common.block.tile.mana.TileSpreader;

public class TileFakeSpreaderWrapper extends TileSpreader{

	ICustomSpreader spreader;
	
	public TileFakeSpreaderWrapper(ICustomSpreader spreader, int x, int y, int z, float rotx, float roty) {
		this.spreader = spreader;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.rotationX = rotx;
		this.rotationY = roty;
	}
	
	@Override
	public World getWorldObj() {
		return spreader.getWorldObj();
	}
	
}
