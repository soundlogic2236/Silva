package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelManaCrystal;
import soundlogic.silva.client.model.ModelManaPotato;
import soundlogic.silva.client.model.ModelPylon;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileManaPotato extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_MANA_POTATO);
	private static final ResourceLocation textureOverlay = new ResourceLocation(LibResources.MODEL_MANA_POTATO_OVERLAY);
	private static final ModelManaPotato model = new ModelManaPotato();

	public static int metadata;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		if(tileentity.getWorldObj() != null) {
			metadata = tileentity.getBlockMetadata();
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		double worldTime = tileentity.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + pticks);

		if(tileentity != null)
			worldTime += new Random(tileentity.xCoord ^ tileentity.yCoord ^ tileentity.zCoord).nextInt(360);

		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);

		GL11.glDisable(GL11.GL_CULL_FACE);
		model.render();

		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		if(metadata==0) {

			if(!ShaderHelper.useShaders()) {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				float alpha = (float) ((Math.sin(worldTime / 20D) / 2D + 0.5) / (false ? 1D : 2D));
				GL11.glColor4f(1F, 1F, 1F, alpha + 0.183F);
			}
	
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			GL11.glTranslatef(0F, -0.12F, 0F);
			GL11.glColor4f(0F, 0F, 1F, .2F);
	
			Minecraft.getMinecraft().renderEngine.bindTexture(textureOverlay);
			ShaderHelper.useShader(ShaderHelper.manaPool);
			model.render();
			ShaderHelper.releaseShader();
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
