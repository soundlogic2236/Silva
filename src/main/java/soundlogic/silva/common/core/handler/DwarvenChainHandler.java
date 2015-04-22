package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import soundlogic.silva.common.entity.ai.EntityAIChained;
import soundlogic.silva.common.item.ModItems;
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
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class DwarvenChainHandler {

	private final static String TAG_LEASH_UUID="SilvaLeashUUID";
	
	public static HashMap<EntityPlayer,UUID> playerLeashesUUIDs=new HashMap<EntityPlayer,UUID>();
	public static HashMap<EntityPlayer,EntityCreature> playerLeashes=new HashMap<EntityPlayer,EntityCreature>();
	public static HashMap<EntityCreature,EntityAIBase> AITasks=new HashMap<EntityCreature,EntityAIBase>();
	
	public static void attachChainToEntity(EntityCreature toLeash, Entity bindTo) {
		if(bindTo instanceof EntityDwarvenChainKnot) {
			attachChainToKnot(toLeash,(EntityDwarvenChainKnot) bindTo);
		}
		if(bindTo instanceof EntityPlayer) {
			attachChainToPlayer(toLeash,(EntityPlayer) bindTo);
		}
	}
	

	private static void attachChainToKnot(EntityCreature toLeash, EntityDwarvenChainKnot bindTo) {
		bindTo.setAttachedEntity(toLeash);
	}

	private static void attachChainToPlayer(EntityCreature toLeash, EntityPlayer bindTo) {
		bindTo.setCurrentItemOrArmor(0, setUUIDForLeash(bindTo.getHeldItem(),toLeash.getPersistentID()));
	}

	public static UUID getUUIDFromLeash(ItemStack stack) {
		if(stack!=null && stack.getItem() == ModItems.dwarfChain && stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_LEASH_UUID))
			return UUID.fromString(stack.getTagCompound().getString(TAG_LEASH_UUID));
		return null;
	}
	
	public static ItemStack setUUIDForLeash(ItemStack stack, UUID uuid) {
		NBTTagCompound cmp=new NBTTagCompound();
		ItemStack output=stack.copy();
		if(output.hasTagCompound())
			cmp=output.getTagCompound();
		cmp.setString(TAG_LEASH_UUID, uuid.toString());
		output.setTagCompound(cmp);
		return output;
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player=(EntityPlayer) event.entityLiving;
			UUID uuid=getUUIDFromLeash(player.getHeldItem());
			playerLeashesUUIDs.put(player, uuid);
		}
		else if ( event.entityLiving instanceof EntityCreature) {
			for(Entry<EntityPlayer, UUID> entry : playerLeashesUUIDs.entrySet()) {
				if(entry.getValue()!=null && entry.getValue().equals(event.entityLiving.getPersistentID())) {
					playerLeashes.put(entry.getKey(), (EntityCreature) event.entityLiving);
					handleLeash((EntityCreature) event.entityLiving,entry.getKey());
				}
			}
		}
	}

	private void handleLeash(EntityCreature entityLiving, Entity entity) {
		if(!AITasks.containsKey(entityLiving)) {
			EntityAIChained task = new EntityAIChained(entityLiving, 1.0D, entity);
			AITasks.put(entityLiving, task);
			entityLiving.tasks.addTask(2, task);
			entityLiving.getNavigator().setAvoidsWater(false);
		}
		
		float f = entityLiving.getDistanceToEntity(entity);
		
        if (f > 4.0F)
        {
        	entityLiving.getNavigator().tryMoveToEntityLiving(entity, 1.0D);
        }

        if (f > 6.0F)
        {
            double d0 = (entity.posX - entityLiving.posX) / (double)f;
            double d1 = (entity.posY - entityLiving.posY) / (double)f;
            double d2 = (entity.posZ - entityLiving.posZ) / (double)f;
            entityLiving.motionX += d0 * Math.abs(d0) * 0.4D;
            entityLiving.motionY += d1 * Math.abs(d1) * 0.4D;
            entityLiving.motionZ += d2 * Math.abs(d2) * 0.4D;
        }

        if (f > 10.0F)
        {
            double d0 = (entity.posX - entityLiving.posX) / (double)f;
            double d1 = (entity.posY - entityLiving.posY) / (double)f;
            double d2 = (entity.posZ - entityLiving.posZ) / (double)f;
            entityLiving.motionX += d0 * Math.abs(d0) * 0.8D;
            entityLiving.motionY += d1 * Math.abs(d1) * 0.8D;
            entityLiving.motionZ += d2 * Math.abs(d2) * 0.8D;
        }
    }
}
