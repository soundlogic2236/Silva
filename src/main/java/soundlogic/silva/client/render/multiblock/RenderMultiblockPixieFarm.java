package soundlogic.silva.client.render.multiblock;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.model.ModelPixie;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.Pixie;
import soundlogic.silva.common.block.tile.multiblocks.PixieFarmTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityPixie;

public class RenderMultiblockPixieFarm extends RenderMultiblock {

	ShaderCallback callback = new ShaderCallback() {

		@Override
		public void call(int shader) {
			// Frag Uniforms
			int disfigurationUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "disfiguration");
			ARBShaderObjects.glUniform1fARB(disfigurationUniform, 0.025F);

			// Vert Uniforms
			int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "grainIntensity");
			ARBShaderObjects.glUniform1fARB(grainIntensityUniform, 0.05F);
		}
	};
	
	ModelPixie model = new ModelPixie();
	
	@Override
	protected void renderCoreTick(TileMultiblockCore tile, double x, double y, double z, float pticks) {
		PixieFarmTileData data = (PixieFarmTileData) tile.getTileData();
		
		RenderManager.instance.renderEngine.bindTexture(getPixieTexture());
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glTranslated(-tile.xCoord, -tile.yCoord, -tile.zCoord);
		for(Pixie pixie : data.pixies) {
			renderPixie(pixie, pticks);
		}
		GL11.glPopMatrix();
	}

	private void renderPixie(Pixie pixie, float pticks) {
		GL11.glPushMatrix();
		GL11.glTranslated(
				pixie.prevPosX + (pixie.posX-pixie.prevPosX)*pticks, 
				pixie.prevPosY + (pixie.posY-pixie.prevPosY)*pticks, 
				pixie.prevPosZ + (pixie.posZ-pixie.prevPosZ)*pticks);
		GL11.glRotatef(pixie.prevRotation + (pixie.rotation-pixie.prevRotation)*pticks+180, 0, 1, 0);
		model.render(pixie, pticks, 0.0625F);
		GL11.glPopMatrix();
		RenderGlobal.drawOutlinedBoundingBox(pixie.getBoundingBox(pixie.posX, pixie.posY, pixie.posZ), 16777215);
	}
	
	private ResourceLocation getPixieTexture() {
		return new ResourceLocation(LibResources.MODEL_PIXIE);
	}

}
