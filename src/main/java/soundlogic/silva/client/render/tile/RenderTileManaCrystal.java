package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelManaCrystal;
import soundlogic.silva.client.model.ModelPylon;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileManaCrystal extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(vazkii.botania.client.lib.LibResources.MODEL_PYLON);

	ModelManaCrystal model;

	public static int metadata;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		if(model == null)
			model = new ModelManaCrystal();

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

			GL11.glTranslated(d0 + 0.2, d1 + 0.05, d2 + 0.8);
			float scale = 0.6F;
			GL11.glScalef(scale, 0.6F, scale);

			GL11.glPushMatrix();
			GL11.glTranslated(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

			GL11.glTranslatef(0.5F, 0F, -0.5F);

			GL11.glRotatef((float) -worldTime, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, 0F, 0.5F);


			GL11.glDisable(GL11.GL_CULL_FACE);
			model.render();

			GL11.glColor4f(1F, 1F, 1F, 1F);
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
			GL11.glTranslatef(-0.05F, -0.1F, 0.05F);

			ShaderHelper.useShader(ShaderHelper.pylonGlow);
			model.render();
			ShaderHelper.releaseShader();

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
	}
}
