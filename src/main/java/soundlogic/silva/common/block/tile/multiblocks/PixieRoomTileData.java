package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.nbt.NBTTagCompound;

public class PixieRoomTileData implements IMultiblockTileData{

	private static final String TAG_HAS_FARM = "hasFarm";
	private static final String TAG_FARM = "farm";
	
	public boolean hasFarm = false;
	public int farmX;
	public int farmY;
	public int farmZ;
	int ticksTillNextSearch;
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_HAS_FARM, hasFarm);
		cmp.setInteger(TAG_FARM+"x", farmX);
		cmp.setInteger(TAG_FARM+"y", farmY);
		cmp.setInteger(TAG_FARM+"z", farmZ);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		hasFarm=cmp.getBoolean(TAG_HAS_FARM);
		farmX=cmp.getInteger(TAG_FARM+"x");
		farmY=cmp.getInteger(TAG_FARM+"y");
		farmZ=cmp.getInteger(TAG_FARM+"z");
	}

}
