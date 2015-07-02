package soundlogic.silva.common.block.tile;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.MathHelper;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileManaPotato extends TileMod implements IManaPool {

	public static final int MAX_MANA = 5000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_OUTPUTTING = "outputting";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_REQUEST_UPDATE = "requestUpdate";
	
	private Random random = new Random();

	boolean outputting = false;

	int ticks = 0;

	int mana = MAX_MANA;
	int knownMana = -1;
	
	boolean requestsClientUpdate = false;
	boolean hasReceivedInitialPacket = false;

	@Override
	public boolean isFull() {
		return true;
	}

	@Override
	public void recieveMana(int mana) {
		if(mana<0)
			this.mana = this.mana + mana;
		if(this.mana==0)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
		if(!worldObj.isRemote) {
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(this.getDescriptionPacket());
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void updateEntity() {
		if(!ManaNetworkHandler.instance.isPoolIn(this))
			ManaNetworkEvent.addPool(this);
		doParticles();
	}
	
	public void fillPotato() {
		if(this.mana==0)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
		this.mana=MAX_MANA;
		if(!worldObj.isRemote) {
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(this.getDescriptionPacket());
		}
	}

	private void doParticles() {
		if(worldObj.isRemote) {
			float chance = ((float)mana) / (((float)MAX_MANA)*4 + mana);
			if(random.nextFloat()<chance) {
				float minLength = (2F + .1F * mana / MAX_MANA)/16F;
				float maxLength = (4F + 4F * mana / MAX_MANA)/16F;
				float length = minLength + (maxLength-minLength) * random.nextFloat();
				Vector3 start = new Vector3(this.xCoord+.5F, this.yCoord + .3F, this.zCoord+.5F);
				Vector3 direction = Vector3.one.copy().rotate(random.nextFloat()*360, new Vector3(1,0,0)).rotate(random.nextFloat()*360, new Vector3(0,1,0)).normalize();
				Vector3 offset = direction.multiply(length);
				Vector3 end = start.copy().add(offset);
				BotaniaAccessHandler.lightningFX(worldObj, start, end, 1, 0x00948B, 0x00E4D7);
			}
		}
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

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(ModBlocks.manaPotato).getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = 0x007eff;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return false;
	}

	@Override
	public boolean isOutputtingPower() {
		return outputting;
	}
}