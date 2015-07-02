package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.core.handler.DwarfForgedHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.helper.EquipmentHelper;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSigned;
import soundlogic.silva.common.crafting.recipe.DwarfTradeForging;
import soundlogic.silva.common.crafting.recipe.DwarfTradeReforging;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSignedSimple;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSimple;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.PortalRecipeSimple;
import soundlogic.silva.common.item.ItemProxy;
import soundlogic.silva.common.item.ModItems;

public class ModPortalTradeRecipes {

	public static HashMap<String,DwarfTradeSigned> dwarfStringsToTradeSigns=new HashMap<String,DwarfTradeSigned>();
	public static HashMap<DwarfTradeSigned,String> dwarfTradeSignsToStrings=new HashMap<DwarfTradeSigned,String>();
	private static List<DwarfTradeWeight> dwarfTradeSignWeights=new ArrayList<DwarfTradeWeight>();

	public static IPortalRecipe dwarfSignRecipe;
	public static IPortalRecipe[] dwarfMushroomRecipes=new IPortalRecipe[16];
	public static IPortalRecipe[] dwarfForgeIronRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfForgeDiamondRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfForgeGoldRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfForgeManasteelRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfForgeTerrasteelRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfForgeElementiumRecipes=new IPortalRecipe[8];
	public static IPortalRecipe[] dwarfReforgeRecipes=new IPortalRecipe[8];
	public static IPortalRecipe dwarfWeedRecipe;
	public static IPortalRecipe dwarfDwarfRockRecipe;
	public static IPortalRecipe dwarfPumpkinToMeadTrade;
	public static IPortalRecipe dwarfMelonToMeadTrade;
	public static IPortalRecipe dwarfChainTrade;
	public static IPortalRecipe dwarfBarrierTrade;
	public static IPortalRecipe dwarfStoneHorseTrade;
	public static IPortalRecipe dwarfManaCrystalTrade;
	public static IPortalRecipe dwarfMasterBoomMossTrade;

	public static List<IPortalRecipe> dwarfForgingDisplay=new ArrayList<IPortalRecipe>();
	public static List<IPortalRecipe> dwarfReforgingDisplay=new ArrayList<IPortalRecipe>();

	
	
	public static void preInit() {
		dwarfSignRecipe=new PortalRecipeSimple(new ItemStack(ModBlocks.dwarvenSign),new ItemStack(Items.sign),new ItemStack(Blocks.iron_block),new ItemStack(ModItems.simpleResource,1,0));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfSignRecipe);
		
