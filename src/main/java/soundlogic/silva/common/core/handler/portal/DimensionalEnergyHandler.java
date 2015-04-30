package soundlogic.silva.common.core.handler.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.DwarvenChainHandler;
import soundlogic.silva.common.core.handler.DwarvenChainHandler.LeashProperties;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import soundlogic.silva.common.network.MessageEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class DimensionalEnergyHandler {
	
	static Random random = new Random();
	
	private final static String TAG_DIMENSIONAL_EXPOSURE = "silvaDimensionalExposure";
	
	private PotionEffect effect = new PotionEffect(0,0);
	
	public static class DimensionalExposureProperties implements IExtendedEntityProperties {

		HashMap<Dimension, Integer> exposures = new HashMap<Dimension, Integer>();
		
		boolean active=false;
		
		private final static String TAG_AMOUNT = "amount";
		private final static String TAG_ACTIVE = "active";
		
		private EntityLivingBase ent;
		
		@Override
		public void saveNBTData(NBTTagCompound compound) {
			NBTTagCompound cmp = new NBTTagCompound();
			for(int i = 0; i < Dimension.values().length;i++) {
				cmp.setInteger(TAG_AMOUNT+i, getExposureLevel(Dimension.values()[i]));
			}
			cmp.setBoolean(TAG_ACTIVE, active);
			compound.setTag(DimensionalEnergyHandler.TAG_DIMENSIONAL_EXPOSURE, cmp);
		}

		@Override
		public void loadNBTData(NBTTagCompound compound) {
			NBTTagCompound cmp = compound.getCompoundTag(DimensionalEnergyHandler.TAG_DIMENSIONAL_EXPOSURE);
			for(int i = 0; i < Dimension.values().length;i++) {
				setExposureLevel(Dimension.values()[i],cmp.getInteger(TAG_AMOUNT+i));
			}
			active=cmp.getBoolean(TAG_ACTIVE);
		}

		@Override
		public void init(Entity entity, World world) {
			ent=(EntityLivingBase) entity;
		}
		
		public int getExposureLevel(Dimension dim) {
			if(exposures.containsKey(dim))
				return exposures.get(dim);
			return 0;
		}
		
		public void setExposureLevel(Dimension dim, int level) {
			exposures.put(dim, Math.max(-1, level));
			if(level == -1)
				updateActive();
			else
				active=true;
		}
		
		public void addExposure(Dimension dim, int amount) {
			setExposureLevel(dim,getExposureLevel(dim)+amount);
		}
		
		public void decrementExposure(Dimension dim) {
			if(active)
				addExposure(dim,-1);
		}
		
		public void clear() {
			for(int i = 0; i < Dimension.values().length;i++) {
				setExposureLevel(Dimension.values()[i],0);
			}
		}
		
		public void updateActive() {
			for(int i = 0; i < Dimension.values().length;i++) {
				if(getExposureLevel(Dimension.values()[i])!=-1) {
					active=true;
					return;
				}
			}
			active=false;
		}

	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
	    if (event.entity instanceof EntityLivingBase)
	    {
	         event.entity.registerExtendedProperties(TAG_DIMENSIONAL_EXPOSURE, new DimensionalExposureProperties());
	    }
	}
	
	public static void applyPortalTick(TilePortalCore core) {
		Dimension dim = core.getDimension();
		if(dim.getState()==State.LOCKED)
			return;
		World world = core.getWorldObj();
		if(core.upgradesPermitEntityExposureTicks()) {
			AxisAlignedBB aabb = getEnergyBoundingBox(core);
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			for(EntityLivingBase ent : ents) {
				getExposureData(ent).addExposure(dim, 3);
			}
		}
		if(core.upgradesPermitBlockExposureTicks()) {
			IDimensionalBlockHandler handler = core.getDimension().getBlockHandler();
			AxisAlignedBB aabb = getEnergyBoundingBoxForBlocks(core);
			if(handler!=null)
			{
				if((core.getTicksOpen() % handler.frequencyForSearch(core))==0 && handler.shouldTryApply(core)) {
					int blocks = handler.getBlocksPerTick(core);
					while(blocks!=0) {
						int tries = handler.triesPerBlock(core);
						while(tries > 0) {
							int[] coords = getRandomBlockInBox(aabb);
							if(handler.tryApplyToBlock(core, world, coords)) {
								tries=0;
							}
							else
								tries--;
						}
						blocks--;
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void applyGeneralTick(LivingUpdateEvent event) {
		DimensionalExposureProperties props = getExposureData(event.entityLiving);
		for(Dimension dim :Dimension.values()) {
			IDimensionalExposureHandler handler = dim.getExposureHandler();
			if(handler!=null)
				handler.onEntityUpdate(event.entityLiving, props.getExposureLevel(dim));
			props.decrementExposure(dim);
		}
		if(props.active)
			MessageEntityData.updateExtendedEntityData(event.entityLiving, TAG_DIMENSIONAL_EXPOSURE);
			
	}
	@SubscribeEvent
	public void milkBucket(PlayerUseItemEvent.Finish event) {
		boolean clears=false;
		for(ItemStack stack : effect.getCurativeItems()) {
			if(event.item.areItemStacksEqual(event.item, stack)) {
				clears=true;
				break;
			}
		}
		if(clears)
		{
			getExposureData(event.entityPlayer).clear();
			MessageEntityData.updateExtendedEntityData(event.entityPlayer, TAG_DIMENSIONAL_EXPOSURE);
		}
	}
	
	public static DimensionalExposureProperties getExposureData(EntityLivingBase entity) {
		return (DimensionalExposureProperties) entity.getExtendedProperties(TAG_DIMENSIONAL_EXPOSURE);
	}
	
	public static int getExposureLevel(EntityLivingBase entity, Dimension dim) {
		return getExposureData(entity).getExposureLevel(dim);
	}
	
	public static int getWidthOffset(TilePortalCore core) {
		return 2;
	}
	public static int getHeightOffset(TilePortalCore core) {
		return 2;
	}
	public static int getLength(TilePortalCore core) {
		return 8;
	}
	
	public static int[] getRandomBlockInBox(AxisAlignedBB aabb) {
		int x = (int) (aabb.minX + random.nextInt((int) (aabb.maxX-aabb.minX)));
		int y = (int) (aabb.minY + random.nextInt((int) (aabb.maxY-aabb.minY)));
		int z = (int) (aabb.minZ + random.nextInt((int) (aabb.maxZ-aabb.minZ)));
		return new int[]{x,y,z};
	}
	
	public static AxisAlignedBB getEnergyBoundingBox(TilePortalCore core) {
		int widthOffset = getWidthOffset(core);
		int heightOffset = getHeightOffset(core);
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(core.xCoord - widthOffset, core.yCoord + 2 - heightOffset, core.zCoord, core.xCoord + 1 + widthOffset, core.yCoord + 3 + heightOffset, core.zCoord + 1);
		ForgeDirection direction = core.getDirection().getOpposite();
		if(direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
			aabb = AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord + 2 - heightOffset, core.zCoord - widthOffset, core.xCoord + 1, core.yCoord + 3 + heightOffset, core.zCoord + 1 + widthOffset);
		int length = getLength(core);
		aabb.minX += direction.offsetX < 0 ? direction.offsetX * length : 0;
		aabb.minY += direction.offsetY < 0 ? direction.offsetY * length : 0;
		aabb.minZ += direction.offsetZ < 0 ? direction.offsetZ * length : 0;
		aabb.maxX += direction.offsetX > 0 ? direction.offsetX * length : 0;
		aabb.maxY += direction.offsetY > 0 ? direction.offsetY * length : 0;
		aabb.maxZ += direction.offsetZ > 0 ? direction.offsetZ * length : 0;

		return aabb;
	}
	public static AxisAlignedBB getEnergyBoundingBoxForBlocks(TilePortalCore core) {
		int widthOffset = getWidthOffset(core);
		int heightOffset = getHeightOffset(core);
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(core.xCoord - widthOffset, core.yCoord + 2 - heightOffset, core.zCoord, core.xCoord + 1 + widthOffset, core.yCoord + 3 + heightOffset, core.zCoord + 1);
		ForgeDirection direction = core.getDirection().getOpposite();
		if(direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
			aabb = AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord + 2 - heightOffset, core.zCoord - widthOffset, core.xCoord + 1, core.yCoord + 3 + heightOffset, core.zCoord + 1 + widthOffset);
		int length = getLength(core);
		aabb.minX += direction.offsetX < 0 ? direction.offsetX * length : direction.offsetX;
		aabb.minY += direction.offsetY < 0 ? direction.offsetY * length : direction.offsetY;
		aabb.minZ += direction.offsetZ < 0 ? direction.offsetZ * length : direction.offsetZ;
		aabb.maxX += direction.offsetX > 0 ? direction.offsetX * length : direction.offsetX;
		aabb.maxY += direction.offsetY > 0 ? direction.offsetY * length : direction.offsetY;
		aabb.maxZ += direction.offsetZ > 0 ? direction.offsetZ * length : direction.offsetZ;

		return aabb;
	}
}
