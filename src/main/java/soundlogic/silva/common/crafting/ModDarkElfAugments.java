package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.helper.EquipmentHelper.EquipmentType;
import soundlogic.silva.common.crafting.recipe.DarkElfActDarkenedDust;
import soundlogic.silva.common.crafting.recipe.DarkElfActFake;
import soundlogic.silva.common.crafting.recipe.DarkElfActFurnace;
import soundlogic.silva.common.crafting.recipe.DarkElfActNoteBlock;
import soundlogic.silva.common.crafting.recipe.DarkElfActOreDict;
import soundlogic.silva.common.crafting.recipe.DarkElfActRedstoneControl;
import soundlogic.silva.common.crafting.recipe.DarkElfActSimple;
import soundlogic.silva.common.crafting.recipe.DarkElfActSpreader;
import soundlogic.silva.common.crafting.recipe.DarkElfActTNT;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;
import soundlogic.silva.common.item.ItemDarkElfAugment;
import soundlogic.silva.common.lib.LibAugments;

public class ModDarkElfAugments {

	public static void preInit() {
		ItemDarkElfAugment.registerAugment(LibAugments.FIND_EYE, EquipmentType.HELMET);
		ItemDarkElfAugment.registerAugment(LibAugments.REDSTONE_EYE, EquipmentType.HELMET);
	}
	
}
