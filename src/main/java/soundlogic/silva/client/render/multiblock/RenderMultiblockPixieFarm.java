package soundlogic.silva.client.render.multiblock;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelPixie;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.block.tile.multiblocks.PixieFarmTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieData;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;

public class RenderMultiblockPixieFarm extends RenderMultiblock {

	ModelPixie model = new ModelPixie();
	
	@Override
	protected void renderCoreTick(TileMultiblockCore tile, double x, double y, double z, float pticks) {
		PixieFarmTileData data = (PixieFarmTileData) tile.getTileData();
		renderPixies(tile, data, x, y, z, pticks);
		renderPowerLevel(tile, data, x, y, z, pticks);
	}
	
	private void renderPowerLevel(TileMultiblockCore tile, PixieFarmTileData data, double x, double y, double z, float pticks) {
		RenderManager.instance.renderEngine.bindTexture(getPowerTexture());
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glTranslated(.5, 0, .5);
		int hash = ("" + tile.xCoord + tile.yCoord + tile.zCoord).hashCode();
		float power = ((float)data.getPowerLevel())/((float)data.getMaxPowerLevel());
		drawBars(Tessellator.instance, hash, power, ClientTickHandler.ticksInGame+pticks);
		GL11.glPopMatrix();
	}
	
	private void drawBars(Tessellator tessellator, int hash, float power, float time) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		if(!ShaderHelper.useShaders()) {
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			float alpha = (float) ((Math.sin(time / 20D) / 2D + 0.5) / (false ? 1D : 2D));
			GL11.glColor4f(1F, 1F, 1F, alpha + 0.183F);
		}
		ShaderHelper.useShader(ShaderHelper.pylonGlow);
		GL11.glPushMatrix();
		GL11.glTranslated(-.5, 0, -.5);
		drawBarsForSide(tessellator, hash, power, time, 0);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glRotated(90, 0, 1, 0);
		GL11.glTranslated(-.5, 0, -.5);
		drawBarsForSide(tessellator, hash, power, time, 1);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 1, 0);
		GL11.glTranslated(-.5, 0, -.5);
		drawBarsForSide(tessellator, hash, power, time, 2);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glRotated(270, 0, 1, 0);
		GL11.glTranslated(-.5, 0, -.5);
		drawBarsForSide(tessellator, hash, power, time, 3);
		GL11.glPopMatrix();
		ShaderHelper.releaseShader();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawBarsForSide(Tessellator tessellator, int hash, float power, float time, int side) {
		GL11.glPushMatrix();
		GL11.glScalef(1/64F, 1/64F, 1/64F);
		GL11.glTranslated(1.5F, 0, -.3);
		for(int i = 0 ; i < 16; i++) {
			int height = Math.round(getBarHeight(hash, power, time, side, i) * 64 * 4);
			tessellator.startDrawingQuads();
			tessellator.setBrightness(240);
			tessellator.addVertexWithUV(
					i * 4, 
					height, 
					0, 
					getMinU(hash, power, time, side, i), 
					getMaxV(hash, power, time, side, i, height));
			tessellator.addVertexWithUV(
					i * 4 + 1, 
					height, 
					0, 
					getMaxU(hash, power, time, side, i), 
					getMaxV(hash, power, time, side, i, height));
			tessellator.addVertexWithUV(
					i * 4 + 1, 
					0, 
					0, 
					getMaxU(hash, power, time, side, i), 
					getMinV(hash, power, time, side, i, height));
			tessellator.addVertexWithUV(
					i * 4, 
					0, 
					0, 
					getMinU(hash, power, time, side, i), 
					getMinV(hash, power, time, side, i, height));
			tessellator.draw();
		}
		GL11.glPopMatrix();
	}

	private double getMinU(int hash, float power, float time, int side, int i) {
		return getUScaleFactor() * getTextureOffsetX(hash, power, time, side, i);
	}

	private double getMaxU(int hash, float power, float time, int side, int i) {
		return getUScaleFactor() * (getTextureOffsetX(hash, power, time, side, i)+1);
	}

	private double getMinV(int hash, float power, float time, int side, int i, int height) {
		return getVScaleFactor(height) * getTextureOffsetY(hash, power, time, side, i);
	}

	private double getMaxV(int hash, float power, float time, int side, int i, int height) {
		return getVScaleFactor(height) * (getTextureOffsetY(hash, power, time, side, i)+1);
	}
	
	private double getUScaleFactor() {
		return 1D/(160D)/getScaleFactor();
	}
	
	private double getVScaleFactor(int height) {
		return ((double)height)/(160D)/getScaleFactor();
	}
	
	private double getScaleFactor() {
		return 0.5D;
	}
	
	private int getTextureOffsetX(int hash, float power, float time, int side, int i) {
		return (int) (((Math.sin(time * (1+power) / 10D + hash * 3 + Math.PI * 2 * Math.sin(hash*(i+1)*(side+1)))+1D)/2D*power)*40);
	}
	
	private int getTextureOffsetY(int hash, float power, float time, int side, int i) {
		return (int) (((Math.sin(time * (1+power) / 10D + hash * 2 + Math.PI * 2 * Math.sin(hash*(i+1)*(side+1)))+1D)/2D*power)*40);
	}
	
	private float getBarHeight(int hash, float power, float time, int side, int i) {
		return (float) ((Math.sin(time * (1+power) / 10D + hash + Math.PI * 2 * Math.sin(hash*(i+1)*(side+1)))+1D)/2D*power);
	}

	private void renderPixies(TileMultiblockCore tile, PixieFarmTileData data, double x, double y, double z, float pticks) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glTranslated(-tile.xCoord, -tile.yCoord, -tile.zCoord);
		for(FarmPixieData pixie : data.pixieGroup.pixies) {
			model.render(pixie, pticks);
		}
		GL11.glPopMatrix();
	}

	private ResourceLocation getPowerTexture() {
		return new ResourceLocation(LibResources.MODEL_PIXIE_POWER);
	}

}
