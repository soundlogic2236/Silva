package soundlogic.silva.common.core.handler.portal;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.DustHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.DarkElfActs;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;

public class DimensionalBlockHandlerSvartalfheim implements IDimensionalBlockHandler{

	private boolean signatureConverted=false;
	private boolean unobserved=false;
	
	@Override
	public void init(Dimension dim) {
		// NO OP
	}

	@Override
	public int frequencyForSearch(TilePortalCore core) {
		return 20;
	}

	@Override
	public boolean shouldTryApply(TilePortalCore core) {
		return signatureConverted && unobserved;
	}

	@Override
	public int getBlocksPerTick(TilePortalCore core) {
		return 5;
	}

	@Override
	public int triesPerBlock(TilePortalCore core) {
		return 20;
	}

	@Override
	public boolean tryApplyToBlock(TilePortalCore core, World world, int[] coords) {
		for(IDarkElfAct act : DarkElfActs.acts) {
			if(act.tryApplyToBlock(world, coords[0], coords[1], coords[2], core)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void generalTick(TilePortalCore core) {
		if((core.getTicksOpen() % frequencyForSearch(core))!=0)
			return;
		unobserved=checkUnobserved(core);
		if(unobserved)
			signatureConverted=tryConvertSignature(core);
/*		System.out.println("unobserved");
		System.out.println(unobserved);
		System.out.println("signatureConverted");
		System.out.println(signatureConverted);*/
	}

	private boolean tryConvertSignature(TilePortalCore core) {
		for(int row = 0; row < DimensionHandler.SIGNATURE_HEIGHT; row++) {
			for(int column = 0; column < DimensionHandler.SIGNATURE_WIDTH; column++) {
				int[] coords=core.getDimensionalSignatureBlockCoords(row,column);
				if(DustHandler.isMagicDust(core.getWorldObj(), coords[0], coords[1], coords[2])) {
					core.getWorldObj().setBlock(coords[0], coords[1], coords[2], ModBlocks.darkenedDust, 0, 3);
				}
			}
		}
		return true;
	}

	private boolean checkUnobserved(TilePortalCore core) {
		World world = core.getWorldObj();
        for (int i = 0; i < world.playerEntities.size(); ++i)
        {
			EntityPlayer player = (EntityPlayer) world.playerEntities.get(i);
			double squareDist = player.getDistanceSq(core.xCoord, core.yCoord, core.zCoord);
			if(squareDist>(100*100))
				continue;
			if(squareDist<(30*30))
				return false;
			if(!checkForNormalCube(
					core.getWorldObj(),
					Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ), 
					Vec3.createVectorHelper(core.xCoord,core.yCoord,core.zCoord)))
				return false;
        }
        return true;
	}
	
	private boolean checkForNormalCube(World world, Vec3 start, Vec3 end) {
		Vec3 current = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);
        int end_x = MathHelper.floor_double(end.xCoord);
        int end_y = MathHelper.floor_double(end.yCoord);
        int end_z = MathHelper.floor_double(end.zCoord);
        int current_x = MathHelper.floor_double(start.xCoord);
        int current_y = MathHelper.floor_double(start.yCoord);
        int current_z = MathHelper.floor_double(start.zCoord);

        int count = 200;

        while (count-- >= 0)
        {
            if (current_x == end_x && current_y == end_y && current_z == end_z)
            {
                return false;
            }

            boolean isXDifferent = true;
            boolean isYDifferent = true;
            boolean isZDifferent = true;
            double adjusted_start_x = 999.0D;
            double adjusted_start_y = 999.0D;
            double adjusted_start_z = 999.0D;

            if (end_x > current_x)
            {
                adjusted_start_x = (double)current_x + 1.0D;
            }
            else if (end_x < current_x)
            {
                adjusted_start_x = (double)current_x + 0.0D;
            }
            else
            {
                isXDifferent = false;
            }

            if (end_y > current_y)
            {
                adjusted_start_y = (double)current_y + 1.0D;
            }
            else if (end_y < current_y)
            {
                adjusted_start_y = (double)current_y + 0.0D;
            }
            else
            {
                isYDifferent = false;
            }

            if (end_z > current_z)
            {
                adjusted_start_z = (double)current_z + 1.0D;
            }
            else if (end_z < current_z)
            {
                adjusted_start_z = (double)current_z + 0.0D;
            }
            else
            {
                isZDifferent = false;
            }

            double dx2 = 999.0D;
            double dy2 = 999.0D;
            double dz2 = 999.0D;
            double dx = end.xCoord - current.xCoord;
            double dy = end.yCoord - current.yCoord;
            double dz = end.zCoord - current.zCoord;

            if (isXDifferent)
            {
                dx2 = (adjusted_start_x - current.xCoord) / dx;
            }

            if (isYDifferent)
            {
                dy2 = (adjusted_start_y - current.yCoord) / dy;
            }

            if (isZDifferent)
            {
                dz2 = (adjusted_start_z - current.zCoord) / dz;
            }

            boolean flag5 = false;
            byte b0;

            if (dx2 < dy2 && dx2 < dz2)
            {
                if (end_x > current_x)
                {
                    b0 = 4;
                }
                else
                {
                    b0 = 5;
                }

                current.xCoord = adjusted_start_x;
                current.yCoord += dy * dx2;
                current.zCoord += dz * dx2;
            }
            else if (dy2 < dz2)
            {
                if (end_y > current_y)
                {
                    b0 = 0;
                }
                else
                {
                    b0 = 1;
                }

                current.xCoord += dx * dy2;
                current.yCoord = adjusted_start_y;
                current.zCoord += dz * dy2;
            }
            else
            {
                if (end_z > current_z)
                {
                    b0 = 2;
                }
                else
                {
                    b0 = 3;
                }

                current.xCoord += dx * dz2;
                current.yCoord += dy * dz2;
                current.zCoord = adjusted_start_z;
            }

            Vec3 vec32 = Vec3.createVectorHelper(current.xCoord, current.yCoord, current.zCoord);
            current_x = (int)(vec32.xCoord = (double)MathHelper.floor_double(current.xCoord));

            if (b0 == 5)
            {
                --current_x;
                ++vec32.xCoord;
            }

            current_y = (int)(vec32.yCoord = (double)MathHelper.floor_double(current.yCoord));

            if (b0 == 1)
            {
                --current_y;
                ++vec32.yCoord;
            }

            current_z = (int)(vec32.zCoord = (double)MathHelper.floor_double(current.zCoord));

            if (b0 == 3)
            {
                --current_z;
                ++vec32.zCoord;
            }

            Block block1 = world.getBlock(current_x, current_y, current_z);
            int l1 = world.getBlockMetadata(current_x, current_y, current_z);
            
            if(block1.isNormalCube(world, current_x, current_y, current_z))
            	return true;
        }
        return false;
	}

	@Override
	public AxisAlignedBB modifyBoundingBox(TilePortalCore core,
			AxisAlignedBB aabb) {
		return aabb.expand(5, 5, 5);
	}

}
