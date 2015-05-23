package soundlogic.silva.common.entity;

import java.util.UUID;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPhantomEndermanEcho extends EntityEnderman implements IEntityFateEcho {

	private static final String TAG_PHANTOM = "phantom";
	private static final String TAG_FLYING = "flying";
	private static final String TAG_ATTACKING = "attacking";
	private static final String TAG_TARGET_UUID = "targetUUID";
	private static final String TAG_ANGLE = "circleAngle";
	private static final String TAG_KEY = "fateKey";
	private int key;
	
	private static final double range = 2.5D;
	
	boolean phantom;
	boolean flying;
	boolean attacking;
	
	private EntityPlayer player;
	
	public EntityPhantomEndermanEcho(World world) {
		super(world);
		this.phantom=true;
		this.flying=true;
		this.attacking=false;
	}
	
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(19, new Float((float)0));
    }
	
	public void onUpdate() {
		super.onUpdate();
		if(this.isScreaming())
			this.attacking=true;
		if(this.attacking) {
			if(phantom && this.worldObj.checkNoEntityCollision(this.boundingBox))
				this.phantom=false;
			if(!this.phantom && World.doesBlockHaveSolidTopSurface(worldObj, (int)posX, (int)this.boundingBox.minY-1, (int)posZ)) {
				this.flying=false;
			}
		}
		updateData();
		getAttackTarget();
	}

    public void onLivingUpdate()
    {
    	boolean originalGrief = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
    	this.worldObj.getGameRules().setOrCreateGameRule("mobGriefing", "false");
    	super.onLivingUpdate();
    	this.worldObj.getGameRules().setOrCreateGameRule("mobGriefing", Boolean.toString(originalGrief));
    	FateHandler.doUpdateForEcho(this);
    	if(this.isDead)
    		return;
    }
	
    private boolean checkForValidTarget() {
		if(player==null)
			return false;
		if(!player.worldObj.equals(worldObj))
			return false;
		return true;
	}

	protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_)
    {
		if(attacking)
			return super.teleportTo(p_70825_1_, p_70825_3_, p_70825_5_);
		return false;
    }

	private void updateData() {
		this.noClip=this.phantom;
		if(!attacking) {
			if(func_146080_bZ()==Blocks.air) {
				switch(worldObj.rand.nextInt(2)) {
				case 0: func_146081_a(Blocks.chest);break;
				case 2: func_146081_a(Blocks.end_portal_frame);break;
				}
			}
		}
		else if(func_146080_bZ()!=Blocks.air)
			func_146081_a(Blocks.air);
	}

    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        if(player==null) {
        	player = (EntityPlayer) FateHandler.getEntityFromKey(key);
        }
        return player;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            Entity entity = source.getEntity();
            if(entity instanceof EntityPlayer) {
            	if(entity.equals(player)) {
            		return super.attackEntityFrom(source, amount);
            	}
            	else
            		return false;
            }
            else
            	return false;
        }
    }

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
    	if(!this.flying)
    		super.updateFallState(p_70064_1_, p_70064_3_);
    }
    protected void fall(float p_70069_1_) {
    	if(!this.flying)
    		super.fall(p_70069_1_);
    }

    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
    	if(!attacking) {
    		if(player!=null) {
    			
    			float angle = getAngle();
    			
    			double targetX = player.posX;
    			double targetY = player.posY;
    			double targetZ = player.posZ;

    			targetX+=range*Math.sin(angle);
    			targetZ+=range*Math.cos(angle);
    			
    			this.setPositionAndRotation(targetX, targetY, targetZ, 0, 0);

    			targetY+=player.boundingBox.minY-boundingBox.minY;

    			this.setPositionAndRotation(targetX, targetY, targetZ, 90F - (float)(angle*180F/Math.PI), 0);

    		}
    	}
    	else if(this.flying) {
	        if (this.isInWater())
	        {
	            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
	            this.moveEntity(this.motionX, this.motionY, this.motionZ);
	            this.motionX *= 0.800000011920929D;
	            this.motionY *= 0.800000011920929D;
	            this.motionZ *= 0.800000011920929D;
	        }
	        else if (this.handleLavaMovement())
	        {
	            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
	            this.moveEntity(this.motionX, this.motionY, this.motionZ);
	            this.motionX *= 0.5D;
	            this.motionY *= 0.5D;
	            this.motionZ *= 0.5D;
	        }
	        else
	        {
	            float f2 = 0.91F;
	
	            if (this.onGround)
	            {
	                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
	            }
	
	            float f3 = 0.16277136F / (f2 * f2 * f2);
	            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * f3 : 0.02F);
	            f2 = 0.91F;
	
	            if (this.onGround)
	            {
	                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
	            }
	
	            this.moveEntity(this.motionX, this.motionY, this.motionZ);
	            this.motionX *= (double)f2;
	            this.motionY *= (double)f2;
	            this.motionZ *= (double)f2;
	        }
	
	        this.prevLimbSwingAmount = this.limbSwingAmount;
	        double d1 = this.posX - this.prevPosX;
	        double d0 = this.posZ - this.prevPosZ;
	        float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
	
	        if (f4 > 1.0F)
	        {
	            f4 = 1.0F;
	        }
	
	        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
	        this.limbSwing += this.limbSwingAmount;
    	}
    	else
    		super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    }

    public void setAngle(float angle)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(angle));
    }

    public float getAngle()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }
    
    public void writeEntityToNBT(NBTTagCompound cmp)
    {
        super.writeEntityToNBT(cmp);
        cmp.setBoolean(TAG_ATTACKING, attacking);
        cmp.setBoolean(TAG_FLYING, flying);
        cmp.setBoolean(TAG_PHANTOM, phantom);
        cmp.setFloat(TAG_ANGLE, getAngle());
        cmp.setInteger(TAG_KEY, key);
    }

    public void readEntityFromNBT(NBTTagCompound cmp)
    {
        super.readEntityFromNBT(cmp);
        attacking=cmp.getBoolean(TAG_ATTACKING);
        flying=cmp.getBoolean(TAG_FLYING);
        phantom=cmp.getBoolean(TAG_PHANTOM);
        setAngle(cmp.getFloat(TAG_ANGLE));
        key=cmp.getInteger(TAG_KEY);
    }

    public boolean isWet()
    {
        return !phantom && super.isWet();
    }

	@Override
	public void setKey(int key) {
		this.key=key;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public float getMaxRangeFromSource() {
		return 30;
	}

	@Override
	public World getWorldObj() {
		return worldObj;
	}

	@Override
	public void setDead() {
		FateHandler.setDead(this);
		super.setDead();
	}
}
