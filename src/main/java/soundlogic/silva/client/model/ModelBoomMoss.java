package soundlogic.silva.client.model;

import java.util.Random;

import soundlogic.silva.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelBoomMoss extends ModelBase {

	int size=64;
	int offset=4;
	
	float gap = 0.0625F;
	
	ForgeDirection[] directionSides=new ForgeDirection[] {
			ForgeDirection.UP,
			ForgeDirection.DOWN,
			ForgeDirection.WEST,
			ForgeDirection.EAST,
			ForgeDirection.SOUTH,
			ForgeDirection.NORTH,
	};

	
	public void renderSide(int side) {
		Tessellator tessellator = Tessellator.instance;
		RenderBlocks renderer = RenderBlocks.getInstance();
		renderer.setRenderFromInside(true);
		renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
		Block block = Blocks.stone;
		int meta = 0;
        float f1 = gap;
        renderer.overrideBlockTexture=ModBlocks.boomMoss.getIcon(0, 0);
        switch(side) {
        case 0:
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addTranslation(0.0F, f1, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 0));
        tessellator.addTranslation(0.0F, -f1, 0.0F);
        tessellator.draw();
        break;
        case 1:
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addTranslation(0.0F, -f1, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 1));
        tessellator.addTranslation(0.0F, f1, 0.0F);
        tessellator.draw();
        break;
        case 2:
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 4));
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        tessellator.draw();
        break;
        case 3:
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addTranslation(-f1, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 5));
        tessellator.addTranslation(f1, 0.0F, 0.0F);
        tessellator.draw();
        break;
        case 4:
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addTranslation(0.0F, 0.0F, f1);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        tessellator.draw();
        break;
        case 5:
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addTranslation(0.0F, 0.0F, -f1);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(meta, 3));
        tessellator.addTranslation(0.0F, 0.0F, f1);
        tessellator.draw();
        break;
        }
		renderer.setRenderFromInside(false);
		renderer.setRenderAllFaces(false);
		renderer.clearOverrideBlockTexture();
		/*		tes.startDrawingQuads();
		for(int i = 0; i < 4 ; i++) {
			int x = vertexPoints[side][i][0];
			int y = vertexPoints[side][i][1];
			int z = vertexPoints[side][i][2];
			tes.addVertexWithUV(x*(Math.random()-.5), y*(Math.random()-.5), z*(Math.random()-.5), 0, 0);
		}
		tes.draw();*/
//		cubeSides[side].render(f);
	}
	public ForgeDirection getDirectionSide(int side) {
		return directionSides[side];
	}
}
