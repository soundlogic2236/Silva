package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockPortalUpgradeCharge;
import soundlogic.silva.common.block.ModBlocks;
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

public class RenderTilePortalUpgradeCharge extends TileEntitySpecialRenderer {

	static RenderItem renderItem=null;
	Random random = new Random();
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float ticks) {
		TilePortalUpgradeCharge tile = (TilePortalUpgradeCharge) p_147500_1_;
		if(tile.getStackInSlot(0)!=null) {
            GL11.glPushMatrix();
            float renderTicks = ClientTickHandler.ticksInGame + ticks;
            float rotationAngle = renderTicks * 5F;
			EntityItem entity = new EntityItem(tile.getWorldObj());
			entity.hoverStart = 0.0F;
			entity.setEntityItemStack(tile.getStackInSlot(0));
			int meta = tile.getBlockMetadata();
			float offsetX=0;
			float offsetY=0;
			float offsetZ=0;
			switch(meta) {
			case 0:
				offsetY=-.25F;
				break;
			case 1:
				offsetY=.25F;
				break;
			case 2:
				offsetZ=-.25F;
				break;
			case 3:
				offsetZ=.25F;
				break;
			case 4:
				offsetX=-.25F;
				break;
			case 5:
				offsetX=.25F;
				break;
			}

			float displacement = -0.2F;
			
            GL11.glTranslatef((float) x + 0.5F, (float) y + displacement + 0.5F, (float) z + 0.5F);
           	GL11.glTranslatef(offsetX,offsetY,offsetZ);
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

            if(renderItem==null) {
				renderItem = new RenderItem();
				renderItem.setRenderManager(RenderManager.instance);
			}
			renderItem.doRender(entity,0,0,0,0,0);
			GL11.glPopMatrix();

		}
    }
}
