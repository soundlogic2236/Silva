package soundlogic.silva.common.block.tile;

import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.entity.EntityEaterManaBurst;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityManaBurst.PositionProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileManaEater extends TileMod implements IWandBindable, IManaReceiver, IManaSpreader, IForestClientTick {

	private static final int[] MAX_MANA = new int[]{3000};
	private static final int[] MANA_TRANSFER = new int[]{1280};
	private static final int[] BURST_COLOR = new int[]{0xFFAA33};
	private static final int[] TICKS_BEFORE_MANA_LOSS = new int[]{150};
	private static final float[] MOTION_MODIFIER = new float[]{2.1F};

	private static final int TICKS_ALLOWED_WITHOUT_PINGBACK = 20;
	private static final double PINGBACK_EXPIRED_SEARCH_DISTANCE = 0.5;
	
	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_REQUEST_UPDATE = "requestUpdate";
	private static final String TAG_ROTATION_X = "rotationX";
	private static final String TAG_ROTATION_Y = "rotationY";

	private static final String TAG_FORCE_CLIENT_BINDING_X = "forceClientBindingX";
	private static final String TAG_FORCE_CLIENT_BINDING_Y = "forceClientBindingY";
	private static final String TAG_FORCE_CLIENT_BINDING_Z = "forceClientBindingZ";

	private static final String TAG_HAS_IDENTITY = "hasIdentity";
	private static final String TAG_UUID_MOST = "uuidMost";
	private static final String TAG_UUID_LEAST = "uuidLeast";
	private static final String TAG_PINGBACK_TICKS = "pingbackTicks";
	private static final String TAG_LAST_PINGBACK_X = "lastPingbackX";
	private static final String TAG_LAST_PINGBACK_Y = "lastPingbackY";
	private static final String TAG_LAST_PINGBACK_Z = "lastPingbackZ";
	
	public static int staticMeta = 0;
	
	int mana;
	int knownMana = -1;
	public float rotationX, rotationY;

	boolean requestsClientUpdate = false;
	boolean hasReceivedInitialPacket = false;

	IManaReceiver receiver = null;
	IManaReceiver receiverLastTick = null;

	public boolean canShootBurst = true;
	public int lastBurstDeathTick = -1;
	public int burstParticleTick = 0;

	UUID identity;
	public int pingbackTicks = 0;
	public double lastPingbackX = 0;
	public double lastPingbackY = -1;
	public double lastPingbackZ = 0;
	
	List<PositionProperties> lastTentativeBurst;
	boolean invalidTentativeBurst = false;


	@Override
	public boolean isFull() {
		return mana >= getMaxMana();
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
	public void updateEntity() {
		if(!Silva.proxy.ForestWandRenderers.contains(this))
			Silva.proxy.ForestWandRenderers.add(this);
		
		if(mana > 0) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tileAt = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if(tileAt instanceof IManaPool) {
					IManaPool pool = (IManaPool) tileAt;
					if(pool != receiver) {
						if(!pool.isFull()) {
							int previousPoolMana=pool.getCurrentMana();
							pool.recieveMana(mana);
							int newPoolMana=pool.getCurrentMana();
							int manaAdded=newPoolMana-previousPoolMana;
							recieveMana(-manaAdded);
							worldObj.markBlockForUpdate(tileAt.xCoord, tileAt.yCoord, tileAt.zCoord);						}
					}
				}
			}
		}

		if(needsNewBurstSimulation())
			checkForReceiver();

		if(!canShootBurst)
			if(pingbackTicks <= 0) {
				double x = lastPingbackX;
				double y = lastPingbackY;
				double z = lastPingbackZ;
				AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE);
				List<IManaBurst> bursts = worldObj.getEntitiesWithinAABB(IManaBurst.class, aabb);
				IManaBurst found = null;
				UUID identity = getIdentifier();
				for(IManaBurst burst : bursts)
					if(burst != null && identity.equals(burst.getShooterUIID())) {
						found = burst;
						break;
					}

				if(found != null)
					found.ping();
				else setCanShoot(true);
			} else pingbackTicks--;		
		
		tryShootBurst();

		if(receiverLastTick != receiver && !worldObj.isRemote) {
			requestsClientUpdate = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		receiverLastTick = receiver;
	}
	
	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, getMaxMana());
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		
		UUID identity = getIdentifier();
		cmp.setBoolean(TAG_HAS_IDENTITY, true);
		cmp.setLong(TAG_UUID_MOST, identity.getMostSignificantBits());
		cmp.setLong(TAG_UUID_LEAST, identity.getLeastSignificantBits());
		
		cmp.setInteger(TAG_PINGBACK_TICKS, pingbackTicks);
		cmp.setDouble(TAG_LAST_PINGBACK_X, lastPingbackX);
		cmp.setDouble(TAG_LAST_PINGBACK_Y, lastPingbackY);
		cmp.setDouble(TAG_LAST_PINGBACK_Z, lastPingbackZ);
		
		cmp.setInteger(TAG_MANA, mana);
		cmp.setFloat(TAG_ROTATION_X, rotationX);
		cmp.setFloat(TAG_ROTATION_Y, rotationY);
		cmp.setBoolean(TAG_REQUEST_UPDATE, requestsClientUpdate);

		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_X, receiver == null ? 0 : ((TileEntity) receiver).xCoord);
		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_Y, receiver == null ? -1 : ((TileEntity) receiver).yCoord);
		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_Z, receiver == null ? 0 : ((TileEntity) receiver).zCoord);

		requestsClientUpdate = false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		
		if(cmp.getBoolean(TAG_HAS_IDENTITY)) {
			long most = cmp.getLong(TAG_UUID_MOST);
			long least = cmp.getLong(TAG_UUID_LEAST);
			UUID identity = getIdentifierUnsafe();
			if(identity == null || most != identity.getMostSignificantBits() || least != identity.getLeastSignificantBits())
				identity = new UUID(most, least);
		} else getIdentifier();
		
		pingbackTicks = cmp.getInteger(TAG_PINGBACK_TICKS);
		lastPingbackX = cmp.getDouble(TAG_LAST_PINGBACK_X);
		lastPingbackY = cmp.getDouble(TAG_LAST_PINGBACK_Y);
		lastPingbackZ = cmp.getDouble(TAG_LAST_PINGBACK_Z);
		
		mana = cmp.getInteger(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);
		requestsClientUpdate = cmp.getBoolean(TAG_REQUEST_UPDATE);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);

		if(requestsClientUpdate && worldObj != null) {
			int x = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_X);
			int y = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_Y);
			int z = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_Z);
			if(y != -1) {
				TileEntity tile = worldObj.getTileEntity(x, y, z);
				if(tile instanceof IManaReceiver)
					receiver = (IManaReceiver) tile;
				else receiver = null;
			} else receiver = null;
		}

		if(worldObj != null && worldObj.isRemote)
			hasReceivedInitialPacket = true;
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return false;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				writeToNBT(nbttagcompound);
				nbttagcompound.setInteger(TAG_KNOWN_MANA, mana);
				if(player instanceof EntityPlayerMP)
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
			}
			worldObj.playSoundAtEntity(player, "botania:ding", 0.1F, 1F);
		} else {
			MovingObjectPosition pos = raytraceFromEntity(worldObj, player, true, 5);
			if(pos != null && pos.hitVec != null && !worldObj.isRemote) {
				double x = pos.hitVec.xCoord - xCoord - 0.5;
				double y = pos.hitVec.yCoord - yCoord - 0.5;
				double z = pos.hitVec.zCoord - zCoord - 0.5;

				if(pos.sideHit != 0 && pos.sideHit != 1) {
					Vector3 clickVector = new Vector3(x, 0, z);
					Vector3 relative = new Vector3(-0.5, 0, 0);
					double angle = Math.acos(clickVector.dotProduct(relative) / (relative.mag() * clickVector.mag())) * 180D / Math.PI;

					rotationX = (float) angle + 180F;
					if(clickVector.z < 0)
						rotationX = 360 - rotationX;
				}

				double angle = y * 180;
				rotationY = -(float) angle;

				checkForReceiver();
				requestsClientUpdate = true;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}
	private boolean needsNewBurstSimulation() {
		if(worldObj.isRemote && !hasReceivedInitialPacket)
			return false;

		if(lastTentativeBurst == null)
			return true;

		for(PositionProperties props : lastTentativeBurst)
			if(!props.contentsEqual(worldObj)) {
				invalidTentativeBurst = props.invalid;
				return !invalidTentativeBurst;
			}

		return false;
	}

	public void tryShootBurst() {
		if(receiver != null && !invalidTentativeBurst) {
			if(canShootBurst && ( receiver.getCurrentMana() > 0)) {
				EntityManaBurst burst = getBurst(false);
				if(burst != null) {
					if(!worldObj.isRemote) {
						worldObj.spawnEntityInWorld(burst);
						if(!BotaniaAccessHandler.BotaianConfig.silentSpreaders)
							worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:spreaderFire", 0.05F, 0.7F + 0.3F * (float) Math.random());
					}
				}
			}
		}
	}

	public void checkForReceiver() {
		EntityManaBurst fakeBurst = getBurst(true);
		fakeBurst.setScanBeam();
		TileEntity receiver = fakeBurst.getCollidedTile(true);

		if(receiver != null && receiver instanceof IManaReceiver)
			this.receiver = (IManaReceiver) receiver;
		else this.receiver = null;
		lastTentativeBurst = fakeBurst.propsList;
	}

	public EntityManaBurst getBurst(boolean fake) {
		EntityManaBurst burst = new EntityEaterManaBurst(this,fake);
		burst.setColor(BURST_COLOR[getMeta()]);
		burst.setMana(2);
		burst.setStartingMana(2);
		burst.setMinManaLoss(TICKS_BEFORE_MANA_LOSS[getMeta()]);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);
		float motionModifier = MOTION_MODIFIER[getMeta()];
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);
		
		return burst;
	}

	public int getMaxManaTransfer() {
		return MANA_TRANSFER[getMeta()];
	}

	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (!world.isRemote && player instanceof EntityPlayer)
			d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP)
			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.func_147447_a(vec3, vec31, par3, !par3, par3);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(ModBlocks.manaEater, 1, getBlockMetadata()).getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = 0x880000;
		vazkii.botania.client.core.handler.HUDHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name, res);

		if(receiver != null) {
			TileEntity receiverTile = (TileEntity) receiver;
			ItemStack recieverStack = new ItemStack(worldObj.getBlock(receiverTile.xCoord, receiverTile.yCoord, receiverTile.zCoord), 1, receiverTile.getBlockMetadata());
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(recieverStack != null && recieverStack.getItem() != null) {
				String stackName = recieverStack.getDisplayName();
				int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
				int x = res.getScaledWidth() / 2 - width;
				int y = res.getScaledHeight() / 2 + 30;

				mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
				RenderHelper.enableGUIStandardItemLighting();
				RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, recieverStack, x, y);
				RenderHelper.disableStandardItemLighting();
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		checkForReceiver();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public ChunkCoordinates getBinding() {
		if(receiver == null)
			return null;

		TileEntity tile = (TileEntity) receiver;
		return new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public int getMeta() {
		if(worldObj==null)
			return this.staticMeta;
		return Math.max(this.blockMetadata,0);
	}

	public int getMaxMana() {
		return MAX_MANA[getMeta()];
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		Vector3 thisVec = Vector3.fromTileEntityCenter(this);
		Vector3 blockVec = new Vector3(x + 0.5, y + 0.5, z + 0.5);

		AxisAlignedBB axis = player.worldObj.getBlock(x, y, z).getCollisionBoundingBoxFromPool(player.worldObj, x, y, z);
		if(axis == null)
			axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);

		if(!blockVec.isInside(axis))
			blockVec = new Vector3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

		Vector3 diffVec =  blockVec.copy().sub(thisVec);
		Vector3 diffVec2D = new Vector3(diffVec.x, diffVec.z, 0);
		Vector3 rotVec = new Vector3(0, 1, 0);
		double angle = rotVec.angle(diffVec2D) / Math.PI * 180.0;

		if(blockVec.x < thisVec.x)
			angle = -angle;

		rotationX = (float) angle + 90;

		rotVec = new Vector3(diffVec.x, 0, diffVec.z);
		angle = diffVec.angle(rotVec) * 180F / Math.PI;
		if(blockVec.y < thisVec.y)
			angle = -angle;
		rotationY = (float) angle;

		checkForReceiver();
		return true;
	}

	public void onClientDisplayTick() {
		if(worldObj != null) {
			EntityEaterManaBurst burst = (EntityEaterManaBurst) getBurst(true);
			burst.getCollidedTile(false);
		}
	}

	public int getBurstParticleTick() {
		return burstParticleTick;
	}

	public void setBurstParticleTick(int burstParticleTick) {
		this.burstParticleTick = burstParticleTick;
	}

	public int getLastBurstDeathTick() {
		return lastBurstDeathTick;
	}

	public void setLastBurstDeathTick(int lastBurstDeathTick) {
		this.lastBurstDeathTick = lastBurstDeathTick;
	}

	@Override
	public float getRotationX() {
		return this.rotationX;
	}

	@Override
	public float getRotationY() {
		return this.rotationY;
	}

	@Override
	public void setCanShoot(boolean arg0) {
		this.canShootBurst=arg0;
	}

	@Override
	public void pingback(IManaBurst burst, UUID expectedIdentity) {
		if(getIdentifier().equals(expectedIdentity)) {
			pingbackTicks = TICKS_ALLOWED_WITHOUT_PINGBACK;
			Entity e = (Entity) burst;
			lastPingbackX = e.posX;
			lastPingbackY = e.posY;
			lastPingbackZ = e.posZ;
			setCanShoot(false);
		}
	}

	@Override
	public UUID getIdentifier() {
		if(identity == null)
			identity = UUID.randomUUID();
		return identity;
	}
	public UUID getIdentifierUnsafe() {
		return identity;
	}
}
