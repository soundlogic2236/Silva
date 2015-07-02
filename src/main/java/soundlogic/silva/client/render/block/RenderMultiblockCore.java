package soundlogic.silva.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.MultiBlockCreationHandler;
import soundlogic.silva.common.core.handler.MultiBlockCreationHandler.RenderData;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderMultiblockCore implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		//NO OP;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		RenderData data = MultiBlockCreationHandler.renderData;
		if(data.normalCube || data.lastX!=x || data.lastY!=y || data.lastZ!=z)
			return renderer.renderStandardBlock(block, x, y, z);
		MultiBlockCreationHandler.renderData.renderFaces(renderer);
		return true;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idMultiblockCore;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
