package soundlogic.silva.common.block.tile;

import soundlogic.silva.client.lib.LibResources;
import vazkii.botania.api.mana.IManaPool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TilePylon extends TileEntity{

	int ticks = 0;

	@Override
	public void updateEntity() {
		++ticks;
	}
}
