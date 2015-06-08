package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.ModBlocks;
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

public class ModDarkElfLoot {

	public static void preInit() {
		DarkElfLoot.addLoot(new ItemStack(Items.rotten_flesh), 1, .03F, 100);
		ItemStack pants = new ItemStack(Items.leather_leggings);
		pants.setItemDamage(40);
		pants.addEnchantment(Enchantment.fireProtection, 6);
		DarkElfLoot.addLoot(pants, 2, .9F, 2);
		DarkElfLoot.addLoot(new ItemStack(Items.spider_eye), 1, .1F, 10);
		DarkElfLoot.addLoot(ItemDarkElfAugment.forAugment(LibAugments.FIND_EYE), 3, 1F, 25);
		DarkElfLoot.addLoot(ItemDarkElfAugment.forAugment(LibAugments.REDSTONE_EYE), 3, 1F, 25);
	}
	
}
