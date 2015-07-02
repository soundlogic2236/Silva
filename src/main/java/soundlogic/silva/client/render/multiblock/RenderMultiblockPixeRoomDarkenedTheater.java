package soundlogic.silva.client.render.multiblock;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.model.ModelPixie;
import soundlogic.silva.common.block.tile.multiblocks.DarkenedTheaterTileData;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater.ActorPixieData;
import soundlogic.silva.common.block.tile.multiblocks.PixieFarmTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;

public class RenderMultiblockPixeRoomDarkenedTheater extends RenderMultiblockPixieRoomBase {

	ModelPixie model = new ModelPixie();
	
	@Override
	protected void renderCoreTick(TileMultiblockCore tile, double x, double y,
			double z, float ticks) {
		this.baseRoomRendering(tile, x, y, z, ticks);
		DarkenedTheaterTileData data = (DarkenedTheaterTileData) tile.getTileData();
		renderPixies(tile, data, x, y, z, ticks);
	}

	private void renderPixies(TileMultiblockCore tile,
			DarkenedTheaterTileData data, double x, double y, double z,
			float ticks) {
		if(!data.activated)
			return;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glTranslated(-tile.xCoord, -tile.yCoord, -tile.zCoord);
		for(ActorPixieData pixie : data.pixieGroup.pixies) {
			model.render(pixie, ticks);
		}
		GL11.glPopMatrix();
	}

}
