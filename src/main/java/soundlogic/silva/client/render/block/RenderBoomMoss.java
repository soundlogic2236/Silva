package soundlogic.silva.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.block.tile.TileManaEater;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderBoomMoss implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		TileManaEater eater = new TileManaEater();
		eater.rotationX = -180F;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(eater, 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idBoomMoss;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

}
