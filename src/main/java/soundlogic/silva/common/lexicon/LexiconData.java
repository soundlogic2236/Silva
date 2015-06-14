package soundlogic.silva.common.lexicon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.ModCraftingRecipes;
import soundlogic.silva.common.crafting.ModDarkElfActs;
import soundlogic.silva.common.crafting.ModPetalRecipes;
import soundlogic.silva.common.crafting.ModPortalTradeRecipes;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.page.PageAdvancedCraftingRecipe;
import soundlogic.silva.common.lexicon.page.PageAdvancedDarkElfAct;
import soundlogic.silva.common.lexicon.page.PageAdvancedFurnaceRecipe;
import soundlogic.silva.common.lexicon.page.PageAdvancedImage;
import soundlogic.silva.common.lexicon.page.PageAdvancedPortalTrade;
import soundlogic.silva.common.lexicon.page.PageAdvancedText;
import soundlogic.silva.common.lexicon.page.PageDimensionSignature;
import soundlogic.silva.common.lexicon.page.PageEnchantmentMoving;
import soundlogic.silva.common.lib.LibLexicon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageLoreText;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class LexiconData {

	public static LexiconCategory categoryWorldTree;
	public static KnowledgeType worldTreeKnowledge;
	public static KnowledgeType dwarvenKnowledge;
	public static KnowledgeType darkElfKnowledge;
	public static LexiconEntry dimensionalTravelAttempt;
	public static LexiconEntry dimensionalTravelFailure;
	public static LexiconEntry paperBundles;
	public static LexiconEntry manaEater;
	public static LexiconEntry darkElfIntro;
	public static LexiconEntry advancedPortals;
	public static LexiconEntry basicDwarvenResources;
	public static LexiconEntry boomMoss;
	public static LexiconEntry dwarfForged;
	public static LexiconEntry worldTreeIntro;
	public static LexiconEntry dwarfIntro;
	public static LexiconEntry dimGinnungagap;
	public static LexiconEntry dimVigridr;
	public static LexiconEntry dimFolkvangr;
	public static LexiconEntry dimValhalla;
	public static LexiconEntry dimHelheim;
	public static LexiconEntry dimAsgard;
	public static LexiconEntry dimAlfheim;
	public static LexiconEntry dimMidgard;
	public static LexiconEntry dimJotunheimr;
	public static LexiconEntry dimSvartalfheim;
	public static LexiconEntry dimMuspelheim;
	public static LexiconEntry dimNiflheim;
	public static LexiconEntry dimNidavellir;
	public static LexiconEntry dimVanaheimr;
	public static LexiconEntry bifrost;
	public static LexiconEntry darkenedDust;
	public static LexiconEntry nubliaMessage;
	public static LexiconEntry darkElves;
	
	public static ItemStack dimensionalPapers;
	public static ItemStack dwarvenPapers;
	public static ItemStack darkElfPapers;
	public static List<ItemStack> papers;
	public static LexiconEntry manaCrystal;
	public static LexiconEntry dwarvenManaPool;
	public static LexiconEntry stoneHorse;
	public static LexiconEntry dwarvenMead;
	public static LexiconEntry dwarvenChain;
	public static LexiconEntry dwarvenBarrierStone;
	public static LexiconEntry lavaShroom;
	public static LexiconEntry carnilotus;
	public static LexiconEntry rhododender;
	public static LexiconEntry portalUpgrades;
	public static LexiconEntry darkElfResources;
	public static LexiconEntry placeableDust;
	public static LexiconEntry enchantmentMoving;
	public static LexiconEntry blazeBloom;
	public static LexiconEntry glitterelle;
	public static LexiconEntry mysticalGrinder;
	public static LexiconEntry slingshot;
	public static LexiconEntry enderCatcher;
	public static LexiconEntry PIXIE_FARM;

	public static void preInit() {
		
		worldTreeKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_TREE, EnumChatFormatting.DARK_PURPLE, false);
		dwarvenKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_DWARVEN, EnumChatFormatting.DARK_RED, false);
		darkElfKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_DARK_ELF, EnumChatFormatting.WHITE, false);
		
		categoryWorldTree=new LexiconCategory(LibLexicon.CATEGORY_TREE).setIcon(new ResourceLocation(LibResources.WORLD_TREE_ICON + ".png"));
		BotaniaAPI.addCategory(categoryWorldTree);
		
		ItemStack thrownPearl=new ItemStack(Items.ender_pearl);
		thrownPearl.setStackDisplayName("Thrown Ender Pearl");
		ItemStack questionMark=new ItemStack(ModItems.proxyItem);
		
		dimensionalTravelAttempt=new SLexiconEntry(LibLexicon.DIMENSIONAL_TRAVEL_ATTEMPT, BotaniaAPI.categoryAlfhomancy);
		dimensionalTravelAttempt.setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("0"), new PageElvenRecipe("1", new RecipeElvenTrade(questionMark,thrownPearl)));
		
		dimensionalTravelFailure=new SLexiconEntry(LibLexicon.DIMENSIONAL_TRAVEL_FAILURE, BotaniaAPI.categoryAlfhomancy);
		dimensionalTravelFailure.setKnowledgeType(worldTreeKnowledge).setLexiconPages(new PageText("0"));

		IRecipe LexiconFusion=new ShapelessRecipes(new ItemStack(ModItems.fakeLexicon), Arrays.asList(new ItemStack[]{new ItemStack(GameRegistry.findItem("Botania", "lexicon")),new ItemStack(ModItems.fakePageBundle)}));
		IRecipe PageDuplication=new ShapelessRecipes(new ItemStack(ModItems.fakePageBundle,2), Arrays.asList(new ItemStack[]{new ItemStack(ModItems.fakePageBundle),new ItemStack(Items.paper)}));
		paperBundles=new SLexiconEntry(LibLexicon.PAPER_BUNDLES, BotaniaAPI.categoryMisc);
		paperBundles.setPriority().setLexiconPages(new PageText("0"),new PageCraftingRecipe("1",LexiconFusion),new PageCraftingRecipe("2",PageDuplication));
		
		worldTreeIntro=new SLexiconEntry(LibLexicon.WORLD_TREE_INTRO, categoryWorldTree);
		worldTreeIntro.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.ELVEN),
				new PageAdvancedText("1", PageBackground.ELVEN),
				new PageAdvancedText("2", PageBackground.ELVEN)).setPriority();
		
		bifrost=new SLexiconEntry(LibLexicon.BIFROST, categoryWorldTree);
		bifrost.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedText("2",PageBackground.DEFAULT),
				new PageAdvancedText("3",PageBackground.DEFAULT),
				new PageAdvancedText("4",PageBackground.DEFAULT),
				new PageAdvancedText("5",PageBackground.DEFAULT),
				new PageAdvancedText("6",PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("7",ModCraftingRecipes.recipeBifrostBlock,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("8",ModCraftingRecipes.recipeBifrostShard,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("9",ModCraftingRecipes.recipeBifrostBlockStairs,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("10",ModCraftingRecipes.recipeBifrostBlockWall,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("11",ModCraftingRecipes.recipeBifrostBlockSlab,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("12",ModCraftingRecipes.recipeBifrostSparkling,PageBackground.DEFAULT)).setPriority();
		
		advancedPortals=new SLexiconEntry(LibLexicon.ADVANCED_PORTALS, categoryWorldTree);
		advancedPortals.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedText("2", PageBackground.DEFAULT),
				new PageAdvancedText("3", PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("4",ModCraftingRecipes.recipePortalCore,PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("5",ModCraftingRecipes.recipePortalPylon,PageBackground.DEFAULT),
				new PageAdvancedImage("6",LibResources.PORTAL_PICS[0],PageBackground.DEFAULT),
				new PageAdvancedImage("7",LibResources.PORTAL_PICS[1],PageBackground.DEFAULT),
				new PageAdvancedImage("8",LibResources.PORTAL_PICS[2],PageBackground.DEFAULT),
				new PageAdvancedImage("9",LibResources.PORTAL_PICS[3],PageBackground.DEFAULT),
				new PageAdvancedImage("10",LibResources.PORTAL_PICS[4],PageBackground.DEFAULT),
				new PageAdvancedImage("11",LibResources.PORTAL_PICS[5],PageBackground.DEFAULT),
				new PageAdvancedImage("12",LibResources.PORTAL_PICS[6],PageBackground.DEFAULT),
				new PageAdvancedText("13",PageBackground.ELVEN)).setPriority();
		
		dwarfIntro=new SLexiconEntry(LibLexicon.DWARF_INTRO, categoryWorldTree);
		dwarfIntro.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfSignRecipe,Dimension.NIDAVELLIR, PageBackground.DWARVEN),
				new PageAdvancedText("3",PageBackground.DWARVEN),
				new PageAdvancedText("4",PageBackground.DWARVEN),
				new PageAdvancedText("5",PageBackground.DWARVEN),
				new PageAdvancedText("6",PageBackground.DWARVEN)).setPriority();
		
		basicDwarvenResources=new SLexiconEntry(LibLexicon.BASIC_DWARVEN_RESOURCES, categoryWorldTree);
		basicDwarvenResources.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("1", ModPortalTradeRecipes.dwarfWeedRecipe,Dimension.NIDAVELLIR, PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfDwarfRockRecipe,Dimension.NIDAVELLIR, PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("3", ModPortalTradeRecipes.dwarfMushroomRecipes,Dimension.NIDAVELLIR, PageBackground.DEFAULT)).setPriority();
		
		boomMoss=new SLexiconEntry(LibLexicon.BOOM_MOSS, BotaniaAPI.categoryDevices);
		boomMoss.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("2",ModCraftingRecipes.recipeBoomMoss,PageBackground.DEFAULT),
				new PageAdvancedText("3",PageBackground.DEFAULT),
				new PageAdvancedText("4",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("5", ModPortalTradeRecipes.dwarfMasterBoomMossTrade,Dimension.NIDAVELLIR, PageBackground.DEFAULT),
				new PageAdvancedText("6",PageBackground.DEFAULT),
				new PageAdvancedFurnaceRecipe("7", new ItemStack(ModBlocks.boomMoss, 1, 2), new ItemStack(ModBlocks.boomMoss, 1, 0), PageBackground.DEFAULT)
				);

		dwarfForged=new SLexiconEntry(LibLexicon.DWARF_FORGED, BotaniaAPI.categoryTools);
		dwarfForged.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedText("2",PageBackground.DEFAULT),
				new PageAdvancedText("3",PageBackground.DEFAULT),
				new PageAdvancedText("4",PageBackground.DEFAULT),
				new PageAdvancedText("5",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("6", ModPortalTradeRecipes.dwarfForgingDisplay,Dimension.NIDAVELLIR, PageBackground.DEFAULT),
				new PageAdvancedText("7",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("8", ModPortalTradeRecipes.dwarfReforgingDisplay,Dimension.NIDAVELLIR, PageBackground.DEFAULT)
				).setIcon(new ItemStack(Blocks.anvil));
		
		manaCrystal=new SLexiconEntry(LibLexicon.MANA_CRYSTAL, BotaniaAPI.categoryMana);
		manaCrystal.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfManaCrystalTrade,Dimension.NIDAVELLIR, PageBackground.DEFAULT)
				);

		dwarvenManaPool=new SLexiconEntry(LibLexicon.DWARVEN_MANA_POOL, BotaniaAPI.categoryMana);
		dwarvenManaPool.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("2",ModCraftingRecipes.recipeDwarvenPool,PageBackground.DEFAULT));

		stoneHorse=new SLexiconEntry(LibLexicon.STONE_HORSE, BotaniaAPI.categoryTools);
		stoneHorse.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfStoneHorseTrade,Dimension.NIDAVELLIR, PageBackground.DEFAULT)
				);

		dwarvenMead=new SLexiconEntry(LibLexicon.DWARVEN_MEAD, BotaniaAPI.categoryTools);
		dwarvenMead.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedText("2",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("3", new IPortalRecipe[]{ModPortalTradeRecipes.dwarfMelonToMeadTrade,ModPortalTradeRecipes.dwarfPumpkinToMeadTrade},Dimension.NIDAVELLIR, PageBackground.DEFAULT));

		dwarvenChain=new SLexiconEntry(LibLexicon.DWARVEN_CHAIN, BotaniaAPI.categoryTools);
		dwarvenChain.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfChainTrade,Dimension.NIDAVELLIR, PageBackground.DEFAULT));

		dwarvenBarrierStone=new SLexiconEntry(LibLexicon.DWARVEN_BARRIER_STONE, BotaniaAPI.categoryTools);
		dwarvenBarrierStone.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfBarrierTrade,Dimension.NIDAVELLIR, PageBackground.DEFAULT));

		dimGinnungagap=new SLexiconEntry(LibLexicon.DIM_GINNUNGAGAP, categoryWorldTree);
		dimGinnungagap.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageAdvancedText("2",PageBackground.DEFAULT), 
				new PageDimensionSignature("3",Dimension.GINNUNGAGAP, PageBackground.DEFAULT));

		dimVigridr=new SLexiconEntry(LibLexicon.DIM_VIGRIOR, categoryWorldTree);
		dimVigridr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.VIGRIDR, PageBackground.DEFAULT));
		
		dimFolkvangr=new SLexiconEntry(LibLexicon.DIM_FOLKVANGR, categoryWorldTree);
		dimFolkvangr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.FOLKVANGR, PageBackground.DEFAULT));
		
		dimValhalla=new SLexiconEntry(LibLexicon.DIM_VALHALLA, categoryWorldTree);
		dimValhalla.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.VALHALLA, PageBackground.DEFAULT));
		
		dimHelheim=new SLexiconEntry(LibLexicon.DIM_HELHEIM, categoryWorldTree);
		dimHelheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.HELHEIM, PageBackground.DEFAULT));
		
		dimAsgard=new SLexiconEntry(LibLexicon.DIM_ASGARD, categoryWorldTree);
		dimAsgard.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageAdvancedText("2",PageBackground.DEFAULT), 
				new PageDimensionSignature("3",Dimension.ASGARD, PageBackground.DEFAULT));
		
		dimAlfheim=new SLexiconEntry(LibLexicon.DIM_ALFHEIM, categoryWorldTree);
		dimAlfheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.ALFHEIM, PageBackground.DEFAULT));
		
		dimMidgard=new SLexiconEntry(LibLexicon.DIM_MIDGARD, categoryWorldTree);
		dimMidgard.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.MIDGARD, PageBackground.DEFAULT));
		
		dimJotunheimr=new SLexiconEntry(LibLexicon.DIM_JOTUNHEIMR, categoryWorldTree);
		dimJotunheimr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.JOTUNHEIMR, PageBackground.DEFAULT));
		
		dimSvartalfheim=new SLexiconEntry(LibLexicon.DIM_SVARTALFHEIM, categoryWorldTree);
		dimSvartalfheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.SVARTALFHEIM, PageBackground.DEFAULT));
		
		dimMuspelheim=new SLexiconEntry(LibLexicon.DIM_MUSPELHEIM, categoryWorldTree);
		dimMuspelheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.MUSPELHEIM, PageBackground.DEFAULT));
		
		dimNiflheim=new SLexiconEntry(LibLexicon.DIM_NIFLHEIM, categoryWorldTree);
		dimNiflheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.NIFLHEIM, PageBackground.DEFAULT));
		
		dimNidavellir=new SLexiconEntry(LibLexicon.DIM_NIDAVELLIR, categoryWorldTree);
		dimNidavellir.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT), 
				new PageAdvancedText("1",PageBackground.DEFAULT), 
				new PageDimensionSignature("2",Dimension.NIDAVELLIR, PageBackground.DEFAULT));
		
		dimVanaheimr=new SLexiconEntry(LibLexicon.DIM_VANAHEIMR, categoryWorldTree);
		dimVanaheimr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DEFAULT),
				new PageAdvancedText("1",PageBackground.DEFAULT));
		
		nubliaMessage=new SLexiconEntry(LibLexicon.NUBLIA_MESSAGE, categoryWorldTree);
		nubliaMessage.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedImage("0",vazkii.botania.client.lib.LibResources.ENTRY_ELVEN_GARDE,PageBackground.ELVEN),
				new PageAdvancedText("1",PageBackground.ELVEN),
				new PageAdvancedText("2",PageBackground.ELVEN),
				new PageAdvancedText("3",PageBackground.ELVEN)).setPriority();
		
		darkElfIntro=new SLexiconEntry(LibLexicon.DARK_ELF_INTRO, categoryWorldTree);
		darkElfIntro.setKnowledgeType(darkElfKnowledge).setLexiconPages(
				new PageAdvancedImage("0",vazkii.botania.client.lib.LibResources.ENTRY_ELVEN_GARDE,PageBackground.ELVEN),
				new PageAdvancedText("1", PageBackground.ELVEN),
				new PageAdvancedText("2", PageBackground.ELVEN),
				new PageAdvancedText("3", PageBackground.ELVEN)
				).setPriority();

		manaEater=new SLexiconEntry(LibLexicon.MANA_EATER, BotaniaAPI.categoryMana);
		manaEater.setKnowledgeType(darkElfKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedDarkElfAct("1",ModDarkElfActs.spreaderConversions,PageBackground.DEFAULT));

		darkenedDust=new SLexiconEntry(LibLexicon.DARKENED_DUST, BotaniaAPI.categoryDevices);
		darkenedDust.setKnowledgeType(darkElfKnowledge).setLexiconPages(new PageAdvancedText("0", PageBackground.DEFAULT));

		portalUpgrades=new SLexiconEntry(LibLexicon.PORTAL_UPGRADES, categoryWorldTree);
		portalUpgrades.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("2", ModCraftingRecipes.recipeUpgradeRedstone, PageBackground.DEFAULT),
				new PageAdvancedText("3", PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("4", ModCraftingRecipes.recipeUpgradeInhibit, PageBackground.DEFAULT),
				new PageAdvancedText("5", PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("6", ModCraftingRecipes.recipeChargedStone, PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("7", ModCraftingRecipes.recipeUpgradeCharge, PageBackground.DEFAULT)
				);

		darkElfResources=new SLexiconEntry(LibLexicon.DARK_ELF_RESOURCES, categoryWorldTree);
		darkElfResources.setKnowledgeType(darkElfKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedDarkElfAct("2",ModDarkElfActs.stoneConversion,PageBackground.DEFAULT),
				new PageAdvancedDarkElfAct("3",ModDarkElfActs.woodConversion,PageBackground.DEFAULT)
					);

		lavaShroom=new SLexiconEntry(LibLexicon.LAVA_SHROOM, BotaniaAPI.categoryGenerationFlowers);
		lavaShroom.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedImage("2",LibResources.LAVA_SHROOM_PICS[0],PageBackground.DEFAULT),
				new PageAdvancedImage("3",LibResources.LAVA_SHROOM_PICS[1],PageBackground.DEFAULT),
				new PageAdvancedText("4", PageBackground.DEFAULT),
				new PageAdvancedImage("5",LibResources.LAVA_SHROOM_PICS[2],PageBackground.DEFAULT),
				new PageAdvancedImage("6",LibResources.LAVA_SHROOM_PICS[3],PageBackground.DEFAULT),
				new PageAdvancedImage("7",LibResources.LAVA_SHROOM_PICS[4],PageBackground.DEFAULT),
				new PageAdvancedText("8", PageBackground.DEFAULT),
				new PageAdvancedText("9", PageBackground.DEFAULT),
				new PageAdvancedText("10",PageBackground.DEFAULT));
		
		darkElves=new SLexiconEntry(LibLexicon.DARK_ELVES_DESCRIPTION, categoryWorldTree);
		darkElves.setKnowledgeType(darkElfKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageAdvancedText("2",PageBackground.ELVEN), 
				new PageAdvancedText("3",PageBackground.ELVEN), 
				new PageAdvancedText("4",PageBackground.ELVEN), 
				new PageAdvancedText("5",PageBackground.ELVEN), 
				new PageAdvancedText("6",PageBackground.ELVEN), 
				new PageAdvancedText("7",PageBackground.ELVEN), 
				new PageAdvancedText("8",PageBackground.ELVEN), 
				new PageAdvancedText("9",PageBackground.ELVEN), 
				new PageAdvancedText("10",PageBackground.ELVEN), 
				new PageAdvancedText("11",PageBackground.ELVEN)).setPriority();
		
		carnilotus=new SLexiconEntry(LibLexicon.CARNILOTUS, BotaniaAPI.categoryGenerationFlowers);
		carnilotus.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedText("2", PageBackground.DEFAULT),
				new PageAdvancedImage("3",LibResources.CARNILOTUS_PICKS[0],PageBackground.DEFAULT),
				new PageAdvancedImage("4",LibResources.CARNILOTUS_PICKS[1],PageBackground.DEFAULT),
				new PageAdvancedImage("5",LibResources.CARNILOTUS_PICKS[2],PageBackground.DEFAULT),
				new PageAdvancedImage("6",LibResources.CARNILOTUS_PICKS[3],PageBackground.DEFAULT),
				new PageAdvancedImage("7",LibResources.CARNILOTUS_PICKS[4],PageBackground.DEFAULT),
				new PageAdvancedImage("8",LibResources.CARNILOTUS_PICKS[5],PageBackground.DEFAULT),
				new PageAdvancedImage("9",LibResources.CARNILOTUS_PICKS[6],PageBackground.DEFAULT),
				new PageAdvancedImage("19",LibResources.CARNILOTUS_PICKS[7],PageBackground.DEFAULT)
				);

		rhododender=new SLexiconEntry(LibLexicon.RHODODENDER, BotaniaAPI.categoryFunctionalFlowers);
		rhododender.setKnowledgeType(BotaniaAPI.basicKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PagePetalRecipe("2", ModPetalRecipes.rhododender)
				);

		placeableDust=new SLexiconEntry(LibLexicon.PLACEABLE_DUST, BotaniaAPI.categoryMisc);
		placeableDust.setKnowledgeType(BotaniaAPI.basicKnowledge).setLexiconPages(new PageAdvancedText("0", PageBackground.DEFAULT)).setPriority();

		enchantmentMoving=new SLexiconEntry(LibLexicon.ENCHANTMENT_MOVING, BotaniaAPI.categoryDevices);
		enchantmentMoving.setKnowledgeType(darkElfKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.DEFAULT),
				new PageAdvancedText("1", PageBackground.DEFAULT),
				new PageAdvancedText("2", PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("3", ModCraftingRecipes.enchantPlateRecipe, PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("4", ModCraftingRecipes.enchantOfferingRecipe, PageBackground.DEFAULT),
				new PageAdvancedCraftingRecipe("5", ModCraftingRecipes.holderEnchantRecipes, PageBackground.DEFAULT),
				new PageAdvancedText("6", PageBackground.DEFAULT),
				new PageEnchantmentMoving("7"),
				new PageAdvancedText("8", PageBackground.DEFAULT),
				new PageAdvancedText("9", PageBackground.DEFAULT)
				);
		
		definePapers();
	}

	public static void definePapers() {
		papers=new ArrayList<ItemStack>();
		dimensionalPapers=new ItemStack(ModItems.pageBundle);
		ItemPapers.setData(dimensionalPapers, worldTreeKnowledge, dimensionalTravelFailure);
		papers.add(dimensionalPapers);

		dwarvenPapers=new ItemStack(ModItems.pageBundle);
		ItemPapers.setData(dwarvenPapers, dwarvenKnowledge, dwarfIntro);
		papers.add(dwarvenPapers);
		
		darkElfPapers=new ItemStack(ModItems.pageBundle);
		ItemPapers.setData(darkElfPapers, darkElfKnowledge, darkElfIntro);
		papers.add(darkElfPapers);
		
	}

}
