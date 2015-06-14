package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.nbt.NBTTagCompound;

public class BlankTileData implements IMultiblockTileData{

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		// NO OP
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		// NO OP
	}

}
