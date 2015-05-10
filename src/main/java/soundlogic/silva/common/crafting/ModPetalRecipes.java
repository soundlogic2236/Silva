package soundlogic.silva.common.crafting;

import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ModPetalRecipes {

	public static RecipePetals rhododender;
	
	public static void preInit() {
		rhododender = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RHODODENDER), "manaPetalGreen", "manaPetalGreen", "manaPetalLime", "manaPetalLime", "manaPetalPurple", "manaPetalPurple", "manaPearl");
	}
}
