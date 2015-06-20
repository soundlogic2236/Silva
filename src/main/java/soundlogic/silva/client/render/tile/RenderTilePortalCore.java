package soundlogic.silva.client.render.tile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.common.block.BlockPortalCore;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import vazkii.botania.common.block.BlockAlfPortal;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderTilePortalCore extends TileEntitySpecialRenderer {

	static RenderBlocks renderBlock=null;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		TilePortalCore portal = (TilePortalCore) tileentity;
		
		if(MinecraftForgeClient.getRenderPass()==0)
			renderInside(portal, d0, d1, d2, pticks);
		else if( MinecraftForgeClient.getRenderPass()==1) {
			if(portal.getDimension() != null) {
				renderPortal(portal, d0, d1, d2, pticks);
			}
		}
	}
	
	private void renderPortal(TilePortalCore portal, double d0, double d1, double d2, float pticks) {
		IIcon portalTex=((BlockPortalCore)ModBlocks.portalCore).portalTex;
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
		float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + pticks) / 8D) + 1D) / 7D + 0.6D) * ((60-portal.portalOpeningTimer) / 60F);
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
	
	private void renderInside(TilePortalCore portal, double d0, double d1, double d2, float pticks) {
		float floatProgress;
		int progressDirection = 0;
		int ticksProgress;
		if(portal.getDimension()==null) {
			floatProgress = portal.portalClosingTimer/60F;
			ticksProgress = 60-portal.portalClosingTimer;
			if(floatProgress>0)
				progressDirection=-1;
		}
		else {
			floatProgress = 1F-portal.portalOpeningTimer/60F;
			ticksProgress = 60-portal.portalOpeningTimer;
			if(floatProgress<1)
				progressDirection=1;
		}
		int ticks = portal.getTicksOpen();
		float size = .35F;
		float height = floatProgress * .25F + .25F;

		if(renderBlock==null) {
        	renderBlock = new RenderBlocks();
		}
		
		renderBlock=RenderBlocks.getInstance();
		
		int hash = ("" + portal.xCoord + portal.yCoord + portal.zCoord).hashCode();
		
		float baseSpeed = .05F;
		
		float secondSpeed = .05F;
		
		float motion = ( (ticks - ticksProgress) + (ticksProgress+pticks) * floatProgress ) * (baseSpeed);
		
		double ang0, ang1, ang2;
		
		double p1a = hash % 19;
		hash/=2;
		double p1b = hash % 19;
		hash/=2;
		double p2a = hash % 19;
		hash/=2;
		double p2b = hash % 19;
		hash/=2;
		double p3a = hash % 19;
		hash/=2;
		double p3b = hash % 19;
		
		double v1 = Math.sin(( motion + p1a ) ) * (2 * Math.PI);
		double v2 = Math.sin(( motion + p2a ) ) * (2 * Math.PI);
		double v3 = Math.sin(( motion + p3a ) ) * (2 * Math.PI);
		
		double a1 = Math.sin( motion + p1b + v1 * p1a * secondSpeed );
		double a2 = Math.sin( motion + p2b + v2 * p2a * secondSpeed );
		double a3 = Math.sin( motion + p3b + v3 * p3a * secondSpeed );
		
		ang0 = a1 * 180 / Math.PI;
		ang1 = a2 * 180 / Math.PI;
		ang2 = a3 * 180 / Math.PI;
		
		ang0 = MathHelper.wrapAngleTo180_double(ang0) * floatProgress;
		ang1 = MathHelper.wrapAngleTo180_double(ang1) * floatProgress;
		ang2 = MathHelper.wrapAngleTo180_double(ang2) * floatProgress;

		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslatef(.5F, height, .5F);
		if(ang0!=0)
			GL11.glRotated(ang0, 1F, 0F, 0F);
		if(ang1!=0)
			GL11.glRotated(ang1, 0F, 1F, 0F);
		if(ang2!=0)
			GL11.glRotated(ang2, 0F, 0F, 1F);
		GL11.glScaled(size, size, size);
		GL11.glTranslatef(-.5F, -.5F, -.5F);
		if(portal.getDimension()==null)
			renderBlock.overrideBlockTexture = ((BlockPortalCore)ModBlocks.portalCore).iconOff;
		else
			renderBlock.overrideBlockTexture = ((BlockPortalCore)ModBlocks.portalCore).iconOn;
		renderBlock.setRenderBounds(0, 0, 0, 1, 1, 1);
		renderBlock();
		renderBlock.clearOverrideBlockTexture();
		GL11.glPopMatrix();
		
	}
	
	private void renderBlock() {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderBlock.renderFaceYNeg(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlock.renderFaceYPos(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderBlock.renderFaceZNeg(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlock.renderFaceZPos(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlock.renderFaceXNeg(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlock.renderFaceXPos(Blocks.stone, 0.0D, 0.0D, 0.0D, null);
        tessellator.draw();
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
