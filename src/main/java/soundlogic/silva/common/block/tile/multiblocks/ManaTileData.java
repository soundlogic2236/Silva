package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.nbt.NBTTagCompound;

public class ManaTileData implements IMultiblockTileData {

	private static final String TAG_MANA = "mana";
	public static final String TAG_KNOWN_MANA = "knownMana";

	public int currentMana = 0;
	public int knownMana = -1;
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, currentMana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		currentMana = cmp.getInteger(TAG_MANA);
		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}

}
