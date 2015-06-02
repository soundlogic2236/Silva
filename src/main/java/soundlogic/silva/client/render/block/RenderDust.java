package soundlogic.silva.client.render.block;

import java.awt.Color;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.block.BlockPixieDust;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.DustHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDust implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		// NO-OP
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        Color color = new Color(block.colorMultiplier(world, x, y, z));
        IIcon iicon = BlockRedstoneWire.getRedstoneWireIcon("cross");
        IIcon iicon1 = BlockRedstoneWire.getRedstoneWireIcon("line");
        IIcon iicon2 = BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
        IIcon iicon3 = BlockRedstoneWire.getRedstoneWireIcon("line_overlay");
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        float red = (float)color.getRed() / 255F;
        float green = (float)color.getGreen() / 255F;
        float blue = (float)color.getBlue() / 255F;

        tessellator.setColorOpaque_F(red, green, blue);
        double d0 = 0.015625D;
        double d1 = 0.015625D;
        boolean flag = shouldConnect(block, world, x - 1, y, z, 1) || !world.getBlock(x - 1, y, z).isBlockNormalCube() && shouldConnect(block, world, x - 1, y - 1, z, -1);
        boolean flag1 = shouldConnect(block, world, x + 1, y, z, 3) || !world.getBlock(x + 1, y, z).isBlockNormalCube() && shouldConnect(block, world, x + 1, y - 1, z, -1);
        boolean flag2 = shouldConnect(block, world, x, y, z - 1, 2) || !world.getBlock(x, y, z - 1).isBlockNormalCube() && shouldConnect(block, world, x, y - 1, z - 1, -1);
        boolean flag3 = shouldConnect(block, world, x, y, z + 1, 0) || !world.getBlock(x, y, z + 1).isBlockNormalCube() && shouldConnect(block, world, x, y - 1, z + 1, -1);

        if (!world.getBlock(x, y + 1, z).isBlockNormalCube())
        {
            if (world.getBlock(x - 1, y, z).isBlockNormalCube() && shouldConnect(block, world, x - 1, y + 1, z, -1))
            {
                flag = true;
            }

            if (world.getBlock(x + 1, y, z).isBlockNormalCube() && shouldConnect(block, world, x + 1, y + 1, z, -1))
            {
                flag1 = true;
            }

            if (world.getBlock(x, y, z - 1).isBlockNormalCube() && shouldConnect(block, world, x, y + 1, z - 1, -1))
            {
                flag2 = true;
            }

            if (world.getBlock(x, y, z + 1).isBlockNormalCube() && shouldConnect(block, world, x, y + 1, z + 1, -1))
            {
                flag3 = true;
            }
        }

        float f4 = (float)(x + 0);
        float f5 = (float)(x + 1);
        float f6 = (float)(z + 0);
        float f7 = (float)(z + 1);
        int i1 = 0;

        if ((flag || flag1) && !flag2 && !flag3)
        {
            i1 = 1;
        }

        if ((flag2 || flag3) && !flag1 && !flag)
        {
            i1 = 2;
        }

        if (i1 == 0)
        {
            int j1 = 0;
            int k1 = 0;
            int l1 = 16;
            int i2 = 16;
            boolean flag4 = true;

            if (!flag)
            {
                f4 += 0.3125F;
            }

            if (!flag)
            {
                j1 += 5;
            }

            if (!flag1)
            {
                f5 -= 0.3125F;
            }

            if (!flag1)
            {
                l1 -= 5;
            }

            if (!flag2)
            {
                f6 += 0.3125F;
            }

            if (!flag2)
            {
                k1 += 5;
            }

            if (!flag3)
            {
                f7 -= 0.3125F;
            }

            if (!flag3)
            {
                i2 -= 5;
            }

            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon.getInterpolatedU((double)l1), (double)iicon.getInterpolatedV((double)i2));
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon.getInterpolatedU((double)l1), (double)iicon.getInterpolatedV((double)k1));
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon.getInterpolatedU((double)j1), (double)iicon.getInterpolatedV((double)k1));
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon.getInterpolatedU((double)j1), (double)iicon.getInterpolatedV((double)i2));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon2.getInterpolatedU((double)l1), (double)iicon2.getInterpolatedV((double)i2));
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon2.getInterpolatedU((double)l1), (double)iicon2.getInterpolatedV((double)k1));
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon2.getInterpolatedU((double)j1), (double)iicon2.getInterpolatedV((double)k1));
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon2.getInterpolatedU((double)j1), (double)iicon2.getInterpolatedV((double)i2));
        }
        else if (i1 == 1)
        {
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon1.getMaxU(), (double)iicon1.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon1.getMinU(), (double)iicon1.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon1.getMinU(), (double)iicon1.getMaxV());
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon3.getMaxU(), (double)iicon3.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon3.getMinU(), (double)iicon3.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon3.getMinU(), (double)iicon3.getMaxV());
        }
        else
        {
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon1.getMinU(), (double)iicon1.getMaxV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon1.getMinU(), (double)iicon1.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon1.getMaxU(), (double)iicon1.getMinV());
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f7, (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
            tessellator.addVertexWithUV((double)f5, (double)y + 0.015625D, (double)f6, (double)iicon3.getMinU(), (double)iicon3.getMaxV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f6, (double)iicon3.getMinU(), (double)iicon3.getMinV());
            tessellator.addVertexWithUV((double)f4, (double)y + 0.015625D, (double)f7, (double)iicon3.getMaxU(), (double)iicon3.getMinV());
        }

        if (!world.getBlock(x, y + 1, z).isBlockNormalCube())
        {
            float f8 = 0.021875F;

            if (world.getBlock(x - 1, y, z).isBlockNormalCube() && isDust(world,x - 1, y + 1, z))
            {
                tessellator.setColorOpaque_F(red, green, blue);
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), (double)iicon1.getMaxU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 1), (double)iicon1.getMinU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 0), (double)iicon1.getMinU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), (double)iicon3.getMaxU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 1), (double)iicon3.getMinU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)(y + 0), (double)(z + 0), (double)iicon3.getMinU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)x + 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
            }

            if (world.getBlock(x + 1, y, z).isBlockNormalCube() && isDust(world,x + 1, y + 1, z))
            {
                tessellator.setColorOpaque_F(red, green, blue);
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 1), (double)iicon1.getMinU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), (double)iicon1.getMaxU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 0), (double)iicon1.getMinU(), (double)iicon1.getMinV());
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 1), (double)iicon3.getMinU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 1), (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)((float)(y + 1) + 0.021875F), (double)(z + 0), (double)iicon3.getMaxU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)(x + 1) - 0.015625D, (double)(y + 0), (double)(z + 0), (double)iicon3.getMinU(), (double)iicon3.getMinV());
            }

            if (world.getBlock(x, y, z - 1).isBlockNormalCube() && isDust(world,x, y + 1, z - 1))
            {
                tessellator.setColorOpaque_F(red, green, blue);
                tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)z + 0.015625D, (double)iicon1.getMinU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, (double)iicon1.getMaxU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z + 0.015625D, (double)iicon1.getMinU(), (double)iicon1.getMinV());
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)z + 0.015625D, (double)iicon3.getMinU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)z + 0.015625D, (double)iicon3.getMaxU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z + 0.015625D, (double)iicon3.getMinU(), (double)iicon3.getMinV());
            }

            if (world.getBlock(x, y, z + 1).isBlockNormalCube() && isDust(world,x, y + 1, z + 1))
            {
                tessellator.setColorOpaque_F(red, green, blue);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, (double)iicon1.getMaxU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)(z + 1) - 0.015625D, (double)iicon1.getMinU(), (double)iicon1.getMinV());
                tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)(z + 1) - 0.015625D, (double)iicon1.getMinU(), (double)iicon1.getMaxV());
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, (double)iicon1.getMaxU(), (double)iicon1.getMaxV());
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                tessellator.addVertexWithUV((double)(x + 1), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, (double)iicon3.getMaxU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)(x + 1), (double)(y + 0), (double)(z + 1) - 0.015625D, (double)iicon3.getMinU(), (double)iicon3.getMinV());
                tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)(z + 1) - 0.015625D, (double)iicon3.getMinU(), (double)iicon3.getMaxV());
                tessellator.addVertexWithUV((double)(x + 0), (double)((float)(y + 1) + 0.021875F), (double)(z + 1) - 0.015625D, (double)iicon3.getMaxU(), (double)iicon3.getMaxV());
            }
        }
        
        doRenderOverrides(world, x, y, z, renderer);
        
        return true;
	}

	protected void doRenderOverrides(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		wrapper.original=renderer.blockAccess;
		renderer.blockAccess=wrapper;
		for(int[] offset : RedstoneRenderBlockAccessWrapper.offsets) {
			doRenderOverride(world, x+offset[0], y+offset[1], z+offset[2], renderer);
		}
		renderer.blockAccess=wrapper.original;
	}

	private void doRenderOverride(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		if(!blockNeedsRenderOverride(world,x,y,z))
			return;
		renderer.renderBlockByRenderType(world.getBlock(x, y, z), x, y, z);
	}

	protected boolean shouldConnect(Block block, IBlockAccess world, int x, int y, int z, int side) {
		return isDust(world, x, y, z);
	}
	
	private boolean isDust(IBlockAccess world, int x, int y, int z) {
		return DustHandler.isDust(world, x, y, z);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
	
	public static boolean blockNeedsRenderOverride(IBlockAccess world, int x, int y, int z) {
		return blockNeedsRenderOverride(world.getBlock(x, y, z));
	}

	public static boolean blockNeedsRenderOverride(Block block) {
		return block==Blocks.redstone_wire;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idDust;
	}
	
	private static final RedstoneRenderBlockAccessWrapper wrapper = new RedstoneRenderBlockAccessWrapper();
	
	static private class RedstoneRenderBlockAccessWrapper implements IBlockAccess {
		static final int[][] offsets = new int[][] {
			{-1,-1, 0},
			{ 1,-1, 0},
			{ 0,-1,-1},
			{ 0,-1, 1}
		};

		IBlockAccess original;
		
		@Override
		public Block getBlock(int x, int y, int z) {
			if(DustHandler.isDust(original, x, y, z))
				return Blocks.redstone_wire;
			return original.getBlock(x, y, z);
		}

		@Override
		public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_,
				int p_147438_3_) {
			return original.getTileEntity(p_147438_1_, p_147438_2_, p_147438_3_);
		}

		@Override
		public int getLightBrightnessForSkyBlocks(int p_72802_1_,
				int p_72802_2_, int p_72802_3_, int p_72802_4_) {
			return original.getLightBrightnessForSkyBlocks(p_72802_1_, p_72802_2_, p_72802_3_, p_72802_4_);
		}

		@Override
		public int getBlockMetadata(int p_72805_1_, int p_72805_2_,
				int p_72805_3_) {
			return original.getBlockMetadata(p_72805_1_, p_72805_2_, p_72805_3_);
		}

		@Override
		public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_,
				int p_72879_3_, int p_72879_4_) {
			return original.isBlockProvidingPowerTo(p_72879_1_, p_72879_2_, p_72879_3_, p_72879_4_);
		}

		@Override
		public boolean isAirBlock(int p_147437_1_, int p_147437_2_,
				int p_147437_3_) {
			return original.isAirBlock(p_147437_1_, p_147437_2_, p_147437_3_);
		}

		@Override
		public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
			return original.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
		}

		@Override
		public int getHeight() {
			return original.getHeight();
		}

		@Override
		public boolean extendedLevelsInChunkCache() {
			return original.extendedLevelsInChunkCache();
		}

		@Override
		public boolean isSideSolid(int x, int y, int z, ForgeDirection side,
				boolean _default) {
			return original.isSideSolid(x, y, z, side, _default);
		}
		
	}

}
