package soundlogic.silva.common.block.tile.multiblocks;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;
import soundlogic.silva.common.block.tile.TileMod;

public abstract class TileMultiblockBase extends TileMod{

	private static final String TAG_ORIGINAL_BLOCK = "originalBlock";
	private static final String TAG_ORIGINAL_METADATA = "originalMetadata";
	private static final String TAG_SOLID = "isSolid";
	private static final String TAG_NEEDS_REFRESH = "refresh";
	
	private Block originalBlock;
	private int originalMetadata;
	
	public IIcon iconsForSides[] = new IIcon[6];
	public boolean solid=true;
	
	private boolean visualNeedsRefresh = true;
	public int lightValue = -1;
	public boolean canCollideCheck = true;
	public int hardness = -2;
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote && visualNeedsRefresh) {
			int[] coords = getRelativePos();
			if(getCore().data!=null) {
				getCore().data.setVisualData(getCore(), this, coords[0], coords[1], coords[2]);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				visualNeedsRefresh = false;
			}
		}
		else if(!isValid())
			breakMultiblock();
	}
	
	protected void breakMultiblock() {
		this.getWorldObj().setBlock(xCoord, yCoord, zCoord, originalBlock, originalMetadata, 3);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_ORIGINAL_BLOCK, Block.getIdFromBlock(originalBlock));
		cmp.setInteger(TAG_ORIGINAL_METADATA, originalMetadata);
		cmp.setBoolean(TAG_SOLID, solid);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		this.originalBlock=Block.getBlockById(cmp.getInteger(TAG_ORIGINAL_BLOCK));
		this.originalMetadata=cmp.getInteger(TAG_ORIGINAL_METADATA);
		this.solid=cmp.getBoolean(TAG_SOLID);
		if(cmp.hasKey(TAG_NEEDS_REFRESH))
			this.visualNeedsRefresh = cmp.getBoolean(TAG_NEEDS_REFRESH);
	}
	
	public void setOriginalBlock(Block block, int meta) {
		this.originalBlock=block;
		this.originalMetadata=meta;
	}
	public Block getOriginalBlock() {
		return this.originalBlock;
	}
	public int getOriginalMeta() {
		return this.originalMetadata;
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
