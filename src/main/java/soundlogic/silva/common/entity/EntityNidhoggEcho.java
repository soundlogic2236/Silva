package soundlogic.silva.common.entity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityNidhoggEcho extends EntityLiving implements IEntityMultiPart, IEntityFateEcho {

	private static final String TAG_KEY = "fateKey";
	private int key;
	
    public double targetX;
    public double targetY;
    public double targetZ;
    public Entity target;
    public boolean forceNewTarget;
    /** Ring buffer array for the last 64 Y-positions and yaw rotations. Used to calculate offsets for the animations. */
    public double[][] ringBuffer = new double[64][3];
    /** Index into the ring buffer. Incremented once per tick and restarts at 0 once it reaches the end of the buffer. */
    public int ringBufferIndex = -1;
    /** An array containing all body parts of this dragon */
    public EntityDragonPart[] dragonPartArray;
    /** The head bounding box of a dragon */
    public EntityDragonPart dragonPartHead;
    /** The body bounding box of a dragon */
    public EntityDragonPart dragonPartBody;
    public EntityDragonPart dragonPartTail1;
    public EntityDragonPart dragonPartTail2;
    public EntityDragonPart dragonPartTail3;
    public EntityDragonPart dragonPartWing1;
    public EntityDragonPart dragonPartWing2;
    /** Animation time at previous tick. */
    public float prevAnimTime;
    /** Animation time, used to control the speed of the animation cycles (wings flapping, jaw opening, etc.) */
    public float animTime;
    
	private EntityLivingBase source;
    private boolean isStored;
    
    public EntityNidhoggEcho(World world) {
		super(world);
		if(world!=null)
			FateHandler.addEcho(this);
        this.dragonPartArray = new EntityDragonPart[] {this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F)};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0F, 8.0F);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
	}
    
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(400.0D);
    }

    /**
     * Return whether this entity is invulnerable to damage.
     */
    public boolean isEntityInvulnerable()
    {
        return true;
    }

    /**
     * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
     * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
     */
    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
    {
        if (this.getHealth() <= 0.0F)
        {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int j = this.ringBufferIndex - p_70974_1_ * 1 & 63;
        int k = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[j][0];
        double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[k][0] - d0);
        adouble[0] = d0 + d1 * (double)p_70974_2_;
        d0 = this.ringBuffer[j][1];
        d1 = this.ringBuffer[k][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;
        adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * (double)p_70974_2_;
        return adouble;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
    	FateHandler.doUpdateForEcho(this);
    	if(this.isDead)
    		return;
        float flapCur;
        float flapPrev;
        
        if (this.worldObj.isRemote)
        {
            flapCur = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            flapPrev = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (flapPrev <= -0.3F && flapCur >= -0.3F)
            {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
            }
        }

        this.prevAnimTime = this.animTime;
        float f2;

        if (this.getHealth() <= 0.0F)
        {
            float explosionX;
            float explosionY;
            
            explosionX = (this.rand.nextFloat() - 0.5F) * 8.0F;
            explosionY = (this.rand.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle("largeexplode", this.posX + (double)explosionX, this.posY + 2.0D + (double)explosionY, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            float animationTimeChange;
            
            animationTimeChange = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
            animationTimeChange *= (float)Math.pow(2.0D, this.motionY);

            this.animTime += animationTimeChange;

            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);

            if (this.ringBufferIndex < 0)
            {
                for (int i = 0; i < this.ringBuffer.length; ++i)
                {
                    this.ringBuffer[i][0] = (double)this.rotationYaw;
                    this.ringBuffer[i][1] = this.posY;
                }
            }

            if (++this.ringBufferIndex == this.ringBuffer.length)
            {
                this.ringBufferIndex = 0;
            }

            this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
            this.ringBuffer[this.ringBufferIndex][1] = this.posY;
            double interpolatedY2;
            double interpolatedZ2;
            double deltaYaw;
            double interpolatedX2;

            if (this.worldObj.isRemote)
            {
                if (this.newPosRotationIncrements > 0)
                {
                    interpolatedX2 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                    interpolatedY2 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                    interpolatedZ2 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                    deltaYaw = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + deltaYaw / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(interpolatedX2, interpolatedY2, interpolatedZ2);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
            }
            else
            {
                double targetOffsetX = this.targetX - this.posX;
                double targetOffsetY = this.targetY - this.posY;
                double targetOffsetZ = this.targetZ - this.posZ;
                double targetDistanceSquare = targetOffsetX * targetOffsetX + targetOffsetY * targetOffsetY + targetOffsetZ * targetOffsetZ;

                if (this.target != null)
                {
                    this.targetX = this.target.posX;
                    this.targetZ = this.target.posZ;
                    double newTargetOffsetX = this.targetX - this.posX;
                    double newTargetOffsetZ = this.targetZ - this.posZ;
                    double newTargetDistanceXZ = Math.sqrt(newTargetOffsetX * newTargetOffsetX + newTargetOffsetZ * newTargetOffsetZ);
                    double d8 = 0.4000000059604645D + newTargetDistanceXZ / 80.0D - 1.0D;

                    if (d8 > 10.0D)
                    {
                        d8 = 10.0D;
                    }

                    this.targetY = this.target.boundingBox.minY + d8;
                }
                else
                {
                    this.targetX += this.rand.nextGaussian() * 2.0D;
                    this.targetZ += this.rand.nextGaussian() * 2.0D;
                }

                if (this.forceNewTarget || (targetDistanceSquare < 100.0D && target==null) || targetDistanceSquare > 22500.0D || ((this.isCollidedHorizontally || this.isCollidedVertically) && target==null))
                {
                    this.setNewTarget();
                }

                double motionYTarget = (targetOffsetY / ((double)MathHelper.sqrt_double(targetOffsetX * targetOffsetX + targetOffsetZ * targetOffsetZ)));
                double maxMotionYTarget = .6;

                if (motionYTarget < (-maxMotionYTarget))
                {
                    motionYTarget = (-maxMotionYTarget);
                }

                if (motionYTarget > maxMotionYTarget)
                {
                    motionYTarget = maxMotionYTarget;
                }

                this.motionY += motionYTarget * 0.10000000149011612D;
                this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                double targetYaw = 180.0D - Math.atan2(targetOffsetX, targetOffsetZ) * 180.0D / Math.PI;
                double yawOffset = MathHelper.wrapAngleTo180_double(targetYaw - (double)this.rotationYaw);

                if (yawOffset > 50.0D)
                {
                    yawOffset = 50.0D;
                }

                if (yawOffset < -50.0D)
                {
                    yawOffset = -50.0D;
                }

                Vec3 vec3 = Vec3.createVectorHelper(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                Vec3 vec32 = Vec3.createVectorHelper((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
                float f5 = (float)(vec32.dotProduct(vec3) + 0.5D) / 1.5F;

                if (f5 < 0.0F)
                {
                    f5 = 0.0F;
                }

                this.randomYawVelocity *= 0.8F;
                float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
                double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

                if (d9 > 40.0D)
                {
                    d9 = 40.0D;
                }

                this.randomYawVelocity = (float)((double)this.randomYawVelocity + yawOffset * (0.699999988079071D / d9 / (double)f6));
                this.rotationYaw += this.randomYawVelocity * 0.1F;
                float f7 = (float)(2.0D / (d9 + 1.0D));
                float f8 = 0.06F;
                this.moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

                this.moveEntity(this.motionX, this.motionY, this.motionZ);

                Vec3 vec31 = Vec3.createVectorHelper(this.motionX, this.motionY, this.motionZ).normalize();
                float f9 = (float)(vec31.dotProduct(vec32) + 1.0D) / 2.0F;
                f9 = 0.8F + 0.15F * f9;
                this.motionX *= (double)f9;
                this.motionZ *= (double)f9;
                this.motionY *= 0.9100000262260437D;
            }

            this.renderYawOffset = this.rotationYaw;
            this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
            this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
            this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
            this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
            this.dragonPartBody.height = 3.0F;
            this.dragonPartBody.width = 5.0F;
            this.dragonPartWing1.height = 2.0F;
            this.dragonPartWing1.width = 4.0F;
            this.dragonPartWing2.height = 3.0F;
            this.dragonPartWing2.width = 4.0F;
            float flapPrev2 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
            f2 = MathHelper.cos(flapPrev2);
            float f10 = -MathHelper.sin(flapPrev2);
            float f3 = this.rotationYaw * (float)Math.PI / 180.0F;
            float f11 = MathHelper.sin(f3);
            float f4 = MathHelper.cos(f3);
            this.dragonPartBody.onUpdate();
            this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f11 * 0.5F), this.posY, this.posZ - (double)(f4 * 0.5F), 0.0F, 0.0F);
            this.dragonPartWing1.onUpdate();
            this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f11 * 4.5F), 0.0F, 0.0F);
            this.dragonPartWing2.onUpdate();
            this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f11 * 4.5F), 0.0F, 0.0F);

            if (!this.worldObj.isRemote && this.hurtTime == 0)
            {
                this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.boundingBox.expand(1.0D, 1.0D, 1.0D)));
            }

            double[] adouble1 = this.getMovementOffsets(5, 1.0F);
            double[] adouble = this.getMovementOffsets(0, 1.0F);
            float f12 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
            float f13 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
            this.dragonPartHead.onUpdate();
            this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f12 * 5.5F * f2), this.posY + (adouble[1] - adouble1[1]) * 1.0D + (double)(f10 * 5.5F), this.posZ - (double)(f13 * 5.5F * f2), 0.0F, 0.0F);

            for (int j = 0; j < 3; ++j)
            {
                EntityDragonPart entitydragonpart = null;

                if (j == 0)
                {
                    entitydragonpart = this.dragonPartTail1;
                }

                if (j == 1)
                {
                    entitydragonpart = this.dragonPartTail2;
                }

                if (j == 2)
                {
                    entitydragonpart = this.dragonPartTail3;
                }

                double[] adouble2 = this.getMovementOffsets(12 + j * 2, 1.0F);
                float f14 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (float)(j + 1) * 2.0F;
                entitydragonpart.onUpdate();
                entitydragonpart.setLocationAndAngles(this.posX - (double)((f11 * f17 + f15 * f18) * f2), this.posY + (adouble2[1] - adouble1[1]) * 1.0D - (double)((f18 + f17) * f10) + 1.5D, this.posZ + (double)((f4 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
            }
        }
    }

    /**
     * Attacks all entities inside this list, dealing 5 hearts of damage.
     */
    private void attackEntitiesInList(List p_70971_1_)
    {
        for (int i = 0; i < p_70971_1_.size(); ++i)
        {
            Entity entity = (Entity)p_70971_1_.get(i);

            if (entity instanceof EntityLivingBase)
            {
            	if(((EntityLivingBase) entity).isEntityUndead())
            		entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
            }
        }
    }

    /**
     * Sets a new target for the flight AI. It can be a random coordinate or a nearby undead.
     */
    private void setNewTarget()
    {
        this.forceNewTarget = false;

        updateSource();
        
        if (this.rand.nextInt(2) == 0)
        {
        	AxisAlignedBB aabb = this.boundingBox.expand(30, 30, 30);
            List<EntityLivingBase> ents = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
            Collections.shuffle(ents);
            for(EntityLivingBase entity : ents) {
            	if(entity.isEntityUndead()) {
            		this.target=entity;
            		return;
            	}
            }
        }
        else
        {
            boolean flag = false;

            do
            {
                this.targetX = source.posX;
                this.targetY = (double)(70.0F + this.rand.nextFloat() * 50.0F);
                this.targetZ = source.posZ;
                this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                double d0 = this.posX - this.targetX;
                double d1 = this.posY - this.targetY;
                double d2 = this.posZ - this.targetZ;
                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            }
            while (!flag);

            this.target = null;
        }
    }

    public void updateSource()
    {
        if(source==null) {
        	source = FateHandler.getEntityFromKey(key);
        }
    }

    /**
     * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
     */
    private float simplifyAngle(double p_70973_1_)
    {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }

    public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_)
    {
        if (p_70965_1_ != this.dragonPartHead)
        {
            p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
        }

        float f1 = this.rotationYaw * (float)Math.PI / 180.0F;
        float f2 = MathHelper.sin(f1);
        float f3 = MathHelper.cos(f1);
        this.targetX = this.posX + (double)(f2 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
        this.targetZ = this.posZ - (double)(f3 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.target = null;

        if (p_70965_2_.getEntity() instanceof EntityLivingBase || p_70965_2_.isExplosion())
        {
            this.func_82195_e(p_70965_2_, p_70965_3_);
        }

        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        return false;
    }

    protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_)
    {
        return super.attackEntityFrom(p_82195_1_, p_82195_2_);
    }

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts()
    {
        return this.dragonPartArray;
    }

    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound cmp)
    {
        super.writeEntityToNBT(cmp);
        cmp.setBoolean(TAG_IS_STORED, isStored);
        cmp.setInteger(TAG_KEY, key);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound cmp)
    {
        super.readEntityFromNBT(cmp);
        isStored=cmp.getBoolean(TAG_IS_STORED);
        key=cmp.getInteger(TAG_KEY);
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    public World func_82194_d()
    {
        return this.worldObj;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.enderdragon.growl";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.enderdragon.hit";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 5.0F;
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
		return 100;
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

	@Override
	public void setStored() {
		this.isStored=true;
	}

	@Override
	public boolean getStored() {
		return isStored;
	}
}
