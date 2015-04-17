package soundlogic.silva.common.block.tile;

import soundlogic.silva.client.lib.LibResources;
import vazkii.botania.api.mana.IManaPool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TilePylon extends TileEntity{

	int ticks = 0;
	
	static boolean[] activatable=new boolean[]{
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
	};

	@Override
	public void updateEntity() {
		++ticks;
		int meta = getBlockMetadata();
		if(activatable[meta]) {
			TileEntity tileBelow=this.getWorldObj().getTileEntity(this.xCoord, this.yCoord-1, this.zCoord);
			if(tileBelow instanceof IManaPool) {
				IManaPool pool=(IManaPool)tileBelow;
			}
		}
	}
}
