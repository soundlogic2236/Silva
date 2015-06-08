package soundlogic.silva.common.entity;

import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpook extends EntityMob {

	private static double mx, my, mz;
	
	final String TAG_LEVEL = "level";
	
	protected int level = -1;
	boolean attributesSet = false;

    private Entity targetedEntity;
	
	public EntitySpook(World world) {
		super(world);
		this.noClip=true;
		this.height=.35F;
		this.setSize(height, height);
	}
	public EntitySpook(World world, int level) {
		this(world);
		this.level=level;
		this.applyAttributesFromLevel();
		this.initPostAttributes();
	}
	
	public void applyAttributesFromLevel() {
		if(this.level==-1)
			return;
		attributesSet = true;
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getMaxHealthByLevel());
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(getAttackByLevel());
        this.experienceValue=getXPByLevel();
	}

	public void initPostAttributes() {
        this.setHealth(this.getMaxHealth());
	}
	

	@Override
    public void onUpdate() {
		if(!attributesSet)
			applyAttributesFromLevel();
		super.onUpdate();
	}
	
	@Override
    public void onLivingUpdate()
    {
		mx = this.motionX;
		my = this.motionY;
		mz = this.motionZ;
		super.onLivingUpdate();
		for(int i = 0 ; i < 2 ; i++) {
			doParticles();
		}
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
//		return super.shouldRenderInPass(pass);
	}
	
	public void doParticles() {
		doParticles(level);
	}

	public void doParticles(int level) {
		doParticles(level, this.worldObj, this.posX, this.posY, this.posZ, this.ticksExisted);
	}
	
	public static void doParticles(int level, World world, double x, double y, double z, int ticks) {
		if(ticks % 2 == 0) {
			world.spawnParticle("mobSpell", 
					x+(Math.random()-.5)/3D, 
					y+(Math.random()-.5)/3D, 
					z+(Math.random()-.5)/3D, 0, 0, 0);
		}
		if(level>0 && ticks % 3 == 0) {
			for(int i = 0 ; i < 2 ; i++) {
				world.spawnParticle("portal", 
						x+(Math.random()-.5)/3D, 
						y+(Math.random()-.5)/3D, 
						z+(Math.random()-.5)/3D, 0, 0, 0);
			}
		}
		if(level>1) {
			for(int i = 0 ; i < 3 ; i++) {
				world.spawnParticle("enchantmenttable", 
						x+(Math.random()-.5)/3D, 
						y+(Math.random()*1.2-.3)/3D, 
						z+(Math.random()-.5)/3D, 0, 0, 0);
			}
		}
	}

	@Override
    protected void updateEntityActionState()
    {
    	double speedScale = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    	float maxSpeed = (float) (getMaxSpeedByLevel() * speedScale);
    	float maxAcceleration = (float) (getAccelerationByLevel() * speedScale);
        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
        if (this.targetedEntity != null && this.targetedEntity.isDead)
        {
            this.targetedEntity = null;
        }
        if (this.targetedEntity == null)
        {
            this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);
        }
        else if (this.targetedEntity.isEntityAlive())
        {
            float f = this.targetedEntity.getDistanceToEntity(this);
            this.attackEntity(this.targetedEntity, f);
        }

        Vector3 targetMotion = new Vector3();
        if (this.targetedEntity != null) {
        	targetMotion = new Vector3(
        			this.targetedEntity.posX-this.posX,
        			this.targetedEntity.posY+this.targetedEntity.height*2D/3D-this.posY,
        			this.targetedEntity.posZ-this.posZ);
        	if(targetMotion.magSquared() > maxSpeed * maxSpeed)
        		targetMotion = targetMotion.normalize().multiply(maxSpeed);
        }
        Vector3 deltaMotion = targetMotion.subtract(new Vector3(mx, my, mz));
        if(deltaMotion.magSquared() > maxAcceleration*maxAcceleration)
        	deltaMotion=deltaMotion.normalize().multiply(maxAcceleration);
        mx+=deltaMotion.x;
        my+=deltaMotion.y;
        mz+=deltaMotion.z;
        this.motionX=mx;
        this.motionY=my;
        this.motionZ=mz;
        this.velocityChanged=true;
    }
	
    @Override
    public void writeEntityToNBT(NBTTagCompound cmp) {
    	super.writeEntityToNBT(cmp);
    	cmp.setInteger(TAG_LEVEL, level);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound cmp) {
    	super.readEntityFromNBT(cmp);
    	this.level=cmp.getInteger(TAG_LEVEL);
    }
    
    public float getAccelerationByLevel() {
    	return getAccelerationByLevel(level);
    }
    public float getAccelerationByLevel(int level) {
    	switch(level) {
    	case 0: return convertRadiusToAcceleration(getMaxSpeedByLevel(level), 3F);
    	case 1: return convertRadiusToAcceleration(getMaxSpeedByLevel(level), 2F);
    	case 2: return convertRadiusToAcceleration(getMaxSpeedByLevel(level), 7F);
    	}
    	return 0;
    }
    
    private static float convertRadiusToAcceleration(float velocity, float radius) {
    	return velocity*velocity/radius;
    }
    
    public float getMaxSpeedByLevel() {
    	return getMaxSpeedByLevel(level);
    }
    public float getMaxSpeedByLevel(int level) {
    	switch(level) {
    	case 0: return .10F;
    	case 1: return .20F;
    	case 2: return .40F;
    	}
    	return 0;
    }
    
    public float getMaxHealthByLevel() {
    	return getMaxHealthByLevel(level);
    }
    public float getMaxHealthByLevel(int level) {
    	switch(level) {
    	case 0: return 15;
    	case 1: return 40;
    	case 2: return 100;
    	}
    	return 0;
    }
    
    public float getAttackByLevel() {
    	return getAttackByLevel(level);
    }
    public float getAttackByLevel(int level) {
    	switch(level) {
    	case 0: return 1;
    	case 1: return 2;
    	case 2: return 4;
    	}
    	return 0;
    }
	
    public int getXPByLevel() {
    	return getXPByLevel(level);
    }
    public int getXPByLevel(int level) {
    	switch(level) {
    	case 0: return 3;
    	case 1: return 7;
    	case 2: return 15;
    	}
    	return 0;
    }
	
	@Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {}
    @Override
    protected void fall(float p_70069_1_) {}
    @Override
    public void setInWeb() {}
    @Override
    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {}
    @Override
    public boolean isOnLadder()
    {
        return false;
    }
    @Override
    public boolean isInWater() {
    	return false;
    }
    @Override
    public boolean handleLavaMovement() {
    	return false;
    }
    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {}
}
