package soundlogic.silva.client.render.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.block.tile.TileManaEater;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderBlazeFire implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        IIcon iicon2 = iicon;

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        double d0 = (double)iicon2.getMinU();
        double d1 = (double)iicon2.getMinV();
        double d2 = (double)iicon2.getMaxU();
        double d3 = (double)iicon2.getMaxV();
        float f = 1.4F;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;

        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !checkSolidSide(world, x, y - 1, z, UP))
        {
            float f2 = 0.2F;
            float f1 = 0.0625F;

            if ((x + y + z & 1) == 1)
            {
                d0 = (double)iicon1.getMinU();
                d1 = (double)iicon1.getMinV();
                d2 = (double)iicon1.getMaxU();
                d3 = (double)iicon1.getMaxV();
            }

            if ((x / 2 + y / 2 + z / 2 & 1) == 1)
            {
                d5 = d2;
                d2 = d0;
                d0 = d5;
            }

            if (checkSolidSide(world, x - 1, y, z, EAST))
            {
                tessellator.addVertexWithUV((double)((float)x + f2), (double)((float)y + f + f1), (double)(z + 1), d2, d1);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 1), d2, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)((float)y + f + f1), (double)(z + 0), d0, d1);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)((float)y + f + f1), (double)(z + 0), d0, d1);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 1), d2, d3);
                tessellator.addVertexWithUV((double)((float)x + f2), (double)((float)y + f + f1), (double)(z + 1), d2, d1);
            }

            if (checkSolidSide(world, x + 1, y, z, WEST))
            {
                tessellator.addVertexWithUV((double)((float)(x + 1) - f2), (double)((float)y + f + f1), (double)(z + 0), d0, d1);
                tessellator.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f1), (double)(z + 1), d2, d3);
                tessellator.addVertexWithUV((double)((float)(x + 1) - f2), (double)((float)y + f + f1), (double)(z + 1), d2, d1);
                tessellator.addVertexWithUV((double)((float)(x + 1) - f2), (double)((float)y + f + f1), (double)(z + 1), d2, d1);
                tessellator.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f1), (double)(z + 1), d2, d3);
                tessellator.addVertexWithUV((double)(x + 1 - 0), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)((float)(x + 1) - f2), (double)((float)y + f + f1), (double)(z + 0), d0, d1);
            }

            if (checkSolidSide(world, x, y, z - 1, SOUTH))
            {
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + f1), (double)((float)z + f2), d2, d1);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 0), d2, d3);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + f1), (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + f1), (double)((float)z + f2), d0, d1);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f1), (double)(z + 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 0), d2, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + f1), (double)((float)z + f2), d2, d1);
            }

            if (checkSolidSide(world, x, y, z + 1, NORTH))
            {
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + f1), (double)((float)(z + 1) - f2), d0, d1);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f1), (double)(z + 1 - 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 1 - 0), d2, d3);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + f1), (double)((float)(z + 1) - f2), d2, d1);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f + f1), (double)((float)(z + 1) - f2), d2, d1);
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 0) + f1), (double)(z + 1 - 0), d2, d3);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 0) + f1), (double)(z + 1 - 0), d0, d3);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f + f1), (double)((float)(z + 1) - f2), d0, d1);
            }

            if (checkSolidSide(world, x, y + 1, z, DOWN))
            {
                d5 = (double)x + 0.5D + 0.5D;
                d6 = (double)x + 0.5D - 0.5D;
                d7 = (double)z + 0.5D + 0.5D;
                d8 = (double)z + 0.5D - 0.5D;
                d9 = (double)x + 0.5D - 0.5D;
                d10 = (double)x + 0.5D + 0.5D;
                d11 = (double)z + 0.5D - 0.5D;
                double d12 = (double)z + 0.5D + 0.5D;
                d0 = (double)iicon.getMinU();
                d1 = (double)iicon.getMinV();
                d2 = (double)iicon.getMaxU();
                d3 = (double)iicon.getMaxV();
                ++y;
                f = -0.2F;

                if ((x + y + z & 1) == 0)
                {
                    tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 0), d2, d1);
                    tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 0), d2, d3);
                    tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 1), d0, d3);
                    tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 1), d0, d1);
                    d0 = (double)iicon1.getMinU();
                    d1 = (double)iicon1.getMinV();
                    d2 = (double)iicon1.getMaxU();
                    d3 = (double)iicon1.getMaxV();
                    tessellator.addVertexWithUV(d10, (double)((float)y + f), (double)(z + 1), d2, d1);
                    tessellator.addVertexWithUV(d6, (double)(y + 0), (double)(z + 1), d2, d3);
                    tessellator.addVertexWithUV(d6, (double)(y + 0), (double)(z + 0), d0, d3);
                    tessellator.addVertexWithUV(d10, (double)((float)y + f), (double)(z + 0), d0, d1);
                }
                else
                {
                    tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d12, d2, d1);
                    tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d8, d2, d3);
                    tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d8, d0, d3);
                    tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d12, d0, d1);
                    d0 = (double)iicon1.getMinU();
                    d1 = (double)iicon1.getMinV();
                    d2 = (double)iicon1.getMaxU();
                    d3 = (double)iicon1.getMaxV();
                    tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d11, d2, d1);
                    tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d7, d2, d3);
                    tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d7, d0, d3);
                    tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d11, d0, d1);
                }
            }
        }
        else
        {
            double d4 = (double)x + 0.5D + 0.2D;
            d5 = (double)x + 0.5D - 0.2D;
            d6 = (double)z + 0.5D + 0.2D;
            d7 = (double)z + 0.5D - 0.2D;
            d8 = (double)x + 0.5D - 0.3D;
            d9 = (double)x + 0.5D + 0.3D;
            d10 = (double)z + 0.5D - 0.3D;
            d11 = (double)z + 0.5D + 0.3D;
            tessellator.addVertexWithUV(d8, (double)((float)y + f), (double)(z + 1), d2, d1);
            tessellator.addVertexWithUV(d4, (double)(y + 0), (double)(z + 1), d2, d3);
            tessellator.addVertexWithUV(d4, (double)(y + 0), (double)(z + 0), d0, d3);
            tessellator.addVertexWithUV(d8, (double)((float)y + f), (double)(z + 0), d0, d1);
            tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 0), d2, d1);
            tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 0), d2, d3);
            tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 1), d0, d3);
            tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 1), d0, d1);
            d0 = (double)iicon1.getMinU();
            d1 = (double)iicon1.getMinV();
            d2 = (double)iicon1.getMaxU();
            d3 = (double)iicon1.getMaxV();
            tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d11, d2, d1);
            tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d7, d2, d3);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d7, d0, d3);
            tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d11, d0, d1);
            tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d10, d2, d1);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d6, d2, d3);
            tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d6, d0, d3);
            tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d10, d0, d1);
            d4 = (double)x + 0.5D - 0.5D;
            d5 = (double)x + 0.5D + 0.5D;
            d6 = (double)z + 0.5D - 0.5D;
            d7 = (double)z + 0.5D + 0.5D;
            d8 = (double)x + 0.5D - 0.4D;
            d9 = (double)x + 0.5D + 0.4D;
            d10 = (double)z + 0.5D - 0.4D;
            d11 = (double)z + 0.5D + 0.4D;
            tessellator.addVertexWithUV(d8, (double)((float)y + f), (double)(z + 0), d0, d1);
            tessellator.addVertexWithUV(d4, (double)(y + 0), (double)(z + 0), d0, d3);
            tessellator.addVertexWithUV(d4, (double)(y + 0), (double)(z + 1), d2, d3);
            tessellator.addVertexWithUV(d8, (double)((float)y + f), (double)(z + 1), d2, d1);
            tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 1), d0, d1);
            tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 1), d0, d3);
            tessellator.addVertexWithUV(d5, (double)(y + 0), (double)(z + 0), d2, d3);
            tessellator.addVertexWithUV(d9, (double)((float)y + f), (double)(z + 0), d2, d1);
            d0 = (double)iicon.getMinU();
            d1 = (double)iicon.getMinV();
            d2 = (double)iicon.getMaxU();
            d3 = (double)iicon.getMaxV();
            tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d11, d0, d1);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d7, d0, d3);
            tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d7, d2, d3);
            tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d11, d2, d1);
            tessellator.addVertexWithUV((double)(x + 1), (double)((float)y + f), d10, d0, d1);
            tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), d6, d0, d3);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), d6, d2, d3);
            tessellator.addVertexWithUV((double)(x + 0), (double)((float)y + f), d10, d2, d1);
        }

        return true;
	}

	protected boolean checkSolidSide(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, dir);
	}
	
	@Override
	public int getRenderId() {
		return LibRenderIDs.idBlazeFire;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

}
