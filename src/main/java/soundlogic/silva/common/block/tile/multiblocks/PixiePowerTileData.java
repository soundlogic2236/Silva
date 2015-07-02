package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PixiePowerTileData extends PixieRoomTileData {

	private static final String TAG_PATTERN = "pattern";
	private static final String TAG_ACTIVATED = "activated";
	private static final String TAG_INVENTORY = "inventory";
	
	public PixieFarmTileData farmData;
	public int patternX, patternY, patternZ;
	public boolean activated = false;
	public ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_PATTERN+"x", patternX);
		cmp.setInteger(TAG_PATTERN+"y", patternY);
		cmp.setInteger(TAG_PATTERN+"z", patternZ);
		cmp.setBoolean(TAG_ACTIVATED, activated);
		cmp.setInteger(TAG_INVENTORY+"size", inventory.size());
		for(int i = 0 ; i < inventory.size() ; i++) {
			NBTTagCompound item = new NBTTagCompound();
			inventory.get(i).writeToNBT(item);
			cmp.setTag(TAG_INVENTORY+i, item);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		patternX = cmp.getInteger(TAG_PATTERN+"x");
		patternY = cmp.getInteger(TAG_PATTERN+"y");
		patternZ = cmp.getInteger(TAG_PATTERN+"z");
		activated = cmp.getBoolean(TAG_ACTIVATED);
		int size = cmp.getInteger(TAG_INVENTORY+"size");
		inventory.clear();
		for(int i = 0 ; i < size ; i++) {
			inventory.add(ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_INVENTORY+i)));
		}
	}
}
