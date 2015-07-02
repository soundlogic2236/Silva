package soundlogic.silva.common.entity;

import static soundlogic.silva.common.entity.EntityPixieProxy.PixieData.default_boundingBox;
import static soundlogic.silva.common.entity.EntityPixieProxy.PixieData.default_speed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.block.tile.multiblocks.PixieFarmTileData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityPixieProxy extends Entity {

	public int ticksSinceUpdate = 0;
	EntityPlayer lastInteractPlayer = null;
	PixieData pixieLink = null;
	
	public EntityPixieProxy(World p_i1582_1_) {
		super(p_i1582_1_);
		this.height=(float) MultiblockDataPixieFarm.FarmPixieData.default_height;
		this.width=(float) MultiblockDataPixieFarm.FarmPixieData.default_width;
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(!worldObj.isRemote) {
			ticksSinceUpdate++;
			if(ticksSinceUpdate>5)
				this.setDead();
		}
	}

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
	protected void entityInit() {
		// NO OP
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		// NO OP
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		// NO OP
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound cmp) {
		return false;
	}
    public boolean attackEntityFrom(DamageSource source, float damage) {
    	if(damage>0)
    		this.setDead();
    	return false;
    }
    
	@Override
    public boolean interactFirst(EntityPlayer player) {
		if(pixieLink!=null)
			return pixieLink.interact(player);
    	return false;
    }

	public void updateFromPixie(PixieData pixie, PixieGroupHandler data) {
		this.setPosition(pixie.posX, pixie.posY, pixie.posZ);
		this.boundingBox.setBB(pixie.getBoundingBox());
		if(this.isDead) {
			pixie.setDead(data);
		}
		if(pixie.isDead())
			this.setDead();
		pixieLink=pixie;
		this.ticksSinceUpdate=0;
	}
	
	public static class PixieData {
		private static List collidingBoundingBoxes = new ArrayList();
		final static double default_speed = 0.08F;
		public final static double default_width = .4;
		public final static double default_height = .4;
		final static AxisAlignedBB default_boundingBox = AxisAlignedBB.getBoundingBox(-default_width/2D, 0, -default_width/2D, default_width/2D, default_height, default_width/2D);
		public double posX, posY, posZ;
		public double prevPosX, prevPosY, prevPosZ;
		public double motionX, motionY, motionZ;
		public double prevMotionX, prevMotionY, prevMotionZ;
		public float rotation;
		public float prevRotation;
		public int ticks=0;
		boolean isDead = false;
		public int type = 0;
		public int proxySlot;

		public PixieData(PixieGroupHandler data) {
			data.maxProxySlot++;
			this.proxySlot=data.maxProxySlot;
			data.pixies.add(this);
		}
		
		
		public boolean interact(EntityPlayer player) {
			return false;
		}

		public void setDead(PixieGroupHandler data) {
			data.pixieDied=true;
			this.isDead=true;
		}
		
		public AxisAlignedBB getBoundingBox(double x, double y, double z) {
			return getBaseBoundingBox().copy().offset(x, y, z);
		}
		public  AxisAlignedBB getBoundingBox() {
			return getBoundingBox(posX,posY,posZ);
		}
		private AxisAlignedBB getBaseBoundingBox() {
			return default_boundingBox;
		}
		
		public double getSpeed() {
			return default_speed;
		}
		
		public boolean isDead() {
			return this.isDead;
		}

		public void tick(World world, PixieGroupHandler data) {
			ticks++;
			checkDead(world, data);
			if(this.isDead())
				return;
			this.prevPosX=this.posX;
			this.prevPosY=this.posY;
			this.prevPosZ=this.posZ;
			this.prevRotation=this.rotation;
			this.prevMotionX=this.motionX;
			this.prevMotionY=this.motionY;
			this.prevMotionZ=this.motionZ;
			if(useDefaultMovement()) {
				if(motionX!=0 || motionZ !=0)
					updateRotation();
				tickMotion(world);
			}
		}
		
		public void updateRotation() {
			this.rotation=(float) (Math.atan2(motionX, motionZ) / Math.PI * 180F);
		}

		public void clientTick(World world, PixieGroupHandler data) {
			// NO OP
		}

		private void tickMotion(World world) {
			double speed = getSpeed();
			double newX = posX+motionX*speed;
			double newY = posY+motionY*speed;
			double newZ = posZ+motionZ*speed;
			boolean shouldMove = true;
			boolean newBlocked = positionBlocked(world, newX, newY, newZ);
			if(newBlocked) {
				boolean curBlocked = positionBlocked(world, posX, posY, posZ);
				if(!curBlocked)
					shouldMove=false;
			}
			if(shouldMove) {
				this.posX=newX;
				this.posY=newY;
				this.posZ=newZ;
			}
			else {
				this.motionX*=-1;
				this.motionY*=-1;
				this.motionZ*=-1;

				newX = posX+motionX*speed;
				newY = posY+motionY*speed;
				newZ = posZ+motionZ*speed;
				
				newBlocked = positionBlocked(world, newX, newY, newZ);
				
				if(!newBlocked) {
					this.posX=newX;
					this.posY=newY;
					this.posZ=newZ;
				}
			}
		}

		private boolean positionBlocked(World world, double x, double y, double z) {
			if(world.isAirBlock(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)))
				return false;
			else if(world.getBlock(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)).isNormalCube())
				return true;
			else
				return !checkCollisions(world,getBoundingBox(x,y,z));
		}

		private static boolean checkCollisions(World world, AxisAlignedBB aabb) {
			collidingBoundingBoxes.clear();
	        int i = MathHelper.floor_double(aabb.minX);
	        int j = MathHelper.floor_double(aabb.maxX + 1.0D);
	        int k = MathHelper.floor_double(aabb.minY);
	        int l = MathHelper.floor_double(aabb.maxY + 1.0D);
	        int i1 = MathHelper.floor_double(aabb.minZ);
	        int j1 = MathHelper.floor_double(aabb.maxZ + 1.0D);

	        for (int k1 = i; k1 < j; ++k1)
	        {
	            for (int l1 = i1; l1 < j1; ++l1)
	            {
	                if (world.blockExists(k1, 64, l1))
	                {
	                    for (int i2 = k - 1; i2 < l; ++i2)
	                    {
	                        Block block;

	                        if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000)
	                        {
	                            block = world.getBlock(k1, i2, l1);
	                        }
	                        else
	                        {
	                            block = Blocks.bedrock;
	                        }
	                        
	                        block.addCollisionBoxesToList(world, k1, i2, l1, aabb, collidingBoundingBoxes , (Entity)null);
	                        if(!collidingBoundingBoxes.isEmpty())
	                        	return false;
	                    }
	                }
	            }
	        }
	        return true;
	    }
		
		private void checkDead(World world, PixieGroupHandler data) {
			if(getLifeSpan()!=-1 && this.ticks>getLifeSpan()) {
				setDead(data);
				return;
			}
			if(world.getBlock((int)posX, (int)posY, (int)posZ).isNormalCube()) {
				setDead(data);
				return;
			}
		}
		
		protected int getLifeSpan() {
			return -1;
		}

		protected void normVelocity() {
			double vel = Math.sqrt(motionX*motionX+motionY*motionY+motionZ*motionZ);
			if(vel!=0) {
				this.motionX = this.motionX/vel;
				this.motionY = this.motionY/vel;
				this.motionZ = this.motionZ/vel;
			}
			else {
				this.motionX=Math.random()-.5;
				this.motionY=Math.random()-.5;
				this.motionZ=Math.random()-.5;
				normVelocity();
			}
		}
		
		public boolean useDefaultMovement() {
			return true;
		}
		
		
		private static final String TAG_PIXIES_POS = "pixiesPos";
		private static final String TAG_PIXIES_MOT = "pixiesMot";
		private static final String TAG_PIXIES_TIME = "pixiesTime";
		private static final String TAG_PIXIES_TYPE = "pixiesType";
		private static final String TAG_PIXIE_ROTATION = "pixiesRotation";
		
		public void writeToNBT(NBTTagCompound cmp) {
			cmp.setDouble(TAG_PIXIES_POS+"x", posX);
			cmp.setDouble(TAG_PIXIES_POS+"y", posY);
			cmp.setDouble(TAG_PIXIES_POS+"z", posZ);
			cmp.setDouble(TAG_PIXIES_MOT+"x", motionX);
			cmp.setDouble(TAG_PIXIES_MOT+"y", motionY);
			cmp.setDouble(TAG_PIXIES_MOT+"z", motionZ);
			cmp.setInteger(TAG_PIXIES_TIME, ticks);
			cmp.setInteger(TAG_PIXIES_TYPE, type);
			cmp.setFloat(TAG_PIXIE_ROTATION, rotation);

			cmp.setDouble(TAG_PIXIES_POS+"px", prevPosX);
			cmp.setDouble(TAG_PIXIES_POS+"py", prevPosY);
			cmp.setDouble(TAG_PIXIES_POS+"pz", prevPosZ);
			cmp.setFloat(TAG_PIXIE_ROTATION+"p", prevRotation);
			cmp.setDouble(TAG_PIXIES_MOT+"px", prevMotionX);
			cmp.setDouble(TAG_PIXIES_MOT+"py", prevMotionY);
			cmp.setDouble(TAG_PIXIES_MOT+"pz", prevMotionZ);
		}
		
		public void readFromNBT(NBTTagCompound cmp) {
			posX=cmp.getDouble(TAG_PIXIES_POS+"x");
			posY=cmp.getDouble(TAG_PIXIES_POS+"y");
			posZ=cmp.getDouble(TAG_PIXIES_POS+"z");
			motionX=cmp.getDouble(TAG_PIXIES_MOT+"x");
			motionY=cmp.getDouble(TAG_PIXIES_MOT+"y");
			motionZ=cmp.getDouble(TAG_PIXIES_MOT+"z");
			ticks=cmp.getInteger(TAG_PIXIES_TIME);
			type=cmp.getInteger(TAG_PIXIES_TYPE);
			rotation=cmp.getFloat(TAG_PIXIE_ROTATION);

			prevPosX=cmp.getDouble(TAG_PIXIES_POS+"px");
			prevPosY=cmp.getDouble(TAG_PIXIES_POS+"py");
			prevPosZ=cmp.getDouble(TAG_PIXIES_POS+"pz");
			prevRotation=cmp.getFloat(TAG_PIXIE_ROTATION+"p");
			prevMotionX=cmp.getDouble(TAG_PIXIES_MOT+"px");
			prevMotionY=cmp.getDouble(TAG_PIXIES_MOT+"py");
			prevMotionZ=cmp.getDouble(TAG_PIXIES_MOT+"pz");
		}

		public boolean useDopplegangerRendering() {
			return this.type==1;
		}
		
		public void doParticles(World world) {
			if(!this.useDopplegangerRendering())
				for(int i = 0; i < 4; i++)
					BotaniaAccessHandler.sparkleFX(world, posX + (Math.random() - 0.5) * 0.25, posY + default_height/2D  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
		}
		
		public void doAttackBurst(World world) {
			if(!this.useDopplegangerRendering())
				for(int i = 0; i < 12; i++)
					BotaniaAccessHandler.sparkleFX(world, posX + (Math.random() - 0.5) * 0.25, posY + default_height/2D  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
		}
		public IEquipmentRenderData getEquipment() {
			return null;
		}
		
		public boolean shouldDispayName() {
			return false;
		}
		
		public String getDisplayName() {
			return "";
		}

	}
	
	public static interface IEquipmentRenderData {
		public ItemStack getEquipment();
		public ItemStack getHelmet();
		public int getItemInUseCount();
		public float getPrevSwingProgress();
		public float getSwingProgress();
		public float getPrevAimYawOffset();
		public float getAimYawOffset();
		public float getPrevAimPitchOffset();
		public float getAimPitchOffset();
		public float getTicksExisted();
		public boolean isSneaking();
		public boolean shouldRenderHeld();
		public boolean shouldRenderHelmet();
	}
	public static class EquipmentRenderDataSimple implements IEquipmentRenderData {

		ItemStack helmet = null;
		ItemStack equipment = null;
		boolean sneaking = false;
		int ticks = 0;
		float prevSwingProgress = 0;
		float swingProgress = 0;
		int itemInUseCount = 0;
		boolean justFinishedUsingItem = false;
		boolean isSwingInProgress = false;
		int swingProgressInt = 0;
		
		@Override
		public ItemStack getHelmet() {
			return helmet;
		}

		public void setHelmet(ItemStack stack) {
			this.helmet=stack;
		}
		
		@Override
		public ItemStack getEquipment() {
			return equipment;
		}
		
		public void setEquipment(ItemStack stack) {
			this.equipment=stack;
			if(stack==null) {
				itemInUseCount=0;
				isSwingInProgress=false;
				swingProgressInt=0;
				swingProgress=0;
				prevSwingProgress=0;
			}
		}

		@Override
		public int getItemInUseCount() {
			return itemInUseCount;
		}

		@Override
		public float getPrevSwingProgress() {
			return prevSwingProgress;
		}

		@Override
		public float getSwingProgress() {
			return swingProgress;
		}

		@Override
		public float getPrevAimYawOffset() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getAimYawOffset() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getPrevAimPitchOffset() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getAimPitchOffset() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void startSwing() {
			if(equipment==null)return;
			isSwingInProgress=true;
		}
		
		public void tick() {
			this.ticks++;
			prevSwingProgress=swingProgress;
	        int i = this.getArmSwingAnimationLength();

	        if (this.isSwingInProgress)
	        {
	            ++this.swingProgressInt ;

	            if (this.swingProgressInt >= i)
	            {
	                this.swingProgressInt = 0;
	                this.isSwingInProgress = false;
	            }
	        }
	        else
	        {
	            this.swingProgressInt = 0;
	        }

	        this.swingProgress = (float)this.swingProgressInt / (float)i;

			justFinishedUsingItem=false;
			if(itemInUseCount>0) {
				itemInUseCount--;
				if(itemInUseCount==0)
					justFinishedUsingItem=true;
			}
		}
		
		private int getArmSwingAnimationLength() {
			return 6;
		}

		public void startUsingItem() {
			if(equipment==null)return;
			itemInUseCount=equipment.getItem().getMaxItemUseDuration(equipment);
		}
		
		public void endUsingItem() {
			if(equipment==null)return;
			itemInUseCount=0;
		}
		
		public void toggleBlocking() {
			if(equipment==null)return;
			if(equipment.getItemUseAction()==EnumAction.block) {
				if(itemInUseCount>0)
					endUsingItem();
				else
					startUsingItem();
			}
		}
		
		public boolean didJustFinishUsingItem() {
			return justFinishedUsingItem;
		}

		@Override
		public float getTicksExisted() {
			return ticks;
		}
		
		public void setSneaking(boolean sneaking) {
			this.sneaking=sneaking;
		}

		@Override
		public boolean isSneaking() {
			return sneaking;
		}

		@Override
		public boolean shouldRenderHeld() {
			return equipment!=null;
		}

		@Override
		public boolean shouldRenderHelmet() {
			return helmet!=null;
		}
		
		
		private final String EQUIPMENT = "equipmentRenderEquipment";
		private final String SNEAKING = "equipmentRenderSneaking";
		private final String TICKS = "equipmentRenderTicks";
		private final String PREVSWINGPROGRESS = "equipmentRenderPrevSwingProgress";
		private final String SWINGPROGRESS = "equipmentRenderSwingProgress";
		private final String ITEMINUSECOUNT = "equipmentRenderItemInUseCount";
		private final String JUSTFINISHEDUSINGITEM = "equipmentRenderJustFinishedUsingItemCount";
		private final String ISSWINGINPROGRESS = "equipmentRenderIsSwingInProgress";
		private final String SWINGPROGRESSINT = "equipmentRenderSwingProgressInt";
		
		public void writeToNBT(NBTTagCompound cmp) {
			if(equipment!=null) {
				NBTTagCompound item = new NBTTagCompound();
				equipment.writeToNBT(item);
				cmp.setTag(EQUIPMENT, item);
			}
			cmp.setBoolean(SNEAKING, sneaking);
			cmp.setInteger(TICKS, ticks);
			cmp.setFloat(PREVSWINGPROGRESS, prevSwingProgress);
			cmp.setFloat(SWINGPROGRESS, swingProgress);
			cmp.setInteger(ITEMINUSECOUNT, itemInUseCount);
			cmp.setBoolean(JUSTFINISHEDUSINGITEM, justFinishedUsingItem);
			cmp.setBoolean(ISSWINGINPROGRESS, isSwingInProgress);
			cmp.setInteger(SWINGPROGRESSINT, swingProgressInt);
		}
		
		public void readFromNBT(NBTTagCompound cmp) {
			if(cmp.hasKey(EQUIPMENT))
				equipment=ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(EQUIPMENT));
			else
				equipment=null;
			sneaking=cmp.getBoolean(SNEAKING);
			ticks=cmp.getInteger(TICKS);
			prevSwingProgress=cmp.getFloat(PREVSWINGPROGRESS);
			swingProgress=cmp.getFloat(SWINGPROGRESS);
			itemInUseCount=cmp.getInteger(ITEMINUSECOUNT);
			justFinishedUsingItem=cmp.getBoolean(JUSTFINISHEDUSINGITEM);
			isSwingInProgress=cmp.getBoolean(ISSWINGINPROGRESS);
			swingProgressInt=cmp.getInteger(SWINGPROGRESSINT);
		}

	}
	
	public static abstract class PixieGroupHandler<PixieType extends PixieData> {
		
		private static final String TAG_PIXIES = "pixies";

		public List<PixieType> pixies = new ArrayList<PixieType>();
		boolean pixieDied = false;
		public boolean pixieDiedLastTick = false;

		public List<EntityPixieProxy> pixieProxies = new ArrayList<EntityPixieProxy>();
		public int maxProxySlot = -1;
		
		public void writeToNBT(NBTTagCompound cmp) {
			cmp.setInteger(TAG_PIXIES, pixies.size());
			for(int i = 0 ; i < pixies.size() ; i++) {
				PixieType pixie = pixies.get(i);
				NBTTagCompound pix = new NBTTagCompound();
				pixie.writeToNBT(pix);
				cmp.setTag(TAG_PIXIES+i, pix);
			}
		}

		public void readFromNBT(NBTTagCompound cmp) {
			pixies.clear();
			int size = cmp.getInteger(TAG_PIXIES);
			for(int i = 0 ; i< size; i++) {
				PixieType pixie = create();
				pixie.readFromNBT(cmp.getCompoundTag(TAG_PIXIES+i));
			}
		}
		
		public void tick(World world) {
			pixieDiedLastTick=false;
			tickPixies(world);
			cleanDeadPixies();
			updatePixieProxiesList(world);
			updatePixieProxies();
			cleanDeadPixies();
		}
		public void clientTick(World world) {
			for(PixieType pixie : pixies) {
				pixie.clientTick(world, this);
				pixie.doParticles(world);
			}
		}
		
		private void tickPixies(World world) {
			for(PixieType pixie : pixies) {
				pixie.tick(world, this);
			}
		}

		private void cleanDeadPixies() {
			if(!pixieDied)
				return;
			pixieDied=false;
			pixieDiedLastTick=true;
			Iterator<PixieType> iter = pixies.iterator();
			while(iter.hasNext()) {
				if(iter.next().isDead())
					iter.remove();
			}
		}

		private void updatePixieProxies() {
			for(PixieType pixie : pixies) {
				EntityPixieProxy proxy = pixieProxies.get(pixie.proxySlot);
				proxy.updateFromPixie(pixie, this);
			}
		}
		
		private void updatePixieProxiesList(World world) {
			int pixieCount = pixies.size();
			int proxyCount = pixieProxies.size();
			if(maxProxySlot>=proxyCount) {
				for(int i = proxyCount ; i <= maxProxySlot ; i++) {
					pixieProxies.add(null);
				}
				for(PixieType pixie : pixies) {
					if(pixie.proxySlot>=proxyCount) {
						EntityPixieProxy proxy = new EntityPixieProxy(world);
						proxy.forceSpawn=true;
						proxy.updateFromPixie(pixie, this);
						world.spawnEntityInWorld(proxy);
						pixieProxies.set(pixie.proxySlot, proxy);
					}
				}
			}
			if(maxProxySlot>pixieCount+10)
				cleanPixieProxies();
		}

		private void cleanPixieProxies() {
			ArrayList<EntityPixieProxy> newList = new ArrayList<EntityPixieProxy>();
			maxProxySlot=-1;
			for(PixieType pixie : pixies) {
				EntityPixieProxy proxy = pixieProxies.get(pixie.proxySlot);
				maxProxySlot++;
				pixie.proxySlot=maxProxySlot;
				newList.add(proxy);
			}
			pixieProxies=newList;
		}
		
		protected abstract PixieType create();

	}
}
