package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

public class LavashroomTileData extends ManaTileData{

	private static final String TAG_LAVA_AMOUNT = "lavaAmount";
	private static final String TAG_FUELS = "fuels";
	private static final String TAG_FUELS_SIZE = "fuelsSize";
	private static final String TAG_FUEL_TICKS = "ticks";
	private static final String TAG_ITEM_REQUIREMENTS = "item";
	private static final String TAG_ACTIVATED = "activated";
	public static final String TAG_KNOWN_LAVA = "knownLava";
	

	public boolean activated = false;
	public int fireRunesNeeded = 3;
	public int earthRunesNeeded = 1;
	public int wrathRunesNeeded = 2;
	public int manaRunesNeeded = 1;
	
	public int lavaAmount = 0;
	public ArrayList<Integer> fuels = new ArrayList<Integer>();
	public int fuelTicks = 0;
	public int knownLava = -1;

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+0, fireRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+1, earthRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+2, wrathRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+3, manaRunesNeeded);

		cmp.setInteger(TAG_LAVA_AMOUNT, lavaAmount);
		cmp.setInteger(TAG_FUELS_SIZE, fuels.size());
		for(int i = 0 ; i < fuels.size() ; i++)
			cmp.setInteger(TAG_FUELS+i, fuels.get(i));
		cmp.setInteger(TAG_FUEL_TICKS, fuelTicks);
		cmp.setBoolean(TAG_ACTIVATED, activated);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		lavaAmount = cmp.getInteger(TAG_LAVA_AMOUNT);
		int size = cmp.getInteger(TAG_FUELS_SIZE);
		fuels.clear();
		for(int i = 0; i < size ; i++)
			fuels.add(cmp.getInteger(TAG_FUELS+i));
		fuelTicks = cmp.getInteger(TAG_FUEL_TICKS);
		activated = cmp.getBoolean(TAG_ACTIVATED);
		if(cmp.hasKey(TAG_KNOWN_LAVA))
			knownLava = cmp.getInteger(TAG_KNOWN_LAVA);
		
	}

}
