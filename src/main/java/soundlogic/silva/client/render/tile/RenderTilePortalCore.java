package soundlogic.silva.client.render.tile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.common.block.BlockPortalCore;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import vazkii.botania.common.block.BlockAlfPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class RenderTilePortalCore extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TilePortalCore portal = (TilePortalCore) tileentity;
		IIcon portalTex=((BlockPortalCore)ModBlocks.portalCore).portalTex;
		if(portal.getDimension() == null)
			return;
		Color color=portal.getDimension().getPortalColor();
		if(portal.getDimension() == Dimension.ALFHEIM) {
			portalTex=BlockAlfPortal.portalTex;
			color=new Color(0xFFFFFF);
		}
		float red=(float)color.getRed()/255F;
		float green=(float)color.getGreen()/255F;
		float blue=(float)color.getBlue()/255F;

		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslatef(-1F, 1F, 0.25F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.getTicksOpen()) / 60F);
		GL11.glColor4f(red, green, blue, alpha);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		if(portal.getDirection().offsetX != 0) {
			GL11.glTranslatef(1.25F, 0F, 1.75F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
		}

		renderIcon(0, 0, portalTex, 3, 3, 240);
		
		if(portal.getDirection().offsetX != 0) {
			GL11.glTranslated(0F, 0F, 0.5F);
			renderIcon(0, 0, portalTex, 3, 3, 240);
			GL11.glTranslated(0F, 0F, -0.5F);
		}

		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glTranslated(-3F, 0F, -0.5F);
		renderIcon(0, 0, portalTex, 3, 3, 240);
		if(portal.getDirection().offsetX != 0) {
			GL11.glTranslated(0F, 0F, 0.5F);
			renderIcon(0, 0, portalTex, 3, 3, 240);
			GL11.glTranslated(0F, 0F, -0.5F);
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}

	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}

}
