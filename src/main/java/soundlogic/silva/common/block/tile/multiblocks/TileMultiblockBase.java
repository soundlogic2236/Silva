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
	private static final String TAG_ORIGINAL_NBT = "originalNBT";
	private static final String TAG_NEEDS_REFRESH = "refresh";
	private static final String TAG_SOLID = "isSolid";
	private static final String TAG_LIGHT = "light";
	private static final String TAG_HARDNESS = "hardness";
	private static final String TAG_HARDNESS_ORIGINAL = "hardnessOriginal";
	
	private Block originalBlock;
	private int originalMetadata;
	private NBTTagCompound originalNBT;
	public float originalHardness;
	
	public IIcon iconsForSides[] = new IIcon[6];
	public boolean solid=true;
	
	private boolean visualNeedsRefresh = true;
	public int lightValue = -1;
	public float hardness = -2;
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote && visualNeedsRefresh) {
			int[] coords = getRelativePos();
			if(getCore()!=null && getCore().data!=null) {
				getCore().data.setVisualData(getCore(), this, coords[0], coords[1], coords[2]);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				visualNeedsRefresh = false;
			}
		}
		else if(!isValid())
			breakMultiblock();
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
		cmp.setBoolean(TAG_SOLID, solid);
		cmp.setInteger(TAG_LIGHT, lightValue);
		cmp.setFloat(TAG_HARDNESS, hardness);
		cmp.setFloat(TAG_HARDNESS_ORIGINAL, originalHardness);
		if(originalNBT!=null)
			cmp.setTag(TAG_ORIGINAL_NBT, originalNBT);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		this.originalBlock=Block.getBlockById(cmp.getInteger(TAG_ORIGINAL_BLOCK));
		this.originalMetadata=cmp.getInteger(TAG_ORIGINAL_METADATA);
		this.solid=cmp.getBoolean(TAG_SOLID);
		this.lightValue=cmp.getInteger(TAG_LIGHT);
		this.hardness=cmp.getFloat(TAG_HARDNESS);
		this.originalHardness=cmp.getFloat(TAG_HARDNESS_ORIGINAL);
		if(cmp.hasKey(TAG_NEEDS_REFRESH))
			this.visualNeedsRefresh = cmp.getBoolean(TAG_NEEDS_REFRESH);
		if(cmp.hasKey(TAG_ORIGINAL_NBT))
			originalNBT=cmp.getCompoundTag(TAG_ORIGINAL_NBT);
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
		this.visualNeedsRefresh=true;
		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setBoolean(TAG_NEEDS_REFRESH, true);
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
		}
	}
	
	public abstract boolean isValid();
	
	public abstract TileMultiblockCore getCore();
	public abstract int[] getRelativePos();

	
}
