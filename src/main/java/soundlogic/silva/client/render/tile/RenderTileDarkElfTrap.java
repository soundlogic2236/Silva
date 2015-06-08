package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelBoomMoss;
import soundlogic.silva.client.model.ModelDarkElfTrap;
import soundlogic.silva.common.block.BlockPortalUpgradeCharge;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelPool;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class RenderTileDarkElfTrap extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_DARK_ELF_TRAP);
	private static final ResourceLocation crystalTexture = new ResourceLocation(vazkii.botania.client.lib.LibResources.MODEL_PYLON);

	static RenderItem renderItem=null;
	Random random = new Random();
	private static final ModelDarkElfTrap model = new ModelDarkElfTrap();
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float ticks) {
		TileDarkElfTrap tile = (TileDarkElfTrap) p_147500_1_;
		
		if(tile.isCharged())
			return;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		model.renderBase();
		model.renderSpikes();
		for(int i = 0 ; i < 2; i++) {
			int color = tile.getColorForBowl(i);
			if(color!=-1) {
		        float red = (float)(color >> 16 & 255) / 255.0F;
		        float green = (float)(color >> 8 & 255) / 255.0F;
		        float blue = (float)(color & 255) / 255.0F;
				GL11.glColor3f(red, green, blue);
				model.renderBowls(i);
			}
		}
		GL11.glColor3f(1F, 1F, 1F);

		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		float scale = 1F/6F;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(crystalTexture);

		double worldTime = tile.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + ticks);

		if(tile != null)
			worldTime += new Random(tile.xCoord ^ tile.yCoord ^ tile.zCoord).nextInt(360);

		GL11.glTranslated(x + 0.2, y + 0.35, z + 0.8);
		GL11.glScalef(.6F, .6F, .6F);

//		GL11.glTranslated(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0F, -0.5F);

		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(180, 1F, 0F, 0F);
		GL11.glRotatef((float) -worldTime, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, 0F, 0.5F);


		GL11.glDisable(GL11.GL_CULL_FACE);
		model.renderCrystal();

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
		model.renderCrystal();
		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		
		if(tile.getLoot()!=null) {
            GL11.glPushMatrix();
    		GL11.glTranslatef(0F, 0.15F, 0F);
            float renderTicks = ClientTickHandler.ticksInGame + ticks;
            float rotationAngle = renderTicks * 5F;
			EntityItem entity = new EntityItem(tile.getWorldObj());
			entity.hoverStart = 0.0F;
			entity.setEntityItemStack(tile.getLoot());
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
