package soundlogic.silva.common.entity;

import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFenrirEcho extends EntityMob implements IBossDisplayData, IEntityFateEcho {

	private static final String TAG_TARGET_UUID = "targetUUID";
	private static final String TAG_KEY = "fateKey";
	private int key;
	
	public static float SCALE_FACTOR = 3F;
	private UUID targetUUID;
	
	private EntityPlayer player;

	public EntityFenrirEcho(World p_i1685_1_) {
		super(p_i1685_1_);
        this.setSize(0.6F * SCALE_FACTOR, 0.8F * SCALE_FACTOR);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	}
	
	public void onUpdate() {
		super.onUpdate();
    	FateHandler.doUpdateForEcho(this);
    	if(this.isDead)
    		return;
		if(targetUUID==null) {
			EntityPlayer player = worldObj.getClosestPlayerToEntity(this, 40F);
			if(player!=null) {
				setPlayerTarget(player);
			}
		}
		if(player==null)
			player=(EntityPlayer) getAttackTarget();
	}
	
    private boolean checkForValidTarget() {
		if(player==null)
			return false;
		if(!player.worldObj.equals(worldObj))
			return false;
		if(this.getDistanceSqToEntity(player)>50*50)
			return false;
		return true;
	}

	public void setPlayerTarget(EntityPlayer player) {
		this.targetUUID=player.getGameProfile().getId();
	}

	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D*2);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(70.0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(9.0D);

    }
    
    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        return targetUUID==null ? null : this.worldObj.func_152378_a(targetUUID);
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound cmp)
    {
        super.writeEntityToNBT(cmp);
        if(targetUUID!=null)
        	cmp.setString(TAG_TARGET_UUID, this.targetUUID.toString());
        cmp.setInteger(TAG_KEY, key);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound cmp)
    {
        super.readEntityFromNBT(cmp);
        if(cmp.hasKey(TAG_TARGET_UUID))
        	this.targetUUID=UUID.fromString(cmp.getString(TAG_TARGET_UUID));
        else
        	this.targetUUID=null;
        key=cmp.getInteger(TAG_KEY);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.wolf.growl";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    public float getEyeHeight()
    {
        return this.height * 0.8F;
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
            	UUID other = ((EntityPlayer) entity).getGameProfile().getId();
            	if(other.equals(targetUUID)) {
            		ItemStack shoe = ((EntityPlayer) entity).getEquipmentInSlot(1);
            		if(shoe==null)
            			return false;
            		if(!shoe.isItemEnchanted())
            			return false;
            		return super.attackEntityFrom(source, amount);
            	}
            	else
            		return false;
            }
            else
            	return false;
        }
    }
	
    @SideOnly(Side.CLIENT)
    public float getTailRotation()
    {
        return 1.5393804F;
    }

    @Override
    public int getTotalArmorValue()
    {
        return 20;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        this.dropItem(ModItems.dwarfChain, 1);
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
		return 50;
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
