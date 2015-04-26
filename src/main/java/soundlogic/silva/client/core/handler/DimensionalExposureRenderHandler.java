package soundlogic.silva.client.core.handler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.lib.LibGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class DimensionalExposureRenderHandler {

	ResourceLocation blankRune = new ResourceLocation(LibResources.BLANK_RUNE);
	
	static Random random = new Random();
	
	static DimensionalExposureGUILocation inventoryGUILocation;
	static DimensionalExposureGUILocation GUIlessGUILocation;
	
	@SubscribeEvent
	public void afterDrawn(DrawScreenEvent.Post event) {
		if(event.gui instanceof InventoryEffectRenderer) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			DimensionalExposureProperties data = DimensionalEnergyHandler.getExposureData(player);
			int i = 0;
			for(Dimension dim : Dimension.values()) {
				int level = data.getExposureLevel(dim);
				if(level > 0) {
					int x = getXForSlot(i,event.gui);
					int y = getYForSlot(i,event.gui);
					drawExposureIcon(dim,level, x, y ,true , event.mouseX, event.mouseY);
					i++;
				}
			}
		}
	}

	@SubscribeEvent
	public void clientTickEnd(TickEvent.RenderTickEvent event) {
		if(event.phase == Phase.END && Minecraft.getMinecraft().currentScreen == null && !Minecraft.getMinecraft().gameSettings.hideGUI) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			DimensionalExposureProperties data = DimensionalEnergyHandler.getExposureData(player);
			int i = 0;
			for(Dimension dim : Dimension.values()) {
				int level = data.getExposureLevel(dim);
				if(level > 0) {
					int x = getXForSlot(i,null);
					int y = getYForSlot(i,null);
					drawExposureIcon(dim,level,x, y, false, -1, -1);
					i++;
				}
			}
		}
	}
	
	private static class DimensionalExposureGUILocation {
		boolean top;
		boolean left;
		int offsetX;
		int offsetY;
		boolean moduloAxisX;
		int moduloCount;
		int offsetXPerSlot;
		int offsetYPerSlot;
		public int getXForSlot(int slot) {
			return (left ? 0 : Minecraft.getMinecraft().displayWidth) + offsetX * (left ? 1 : -1) + (moduloAxisX ? slot % moduloCount * offsetXPerSlot : slot / moduloCount * offsetXPerSlot);
		}
		public int getYForSlot(int slot) {
			return (top ? 0 : Minecraft.getMinecraft().displayHeight) + offsetY * (top ? 1 : -1) + (!moduloAxisX ? slot % moduloCount * offsetYPerSlot : slot / moduloCount * offsetYPerSlot);
		}
	}
	
	public int getXForSlot(int slot,  GuiScreen gui) {
		return gui==null ? GUIlessGUILocation.getXForSlot(slot) : inventoryGUILocation.getXForSlot(slot);
	}
	public int getYForSlot(int slot,  GuiScreen gui) {
		return gui==null ? GUIlessGUILocation.getYForSlot(slot) : inventoryGUILocation.getYForSlot(slot);
	}
	
	public static void defineGUILocation(String corner, int offsetX, int offsetY, String direction, int length, boolean GUI) {
		DimensionalExposureGUILocation temp = getGUILocation(corner, offsetX, offsetY, direction, length);
		if(temp==null) {
			temp = getDefaultGUILocation(GUI);
			FMLLog.log(Level.ERROR, "[Silva] could not define gui location for dimensional exposure");
		}
		if(GUI)
			inventoryGUILocation = temp;
		else
			GUIlessGUILocation = temp;
	}
	
	private static DimensionalExposureGUILocation getGUILocation(String corner, int offsetX, int offsetY, String direction, int length) {
		DimensionalExposureGUILocation temp = new DimensionalExposureGUILocation();
		temp.offsetX = offsetX;
		temp.offsetY = offsetY;
		
		int slotOffsetX = 16;
		int slotOffsetY = 16;
		
		if(corner.equals("TOP-LEFT")) {
			temp.top=true;
			temp.left=true;
		}
		else if(corner.equals("TOP-RIGHT")) {
			temp.top=true;
			temp.left=false;
		}
		else if(corner.equals("BOTTOM-LEFT")) {
			temp.top=false;
			temp.left=true;
		}
		else if(corner.equals("BOTTOM-RIGHT")) {
			temp.top=false;
			temp.left=false;
		}
		else
			return null;
		if(direction.equals("LEFT")) {
			temp.moduloAxisX=true;
			temp.offsetXPerSlot=slotOffsetX;
			temp.offsetYPerSlot=slotOffsetY * (temp.top ? 1 : -1);
		}
		else if(direction.equals("RIGHT")) {
			temp.moduloAxisX=true;
			temp.offsetXPerSlot=-slotOffsetX;
			temp.offsetYPerSlot=slotOffsetY * (temp.top ? 1 : -1);
		}
		else if(direction.equals("UP")) {
			temp.moduloAxisX=false;
			temp.offsetXPerSlot=slotOffsetX * (temp.left ? 1 : -1);
			temp.offsetYPerSlot=-slotOffsetY;
		}
		else if(direction.equals("DOWN")) {
			temp.moduloAxisX=false;
			temp.offsetXPerSlot=slotOffsetX * (temp.left ? 1 : -1);
			temp.offsetYPerSlot=slotOffsetY;
		}
		else
			return null;
		temp.moduloCount=length;
		return temp;
	}
	
	private static DimensionalExposureGUILocation getDefaultGUILocation(boolean GUI) {
		if(GUI)
			return getGUILocation("TOP-LEFT", 16, 16, "LEFT", 6);
		else
			return getGUILocation("TOP-LEFT", 16, 16, "LEFT", 6);
	}
	
	public void drawExposureIcon(Dimension dim, int level, int x, int y, boolean gui, int mousex, int mousey) {
		drawDimensionRune(dim,x,y, gui);
		drawExposureOverlay(dim,x,y,level, gui);
		if(gui)
			drawTooltipForRune(dim,level,x,y,mousex, mousey);
	}
	
	public void drawDimensionRune(Dimension dim, int x, int y, boolean gui) {
		drawBlankRune(x,y, gui);
		drawTinyDimensionSignature(dim,x,y, gui);
	}

	public void drawBlankRune(int x, int y, boolean gui) {
		Minecraft.getMinecraft().renderEngine.bindTexture(blankRune);
		drawTexturedModalRect(x, y, 0, 0, 16, 16);
	}

	public void drawTinyDimensionSignature(Dimension dim, int x, int y, boolean gui) {
		List<boolean[][]> sigs = DimensionHandler.getSignaturesFromDimension(dim);
		if(sigs.size() == 0)
			return;
		boolean[][] signature = sigs.get(0);
		float r = (float)dim.getSparkColor().getRed()/256F;
		float g = (float)dim.getSparkColor().getGreen()/256F;
		float b = (float)dim.getSparkColor().getBlue()/256F;
		for(int i = 0; i < DimensionHandler.SIGNATURE_WIDTH ; i++) {
			for(int j = 0; j < DimensionHandler.SIGNATURE_HEIGHT ; j++) {
				if(signature[j][i]) {
					int nx = x+i*2 + 4;
					int ny = y-j*2 + 9;
					GL11.glColor4f(r, g, b, 1f);
					drawRect(nx, ny, nx+1, ny+1);
					GL11.glColor4f(1f, 1f, 1f, 1f);
				}
			}
		}
	}
	
	public void drawExposureOverlay(Dimension dim, int x, int y, int level, boolean gui) {
		float progress = ((float)level)/((float)level+20*60*2);
		drawExposurePie(x,y,progress, dim.getPortalColor());
	}
	
	public void drawExposurePie(int x, int y, float progress, Color color) {
		int r = 8;
		int nx = x + 8;
		int ny = y + 8;
		int degs = (int) (360 * progress);
		float a = 0.5F + 0.2F * ((float) Math.cos((double) ClientTickHandler.ticksInGame / 10) * 0.5F + 0.5F);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		GL11.glStencilMask(0x00);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glColor4f(0F, 0.5F, 0.5F, a);
		GL11.glVertex2i(nx, ny);
		GL11.glColor4f(0F, 1F, 0.5F, a);
		for(int i = degs; i > 0; i--) {
			double rad = (i - 90) / 180F * Math.PI;
			GL11.glVertex2d(nx + Math.cos(rad) * r, ny + Math.sin(rad) * r);
		}
		GL11.glVertex2i(nx, ny);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	public void drawTooltipForRune(Dimension dim, int level, int x, int y, int mousex, int mousey) {
		if(mousex<=x+16 && mousex>=x && mousey<=y+16 && mousey>=y) {
			List<String> tooltip=new ArrayList<String>();
			tooltip.add(StatCollector.translateToLocal(String.format(StatCollector.translateToLocal(LibGUI.ENERGY_EXPOSURE_TOOLTIP), StatCollector.translateToLocal(dim.getUnlocalizedName()))));
			tooltip.add(StatCollector.translateToLocal(StringUtils.ticksToElapsedTime(level)));
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mousex, mousey, tooltip);
		}
	}

    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        float f = 1F/16F;
        float f1 = 1F/16F;
        float zLevel=0;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + p_73729_6_), (double)zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + p_73729_6_), (double)zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + 0), (double)zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.draw();
    }

    public static void drawRect(int p_73734_0_, int p_73734_1_, int p_73734_2_, int p_73734_3_)
    {
    	float zLevel=1;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)p_73734_0_, (double)p_73734_3_, zLevel);
        tessellator.addVertex((double)p_73734_2_, (double)p_73734_3_, zLevel);
        tessellator.addVertex((double)p_73734_2_, (double)p_73734_1_, zLevel);
        tessellator.addVertex((double)p_73734_0_, (double)p_73734_1_, zLevel);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
