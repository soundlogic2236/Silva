package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;

public abstract class DwarfTradeSigned extends DwarfTrade {

	HashMap<Object,Integer> values=null;
	
	protected boolean isSelected(TilePortalCore core) {
		TileDwarvenSign[] signs=core.getDwarvenSigns();
		for(TileDwarvenSign sign : signs)
			if(sign!=null && sign.isActivated())
				return sign.matchesRecipe(this);
		return false;
	}

	private static Random random=new Random();
	
	public abstract boolean isReputationSufficent(int reputation);
	
	protected boolean canApply(TilePortalCore core) {
		return super.canApply(core) && isReputationSufficent(core.dwarfData.getReputation());
	}
	

	public void prepRecipe(int reputation) {
		// NO OP
	}
	
	public void writeNBTData(NBTTagCompound cmp) {
		// NO OP
	}
	
	public void readNBTData(NBTTagCompound cmp) {
		// NO OP
	}
	
	private void prepValues(){
		if(values != null)
			return;
		values=new HashMap<Object,Integer>();
		values.put(new ItemStack(Items.diamond), 6);
		values.put(new ItemStack(Items.emerald), 5);
		values.put(new ItemStack(Items.gold_ingot), 3);
		values.put(new ItemStack(Items.quartz), 1);
		values.put(new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,4), 10);
		values.put(new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,5), 20);
	}
	
	public List<Object> getExtraCosts(int cost) {
		prepValues();
		List<Object> result = new ArrayList<Object>();
		result.addAll(values.keySet());
		Collections.shuffle(result);
		result=result.subList(0, cost > 15 ? cost > 30 ? 3 : 2 : 1 );
		HashMap<Object,Integer> costs=new HashMap<Object,Integer>();
		for(Object o : result) {
			costs.put(o, 1);
		}
		while(getExtraCostsValue(costs)<cost) {
			Object key = result.get(random.nextInt(result.size()));
			costs.put(key, costs.get(key) + 1);
		}
		result.clear();
		for(Entry<Object,Integer> entry : costs.entrySet()) {
			ItemStack stack=((ItemStack)entry.getKey()).copy();
			stack.stackSize=entry.getValue();
			result.add(stack);
		}
		
		return result;
	}
	
	public int getExtraCostsValue(HashMap<Object,Integer> costs) {
		int value=0;
		for(Entry<Object,Integer> entry : costs.entrySet()) {
			value+=values.get(entry.getKey())*entry.getValue();
		}
		return value;
	}
	
	public abstract boolean doesStackMatchSlotForDisplay(ItemStack stack, int slot);
}