		for(int i=0;i<16;i++) {
			dwarfMushroomRecipes[i]=new DwarfTradeSimple(new ItemStack(BotaniaAccessHandler.findBlock("mushroom"),1,i),1,5,new ItemStack(BotaniaAccessHandler.findBlock("flower"),1,i));
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfMushroomRecipes[i]);
		}
		
		dwarfWeedRecipe=new DwarfTradeSimple(new ItemStack(ModBlocks.dwarfWeed),1,5,new ItemStack(Blocks.tallgrass));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfWeedRecipe);
		
		dwarfDwarfRockRecipe=new DwarfTradeSimple(new ItemStack(ModBlocks.dwarfRock),1,5,new ItemStack(BotaniaAccessHandler.findBlock("livingrock")));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfDwarfRockRecipe);
		
		dwarfPumpkinToMeadTrade=new DwarfTradeSignedSimple(new ItemStack(ModItems.dwarfMead),4,1,15,new ItemStack(Blocks.pumpkin));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfPumpkinToMeadTrade);
		
		dwarfMelonToMeadTrade=new DwarfTradeSignedSimple(new ItemStack(ModItems.dwarfMead),4,1,15,new ItemStack(Blocks.melon_block));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfMelonToMeadTrade);
		
		dwarfChainTrade=new DwarfTradeSignedSimple(new ItemStack(ModItems.dwarfChain),5,2,15,new ItemStack(Items.golden_apple));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfChainTrade);
		
		dwarfBarrierTrade=new DwarfTradeSignedSimple(new ItemStack(ModItems.dwarfBarrier),4,1,12,new ItemStack(ModBlocks.dwarfRock),new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,1));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfBarrierTrade);
		
		dwarfStoneHorseTrade=new DwarfTradeSignedSimple(new ItemStack(ModItems.stoneHorse),7,5,16,new ItemStack(ModBlocks.dwarfRock), new ItemStack(Items.saddle),new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,5));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfStoneHorseTrade);
		
		dwarfManaCrystalTrade=new DwarfTradeSignedSimple(new ItemStack(ModBlocks.manaCrystal),12,4,22,new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,2),new ItemStack(BotaniaAccessHandler.findItem("manaBottle")));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfManaCrystalTrade);
		
		dwarfMasterBoomMossTrade=new DwarfTradeSignedSimple(new ItemStack(ModBlocks.boomMoss,1,1),11,3,20,new ItemStack(ModBlocks.boomMoss),new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,5),new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,5));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfMasterBoomMossTrade);

		dwarfForgeIronRecipes=getForgeRecipes(1,3,2,10,new ItemStack(Items.iron_ingot),
				new ItemStack(Items.iron_helmet),
				new ItemStack(Items.iron_chestplate),
				new ItemStack(Items.iron_leggings),
				new ItemStack(Items.iron_boots),
				new ItemStack(Items.iron_pickaxe),
				new ItemStack(Items.iron_shovel),
				new ItemStack(Items.iron_axe),
				new ItemStack(Items.iron_sword));
		dwarfForgeDiamondRecipes=getForgeRecipes(4,3,4,12,new ItemStack(Items.diamond),
				new ItemStack(Items.diamond_helmet),
				new ItemStack(Items.diamond_chestplate),
				new ItemStack(Items.diamond_leggings),
				new ItemStack(Items.diamond_boots),
				new ItemStack(Items.diamond_pickaxe),
				new ItemStack(Items.diamond_shovel),
				new ItemStack(Items.diamond_axe),
				new ItemStack(Items.diamond_sword));
		dwarfForgeGoldRecipes=getForgeRecipes(2,3,2,12,new ItemStack(Items.gold_ingot),
				new ItemStack(Items.golden_helmet),
				new ItemStack(Items.golden_chestplate),
				new ItemStack(Items.golden_leggings),
				new ItemStack(Items.golden_boots),
				new ItemStack(Items.golden_pickaxe),
				new ItemStack(Items.golden_shovel),
				new ItemStack(Items.golden_axe),
				new ItemStack(Items.golden_sword));
		dwarfForgeManasteelRecipes=getForgeRecipes(3,3,4,12,new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,0),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelHelm")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelChest")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelLegs")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelBoots")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelPick")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelShovel")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelAxe")),
				new ItemStack(BotaniaAccessHandler.findItem("manasteelSword")));
		dwarfForgeTerrasteelRecipes=getForgeRecipes(5,3,4,12,new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,4),
				new ItemStack(BotaniaAccessHandler.findItem("terrasteelHelm")),
				new ItemStack(BotaniaAccessHandler.findItem("terrasteelChest")),
				new ItemStack(BotaniaAccessHandler.findItem("terrasteelLegs")),
				new ItemStack(BotaniaAccessHandler.findItem("terrasteelBoots")),
				null,
				null,
				null,
				new ItemStack(BotaniaAccessHandler.findItem("terraSword")));
		dwarfForgeElementiumRecipes=getForgeRecipes(4,3,4,12,new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,7),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumHelm")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumChest")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumLegs")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumBoots")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumPick")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumShovel")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumAxe")),
				new ItemStack(BotaniaAccessHandler.findItem("elementiumSword")));

		dwarfReforgeRecipes[0]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.HELMET,18,12,40);
		dwarfReforgeRecipes[1]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.CHESTPLATE,18,15,40);
		dwarfReforgeRecipes[2]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.LEGGINGS,18,13,40);
		dwarfReforgeRecipes[3]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.BOOTS,18,10,40);
		dwarfReforgeRecipes[4]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.PICKAXE,21,10,40);
		dwarfReforgeRecipes[5]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.SHOVEL,18,8,40);
		dwarfReforgeRecipes[6]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.AXE,18,6,40);
		dwarfReforgeRecipes[7]=new DwarfTradeReforging(EquipmentHelper.EquipmentType.SWORD,21,9,40);

		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.HELMET)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.HELMET), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.CHESTPLATE)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.CHESTPLATE), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.LEGGINGS)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.LEGGINGS), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.BOOTS)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.BOOTS), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.PICKAXE)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.PICKAXE), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.SHOVEL)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.SHOVEL), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.AXE)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.AXE), new ItemStack(ModItems.priceProxyItem)));
		dwarfReforgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.SWORD)),EquipmentHelper.getProxyStackForType(EquipmentHelper.EquipmentType.SWORD), new ItemStack(ModItems.priceProxyItem)));
		
		for(int i=0;i<8;i++) {
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeIronRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeDiamondRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeGoldRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeManasteelRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeTerrasteelRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfForgeElementiumRecipes[i]);
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfReforgeRecipes[i]);
		}

		indexDwarfRecipes();
		weighDwarfRecipes();
	}

	private static IPortalRecipe[] getForgeRecipes(int cost, int minRep, int repBoost, int maxRep, ItemStack ingredient, ItemStack helm, ItemStack chest, ItemStack legs, ItemStack boots, ItemStack pick, ItemStack shovel, ItemStack axe, ItemStack sword) {
		IPortalRecipe[] recipes=new IPortalRecipe[8];
		if(helm!=null) {
			recipes[0]=new DwarfTradeForging(helm,5*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),4,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(helm),new ItemStack(ingredient.getItem(),4,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(chest!=null) {
			recipes[1]=new DwarfTradeForging(chest,10*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),7,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(chest),new ItemStack(ingredient.getItem(),7,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(legs!=null) {
			recipes[2]=new DwarfTradeForging(legs,8*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),6,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(legs),new ItemStack(ingredient.getItem(),6,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(boots!=null) {
			recipes[3]=new DwarfTradeForging(boots,4*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),3,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(boots),new ItemStack(ingredient.getItem(),3,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(pick!=null) {
			recipes[4]=new DwarfTradeForging(pick,5*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(pick),new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(shovel!=null) {
			recipes[5]=new DwarfTradeForging(shovel,2*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(shovel),new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(axe!=null) {
			recipes[6]=new DwarfTradeForging(axe,4*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(axe),new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		if(sword!=null) {
			recipes[7]=new DwarfTradeForging(sword,3*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()));
			dwarfForgingDisplay.add(new PortalRecipeSimple(DwarfForgedHandler.dwarfForgeStack(sword),new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()), new ItemStack(ModItems.priceProxyItem)));
		}
		return recipes;
	}

	private static void weighDwarfRecipes() {
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfPumpkinToMeadTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfMelonToMeadTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfChainTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfBarrierTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfStoneHorseTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfManaCrystalTrade,1600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfMasterBoomMossTrade,1600));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[4],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[5],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[6],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeIronRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[4],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[5],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[6],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeDiamondRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[4],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[5],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[6],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeGoldRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[4],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[5],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[6],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeManasteelRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeTerrasteelRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeTerrasteelRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeTerrasteelRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeTerrasteelRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeTerrasteelRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[0],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[1],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[2],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[3],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[4],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[5],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[6],100));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfForgeElementiumRecipes[7],100));

		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[0],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[1],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[2],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[3],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[4],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[5],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[6],600));
		dwarfTradeSignWeights.add(new DwarfTradeWeight((DwarfTradeSigned)dwarfReforgeRecipes[7],600));
	}
	
	public static DwarfTradeSigned getRandomWeighedTrade(int reputation, DwarfTradeSigned[] blacklist) {
		List<DwarfTradeWeight> curList=new ArrayList(dwarfTradeSignWeights);
		while(!curList.isEmpty()) {
			DwarfTradeWeight trade=(DwarfTradeWeight)WeightedRandom.getRandomItem(new Random(), dwarfTradeSignWeights);
			if(trade.trade.isReputationSufficent(reputation)) {
				boolean blacklisted=false;
				for(DwarfTradeSigned blackTrade : blacklist) {
					if(trade.trade==blackTrade)
						blacklisted=true;
				}
				if(!blacklisted)
					return trade.trade;
			}
			curList.remove(trade);
		}
		return null;
	}
	
	private static class DwarfTradeWeight extends WeightedRandom.Item {

		DwarfTradeSigned trade;
		public DwarfTradeWeight(DwarfTradeSigned trade, int weight) {
			super(weight);
			this.trade=trade;
		}
	}

	private static void indexDwarfRecipes() {
		mapDwarfTrade("pumpkinmead",(DwarfTradeSigned)dwarfPumpkinToMeadTrade);
		mapDwarfTrade("melonmead",(DwarfTradeSigned)dwarfMelonToMeadTrade);
		mapDwarfTrade("dwarfchain",(DwarfTradeSigned)dwarfChainTrade);
		mapDwarfTrade("dwarfbarrier",(DwarfTradeSigned)dwarfBarrierTrade);
		mapDwarfTrade("stonehorse",(DwarfTradeSigned)dwarfStoneHorseTrade);
		mapDwarfTrade("manacrystal",(DwarfTradeSigned)dwarfManaCrystalTrade);
		mapDwarfTrade("masterboommoss",(DwarfTradeSigned)dwarfMasterBoomMossTrade);

		mapDwarfTrade("forgeiron0",(DwarfTradeSigned)dwarfForgeIronRecipes[0]);
		mapDwarfTrade("forgeiron1",(DwarfTradeSigned)dwarfForgeIronRecipes[1]);
		mapDwarfTrade("forgeiron2",(DwarfTradeSigned)dwarfForgeIronRecipes[2]);
		mapDwarfTrade("forgeiron3",(DwarfTradeSigned)dwarfForgeIronRecipes[3]);
		mapDwarfTrade("forgeiron4",(DwarfTradeSigned)dwarfForgeIronRecipes[4]);
		mapDwarfTrade("forgeiron5",(DwarfTradeSigned)dwarfForgeIronRecipes[5]);
		mapDwarfTrade("forgeiron6",(DwarfTradeSigned)dwarfForgeIronRecipes[6]);
		mapDwarfTrade("forgeiron7",(DwarfTradeSigned)dwarfForgeIronRecipes[7]);

		mapDwarfTrade("forgediamond0",(DwarfTradeSigned)dwarfForgeDiamondRecipes[0]);
		mapDwarfTrade("forgediamond1",(DwarfTradeSigned)dwarfForgeDiamondRecipes[1]);
		mapDwarfTrade("forgediamond2",(DwarfTradeSigned)dwarfForgeDiamondRecipes[2]);
		mapDwarfTrade("forgediamond3",(DwarfTradeSigned)dwarfForgeDiamondRecipes[3]);
		mapDwarfTrade("forgediamond4",(DwarfTradeSigned)dwarfForgeDiamondRecipes[4]);
		mapDwarfTrade("forgediamond5",(DwarfTradeSigned)dwarfForgeDiamondRecipes[5]);
		mapDwarfTrade("forgediamond6",(DwarfTradeSigned)dwarfForgeDiamondRecipes[6]);
		mapDwarfTrade("forgediamond7",(DwarfTradeSigned)dwarfForgeDiamondRecipes[7]);

		mapDwarfTrade("forgegold0",(DwarfTradeSigned)dwarfForgeGoldRecipes[0]);
		mapDwarfTrade("forgegold1",(DwarfTradeSigned)dwarfForgeGoldRecipes[1]);
		mapDwarfTrade("forgegold2",(DwarfTradeSigned)dwarfForgeGoldRecipes[2]);
		mapDwarfTrade("forgegold3",(DwarfTradeSigned)dwarfForgeGoldRecipes[3]);
		mapDwarfTrade("forgegold4",(DwarfTradeSigned)dwarfForgeGoldRecipes[4]);
		mapDwarfTrade("forgegold5",(DwarfTradeSigned)dwarfForgeGoldRecipes[5]);
		mapDwarfTrade("forgegold6",(DwarfTradeSigned)dwarfForgeGoldRecipes[6]);
		mapDwarfTrade("forgegold7",(DwarfTradeSigned)dwarfForgeGoldRecipes[7]);

		mapDwarfTrade("forgemanasteel0",(DwarfTradeSigned)dwarfForgeManasteelRecipes[0]);
		mapDwarfTrade("forgemanasteel1",(DwarfTradeSigned)dwarfForgeManasteelRecipes[1]);
		mapDwarfTrade("forgemanasteel2",(DwarfTradeSigned)dwarfForgeManasteelRecipes[2]);
		mapDwarfTrade("forgemanasteel3",(DwarfTradeSigned)dwarfForgeManasteelRecipes[3]);
		mapDwarfTrade("forgemanasteel4",(DwarfTradeSigned)dwarfForgeManasteelRecipes[4]);
		mapDwarfTrade("forgemanasteel5",(DwarfTradeSigned)dwarfForgeManasteelRecipes[5]);
		mapDwarfTrade("forgemanasteel6",(DwarfTradeSigned)dwarfForgeManasteelRecipes[6]);
		mapDwarfTrade("forgemanasteel7",(DwarfTradeSigned)dwarfForgeManasteelRecipes[7]);

		mapDwarfTrade("forgeterrasteel0",(DwarfTradeSigned)dwarfForgeTerrasteelRecipes[0]);
		mapDwarfTrade("forgeterrasteel1",(DwarfTradeSigned)dwarfForgeTerrasteelRecipes[1]);
		mapDwarfTrade("forgeterrasteel2",(DwarfTradeSigned)dwarfForgeTerrasteelRecipes[2]);
		mapDwarfTrade("forgeterrasteel3",(DwarfTradeSigned)dwarfForgeTerrasteelRecipes[3]);
		mapDwarfTrade("forgeterrasteel7",(DwarfTradeSigned)dwarfForgeTerrasteelRecipes[7]);

		mapDwarfTrade("forgeelementum0",(DwarfTradeSigned)dwarfForgeElementiumRecipes[0]);
		mapDwarfTrade("forgeelementum1",(DwarfTradeSigned)dwarfForgeElementiumRecipes[1]);
		mapDwarfTrade("forgeelementum2",(DwarfTradeSigned)dwarfForgeElementiumRecipes[2]);
		mapDwarfTrade("forgeelementum3",(DwarfTradeSigned)dwarfForgeElementiumRecipes[3]);
		mapDwarfTrade("forgeelementum4",(DwarfTradeSigned)dwarfForgeElementiumRecipes[4]);
		mapDwarfTrade("forgeelementum5",(DwarfTradeSigned)dwarfForgeElementiumRecipes[5]);
		mapDwarfTrade("forgeelementum6",(DwarfTradeSigned)dwarfForgeElementiumRecipes[6]);
		mapDwarfTrade("forgeelementum7",(DwarfTradeSigned)dwarfForgeElementiumRecipes[7]);

		mapDwarfTrade("reforge0",(DwarfTradeSigned)dwarfReforgeRecipes[0]);
		mapDwarfTrade("reforge1",(DwarfTradeSigned)dwarfReforgeRecipes[1]);
		mapDwarfTrade("reforge2",(DwarfTradeSigned)dwarfReforgeRecipes[2]);
		mapDwarfTrade("reforge3",(DwarfTradeSigned)dwarfReforgeRecipes[3]);
		mapDwarfTrade("reforge4",(DwarfTradeSigned)dwarfReforgeRecipes[4]);
		mapDwarfTrade("reforge5",(DwarfTradeSigned)dwarfReforgeRecipes[5]);
		mapDwarfTrade("reforge6",(DwarfTradeSigned)dwarfReforgeRecipes[6]);
		mapDwarfTrade("reforge7",(DwarfTradeSigned)dwarfReforgeRecipes[7]);
	}
	
	public static void mapDwarfTrade(String key, DwarfTradeSigned trade) {
		dwarfStringsToTradeSigns.put(key, trade);
		dwarfTradeSignsToStrings.put(trade, key);
	}
}
