package soundlogic.silva.common.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibRenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBlazeFire extends Block {

	public BlockBlazeFire() {
        super(Material.fire);
        this.setTickRandomly(true);
	}
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }
    public boolean isOpaqueCube()
    {
        return false;
    }
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    public int getRenderType()
    {
        return LibRenderIDs.idBlazeFire;
    }
    public int quantityDropped(Random rand)
    {
        return 0;
    }
    public int tickRate(World world)
    {
        return 30;
    }
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (world.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
        	if(Blocks.fire.canPlaceBlockAt(world, x, y, z))
        		world.setBlock(x, y, z, Blocks.fire,0,3);
        }
        else
        	world.setBlockToAir(x, y, z);
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    	if(!canPlaceBlockAt(world,x,y,z))
    		world.setBlockToAir(x, y, z);
    }
    
    public boolean isCollidable()
    {
        return false;
    }
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
        if (p_149734_5_.nextInt(24) == 0)
        {
            p_149734_1_.playSound((double)((float)p_149734_2_ + 0.5F), (double)((float)p_149734_3_ + 0.5F), (double)((float)p_149734_4_ + 0.5F), "fire.fire", 1.0F + p_149734_5_.nextFloat(), p_149734_5_.nextFloat() * 0.7F + 0.3F, false);
        }
        for (int l = 0; l < 3; ++l)
        {
            float f = (float)p_149734_2_ + p_149734_5_.nextFloat();
            float f1 = (float)p_149734_3_ + p_149734_5_.nextFloat() * 0.5F + 0.5F;
            float f2 = (float)p_149734_4_ + p_149734_5_.nextFloat();
            p_149734_1_.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
        }
    }

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if(!super.canPlaceBlockAt(world, x, y, z))
			return false;
		for(ForgeDirection dir : ForgeDirection.values())
			if(checkFireHolderInDirection(world, x,y,z,dir))
				return true;
		return false;
	}

	protected boolean checkFireHolderInDirection(World world, int x, int y, int z, ForgeDirection dir) {
		return world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).isSideSolid(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.getOpposite());
	}

}
