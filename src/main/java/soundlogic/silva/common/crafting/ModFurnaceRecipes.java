package soundlogic.silva.common.crafting;

import soundlogic.silva.common.block.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ModFurnaceRecipes {

	public static void preInit() {
		GameRegistry.addSmelting(new ItemStack(ModBlocks.boomMoss,1,0), new ItemStack(ModBlocks.boomMoss,1,2), 0);
	}
}
