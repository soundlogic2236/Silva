package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import soundlogic.silva.common.entity.ai.EntityAIChained;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.network.MessageEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class DwarvenChainHandler {

	private final static String TAG_LEASH="SilvaLeash";
	
	public static HashMap<EntityCreature,EntityAIBase> AITasks=new HashMap<EntityCreature,EntityAIBase>();
	
	public static class LeashProperties implements IExtendedEntityProperties {

		private final static String TAG_LEASH_META="SilvaLeash";
		private final static String TAG_LEASH_UUID="SilvaLeashUUID";
		private final static String TAG_LEASH_SHARED_ID="SilvaLeashSharedID";
		private final static String TAG_LEASH_X="SilvaLeashX";
		private final static String TAG_LEASH_Y="SilvaLeashY";
		private final static String TAG_LEASH_Z="SilvaLeashZ";
		private final static String TAG_LEASH_ACTIVE="SilvaLeashActive";
		private final static String TAG_LEASH_KNOT="SilvaLeashKnot";

		Entity owner;
		
		Entity entity;
		int entitySharedID;
		UUID entityUUID;
		boolean active;
		boolean searching;
		int searchTicks;
		AxisAlignedBB searchSpace;
		boolean knot;
		boolean AI;
		boolean originalAvoidWater;
		
		public LeashProperties() {
			setStartingData();
		}
		
		public void setStartingData() {
			entitySharedID = -1;
			active = false;
			searching = false;
			searchTicks = 0;
			AI = false;
		}
		
		public LeashProperties(Entity entity) {
			this();
			this.attachToEntity(entity);
		}
		
		@Override
		public void saveNBTData(NBTTagCompound compound) {
			NBTTagCompound cmp = new NBTTagCompound();
			cmp.setBoolean(TAG_LEASH_ACTIVE, active);
			if(!active || entity == null)
				return;
			cmp.setDouble(TAG_LEASH_X, entity.posX);
			cmp.setDouble(TAG_LEASH_Y, entity.posY);
			cmp.setDouble(TAG_LEASH_Z, entity.posZ);
			cmp.setString(TAG_LEASH_UUID, entity.getPersistentID().toString());
			cmp.setBoolean(TAG_LEASH_KNOT, entity instanceof EntityDwarvenChainKnot);
			cmp.setInteger(TAG_LEASH_SHARED_ID, entity.getEntityId());
			compound.setTag(TAG_LEASH_META, cmp);
		}

		@Override
		public void loadNBTData(NBTTagCompound compound) {
			NBTTagCompound cmp = compound.getCompoundTag(TAG_LEASH_META);
			active=cmp.getBoolean(TAG_LEASH_ACTIVE);
			searching=searching && active;
			if(!active)
				return;
			double entityX = cmp.getDouble(TAG_LEASH_X);
			double entityY = cmp.getDouble(TAG_LEASH_Y);
			double entityZ = cmp.getDouble(TAG_LEASH_Z);
			entityUUID=UUID.fromString(cmp.getString(TAG_LEASH_UUID));
			entitySharedID=cmp.getInteger(TAG_LEASH_SHARED_ID);
			knot=cmp.getBoolean(TAG_LEASH_KNOT);
			
			searchSpace = AxisAlignedBB.getBoundingBox(entityX,entityY,entityZ,entityX,entityY,entityZ).expand(10, 10, 10);
		}

		@Override
		public void init(Entity entity, World world) {
			searching=true;
			owner=entity;
		}
		
		public void searchTick(Entity ent, boolean client) {
			if(client) {
				if(entity == null && entitySharedID != -1 ) {
					entity=ent.worldObj.getEntityByID(entitySharedID);
				}
				return;
			}
            searchTicks++;
			if(entity!=null && searchTicks == 10)
				update();
			if(!searching || ! active || entity != null)
				return;
            List<Entity> list;
            if(!knot)
            	list = ent.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, searchSpace);
            else
            	list = ent.worldObj.getEntitiesWithinAABB(EntityDwarvenChainKnot.class, searchSpace);
            for(Entity check : list) {
            	if(check.getPersistentID().equals(entityUUID)) {
            		entity = check;
            		searching=false;
            		return;
            	}
            }
            if(searchTicks>10) {
            	searching=false;
            }
		}
		
		public Entity getEntity() {
			return entity;
		}
		
		public boolean getActive() {
			return active;
		}
		
		public void setActive(boolean active) {
			this.active=active;
			update();
		}
		
		public void update() {
			if(!active)
				this.entity=null;
			MessageEntityData.updateExtendedEntityData(owner, DwarvenChainHandler.TAG_LEASH);
		}
		
		public void attachToEntity(Entity bindTo) {
			this.entity=bindTo;
			this.searching=false;
			this.setActive(true);
			
		}
	}
	
	public static void attachChainToEntity(EntityCreature toLeash, Entity bindTo) {
		LeashProperties props=(LeashProperties) toLeash.getExtendedProperties(TAG_LEASH);
		if(props==null) {
			props=new LeashProperties(bindTo);
			toLeash.registerExtendedProperties(TAG_LEASH, props);
		}
		else {
			props.setStartingData();
			props.attachToEntity(bindTo);
		}
	}
	
	public static boolean isChainAttached(EntityCreature creature, Entity holder) {
		if(holder instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) holder;
			return player.getHeldItem() !=null &&
					player.getHeldItem().getItem()==ModItems.dwarfChain &&
					!player.isDead &&
					player.worldObj == creature.worldObj;
				
		}
		if(holder instanceof EntityDwarvenChainKnot) {
			EntityDwarvenChainKnot knot = (EntityDwarvenChainKnot) holder;
			return knot.isChainAttached(creature);
		}
		return false;
	}
	
	public static LeashProperties getChainForEntity(EntityCreature creature) {
		return (LeashProperties) creature.getExtendedProperties(TAG_LEASH);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
	    if (event.entity instanceof EntityLiving)
	    {
	         event.entity.registerExtendedProperties(TAG_LEASH, new LeashProperties());
	    }
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entityLiving.worldObj.isRemote)
			return;
		if ( event.entityLiving instanceof EntityCreature) {
			EntityCreature creature = (EntityCreature) event.entityLiving;
			LeashProperties props = getChainForEntity(creature);
			if(props==null)
				return;
			props.searchTick(creature, false);
			if(!creature.worldObj.isRemote)
				handleLeash(creature, props);
		}
	}

	private void handleLeash(EntityCreature creature, LeashProperties props) {
		if(!props.getActive()) {
			if(props.AI) {
				EntityAIBase task = AITasks.get(creature);
				AITasks.remove(creature);
				creature.tasks.removeTask(task);
				creature.getNavigator().setAvoidsWater(props.originalAvoidWater);
				props.AI=false;
			}
			return;
		}
		Entity entity = props.getEntity();
		if(entity == null)
			return;
		
		if(!isChainAttached(creature, entity)) {
			props.setActive(false);
			return;
		}
		
		if(!props.AI) {
			EntityAIChained task = new EntityAIChained(creature, 1.0D, entity);
			AITasks.put(creature, task);
			creature.tasks.addTask(2, task);
			props.originalAvoidWater = creature.getNavigator().getAvoidsWater();
			creature.getNavigator().setAvoidsWater(false);
			props.AI=true;
		}
		
		float f = creature.getDistanceToEntity(entity);
		
        if (f > 4.0F)
        {
        	creature.getNavigator().tryMoveToEntityLiving(entity, 1.0D);
        }

        if (f > 6.0F)
        {
            double d0 = (entity.posX - creature.posX) / (double)f;
            double d1 = (entity.posY - creature.posY) / (double)f;
            double d2 = (entity.posZ - creature.posZ) / (double)f;
            creature.motionX += d0 * Math.abs(d0) * 0.4D;
            creature.motionY += d1 * Math.abs(d1) * 0.4D;
            creature.motionZ += d2 * Math.abs(d2) * 0.4D;
        }

        if (f > 10.0F)
        {
            double d0 = (entity.posX - creature.posX) / (double)6;
            double d1 = (entity.posY - creature.posY) / (double)6;
            double d2 = (entity.posZ - creature.posZ) / (double)6;
            creature.motionX += d0 * Math.abs(d0) * 0.8D;
            creature.motionY += d1 * Math.abs(d1) * 0.8D;
            creature.motionZ += d2 * Math.abs(d2) * 0.8D;
        }
    }
}
