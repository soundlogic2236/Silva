package soundlogic.silva.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.PortalRecipeSimple;
import soundlogic.silva.common.item.ModItems;

public class ModPortalTradeRecipes {

	public static IPortalRecipe dwarfSignRecipe;
	
	public static void preInit() {
		dwarfSignRecipe=new PortalRecipeSimple(new ItemStack(ModBlocks.dwarvenSign),new ItemStack(Items.sign),new ItemStack(Blocks.iron_block),new ItemStack(ModItems.bifrostShard));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfSignRecipe);
	}
}
