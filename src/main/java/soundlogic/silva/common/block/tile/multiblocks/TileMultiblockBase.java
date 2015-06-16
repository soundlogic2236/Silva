package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;
import soundlogic.silva.common.block.tile.TileMod;

public abstract class TileMultiblockBase extends TileMod{

	private static final String TAG_ORIGINAL_BLOCK = "originalBlock";
	private static final String TAG_ORIGINAL_METADATA = "originalMetadata";
	private static final String TAG_ORIGINAL_HARDNESS = "originalHardness";
	private static final String TAG_ORIGINAL_NBT = "originalNBT";
	private static final String TAG_NEEDS_REFRESH = "refresh";
	private static final String TAG_SOLID = "isSolid";
	private static final String TAG_LIGHT = "light";
	private static final String TAG_HARDNESS = "hardness";
	private static final String TAG_BB = "BB";
	
	private Block originalBlock;
	private int originalMetadata;
	private NBTTagCompound originalNBT;
	public float originalHardness;
	
	public IIcon iconsForSides[] = new IIcon[6];
	public boolean solid=true;
	public float minBBX = 0;
	public float minBBY = 0;
	public float minBBZ = 0;
	public float maxBBX = 1;
	public float maxBBY = 1;
	public float maxBBZ = 1;
	
	protected boolean needsRefresh = true;
	public int lightValue = -1;
	public float hardness = -2;
	
	boolean firstPacket = false;
	
	@Override
	public void updateEntity() {
		if(needsRefresh)
			doRefresh();
		else if(!isValid())
			breakMultiblock();
	}
	
	protected void doRefresh() {
		int[] coords = getRelativePos();
		if(getCore()!=null && getCore().data!=null) {
			if(worldObj.isRemote)
				getCore().data.setVisualData(getCore(), this, coords[0], coords[1], coords[2]);
			getCore().data.setPhysicalData(getCore(), this, coords[0], coords[1], coords[2]);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			needsRefresh = false;
		}
	}
	
	public void breakMultiblock() {
		this.getWorldObj().setBlock(xCoord, yCoord, zCoord, originalBlock, originalMetadata, 3);
		if(originalNBT!=null)
			this.getWorldObj().getTileEntity(xCoord, yCoord, zCoord).readFromNBT(originalNBT);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_ORIGINAL_BLOCK, Block.getIdFromBlock(originalBlock));
		cmp.setInteger(TAG_ORIGINAL_METADATA, originalMetadata);
		cmp.setFloat(TAG_ORIGINAL_HARDNESS, originalHardness);
		cmp.setBoolean(TAG_SOLID, solid);
		cmp.setInteger(TAG_LIGHT, lightValue);
		cmp.setFloat(TAG_HARDNESS, hardness);
		cmp.setFloat(TAG_BB+"minX", minBBX);
		cmp.setFloat(TAG_BB+"minY", minBBY);
		cmp.setFloat(TAG_BB+"minZ", minBBZ);
		cmp.setFloat(TAG_BB+"maxX", maxBBX);
		cmp.setFloat(TAG_BB+"maxY", maxBBY);
		cmp.setFloat(TAG_BB+"maxZ", maxBBZ);
		if(originalNBT!=null)
			cmp.setTag(TAG_ORIGINAL_NBT, originalNBT);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		this.originalBlock=Block.getBlockById(cmp.getInteger(TAG_ORIGINAL_BLOCK));
		this.originalMetadata=cmp.getInteger(TAG_ORIGINAL_METADATA);
		this.originalHardness=cmp.getFloat(TAG_ORIGINAL_HARDNESS);
		this.solid=cmp.getBoolean(TAG_SOLID);
		this.lightValue=cmp.getInteger(TAG_LIGHT);
		this.hardness=cmp.getFloat(TAG_HARDNESS);
		this.minBBX=cmp.getFloat(TAG_BB+"minX");
		this.minBBY=cmp.getFloat(TAG_BB+"minY");
		this.minBBZ=cmp.getFloat(TAG_BB+"minZ");
		this.maxBBX=cmp.getFloat(TAG_BB+"maxX");
		this.maxBBY=cmp.getFloat(TAG_BB+"maxY");
		this.maxBBZ=cmp.getFloat(TAG_BB+"maxZ");
		if(cmp.hasKey(TAG_NEEDS_REFRESH))
			this.needsRefresh = cmp.getBoolean(TAG_NEEDS_REFRESH);
		if(cmp.hasKey(TAG_ORIGINAL_NBT))
			originalNBT=cmp.getCompoundTag(TAG_ORIGINAL_NBT);
		firstPacket=true;
	}
	
	public void setOriginalBlock(Block block, int meta, NBTTagCompound cmp, float hardness) {
		this.originalBlock=block;
		this.originalMetadata=meta;
		this.originalNBT=cmp;
		this.originalHardness=hardness;
	}
	public Block getOriginalBlock() {
		return this.originalBlock;
	}
	public int getOriginalMeta() {
		return this.originalMetadata;
	}
	public NBTTagCompound getOriginalNBT() {
		return this.originalNBT;
	}
	public void markForRefresh() {
		this.needsRefresh=true;
		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setBoolean(TAG_NEEDS_REFRESH, true);
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
		}
	}

	public IIcon getIconForSide(int side) {
		if(needsRefresh)
			doRefresh();
		return iconsForSides[side];
	}

	
	public abstract boolean isValid();
	
	public abstract TileMultiblockCore getCore();
	public abstract int[] getRelativePos();

	
}
