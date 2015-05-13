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

public class ModPureDaisyRecipes {

	public static void preInit() {
		BotaniaAPI.registerPureDaisyRecipe(LibOreDict.DARKENED_STONE, ModBlocks.paradoxStone, 0);
		BotaniaAPI.registerPureDaisyRecipe(LibOreDict.DARKENED_WOOD, ModBlocks.paradoxWood, 0);
	}
}
