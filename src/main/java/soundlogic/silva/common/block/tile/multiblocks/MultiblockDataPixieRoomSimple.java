package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import vazkii.botania.api.lexicon.LexiconEntry;

public abstract class MultiblockDataPixieRoomSimple extends MultiblockDataPixieRoomBase {

	final List<Object> requirements;

	public MultiblockDataPixieRoomSimple(List<Object> requirements) {
		this.requirements = new ArrayList<Object>();
		for(Object obj : requirements) {
			if(obj instanceof String) {
				this.requirements.add(obj);
			}
			else if(obj instanceof ItemStack) {
				ItemStack single = ((ItemStack) obj).copy();
				int count = single.stackSize;
				single.stackSize=1;
				for(int i = 0 ; i<count ; i++)
					this.requirements.add(single);
			}
			else throw new IllegalArgumentException("Invalid input");
		}
	}
	public MultiblockDataPixieRoomSimple(Object... requirements) {
		this(Arrays.asList(requirements));
	}
	
	@Override
	protected boolean canActivate(TileMultiblockCore core, World world, PixiePowerTileData data, ArrayList<ItemStack> inventory) {
		PixiePowerSimpleTileData simpleData = (PixiePowerSimpleTileData)data;
		if(!simpleData.requirementsDirty)
			return false;
		simpleData.requirementsDirty=false;
		return getMissing(requirements, inventory).size()==0;
	}

	@Override
	protected void onAbsorb(TileMultiblockCore core, PixiePowerTileData data, EntityItem item, int count) {
		PixiePowerSimpleTileData simpleData = (PixiePowerSimpleTileData)data;
		simpleData.requirementsDirty=true;
		simpleData.nextStackDirty=true;
	}

	@Override
	protected int shouldAbsorb(TileMultiblockCore core, PixiePowerTileData data, ArrayList<ItemStack> inventory, ItemStack item) {
		List<Object> missing = getMissing(requirements, inventory);
		List<ItemStack> flattenedStack = new ArrayList<ItemStack>();
		ItemStack single = item.copy();
		int size = single.stackSize;
		single.stackSize=1;
		for(int i = 0 ; i<size;i++) {
			flattenedStack.add(single);
		}
		List<Object> missingWithoutStacks = getMissing(missing, flattenedStack);
		
		return missing.size()-missingWithoutStacks.size();
	}
	
	private List<Object> getMissing(List<Object> requirements, List<ItemStack> inventory) {
		ArrayList<Object> missing = new ArrayList<Object>(requirements);
		List<ItemStack> remainingStacks = new ArrayList<ItemStack>(inventory);
		while(remainingStacks.size()>0) {
			ItemStack curStack = remainingStacks.get(0);
			int requirementIndex = -1;
			for(int i = 0 ; i < missing.size() ; i++) {
				if(stackMatches(curStack, missing.get(i))) {
					requirementIndex=i;
					break;
				}
			}
			remainingStacks.remove(0);
			if(requirementIndex!=-1)
				missing.remove(requirementIndex);
		}
		return missing;
	}

	private boolean stackMatches(ItemStack stack, Object searchTarget) {
		if(stack.getItem()==ItemBlock.getItemFromBlock(Blocks.bedrock))
			return true;
		if(searchTarget instanceof String) {
			List<ItemStack> validStacks = OreDictionary.getOres((String) searchTarget);
			for(ItemStack test : validStacks) {
				ItemStack temp=test.copy();
				if(temp.getItemDamage() == Short.MAX_VALUE)
					temp.setItemDamage(stack.getItemDamage());
				if(stack.isItemEqual(temp) && stack.getItemDamage() == temp.getItemDamage()) {
					return true;
				}
			}
		}
		if(searchTarget instanceof ItemStack) {
			return stack.getItem() == ((ItemStack) searchTarget).getItem() && stack.getItemDamage() == ((ItemStack) searchTarget).getItemDamage();
		}
		return false;
	}

	protected Object getNextRequestedObject(TileMultiblockCore core, PixiePowerTileData data, ArrayList<ItemStack> inventory) {
		PixiePowerSimpleTileData simpleData = (PixiePowerSimpleTileData)data;
		if(simpleData.nextStackDirty) {
			simpleData.nextStackDirty=false;
			simpleData.nextRequestedObject=getMissing(requirements, inventory).get(0);
		}
		return simpleData.nextRequestedObject;
	}
	
}
