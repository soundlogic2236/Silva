package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelPylon;
import soundlogic.silva.client.model.ModelSlingshot;
import soundlogic.silva.common.block.tile.TileSlingshot;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileSlingshot extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SLINGSHOT);

	static RenderItem renderItem=null;
	
	ModelSlingshot model = new ModelSlingshot();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ticks) {
		TileSlingshot tile = (TileSlingshot) tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glRotatef(tile.rotation, 0, 1, 0);
		model.render();

		GL11.glPopMatrix();
		
		if(tile.getStackInSlot(0)!=null) {
            GL11.glPushMatrix();
    		GL11.glTranslatef(0F, 0.15F, 0F);
            float renderTicks = ClientTickHandler.ticksInGame + ticks;
            float rotationAngle = renderTicks * 5F;
			EntityItem entity = new EntityItem(tile.getWorldObj());
			entity.hoverStart = 0.0F;
			entity.setEntityItemStack(tile.getStackInSlot(0));
			int meta = tile.getBlockMetadata();
			float offsetX=0;
			float offsetY=0;
			float offsetZ=0;

			float displacement = -0.2F;
			
            GL11.glTranslatef((float) x + 0.5F, (float) y + displacement + 0.5F, (float) z + 0.5F);
           	GL11.glTranslatef(offsetX,offsetY,offsetZ);
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

            if(renderItem==null) {
				renderItem = new RenderItem();
				renderItem.setRenderManager(RenderManager.instance);
			}
    		GL11.glScalef(.5F, .5F, .5F);
			renderItem.doRender(entity,0,0,0,0,0);
			GL11.glPopMatrix();

		}
	}
}
