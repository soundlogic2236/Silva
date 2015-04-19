package soundlogic.silva.common.block.tile;

import java.util.List;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.ModBlocks;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.MathHelper;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileManaCrystal extends TileMod implements IManaPool, IManaCollector, IManaSpreader, IForestClientTick {

	public static final int MAX_MANA = 100000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_OUTPUTTING = "outputting";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_REQUEST_UPDATE = "requestUpdate";

	boolean outputting = false;

	int ticks = 0;

	int mana;
	int knownMana = -1;
	
	boolean requestsClientUpdate = false;
	boolean hasReceivedInitialPacket = false;

	IManaReceiver receiver = null;
	IManaReceiver receiverLastTick = null;

	public boolean canShootBurst = true;

	List<PositionProperties> lastTentativeBurst;

	public int lastBurstDeathTick = -1;
	public int burstParticleTick = 0;

	@Override
	public boolean isFull() {
		return mana >= getMaxMana();
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, getMaxMana());
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removeCollector(this);
		ManaNetworkEvent.removePool(this);
		Silva.proxy.ForestWandRenderers.remove(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removeCollector(this);
		ManaNetworkEvent.removePool(this);
		Silva.proxy.ForestWandRenderers.remove(this);
	}

	@Override
	public void updateEntity() {
		++ticks;
		
		if(!Silva.proxy.ForestWandRenderers.contains(this))
			Silva.proxy.ForestWandRenderers.add(this);
		if(!ManaNetworkHandler.instance.isPoolIn(this))
			ManaNetworkEvent.addPool(this);
		if(!ManaNetworkHandler.instance.isCollectorIn(this))
			ManaNetworkEvent.addCollector(this);

		if(needsNewBurstSimulation())
			checkForReceiver();

		tryShootBurst();

		if(receiverLastTick != receiver && !worldObj.isRemote) {
			requestsClientUpdate = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		receiverLastTick = receiver;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setBoolean(TAG_OUTPUTTING, outputting);
		cmp.setInteger(TAG_MANA, mana);
		cmp.setBoolean(TAG_REQUEST_UPDATE, requestsClientUpdate);

		requestsClientUpdate = false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		mana = cmp.getInteger(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);
		requestsClientUpdate = cmp.getBoolean(TAG_REQUEST_UPDATE);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);

		if(worldObj != null && worldObj.isRemote)
			hasReceivedInitialPacket = true;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
		}

		worldObj.playSoundAtEntity(player, "botania:ding", 0.11F, 1F);
	}

	private boolean needsNewBurstSimulation() {
		if(worldObj.isRemote && !hasReceivedInitialPacket)
			return false;

		if(lastTentativeBurst == null)
			return true;

		for(PositionProperties props : lastTentativeBurst)
			if(!props.contentsEqual(worldObj))
				return true;

		return false;
	}

	public void tryShootBurst() {
		if(receiver != null) {
			if(canShootBurst && receiver.canRecieveManaFromBursts() && !receiver.isFull()) {
				EntityManaBurst burst = getBurst(false);
				if(burst != null) {
					if(!worldObj.isRemote) {
						mana -= burst.getStartingMana();
						worldObj.spawnEntityInWorld(burst);
						if(!vazkii.botania.common.core.handler.ConfigHandler.silentSpreaders)
							worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:spreaderFire", 0.05F, 0.7F + 0.3F * (float) Math.random());
					}

					canShootBurst = false;
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
		EntityManaBurst burst = new EntityManaBurst(this, fake);

		int maxMana = 360;
		int color = 0x007eff;
		int ticksBeforeManaLoss = 80;
		float manaLossPerTick = 10F;
		float motionModifier = 1.25F;
		float gravity = 0F;
		BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

		if(getCurrentMana() >= props.maxMana || fake) {
			burst.setColor(props.color);
			burst.setMana(props.maxMana);
			burst.setStartingMana(props.maxMana);
			burst.setMinManaLoss(props.ticksBeforeManaLoss);
			burst.setManaLossPerTick(props.manaLossPerTick);
			burst.setGravity(props.gravity);
			burst.setMotion(burst.motionX * props.motionModifier, burst.motionY * props.motionModifier, burst.motionZ * props.motionModifier);

			return burst;
		}
		return null;
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(ModBlocks.manaCrystal).getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = 0x007eff;
		HUDHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name, res);

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
	public void onClientDisplayTick() {
		if(worldObj != null) {
			EntityManaBurst burst = getBurst(true);
			burst.getCollidedTile(false);
		}
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	@Override
	public boolean isOutputtingPower() {
		return outputting;
	}

	@Override
	public float getManaYieldMultiplier(IManaBurst burst) {
		return burst.getMana() < 16 ? 0F : 0.95F;
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
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
		return 0;
	}

	@Override
	public float getRotationY() {
		return -90;
	}

	@Override
	public void setCanShoot(boolean arg0) {
		this.canShootBurst=arg0;
	}
}