package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TilePortalCore;

public abstract class DwarfTrade implements IPortalRecipe {

	protected abstract boolean isSelected(TilePortalCore core);

	protected boolean canApply(TilePortalCore core) {
		return isSelected(core);
	}
	
	protected abstract int getMaxRep();

	protected abstract int getRepBoost();
	
	protected DwarvenTradeTransaction getTransaction(List<ItemStack> result,List<ItemStack> stacksToRemove) {
		return new DwarvenTradeTransaction(result,stacksToRemove,getRepBoost(),getMaxRep());
	}

}