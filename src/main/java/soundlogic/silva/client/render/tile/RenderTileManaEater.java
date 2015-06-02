package soundlogic.silva.client.render.tile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TileManaEater;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.common.item.lens.ItemLens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class RenderTileManaEater extends TileEntitySpecialRenderer{

	private static final ResourceLocation[] textures = new ResourceLocation[]{new ResourceLocation(LibResources.MODEL_MANA_EATER)};
	private static final ResourceLocation texture_teeth = new ResourceLocation(LibResources.MODEL_MANA_EATER_TEETH);
	
	private static final ModelSpreader model = new ModelSpreader();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks) {
		TileManaEater eater = (TileManaEater) tileentity;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glRotatef(eater.rotationX + 90F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -1F, 0F);
		GL11.glRotatef(eater.rotationY, 1F, 0F, 0F);
		GL11.glTranslatef(0F, 1F, 0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(textures[eater.getMeta()]);
		GL11.glScalef(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		model.render();
		GL11.glColor3f(1F, 1F, 1F);

		GL11.glPushMatrix();
		double worldTicks = tileentity.getWorldObj() == null ? 0 : time;
		GL11.glRotatef((float) worldTicks % 360, 0F, 1F, 0F);
		GL11.glTranslatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GL11.glPopMatrix();
		GL11.glScalef(1F, -1F, -1F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture_teeth);
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.4F, -1.4F, -0.4375F);
		GL11.glScalef(0.8F, 0.8F, 0.8F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		ItemRenderer.renderItemIn2D(Tessellator.instance, 0, 0, 1, 1, 16, 16, 1F / 16F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
	
}

