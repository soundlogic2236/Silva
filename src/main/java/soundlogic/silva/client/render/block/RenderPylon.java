package soundlogic.silva.client.render.block;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.render.tile.RenderTilePylon;
import soundlogic.silva.common.block.tile.TilePylon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderPylon implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.7F, -0.5F);
		RenderTilePylon.metadata = metadata;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TilePylon(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idPylon;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

}
