package soundlogic.silva.client.core.handler;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.IForestClientTick;
import soundlogic.silva.common.block.tile.TileManaEater;
import vazkii.botania.common.item.ItemTwigWand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ClientTickHandler {

	public static float ticksInGame = 0;
	public float partialTicks = 0;

	@SubscribeEvent
	public void renderTickStart(RenderTickEvent event) {
		if(event.phase == Phase.START)
			partialTicks = event.renderTickTime;
	}
	
	@SubscribeEvent
	public void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {

			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) {
				ticksInGame++;
				
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if(player != null) {
					ItemStack stack = player.getCurrentEquippedItem();
					if(stack != null && stack.getItem() instanceof ItemTwigWand) {
						for(IForestClientTick renderer : Silva.proxy.ForestWandRenderers) {
							((IForestClientTick) renderer).onClientDisplayTick();
						}
					}
				}
			}
		}
	}
}
