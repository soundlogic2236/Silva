package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.nbt.NBTTagCompound;

public interface IMultiblockTileData {

	public void writeCustomNBT(NBTTagCompound cmp);
	public void readCustomNBT(NBTTagCompound cmp);

}
