package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import soundlogic.silva.common.block.tile.TileMod;

public class TileMultiblockProxy extends TileMultiblockBase{

	private static final String TAG_CORE_X = "multiblockCoreX";
	private static final String TAG_CORE_Y = "multiblockCoreY";
	private static final String TAG_CORE_Z = "multiblockCoreZ";
	
	int relativeX;
	int relativeY;
	int relativeZ;
	private TileMultiblockCore core;
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_CORE_X, relativeX);
		cmp.setInteger(TAG_CORE_Y, relativeY);
		cmp.setInteger(TAG_CORE_Z, relativeZ);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		relativeX = cmp.getInteger(TAG_CORE_X);
		relativeY = cmp.getInteger(TAG_CORE_Y);
		relativeZ = cmp.getInteger(TAG_CORE_Z);
	}
	
	public TileMultiblockCore getCore() {
		if(core==null) {
			TileEntity tile = worldObj.getTileEntity(xCoord+relativeX,yCoord+relativeY,zCoord+relativeZ);
			if(tile instanceof TileMultiblockCore)
				core=(TileMultiblockCore) tile;
			if(core!=null) {
				this.needsRefresh=true;
				core.proxies.add(this);
			}
		}
		return core;
	}
	
	public int[] getRelativePos() {
		return new int[] {-relativeX,-relativeY,-relativeZ};
	}

	@Override
	public boolean isValid() {
		return getCore()!=null && !getCore().isInvalid();
	}
}
