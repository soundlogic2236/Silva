package soundlogic.silva.common.crafting;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
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
import soundlogic.silva.common.crafting.recipe.DarkElfAugmentRecipe;
import soundlogic.silva.common.crafting.recipe.HolderIntoBookRecipe;
import soundlogic.silva.common.crafting.recipe.PapersCloneRecipe;
import soundlogic.silva.common.crafting.recipe.PapersIntoLexiconRecipe;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lib.LibOreDict;
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
	public static List<IRecipe> holderEnchantRecipes;
	public static IRecipe enchantPlateRecipe;
	public static IRecipe enchantOfferingRecipe;
	public static IRecipe darkElfTrapRecipe;

	public static void preInit() {
		addOreDictRecipe(new ItemStack(ModBlocks.dimensionalPylon,1,0),
				" S ",
				"GPG",
				" S ",
				'S',LibOreDict.BIFROST_SHARD,
				'G',new ItemStack(Blocks.gold_block),
				'P',new ItemStack(GameRegistry.findBlock("Botania", "pylon")));
		recipePortalPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.dimensionalPylon,1,1),
				" M ",
				"GPG",
				" M ",
				'M',"manaPearl",
				'G',new ItemStack(Blocks.gold_block),
				'P',new ItemStack(GameRegistry.findBlock("Botania", "pylon")));
		recipeUntunedPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.bifrostBlock),
				"SSS",
				"SSS",
				"SSS",
				'S',LibOreDict.BIFROST_SHARD);
		recipeBifrostBlock = BotaniaAPI.getLatestAddedRecipe();

		addOreDictRecipe(new ItemStack(ModItems.simpleResource,9,0),
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
				new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,8));
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
				'G', "eternalLifeEssence",
				'S',LibOreDict.BIFROST_SHARD,
				'T',new ItemStack(GameRegistry.findBlock("Botania", "storage"),1,1));
		recipePortalCore = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.dwarvenManaPool),
				"SDS",
				"SSS",
				'S', new ItemStack(ModBlocks.dwarfRock),
				'D', new ItemStack(GameRegistry.findBlock("Botania", "pool")));
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
				'B', LibOreDict.BIFROST_SHARD);
		recipeUpgradeCharge = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(ModItems.chargedStone),
				new ItemStack(Items.diamond),
				LibOreDict.BIFROST_SHARD,
				"manaPearl",
				"elvenPixieDust");
		recipeChargedStone = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 0),
				"SSS",
				"S S",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 1),
				"S S",
				"SSS",
				"SSS",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 2),
				"SSS",
				"S S",
				"S S",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 3),
				"S S",
				"S S",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 4),
				"SSS",
				" T ",
				" T ",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 5),
				"S",
				"T",
				"T",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 6),
				"SS",
				"TS",
				"T ",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 7),
				"S",
				"S",
				"T",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 8),
				" TS",
				"T S",
				" TS",
				'S', "manaString",
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 9),
				"SS",
				"T ",
				"T ",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		addOreDictRecipe(new ItemStack(ModItems.enchantHolder, 1, 10),
				"S ",
				" S",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'T', LibOreDict.PARADOX_TWIG);
		holderEnchantRecipes=BotaniaAPI.getLatestAddedRecipes(11);
		
		addOreDictRecipe(new ItemStack(ModBlocks.enchantPlate),
				"SGS",
				"RPR",
				"123",
				'S', new ItemStack(ModBlocks.paradoxStone, 1, 0),
				'G', new ItemStack(Blocks.gold_block),
				'P', "manaPearl",
				'R', "runeManaB",
				'1', "runeGreedB",
				'2', "runeSlothB",
				'3', "runePrideB");
		enchantPlateRecipe=BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.simpleResource, 1, 4),
				"QDQ",
				"DPD",
				"QDQ",
				'P', "manaPearl",
				'D', new ItemStack(ModBlocks.darkenedDust),
				'Q', new ItemStack(Items.quartz));
		enchantOfferingRecipe=BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(ModBlocks.darkElfTrap),
				"S S",
				"BDB",
				"MIM",
				'S', new ItemStack(GameRegistry.findItem("Botania", "terraSword")),
				'B', new ItemStack(Items.bowl),
				'D', "manaDiamond",
				'M', "ingotManasteel",
				'I', new ItemStack(GameRegistry.findItem("Botania", "phantomInk")));
		darkElfTrapRecipe = BotaniaAPI.getLatestAddedRecipe();
		
		defineCustomRecipes();
	}

	private static void defineCustomRecipes() {
		GameRegistry.addRecipe(new PapersIntoLexiconRecipe());
		GameRegistry.addRecipe(new PapersCloneRecipe());
		GameRegistry.addRecipe(new HolderIntoBookRecipe());
		GameRegistry.addRecipe(new DarkElfAugmentRecipe());

	}

	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
