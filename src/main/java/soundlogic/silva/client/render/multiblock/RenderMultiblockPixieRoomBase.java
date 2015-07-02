package soundlogic.silva.client.render.multiblock;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemRenderer;
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
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataMysticalGrinder;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomBase;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomEmpty;
import soundlogic.silva.common.block.tile.multiblocks.MysticalGrinderTileData;
import soundlogic.silva.common.block.tile.multiblocks.PixiePowerTileData;
import soundlogic.silva.common.block.tile.multiblocks.PixieRoomTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;

public abstract class RenderMultiblockPixieRoomBase extends RenderMultiblock {

	static RenderItem renderItem=null;
	protected void baseRoomRendering(TileMultiblockCore tile, double x, double y, double z, float ticks) {
		MultiblockDataPixieRoomBase room = (MultiblockDataPixieRoomBase) tile.getData();
		renderRequestedItem(tile, room, x, y, z, ticks);
	}
	private void renderRequestedItem(TileMultiblockCore tile, MultiblockDataPixieRoomBase room, double x, double y, double z, float ticks) {
		PixiePowerTileData data = (PixiePowerTileData) tile.getTileData();
		if(data.activated)
			return;
		ItemStack stack = getCurrentStack(room.getNextRequestedObject(tile));
		if(stack==null)
			return;
		EntityItem entity = new EntityItem(tile.getWorldObj());
		entity.hoverStart = 0.0F;
        if(renderItem==null) {
			renderItem = new RenderItem();
			renderItem.setRenderManager(RenderManager.instance);
		}
	    float renderTicks = ClientTickHandler.ticksInGame + ticks;
        float rotationAngle = renderTicks * 5F;
	    GL11.glPushMatrix();
        GL11.glTranslated(x,y,z);
        GL11.glTranslated(-tile.xCoord,-tile.yCoord,-tile.zCoord);
	    int[] offset = room.getOffsetForRequirements(tile);
       	GL11.glTranslatef(offset[0],offset[1],offset[2]);
       	GL11.glTranslatef(.5F,.4F,.5F);
        GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
        entity.setEntityItemStack(stack);
       	renderItem.doRender(entity,0,0,0,0,0);
        GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}
	private ItemStack getCurrentStack(Object obj) {
		if(obj instanceof ItemStack)
			return (ItemStack) obj;
		if(obj instanceof String) {
			List<ItemStack> ores = OreDictionary.getOres((String) obj);
			int slot = (int) (ClientTickHandler.ticksInGame / 40);
			slot = slot % ores.size();
			return ores.get(slot);
		}
		return null;
	}
}
