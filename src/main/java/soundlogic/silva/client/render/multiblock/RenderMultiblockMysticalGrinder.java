package soundlogic.silva.client.render.multiblock;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataMysticalGrinder;
import soundlogic.silva.common.block.tile.multiblocks.MysticalGrinderTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;

public class RenderMultiblockMysticalGrinder extends RenderMultiblock {

	static RenderItem renderItem=null;
	static RenderBlocks renderBlock=null;
	@Override
	protected void renderCoreTick(TileMultiblockCore tile, double x, double y, double z, float ticks) {
		MysticalGrinderTileData data = (MysticalGrinderTileData) tile.getTileData();
		MultiblockDataMysticalGrinder grinder = (MultiblockDataMysticalGrinder) tile.getData();
		EntityItem entity = new EntityItem(tile.getWorldObj());
		entity.hoverStart = 0.0F;
        if(renderItem==null) {
			renderItem = new RenderItem();
			renderItem.setRenderManager(RenderManager.instance);
		}
	    float renderTicks = ClientTickHandler.ticksInGame + ticks;
        float rotationAngle = renderTicks * 5F;
	    GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        for(int i = 0 ; i < data.weedReference.length ; i++) {
        	int reference = data.weedReference[i];
        	if(reference==-1)
        		continue;
			ItemStack stack = data.stacks[reference];
		    GL11.glPushMatrix();
		    int[] offset = grinder.getOffsetForWeed(tile, i);
           	GL11.glTranslatef(offset[0],offset[1],offset[2]);
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
            entity.setEntityItemStack(stack);
            if(data.fractions[reference]==1)
            	renderItem.doRender(entity,0,0,0,0,0);
            else
            	renderFraction(data, entity, stack, data.weedParts[i], data.fractions[reference]);
			GL11.glPopMatrix();
        }
		GL11.glPopMatrix();
	}
	
	protected void renderFraction(MysticalGrinderTileData data, EntityItem entity, ItemStack itemstack, int part, int fraction) {
        if(renderBlock==null) {
        	renderBlock = new RenderBlocks();
		}
        setRenderBounds(renderBlock, part, fraction);
        renderBlock.lockBlockBounds=true;
        Block block = Block.getBlockFromItem(itemstack.getItem());
       	GL11.glTranslated(0,1D-getSize(fraction),0);
       	RenderManager.instance.renderEngine.bindTexture(RenderManager.instance.renderEngine.getResourceLocation(entity.getEntityItem().getItemSpriteNumber()));
        TextureUtil.func_152777_a(false, false, 1.0F);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        float f9 = 0.25F;
        int k = block.getRenderType();

        if (k == 1 || k == 19 || k == 12 || k == 2)
        {
            f9 = 0.5F;
        }

        if (block.getRenderBlockPass() > 0)
        {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }

        GL11.glScalef(f9, f9, f9);

        GL11.glPushMatrix();
        renderBlock.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
        GL11.glPopMatrix();

        if (block.getRenderBlockPass() > 0)
        {
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        TextureUtil.func_147945_b();
        renderBlock.lockBlockBounds=false;
	}

	private double getSize(double fraction) {
		return Math.pow(1D/fraction, 1D/3D);
	}
	
	private void setRenderBounds(RenderBlocks renderBlock, double part, double fraction) {
		double size = getSize(fraction);
		double offset = ((1D-size)/fraction)*part;
		renderBlock.renderMinX=offset;
		renderBlock.renderMinY=offset;
		renderBlock.renderMinZ=offset;
		renderBlock.renderMaxX=offset+size;
		renderBlock.renderMaxY=offset+size;
		renderBlock.renderMaxZ=offset+size;
	}
}
