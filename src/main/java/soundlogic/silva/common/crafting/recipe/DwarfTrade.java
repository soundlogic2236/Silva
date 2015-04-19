package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TilePortalCore;

public abstract class DwarfTrade implements IPortalRecipe {

	private boolean isSelected(TilePortalCore core) {
		TileDwarvenSign[] signs=core.getDwarvenSigns();
		for(TileDwarvenSign sign : signs)
			if(sign!=null && sign.isActivated())
				return sign.matchesRecipe(this);
		return false;
	}
	
	public abstract boolean isReputationSufficent(int reputation);
	
	protected boolean canApply(TilePortalCore core) {
		return isSelected(core) && isReputationSufficent(core.dwarfData.getReputation());
	}
}
