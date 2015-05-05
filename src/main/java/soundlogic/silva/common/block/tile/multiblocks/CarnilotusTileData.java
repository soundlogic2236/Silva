package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CarnilotusTileData implements IMultiblockTileData{

	private static final String TAG_DROPS = "drops";
	private static final String TAG_DROPS_SIZE = "dropsSize";
	private static final String TAG_MANA = "mana";
	private static final String TAG_FUEL_TICKS = "ticks";
	private static final String TAG_BONEMEAL_LEVEL = "bonemeal";
	private static final String TAG_ITEM_REQUIREMENTS = "item";
	private static final String TAG_ACTIVATED = "activated";
	public static final String TAG_KNOWN_MANA = "knownMana";
	

	public boolean activated = false;
	public int waterRunesNeeded = 3;
	public int gluttonyRunesNeeded = 2;
	public int wrathRunesNeeded = 1;
	public int manaRunesNeeded = 1;
	public int sugarNeeded = 32;
	
	public ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	public int currentMana = 0;
	public int fuelTicks = 0;
	public int knownMana = -1;
	
	public int bonemealLevel = 0;

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+0, waterRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+1, gluttonyRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+2, wrathRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+3, manaRunesNeeded);
		cmp.setInteger(TAG_ITEM_REQUIREMENTS+4, sugarNeeded);

		cmp.setInteger(TAG_DROPS_SIZE, drops.size());
		for(int i = 0 ; i < drops.size() ; i++) {
			NBTTagCompound stack = new NBTTagCompound();
			drops.get(i).writeToNBT(stack);
			cmp.setTag(TAG_DROPS+i, stack);
		}
		cmp.setInteger(TAG_MANA, currentMana);
		cmp.setInteger(TAG_FUEL_TICKS, fuelTicks);
		cmp.setBoolean(TAG_ACTIVATED, activated);
		cmp.setInteger(TAG_BONEMEAL_LEVEL, bonemealLevel);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		int size = cmp.getInteger(TAG_DROPS_SIZE);
		drops.clear();
		for(int i = 0; i < size ; i++)
			drops.add(ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_DROPS+i)));
		currentMana = cmp.getInteger(TAG_MANA);
		fuelTicks = cmp.getInteger(TAG_FUEL_TICKS);
		activated = cmp.getBoolean(TAG_ACTIVATED);
		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
		bonemealLevel = cmp.getInteger(TAG_BONEMEAL_LEVEL);
	}

}
