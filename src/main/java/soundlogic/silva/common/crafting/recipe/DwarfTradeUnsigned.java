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

public abstract class DwarfTradeUnsigned extends DwarfTrade {

	protected boolean isSelected(TilePortalCore core) {
		TileDwarvenSign[] signs=core.getDwarvenSigns();
		for(TileDwarvenSign sign : signs)
			if(sign!=null && sign.isActivated())
				return false;
		return true;
	}
}
