package soundlogic.silva.common.block.tile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibReflectionNames;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileSlingshot extends TileMod implements IWandBindable, IForestClientTick, IInventory {

	private static final Method getProjectileEntity = ReflectionHelper.findMethod(BehaviorProjectileDispense.class, null, LibReflectionNames.getProjectileEntity, World.class, IPosition.class);
	private static final Method getLaunchVelocity = ReflectionHelper.findMethod(BehaviorProjectileDispense.class, null, LibReflectionNames.getLaunchVelocity);
	private static final Method getGravityVelocity = ReflectionHelper.findMethod(EntityThrowable.class, null, LibReflectionNames.getGravityVelocity);
	private static HashMap<Item, Boolean> canLaunch = new HashMap<Item, Boolean>();
	
	private static final String TAG_STACK = "stack";
	private static final String TAG_TARGET = "target";
	private static final String TAG_TARGET_BLOCK = "targetBlock";
	private static final String TAG_ROTATION = "rotation";
	private static final String TAG_FIRE_ANGLE = "fireAngle";
	private static final String TAG_LAUNCH_MULTIPLIER = "launchMultiplier";
	private static final String TAG_GRAVITY = "gravity";
	private static final String TAG_SOLUTION = "solution";
	private static final String TAG_NEEDS_TRAJECTORY = "trajectory";
	private static final String TAG_CLIENT_VELOCITY = "cvelocity";
	
	private EntityThrowable sampleProjectile = null;
	private ItemStack currentStack = null;
	private Vector3 target = Vector3.zero.copy();
	private int targetX = 0;
	private int targetY = 0;
	private int targetZ = 0;

	public float rotation = 0;
	public float fireAngle = 0;
	public float launchMultiplier = 0;
	private float gravity = 0;
	public boolean solution = false;
	
	boolean needsTrajectory = false;
	double client_velocity_x = 0;
	double client_velocity_y = 0;
	double client_velocity_z = 0;
	
	List<Vector3> trajectory = new ArrayList();

	@Override
	public void updateEntity() {
		if(!Silva.proxy.ForestWandRenderers.contains(this))
			Silva.proxy.ForestWandRenderers.add(this);
		if(this.needsTrajectory || (solution && trajectory.isEmpty()))
			this.rebuildTrajectory();
	}

	public boolean fireProjectile(EntityPlayer player) {
		if(currentStack==null || !solution)
			return false;
		if(worldObj.isRemote)
			return true;
		EntityThrowable throwable = (EntityThrowable) getProjectileEntity(currentStack);
		throwable.setThrowableHeading(Math.sin(rotation*Math.PI/180D)*Math.sin(fireAngle*Math.PI/180D), Math.cos(fireAngle*Math.PI/180D), -Math.cos(rotation*Math.PI/180D)*Math.sin(fireAngle*Math.PI/180D), launchMultiplier, 0);
		if(player!=null) {
			NBTTagCompound access = new NBTTagCompound();
			throwable.writeEntityToNBT(access);
			access.setString("ownerName", player.getCommandSenderName());
			throwable.readEntityFromNBT(access);
		}
		int x = MathHelper.floor_double(throwable.posX);
		int y = MathHelper.floor_double(throwable.posY);
		int z = MathHelper.floor_double(throwable.posZ);
		while(x==xCoord && y==yCoord && z==zCoord) {
			throwable.setPosition(
					throwable.posX+throwable.motionX,
					throwable.posY+throwable.motionY,
					throwable.posZ+throwable.motionZ);
			throwable.motionY-=gravity;
			x = MathHelper.floor_double(throwable.posX);
			y = MathHelper.floor_double(throwable.posY);
			z = MathHelper.floor_double(throwable.posZ);
		}
		worldObj.spawnEntityInWorld(throwable);
		this.currentStack=null;
		return true;
	}
	
	
	public void writeCustomNBT(NBTTagCompound cmp) {
		if(currentStack!=null) {
			NBTTagCompound item = new NBTTagCompound();
			currentStack.writeToNBT(item);
			cmp.setTag(TAG_STACK, item);
		}
		cmp.setDouble(TAG_TARGET+"x", target.x);
		cmp.setDouble(TAG_TARGET+"y", target.y);
		cmp.setDouble(TAG_TARGET+"z", target.z);
		cmp.setInteger(TAG_TARGET_BLOCK+"x", targetX);
		cmp.setInteger(TAG_TARGET_BLOCK+"y", targetY);
		cmp.setInteger(TAG_TARGET_BLOCK+"z", targetZ);
		cmp.setFloat(TAG_ROTATION, rotation);
		cmp.setFloat(TAG_FIRE_ANGLE, fireAngle);
		cmp.setFloat(TAG_LAUNCH_MULTIPLIER, launchMultiplier);
		cmp.setFloat(TAG_GRAVITY, gravity);
		cmp.setBoolean(TAG_SOLUTION, solution);
	}

	public void readCustomNBT(NBTTagCompound cmp) {
		if(cmp.hasKey(TAG_STACK))
			currentStack = ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_STACK));
		else
			currentStack = null;
		this.target = new Vector3(
				cmp.getDouble(TAG_TARGET+"x"),
				cmp.getDouble(TAG_TARGET+"y"),
				cmp.getDouble(TAG_TARGET+"z"));
		this.targetX=cmp.getInteger(TAG_TARGET_BLOCK+"x");
		this.targetY=cmp.getInteger(TAG_TARGET_BLOCK+"y");
		this.targetZ=cmp.getInteger(TAG_TARGET_BLOCK+"z");
		this.rotation=cmp.getFloat(TAG_ROTATION);
		this.fireAngle=cmp.getFloat(TAG_FIRE_ANGLE);
		this.launchMultiplier=cmp.getFloat(TAG_LAUNCH_MULTIPLIER);
		this.gravity=cmp.getFloat(TAG_GRAVITY);
		this.solution=cmp.getBoolean(TAG_SOLUTION);
		if(cmp.hasKey(TAG_NEEDS_TRAJECTORY)) {
			this.needsTrajectory=cmp.getBoolean(TAG_NEEDS_TRAJECTORY);
			this.client_velocity_x = cmp.getDouble(TAG_CLIENT_VELOCITY+"x");
			this.client_velocity_y = cmp.getDouble(TAG_CLIENT_VELOCITY+"y");
			this.client_velocity_z = cmp.getDouble(TAG_CLIENT_VELOCITY+"z");
		}
	}
	
	private void updateSampleProjectile() {
		if(worldObj.isRemote)
			return;
		if(this.currentStack==null) {
			sampleProjectile = new EntitySnowball(null);
			return;
		}
		sampleProjectile = (EntityThrowable) getProjectileEntity(currentStack);
	}
	
	private void setProjectileTrajectory(float rotation, float fireAngle, float multiplier) {
//		fireAngle = 45;
		sampleProjectile.setThrowableHeading(Math.sin(rotation*Math.PI/180D)*Math.sin(fireAngle*Math.PI/180D), Math.cos(fireAngle*Math.PI/180D), -Math.cos(rotation*Math.PI/180D)*Math.sin(fireAngle*Math.PI/180D), multiplier, 0);
	}

	private void updateFireAngle() {
		if(worldObj.isRemote)
			return;
		if(target==null)
			return;
		Vector3 velocity;
		double deltaHeight;
		gravity = getGravityVelocity(sampleProjectile);
		float originalLaunchMultiplier = getLaunchVelocity(currentStack);
		float newLaunchMultiplier = originalLaunchMultiplier*2;
		setProjectileTrajectory(rotation, 45, newLaunchMultiplier);
		velocity = new Vector3(sampleProjectile.motionX, sampleProjectile.motionY, sampleProjectile.motionZ);
		Vector3 deltaPosition = target.copy().subtract(Vector3.fromTileEntityCenter(this));
		Vector3 deltaPosition2d = new Vector3(deltaPosition.x, 0, deltaPosition.z);
		double targetDistance = deltaPosition2d.mag();
		deltaHeight = deltaPosition.y;
		double maxDistance = getLongDistance(deltaHeight, velocity, gravity);
		if(maxDistance<targetDistance) {
			setToNoSolution();
			return;
		}
		double weakDistance=maxDistance;
		float minLaunchMultiplier = (float) (newLaunchMultiplier * Math.sqrt( deltaPosition2d.mag() / weakDistance ));
		int tries = 0;
		while(Math.abs(weakDistance-targetDistance)>.01D && tries<10) {
			setProjectileTrajectory(rotation, 45, minLaunchMultiplier);
			velocity = new Vector3(sampleProjectile.motionX, sampleProjectile.motionY, sampleProjectile.motionZ);
			weakDistance = getLongDistance(deltaHeight, velocity, gravity);
			minLaunchMultiplier = (float) (minLaunchMultiplier * Math.sqrt( deltaPosition2d.mag() / weakDistance ));
			tries++;
		}
		if(Math.abs(weakDistance-targetDistance)>.01D) {
			setToNoSolution();
			return;
		}
//		chooseFireAngle(minLaunchMultiplier, newLaunchMultiplier);
		launchMultiplier=minLaunchMultiplier;
		fireAngle=45;
		solution=true;
		rebuildTrajectory();
	}
	
	private void chooseFireAngle(float minLaunchMultiplier, float maxLaunchMultiplier) {
		launchMultiplier = (float) (minLaunchMultiplier + (maxLaunchMultiplier-minLaunchMultiplier)*Math.pow(worldObj.rand.nextFloat(),4));
		launchMultiplier = minLaunchMultiplier;
		setProjectileTrajectory(rotation, 45, launchMultiplier);
		Vector3 velocity = new Vector3(sampleProjectile.motionX, sampleProjectile.motionY, sampleProjectile.motionZ);
		double totalVelocity = velocity.mag();
		Vector3 deltaPosition = target.copy().subtract(Vector3.fromTileEntityCenter(this));
		Vector3 deltaPosition2d = new Vector3(deltaPosition.x, 0, deltaPosition.z);
		double targetDistance = deltaPosition2d.mag();
		double deltaHeight = deltaPosition.y;
		double det = Math.pow(totalVelocity, 4) - gravity * ( gravity * targetDistance * targetDistance + 2 * deltaHeight * totalVelocity * totalVelocity);
//		det=0;
		fireAngle=0;
		for(int i = 0 ; i < 360 ; i ++) {
			setProjectileTrajectory(rotation, i, launchMultiplier);
			velocity = new Vector3(sampleProjectile.motionX, sampleProjectile.motionY, sampleProjectile.motionZ);
			if(Math.abs(getLongDistance(deltaHeight, velocity, gravity)-targetDistance)<.1) {
				fireAngle = i;
				break;
			}
		}
//		double angle1 = Math.atan2((totalVelocity*totalVelocity+Math.sqrt(det)),(gravity*targetDistance))/Math.PI*180D;
//		double angle2 = Math.atan2((totalVelocity*totalVelocity-Math.sqrt(det)),(gravity*targetDistance))/Math.PI*180D;
//		fireAngle = (float) (Math.max(angle1, angle2));
		solution=true;
	}

	private double getShortDistance(double deltaHeight, Vector3 velocity, double gravity) {
		double det = velocity.y*velocity.y-2*gravity*deltaHeight;
		Vector3 velocity2d = new Vector3(velocity.x, 0, velocity.y);
		if(det<0)
			return 0;
		double deltaTime = (velocity.y-Math.sqrt(det))/gravity;
		return deltaTime*velocity2d.mag();
	}
	private double getLongDistance(double deltaHeight, Vector3 velocity, double gravity) {
		double det = velocity.y*velocity.y-2*gravity*deltaHeight;
		Vector3 velocity2d = new Vector3(velocity.x, 0, velocity.y);
		if(det<0)
			return 0;
		double deltaTime = (velocity.y+Math.sqrt(det))/gravity;
		return deltaTime*velocity2d.mag();
	}
	
	private void setToNoSolution() {
		solution=false;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		Silva.proxy.ForestWandRenderers.remove(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		Silva.proxy.ForestWandRenderers.remove(this);
	}
	
	@Override
	public ChunkCoordinates getBinding() {
		return new ChunkCoordinates(targetX,targetY,targetZ);
	}

	@Override
	public void onClientDisplayTick() {
		float r = 1F;
		float g = 1F;
		float b = 1F;
		for(Vector3 point : trajectory) {
			Botania.proxy.sparkleFX(worldObj, point.x, point.y, point.z, r, g, b, 0.8F, 1, true);
		}
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		Vector3 thisVec = Vector3.fromTileEntityCenter(this);
		target = new Vector3(x + 0.5, y + 0.5, z + 0.5);
		targetX=x;
		targetY=y;
		targetZ=z;

		AxisAlignedBB axis = player.worldObj.getBlock(x, y, z).getCollisionBoundingBoxFromPool(player.worldObj, x, y, z);
		if(axis == null)
			axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);

		if(!target.isInside(axis))
			target = new Vector3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

		Vector3 diffVec =  target.copy().sub(thisVec);
		rotation = (float) (Math.atan2(diffVec.z, diffVec.x)/Math.PI * 180D) + 90;
		updateSampleProjectile();
		updateFireAngle();
		this.markDirty();
		return true;
	}
	
	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return currentStack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
        if (currentStack != null)
        {
            ItemStack itemstack;

            if (currentStack.stackSize <= amount)
            {
                itemstack = currentStack;
                this.currentStack = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.currentStack.splitStack(amount);

                if (currentStack.stackSize == 0)
                {
                	currentStack = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack amount) {
        currentStack = amount;
		updateSampleProjectile();
		updateFireAngle();

        if (amount != null && amount.stackSize > this.getInventoryStackLimit())
        {
            amount.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "slingshot";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return false;
	}

	@Override
	public void openInventory() {
		// NO OP
	}

	@Override
	public void closeInventory() {
		// NO OP
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isStackThrowable(stack);
	}

	private boolean isStackThrowable(ItemStack stack) {
		Item item = stack.getItem();
		if(canLaunch.containsKey(item))
			return canLaunch.get(item);
		if(stack.getItem()==Items.ender_pearl)
			return true;
		BehaviorDefaultDispenseItem behavior = (BehaviorDefaultDispenseItem)BlockDispenser.dispenseBehaviorRegistry.getObject(stack.getItem());
		if(!(behavior instanceof BehaviorProjectileDispense)) {
			canLaunch.put(item, false);
			return false;
		}
		IProjectile projectile = getProjectileEntity(stack);
		if(projectile instanceof EntityThrowable) {
			canLaunch.put(item, true);
			return true;
		}
		canLaunch.put(item, false);
		return false;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private void rebuildTrajectory() {
		this.trajectory.clear();
		if(!this.solution)
			return;
		double x = this.xCoord+.5;
		double y = this.yCoord+.5;
		double z = this.zCoord+.5;
		Vector3 velocity;
		if(worldObj.isRemote) {
			velocity = new Vector3(client_velocity_x,client_velocity_y,client_velocity_z);
			Vector3 gravity = new Vector3(0, -this.gravity, 0);
			Vector3 pos = new Vector3(x,y,z);
			trajectory.add(pos);
			for(int i = 0 ; i < 1000 ; i++) {
				Vector3 newPos = new Vector3(x,y,z);
				if(pos.copy().subtract(newPos).magSquared()>1) {
					pos=newPos;
					trajectory.add(pos);
				}
				x+=velocity.x;
				y+=velocity.y;
				z+=velocity.z;
				velocity=velocity.add(gravity);
				if(target.copy().subtract(newPos).magSquared()<.1)
					break;
			}
			this.needsTrajectory=false;
		}
		else {
			updateSampleProjectile();
			setProjectileTrajectory(rotation, fireAngle, launchMultiplier);
			velocity = new Vector3(sampleProjectile.motionX, sampleProjectile.motionY, sampleProjectile.motionZ);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeToNBT(nbttagcompound);
			nbttagcompound.setDouble(TAG_CLIENT_VELOCITY+"x", sampleProjectile.motionX);
			nbttagcompound.setDouble(TAG_CLIENT_VELOCITY+"y", sampleProjectile.motionY);
			nbttagcompound.setDouble(TAG_CLIENT_VELOCITY+"z", sampleProjectile.motionZ);
			nbttagcompound.setBoolean(TAG_NEEDS_TRAJECTORY, true);
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
		}
	}

	private IProjectile getProjectileEntity(ItemStack stack) {
		return getProjectileEntity(stack, this.worldObj, xCoord+.5, yCoord+.5, zCoord+.5);
	}
	private static IProjectile getProjectileEntity(ItemStack stack, World world, double x, double y, double z) {
		if(stack.getItem()==Items.ender_pearl)
			return new EntityEnderPearl(world, x, y, z);
		BehaviorProjectileDispense behavior = (BehaviorProjectileDispense)BlockDispenser.dispenseBehaviorRegistry.getObject(stack.getItem());
		return getProjectileEntity(behavior, world, new PositionImpl(x, y, z));
	}
	private static float getLaunchVelocity(ItemStack stack) {
		if(stack==null)
			stack = new ItemStack(Items.snowball);
		if(stack.getItem()==Items.ender_pearl)
			return 1.1F;
		BehaviorProjectileDispense behavior = (BehaviorProjectileDispense)BlockDispenser.dispenseBehaviorRegistry.getObject(stack.getItem());
		return getLaunchVelocity(behavior);
	}
	
	private static IProjectile getProjectileEntity(BehaviorProjectileDispense behavior, World world, IPosition position) {
		try {
			return (IProjectile) getProjectileEntity.invoke(behavior, world, position);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static float getLaunchVelocity(BehaviorProjectileDispense behavior) {
		try {
			return (Float) getLaunchVelocity.invoke(behavior);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0F;
	}
	private static float getGravityVelocity(EntityThrowable entity) {
		try {
			return (Float) getGravityVelocity.invoke(entity);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
