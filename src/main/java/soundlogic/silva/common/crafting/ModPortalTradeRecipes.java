package soundlogic.silva.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.PortalRecipeSimple;

public class ModPortalTradeRecipes {

	public static IPortalRecipe dwarfSignRecipe;
	
	public static void preInit() {
		dwarfSignRecipe=new PortalRecipeSimple(new ItemStack(Blocks.anvil),new ItemStack(Items.apple),new ItemStack(Items.sign),new ItemStack(Blocks.cactus));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfSignRecipe);
	}
}
