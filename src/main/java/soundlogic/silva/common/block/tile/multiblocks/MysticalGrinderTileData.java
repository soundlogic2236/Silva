package soundlogic.silva.common.block.tile.multiblocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MysticalGrinderTileData implements IMultiblockTileData {

	private static final String TAG_STACKS = "stacks";
	private static final String TAG_FRACTIONS = "fractions";
	private static final String TAG_TIME = "time";
	private static final String TAG_HARDNESS = "hardness";
	private static final String TAG_REFERENCE = "reference";
	private static final String TAG_PARTS = "parts";
	
	private static final int maxStacksStored = 30;
	private static final int weeds = 14;

	private static Random random = new Random();
	
	public ItemStack[] stacks = new ItemStack[maxStacksStored];
	public int[] fractions = new int[maxStacksStored];
	public double[] processedTime = new double[maxStacksStored];
	public float[] hardness = new float[maxStacksStored];
	public boolean[] canSplit = new boolean[maxStacksStored];
	
	public int[] weedReference = new int[weeds];
	public int[] weedParts = new int[weeds];
	
	public ItemStack[] toDropStacks = new ItemStack[maxStacksStored];
	public int toDropStacksCount = 0;
	
	public boolean dirty = false;
	public boolean visualDirty = false;
	
	public MysticalGrinderTileData() {
		for(int i = 0; i < weeds; i++) {
			weedReference[i] = -1;
		}
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		for(int i = 0 ; i < maxStacksStored ; i++) {
			if(stacks[i]!=null) {
				NBTTagCompound item = new NBTTagCompound();
				stacks[i].writeToNBT(item);
				cmp.setTag(TAG_STACKS+i, item);
			}
			cmp.setInteger(TAG_FRACTIONS+i, fractions[i]);
			cmp.setDouble(TAG_TIME+i, processedTime[i]);
			cmp.setFloat(TAG_HARDNESS+i, hardness[i]);
		}
		for(int i = 0 ; i < weeds ; i++) {
			cmp.setInteger(TAG_REFERENCE+i, weedReference[i]);
			cmp.setInteger(TAG_PARTS+i, weedParts[i]);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		for(int i = 0 ; i < maxStacksStored ; i++) {
			if(cmp.hasKey(TAG_STACKS+i))
				stacks[i]=ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_STACKS+i));
			else
				stacks[i]=null;
			fractions[i]=cmp.getInteger(TAG_FRACTIONS+i);
			processedTime[i]=cmp.getDouble(TAG_TIME+i);
			hardness[i]=cmp.getFloat(TAG_HARDNESS+i);
		}
		for(int i = 0 ; i < weeds ; i++) {
			if(cmp.hasKey(TAG_REFERENCE+i))
				weedReference[i]=cmp.getInteger(TAG_REFERENCE+i);
			else
				weedReference[i]=-1;
			weedParts[i]=cmp.getInteger(TAG_PARTS+i);
		}
	}

	public boolean addStack(ItemStack tryAdd, float hardness) {
		int freeWeed = findFreeWeed();
		if(freeWeed==-1)
			return false;
		int freeStack = findFreeStack();
		this.stacks[freeStack]=tryAdd;
		this.fractions[freeStack]=1;
		this.processedTime[freeStack]=0;
		this.hardness[freeStack]=hardness;
		this.canSplit[freeStack]=canSplit(tryAdd);
		this.weedReference[freeWeed]=freeStack;
		this.weedParts[freeWeed]=0;
		dirty=true;
		visualDirty=true;
		return true;
	}
	
	private boolean canSplit(ItemStack tryAdd) {
		Item item = tryAdd.getItem();
		if(!(item instanceof ItemBlock))
			return false;
		ItemBlock itemBlock = (ItemBlock) item;
		Block block = itemBlock.field_150939_a;
		if(block.isNormalCube())
			return true;
		block.setBlockBoundsForItemRender();
		if(Math.abs(1-block.getBlockBoundsMaxX())>.01D) return false;
		if(Math.abs(1-block.getBlockBoundsMaxY())>.01D) return false;
		if(Math.abs(1-block.getBlockBoundsMaxZ())>.01D) return false;
		if(Math.abs(block.getBlockBoundsMinX())>0.01D) return false;
		if(Math.abs(block.getBlockBoundsMinY())>0.01D) return false;
		if(Math.abs(block.getBlockBoundsMinZ())>0.01D) return false;
		return true;
	}

	public void tick() {
		tickWeeds();
		finishStacks();
		spreadStacks();
	}
	
	private void finishStacks() {
		toDropStacksCount=0;
		for(int i = 0 ; i < maxStacksStored ; i++) {
			if(stacks[i]==null)
				continue;
			if(processedTime[i]>getProcessingTicks(hardness[i])) {
				finishStack(i);
			}
		}
	}

	private void finishStack(int stack) {
		dropStack(stacks[stack]);
		stacks[stack]=null;
		for(int i = 0 ; i < weeds ; i++) {
			if(weedReference[i]==stack)
				weedReference[i]=-1;
		}
		dirty=true;
		visualDirty=true;
	}

	private void dropStack(ItemStack itemStack) {
		toDropStacks[toDropStacksCount]=itemStack;
		toDropStacksCount++;
	}
	public void clearDrops() {
		toDropStacks = new ItemStack[maxStacksStored];
		toDropStacksCount = 0;
	}

	private void spreadStacks() {
		for(int i = 0 ; i < maxStacksStored ; i++) {
			if(stacks[i]==null || !canSplit[i])
				continue;
			if(random.nextFloat()<.1F) {
				int k = findFreeWeed();
				if(k==-1)
					return;
				weedReference[k]=i;
				weedParts[k]=fractions[i];
				fractions[i]++;
				dirty=true;
				visualDirty=true;
			}
		}
	}

	private void tickWeeds() {
		for(int i = 0 ; i < weeds ; i++) {
			int reference = weedReference[i];
			if(reference==-1)
				continue;
			processedTime[reference]+=Math.sqrt(fractions[reference]);
			dirty=true;
		}
	}

	public int getProcessingTicks(float hardness) {
		return (int) (hardness*20*60);
	}
	
	private int findFreeStack() {
		for(int i = 0 ; i < maxStacksStored ; i++) {
			if(stacks[i]==null) {
				return i;
			}
		}
		return -1;
	}
	
	private int findFreeWeed() {
		int[] free = new int[weeds];
		int j = 0;
		for(int i = 0 ; i < weeds; i++) {
			if(weedReference[i]==-1) {
				free[j]=i;
				j++;
			}
		}
		if(j==0)
			return -1;
		return free[random.nextInt(j)];
	}
}
