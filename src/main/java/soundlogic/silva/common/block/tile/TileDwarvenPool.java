package soundlogic.silva.common.block.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;

public class TileDwarvenPool extends TilePool{

	public static final int MAX_MANA=TilePool.MAX_MANA * 2;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		ManaNetworkEvent.removePool(this);
	}
}
