package soundlogic.silva.common.crafting.recipe;

import java.util.List;

import soundlogic.silva.common.block.tile.TilePortalCore;
import net.minecraft.item.ItemStack;

public class DwarvenTradeTransaction extends PortalRecipeTransaction{

	int repBoost;
	int maxRep;
	
	public DwarvenTradeTransaction(List<ItemStack> output,
			List<ItemStack> toRemove, int repBoost, int maxRep) {
		super(output, toRemove);
		this.repBoost=repBoost;
		this.maxRep=maxRep;
	}

	public void doTransaction(TilePortalCore tilePortalCore) {
		int rep=tilePortalCore.dwarfData.getReputation();
		int targetRep=Math.min(rep+repBoost,maxRep);
		int newRep=Math.max(rep, targetRep);
		tilePortalCore.dwarfData.setReputation(newRep);
	}

}
