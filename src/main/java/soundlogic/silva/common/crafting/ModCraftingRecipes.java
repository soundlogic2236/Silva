package soundlogic.silva.common.crafting;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.AlfheimPortalRecipeWrapper;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.BotaniaAPI;

public class ModCraftingRecipes {

	public static IRecipe recipePortalPylon;
	public static IRecipe recipeUntunedPylon;
	public static IRecipe recipeBifrostBlock;
	public static IRecipe recipeBifrostBlockSlab;
	public static IRecipe recipeBifrostBlockStairs;
	public static IRecipe recipeBifrostBlockWall;
	public static IRecipe recipeBifrostShard;
	public static IRecipe recipeBoomMoss;
	public static IRecipe recipePortalCore;
	public static IRecipe recipeBifrostSparkling;
	public static IRecipe recipeDwarvenPool;
	public static IRecipe recipeUpgradeRedstone;
	public static IRecipe recipeUpgradeInhibit;
	public static IRecipe recipeChargedStone;
	public static IRecipe recipeUpgradeCharge;

	public static void preInit() {
		addOreDictRecipe(new ItemStack(ModBlocks.dimensionalPylon,1,0),
				" S ",
				"GPG",
				" S ",
				'S',new ItemStack(ModItems.bifrostShard),
				'G',new ItemStack(Blocks.gold_block),
				'P',new ItemStack(vazkii.botania.common.block.ModBlocks.pylon,1,0));
		recipePortalPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.dimensionalPylon,1,1),
				" I ",
				"GPG",
				" I ",
				'I',new ItemStack(vazkii.botania.common.item.ModItems.manaResource, 1, 14),
				'G',new ItemStack(Blocks.gold_block),
				'P',new ItemStack(vazkii.botania.common.block.ModBlocks.pylon,1,0));
		recipeUntunedPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.bifrostBlock),
				"SSS",
				"SSS",
				"SSS",
				'S',new ItemStack(ModItems.bifrostShard));
		recipeBifrostBlock = BotaniaAPI.getLatestAddedRecipe();

		addOreDictRecipe(new ItemStack(ModItems.bifrostShard,9),
				"B",
				'B',new ItemStack(ModBlocks.bifrostBlock));
		recipeBifrostShard = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.bifrostBlockSlab, 6),
				"BBB",
				'B',new ItemStack(ModBlocks.bifrostBlock));
		recipeBifrostBlockSlab = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.bifrostBlockStairs, 4),
				"B  ",
				"BB ",
				"BBB",
				'B',new ItemStack(ModBlocks.bifrostBlock));
		recipeBifrostBlockStairs = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.bifrostBlockWall, 6),
				"BBB",
				"BBB",
				'B',new ItemStack(ModBlocks.bifrostBlock));
		recipeBifrostBlockWall = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModBlocks.bifrostBlockSparkling, 1),
				new ItemStack(ModBlocks.bifrostBlock),
				new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8));
		recipeBifrostSparkling = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.boomMoss, 6),
				"WPW",
				"VTV",
				"WVW",
				'W',new ItemStack(ModBlocks.dwarfWeed),
				'P',new ItemStack(Items.diamond_pickaxe),
				'V',new ItemStack(Blocks.vine),
				'T',new ItemStack(Blocks.tnt));
		recipeBoomMoss = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.portalCore),
				"GSG",
				"STS",
				"GSG",
				'G', vazkii.botania.common.lib.LibOreDict.LIFE_ESSENCE,
				'S',new ItemStack(ModItems.bifrostShard),
				'T',new ItemStack(vazkii.botania.common.block.ModBlocks.storage,1,1));
		recipePortalCore = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.dwarvenManaPool),
				"SDS",
				"SSS",
				'S', new ItemStack(ModBlocks.dwarfRock),
				'D', new ItemStack(vazkii.botania.common.block.ModBlocks.pool));
		recipeDwarvenPool = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.portalUpgradeRedstone),
				" R ",
				"RSR",
				" R ",
				'S', new ItemStack(ModBlocks.bifrostBlockSparkling),
				'R', new ItemStack(Items.redstone));
		recipeUpgradeRedstone = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.portalUpgradeInhibit),
				" P ",
				"ISI",
				" Q ",
				'S', new ItemStack(ModBlocks.bifrostBlockSparkling),
				'P', "manaPearl",
				'I', new ItemStack(Items.iron_ingot),
				'Q', new ItemStack(Items.quartz));
		recipeUpgradeInhibit = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.portalUpgradeCharge),
				"BPB",
				"MSE",
				"BDB",
				'S', new ItemStack(ModBlocks.bifrostBlockSparkling),
				'P', "manaPearl",
				'M', "ingotManasteel",
				'E', "ingotElvenElementium",
				'D', "elvenDragonstone",
				'B', new ItemStack(ModItems.bifrostShard));
		recipeUpgradeCharge = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(ModItems.chargedStone),
				new ItemStack(Items.diamond),
				new ItemStack(ModItems.bifrostShard),
				"manaPearl",
				"elvenPixieDust");
		recipeChargedStone = BotaniaAPI.getLatestAddedRecipe();
		
	}

	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
