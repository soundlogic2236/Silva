package soundlogic.silva.common.crafting;

import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.lib.LibBlockNames;
import soundlogic.silva.common.lib.LibOreDict;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDict {

	public static void preInit() {
		OreDictionary.registerOre(LibOreDict.DARKENED_STONE, new ItemStack(ModBlocks.darkenedStone));
		OreDictionary.registerOre(LibOreDict.DARKENED_WOOD, new ItemStack(ModBlocks.darkenedWood));
		
	}
}
