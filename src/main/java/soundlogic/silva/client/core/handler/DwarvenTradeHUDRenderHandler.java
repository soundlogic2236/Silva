package soundlogic.silva.client.core.handler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.render.tile.RenderTileDwarvenSign;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.lib.LibGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class DwarvenTradeHUDRenderHandler {

	private static final String check_mark = EnumChatFormatting.GREEN+"\u2714";
	private static final String x_mark = EnumChatFormatting.RED+"\u2718";
	
	@SubscribeEvent
	public void clientTickEnd(TickEvent.RenderTickEvent event) {
		if(event.phase == Phase.END && Minecraft.getMinecraft().currentScreen == null && !Minecraft.getMinecraft().gameSettings.hideGUI) {
			if(RenderTileDwarvenSign.hasTooltip) {
				RenderTileDwarvenSign.hasTooltip=false;
				vazkii.botania.client.core.helper.RenderHelper.renderTooltip(
						(int)(Minecraft.getMinecraft().displayWidth/4),
						(int)(Minecraft.getMinecraft().displayHeight/4),
						RenderTileDwarvenSign.tooltipData);
				if(RenderTileDwarvenSign.tooltipInput) {
					FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
					vazkii.botania.client.core.helper.RenderHelper.renderTooltip(
							Minecraft.getMinecraft().displayWidth/4 - fontRenderer.getStringWidth(" ")*8, 
							Minecraft.getMinecraft().displayHeight/4,
							Arrays.asList(RenderTileDwarvenSign.heldItemValid ? check_mark : x_mark));
				}
			}
		}
	}
}
