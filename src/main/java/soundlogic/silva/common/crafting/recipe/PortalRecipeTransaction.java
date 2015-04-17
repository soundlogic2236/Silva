package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.TilePortalCore;
import net.minecraft.item.ItemStack;

public class PortalRecipeTransaction implements IPortalRecipeTransaction{

	List<ItemStack> toRemove;
	List<ItemStack> output;
	
	public PortalRecipeTransaction(List<ItemStack> output, List<ItemStack> toRemove) {
		this.output=output;
		this.toRemove=toRemove;
	}
	
	public void removeItems(ArrayList<ItemStack> inventory) {
		for(ItemStack remove : toRemove)
			inventory.remove(remove);
	}
	public void doTransaction(TilePortalCore tilePortalCore) {
		// NO-OP
	}
	public List<ItemStack> getOutput() {
		return output;
	}
}
