package soundlogic.silva.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.block.BlockPortalUpgradeCharge;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderPortalUpgradeCharge implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
		Tessellator tessellator=Tessellator.instance;
		
		int meta=5;
        float f1 = 0.0f;
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addTranslation(0.0F, 0.0F, f1);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 3));
        tessellator.addTranslation(0.0F, 0.0F, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 4));
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 5));
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        tessellator.draw();
		renderer.setRenderBounds(.5, .25, .25, 1, .75, .75);
		renderer.setRenderFromInside(true);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addTranslation(0.0F, 0.0F, f1);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 3));
        tessellator.addTranslation(0.0F, 0.0F, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 4));
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        tessellator.draw();
		renderer.setRenderFromInside(false);
		renderer.setRenderAllFaces(false);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int meta = world.getBlockMetadata(x, y, z);
		double offsetX=0;
		double offsetY=0;
		double offsetZ=0;
		switch(meta) {
		case 0:
			offsetY=-.25;
			break;
		case 1:
			offsetY=.25;
			break;
		case 2:
			offsetZ=-.25;
			break;
		case 3:
			offsetZ=.25;
			break;
		case 4:
			offsetX=-.25;
			break;
		case 5:
			offsetX=.25;
			break;
		}
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setOverrideBlockTexture(ModBlocks.bifrostBlockSparkling.getBlockTextureFromSide(0));
		renderer.setRenderAllFaces(true);
		renderer.setRenderBounds(.25+offsetX, .25+offsetY, .25+offsetZ, .75+offsetX, .75+offsetY, .75+offsetZ);
		renderer.setRenderFromInside(true);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderFromInside(false);
		renderer.setRenderAllFaces(false);
		renderer.clearOverrideBlockTexture();
		return true;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idPortalUpgradeCharge;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
