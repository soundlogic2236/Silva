package soundlogic.silva.common.entity;

import soundlogic.silva.common.block.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityDwarvenBarrier extends EntityThrowable{

	private double startX;
	private double startY;
	private double startZ;
	private boolean ticked;
	
    public EntityDwarvenBarrier(World p_i1782_1_)
    {
        super(p_i1782_1_);
    }

    public EntityDwarvenBarrier(World p_i1783_1_, EntityLivingBase p_i1783_2_)
    {
        super(p_i1783_1_, p_i1783_2_);
    }

    @SideOnly(Side.CLIENT)
    public EntityDwarvenBarrier(World p_i1784_1_, double p_i1784_2_, double p_i1784_4_, double p_i1784_6_)
    {
        super(p_i1784_1_, p_i1784_2_, p_i1784_4_, p_i1784_6_);
    }
    
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(!ticked) {
			startX=this.posX;
			startY=this.posY;
			startZ=this.posZ;
			ticked=true;
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
        if (pos.entityHit != null)
        {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 2.0F);
        }

        if (!this.worldObj.isRemote)
        {
        	if(pos.typeOfHit==MovingObjectType.BLOCK) {
        		ForgeDirection extend=directionFromSide(pos.sideHit);
        		double x=Math.abs(posX-startX)+1;
        		double y=Math.abs(posY-startY)+1;
        		double z=Math.abs(posZ-startZ)+1;
        		
        		x+=Math.abs(this.motionX);
        		y+=Math.abs(this.motionY);
        		z+=Math.abs(this.motionZ);
        		        		
        		x *= 1 - Math.abs(extend.offsetX);
        		y *= 1 - Math.abs(extend.offsetY);
        		z *= 1 - Math.abs(extend.offsetZ);
        		
        		double max = Math.max(x, Math.max(y, z));
        		
       			x *= x!=max ? 1 : 0;
       			y *= y!=max ? 1 : 0;
       			z *= z!=max ? 1 : 0;
        		
        		int xDisplace = x == 0.0 ? 0 : 1;
        		int yDisplace = y == 0.0 ? 0 : 1;
        		int zDisplace = z == 0.0 ? 0 : 1;
        		
        		spawnPlane(pos.blockX,pos.blockY,pos.blockZ,extend,xDisplace,yDisplace,zDisplace);
        		
        	}
            this.setDead();
        }
        
	}
    
	private void spawnPlane(int blockX, int blockY, int blockZ,
			ForgeDirection extend, int xDisplace, int yDisplace, int zDisplace) {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				int x=blockX;
				int y=blockY;
				int z=blockZ;

				x += (i+1)*extend.offsetX;
				y += (i+1)*extend.offsetY;
				z += (i+1)*extend.offsetZ;
				
				x += xDisplace * (j-2);
				y += yDisplace * (j-2);
				z += zDisplace * (j-2);
				Block block=worldObj.getBlock(x, y, z);
				if(block.isAir(worldObj, x, y, z) || block.isReplaceable(worldObj, x, y, z))
					worldObj.setBlock(x, y, z, ModBlocks.dwarfRock,0,3);
			}
		}
	}

	private ForgeDirection directionFromSide(int side) {
	    /**
	     * Which side was hit. If its -1 then it went the full length of the ray trace. Bottom = 0, Top = 1, East = 2, West
	     * = 3, North = 4, South = 5.
	     */
		switch(side) {
		case 0:return ForgeDirection.DOWN;
		case 1:return ForgeDirection.UP;
		case 2:return ForgeDirection.NORTH;
		case 3:return ForgeDirection.SOUTH;
		case 4:return ForgeDirection.WEST;
		case 5:return ForgeDirection.EAST;
		}
		return null;
	}
    
}
