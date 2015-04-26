package soundlogic.silva.common.lexicon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.ModCraftingRecipes;
import soundlogic.silva.common.crafting.ModPortalTradeRecipes;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.page.PageAdvancedCraftingRecipe;
import soundlogic.silva.common.lexicon.page.PageAdvancedFurnaceRecipe;
import soundlogic.silva.common.lexicon.page.PageAdvancedImage;
import soundlogic.silva.common.lexicon.page.PageAdvancedPortalTrade;
import soundlogic.silva.common.lexicon.page.PageAdvancedText;
import soundlogic.silva.common.lexicon.page.PageDimensionSignature;
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
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class LexiconData {

	public static LexiconCategory categoryWorldTree;
	public static LexiconCategory categoryDwarven;
	public static LexiconCategory categoryDarkElf;
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
	public static LexiconEntry dimVigrior;
	public static LexiconEntry dimFolkvangr;
	public static LexiconEntry dimValhalla;
	public static LexiconEntry dimHelheim;
	public static LexiconEntry dimAsgard;
	public static LexiconEntry dimAlfheim;
	public static LexiconEntry dimMinegard;
	public static LexiconEntry dimJotunheimr;
	public static LexiconEntry dimSvartalfheim;
	public static LexiconEntry dimMuspelheim;
	public static LexiconEntry dimNiflheim;
	public static LexiconEntry dimNidavellir;
	public static LexiconEntry dimVanaheimr;
	public static LexiconEntry bifrost;
	public static LexiconEntry darkenedDust;
	public static LexiconEntry nubliaMessage;
	
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

	public static void preInit() {
		
		worldTreeKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_TREE, EnumChatFormatting.DARK_PURPLE, false);
		dwarvenKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_DWARVEN, EnumChatFormatting.DARK_GRAY, false);
		darkElfKnowledge=BotaniaAPI.registerKnowledgeType(LibLexicon.KNOWLEDGE_DARK_ELF, EnumChatFormatting.WHITE, false);
		
		categoryWorldTree=new LexiconCategory(LibLexicon.CATEGORY_TREE);
		BotaniaAPI.addCategory(categoryWorldTree);
		categoryDwarven=new LexiconCategory(LibLexicon.CATEGORY_DWARVEN);
		BotaniaAPI.addCategory(categoryDwarven);
		categoryDarkElf=new LexiconCategory(LibLexicon.CATEGORY_DARK_ELF);
		BotaniaAPI.addCategory(categoryDarkElf);
		
		ItemStack thrownPearl=new ItemStack(Items.ender_pearl);
		thrownPearl.setStackDisplayName("Thrown Ender Pearl");
		ItemStack questionMark=new ItemStack(ModItems.proxyItem);
		
		dimensionalTravelAttempt=new SLexiconEntry(LibLexicon.DIMENSIONAL_TRAVEL_ATTEMPT, BotaniaAPI.categoryAlfhomancy);
		dimensionalTravelAttempt.setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("0"), new PageElvenRecipe("1", new RecipeElvenTrade(questionMark,thrownPearl)));
		
		dimensionalTravelFailure=new SLexiconEntry(LibLexicon.DIMENSIONAL_TRAVEL_FAILURE, BotaniaAPI.categoryAlfhomancy);
		dimensionalTravelFailure.setKnowledgeType(worldTreeKnowledge).setLexiconPages(new PageText("0"));

		IRecipe LexiconFusion=new ShapelessRecipes(new ItemStack(ModItems.fakeLexicon), Arrays.asList(new ItemStack[]{new ItemStack(vazkii.botania.common.item.ModItems.lexicon),new ItemStack(ModItems.fakePageBundle)}));
		IRecipe PageDuplication=new ShapelessRecipes(new ItemStack(ModItems.fakePageBundle,2), Arrays.asList(new ItemStack[]{new ItemStack(ModItems.fakePageBundle),new ItemStack(Items.paper)}));
		paperBundles=new SLexiconEntry(LibLexicon.PAPER_BUNDLES, BotaniaAPI.categoryMisc);
		paperBundles.setPriority().setLexiconPages(new PageText("0"),new PageCraftingRecipe("1",LexiconFusion),new PageCraftingRecipe("2",PageDuplication));
		
		darkElfIntro=new SLexiconEntry(LibLexicon.DARK_ELF_INTRO, categoryDarkElf);
		darkElfIntro.setKnowledgeType(darkElfKnowledge).setLexiconPages(new PageAdvancedText("0", PageBackground.ELVEN)).setPriority();

		manaEater=new SLexiconEntry(LibLexicon.MANA_EATER, categoryDarkElf);
		manaEater.setKnowledgeType(darkElfKnowledge).setLexiconPages(new PageAdvancedText("0", PageBackground.ELVEN));

		darkenedDust=new SLexiconEntry(LibLexicon.DARKENED_DUST, categoryDarkElf);
		darkenedDust.setKnowledgeType(darkElfKnowledge).setLexiconPages(new PageAdvancedText("0", PageBackground.ELVEN));

		worldTreeIntro=new SLexiconEntry(LibLexicon.WORLD_TREE_INTRO, categoryWorldTree);
		worldTreeIntro.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.ELVEN),
				new PageAdvancedText("1", PageBackground.ELVEN),
				new PageAdvancedText("2", PageBackground.ELVEN)).setPriority();
		
		bifrost=new SLexiconEntry(LibLexicon.BIFROST, categoryWorldTree);
		bifrost.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN),
				new PageAdvancedText("1",PageBackground.ELVEN),
				new PageAdvancedText("2",PageBackground.ELVEN),
				new PageAdvancedText("3",PageBackground.ELVEN),
				new PageAdvancedText("4",PageBackground.ELVEN),
				new PageAdvancedText("5",PageBackground.ELVEN),
				new PageAdvancedText("6",PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("7",ModCraftingRecipes.recipeBifrostBlock,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("8",ModCraftingRecipes.recipeBifrostShard,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("9",ModCraftingRecipes.recipeBifrostBlockStairs,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("10",ModCraftingRecipes.recipeBifrostBlockWall,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("11",ModCraftingRecipes.recipeBifrostBlockSlab,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("12",ModCraftingRecipes.recipeBifrostSparkling,PageBackground.ELVEN)).setPriority();
		
		advancedPortals=new SLexiconEntry(LibLexicon.ADVANCED_PORTALS, categoryWorldTree);
		advancedPortals.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0", PageBackground.ELVEN),
				new PageAdvancedText("1", PageBackground.ELVEN),
				new PageAdvancedText("2", PageBackground.ELVEN),
				new PageAdvancedText("3", PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("4",ModCraftingRecipes.recipePortalCore,PageBackground.ELVEN),
				new PageAdvancedCraftingRecipe("5",ModCraftingRecipes.recipePortalPylon,PageBackground.ELVEN),
				new PageAdvancedImage("6",LibResources.PORTAL_PICS[0],PageBackground.ELVEN),
				new PageAdvancedImage("7",LibResources.PORTAL_PICS[1],PageBackground.ELVEN),
				new PageAdvancedImage("8",LibResources.PORTAL_PICS[2],PageBackground.ELVEN),
				new PageAdvancedImage("9",LibResources.PORTAL_PICS[3],PageBackground.ELVEN),
				new PageAdvancedImage("10",LibResources.PORTAL_PICS[4],PageBackground.ELVEN),
				new PageAdvancedImage("11",LibResources.PORTAL_PICS[5],PageBackground.ELVEN),
				new PageAdvancedImage("12",LibResources.PORTAL_PICS[6],PageBackground.ELVEN),
				new PageAdvancedText("13",PageBackground.ELVEN)).setPriority();
		
		dwarfIntro=new SLexiconEntry(LibLexicon.DWARF_INTRO, categoryDwarven);
		dwarfIntro.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfSignRecipe,Dimension.NIDAVELLIR),
				new PageAdvancedText("3",PageBackground.DWARVEN),
				new PageAdvancedText("4",PageBackground.DWARVEN),
				new PageAdvancedText("5",PageBackground.DWARVEN),
				new PageAdvancedText("6",PageBackground.DWARVEN)).setPriority();
		
		basicDwarvenResources=new SLexiconEntry(LibLexicon.BASIC_DWARVEN_RESOURCES, categoryDwarven);
		basicDwarvenResources.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("1", ModPortalTradeRecipes.dwarfWeedRecipe,Dimension.NIDAVELLIR),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfDwarfRockRecipe,Dimension.NIDAVELLIR),
				new PageAdvancedPortalTrade("3", ModPortalTradeRecipes.dwarfMushroomRecipes,Dimension.NIDAVELLIR),
				new PageAdvancedPortalTrade("4", ModPortalTradeRecipes.dwarfLivingRockRecipe,Dimension.NIDAVELLIR)).setPriority();
		
		boomMoss=new SLexiconEntry(LibLexicon.BOOM_MOSS, categoryDwarven);
		boomMoss.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedCraftingRecipe("2",ModCraftingRecipes.recipeBoomMoss,PageBackground.DWARVEN),
				new PageAdvancedText("3",PageBackground.DWARVEN),
				new PageAdvancedText("4",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("5", ModPortalTradeRecipes.dwarfMasterBoomMossTrade,Dimension.NIDAVELLIR),
				new PageAdvancedText("6",PageBackground.DWARVEN),
				new PageAdvancedFurnaceRecipe("7", new ItemStack(ModBlocks.boomMoss, 1, 2), new ItemStack(ModBlocks.boomMoss, 1, 0), PageBackground.DWARVEN)
				);

		dwarfForged=new SLexiconEntry(LibLexicon.DWARF_FORGED, categoryDwarven);
		dwarfForged.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedText("2",PageBackground.DWARVEN),
				new PageAdvancedText("3",PageBackground.DWARVEN),
				new PageAdvancedText("4",PageBackground.DWARVEN),
				new PageAdvancedText("5",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("6", ModPortalTradeRecipes.dwarfForgingDisplay,Dimension.NIDAVELLIR),
				new PageAdvancedText("7",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("8", ModPortalTradeRecipes.dwarfReforgingDisplay,Dimension.NIDAVELLIR)
				).setIcon(new ItemStack(Blocks.anvil));
		
		manaCrystal=new SLexiconEntry(LibLexicon.MANA_CRYSTAL, categoryDwarven);
		manaCrystal.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfManaCrystalTrade,Dimension.NIDAVELLIR)
				);

		dwarvenManaPool=new SLexiconEntry(LibLexicon.DWARVEN_MANA_POOL, categoryDwarven);
		dwarvenManaPool.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedCraftingRecipe("2",ModCraftingRecipes.recipeDwarvenPool,PageBackground.DWARVEN));

		stoneHorse=new SLexiconEntry(LibLexicon.STONE_HORSE, categoryDwarven);
		stoneHorse.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfStoneHorseTrade,Dimension.NIDAVELLIR)
				);

		dwarvenMead=new SLexiconEntry(LibLexicon.DWARVEN_MEAD, categoryDwarven);
		dwarvenMead.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedText("2",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("3", new IPortalRecipe[]{ModPortalTradeRecipes.dwarfMelonToMeadTrade,ModPortalTradeRecipes.dwarfPumpkinToMeadTrade},Dimension.NIDAVELLIR));

		dwarvenChain=new SLexiconEntry(LibLexicon.DWARVEN_CHAIN, categoryDwarven);
		dwarvenChain.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfChainTrade,Dimension.NIDAVELLIR));

		dwarvenBarrierStone=new SLexiconEntry(LibLexicon.DWARVEN_BARRIER_STONE, categoryDwarven);
		dwarvenBarrierStone.setKnowledgeType(dwarvenKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.DWARVEN),
				new PageAdvancedText("1",PageBackground.DWARVEN),
				new PageAdvancedPortalTrade("2", ModPortalTradeRecipes.dwarfBarrierTrade,Dimension.NIDAVELLIR));

		dimGinnungagap=new SLexiconEntry(LibLexicon.DIM_GINNUNGAGAP, categoryWorldTree);
		dimGinnungagap.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageAdvancedText("2",PageBackground.ELVEN), 
				new PageDimensionSignature("3",Dimension.GINNUNGAGAP, PageBackground.ELVEN));

		dimVigrior=new SLexiconEntry(LibLexicon.DIM_VIGRIOR, categoryWorldTree);
		dimVigrior.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.VIGRIOR, PageBackground.ELVEN));
		
		dimFolkvangr=new SLexiconEntry(LibLexicon.DIM_FOLKVANGR, categoryWorldTree);
		dimFolkvangr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.FOLKVANGR, PageBackground.ELVEN));
		
		dimValhalla=new SLexiconEntry(LibLexicon.DIM_VALHALLA, categoryWorldTree);
		dimValhalla.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.VALHALLA, PageBackground.ELVEN));
		
		dimHelheim=new SLexiconEntry(LibLexicon.DIM_HELHEIM, categoryWorldTree);
		dimHelheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.HELHEIM, PageBackground.ELVEN));
		
		dimAsgard=new SLexiconEntry(LibLexicon.DIM_ASGARD, categoryWorldTree);
		dimAsgard.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageAdvancedText("2",PageBackground.ELVEN), 
				new PageDimensionSignature("3",Dimension.ASGARD, PageBackground.ELVEN));
		
		dimAlfheim=new SLexiconEntry(LibLexicon.DIM_ALFHEIM, categoryWorldTree);
		dimAlfheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.ALFHEIM, PageBackground.ELVEN));
		
		dimMinegard=new SLexiconEntry(LibLexicon.DIM_MINEGARD, categoryWorldTree);
		dimMinegard.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.MINEGARD, PageBackground.ELVEN));
		
		dimJotunheimr=new SLexiconEntry(LibLexicon.DIM_JOTUNHEIMR, categoryWorldTree);
		dimJotunheimr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.JOTUNHEIMR, PageBackground.ELVEN));
		
		dimSvartalfheim=new SLexiconEntry(LibLexicon.DIM_SVARTALFHEIM, categoryWorldTree);
		dimSvartalfheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.SVARTALFHEIM, PageBackground.ELVEN));
		
		dimMuspelheim=new SLexiconEntry(LibLexicon.DIM_MUSPELHEIM, categoryWorldTree);
		dimMuspelheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.MUSPELHEIM, PageBackground.ELVEN));
		
		dimNiflheim=new SLexiconEntry(LibLexicon.DIM_NIFLHEIM, categoryWorldTree);
		dimNiflheim.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageDimensionSignature("2",Dimension.NIFLHEIM, PageBackground.ELVEN));
		
		dimNidavellir=new SLexiconEntry(LibLexicon.DIM_NIDAVELLIR, categoryWorldTree);
		dimNidavellir.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN), 
				new PageAdvancedText("1",PageBackground.ELVEN), 
				new PageAdvancedText("2",PageBackground.ELVEN), 
				new PageDimensionSignature("3",Dimension.NIDAVELLIR, PageBackground.ELVEN));
		
		dimVanaheimr=new SLexiconEntry(LibLexicon.DIM_VANAHEIMR, categoryWorldTree);
		dimVanaheimr.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedText("0",PageBackground.ELVEN),
				new PageAdvancedText("1",PageBackground.ELVEN));
		
		nubliaMessage=new SLexiconEntry(LibLexicon.NUBLIA_MESSAGE, categoryWorldTree);
		nubliaMessage.setKnowledgeType(worldTreeKnowledge).setLexiconPages(
				new PageAdvancedImage("0",vazkii.botania.client.lib.LibResources.ENTRY_ELVEN_GARDE,PageBackground.ELVEN),
				new PageAdvancedText("1",PageBackground.ELVEN),
				new PageAdvancedText("2",PageBackground.ELVEN),
				new PageAdvancedText("3",PageBackground.ELVEN)).setPriority();
		
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
