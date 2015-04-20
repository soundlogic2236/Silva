package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.DwarfForgedHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.DwarfTrade;
import soundlogic.silva.common.crafting.recipe.DwarfTradeForging;
import soundlogic.silva.common.crafting.recipe.DwarfTradeReforging;
import soundlogic.silva.common.crafting.recipe.DwarfTradeReforging.ReforgeType;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSimple;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.PortalRecipeSimple;
import soundlogic.silva.common.item.ModItems;

public class ModPortalTradeRecipes {

	public static List<DwarfTrade> dwarfTradesIndex=new ArrayList<DwarfTrade>();
	private static List<DwarfTradeWeight> dwarfTradeWeights=new ArrayList<DwarfTradeWeight>();

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
	public static IPortalRecipe dwarfLivingRockRecipe;
	public static IPortalRecipe dwarfDwarfRockRecipe;
	public static IPortalRecipe dwarfdeepQuartzRecipe;
	public static IPortalRecipe dwarfPumpkinToMeadTrade;
	public static IPortalRecipe dwarfMelonToMeadTrade;
	public static IPortalRecipe dwarfChainTrade;
	public static IPortalRecipe dwarfBarrierTrade;
	public static IPortalRecipe dwarfStoneHorseTrade;
	public static IPortalRecipe dwarfManaCrystalTrade;
	public static IPortalRecipe dwarfMasterBoomMossTrade;

	public static IPortalRecipe[] dwarfIronTrades;
	public static IPortalRecipe[] dwarfDiamondTrades;
	public static IPortalRecipe[] dwarfGoldTrades;
	public static IPortalRecipe[] dwarfManasteelTrades;
	public static IPortalRecipe[] dwarfTerrasteelTrades;
	public static IPortalRecipe[] dwarfElementumTrades;
	
	
	public static void preInit() {
		dwarfSignRecipe=new PortalRecipeSimple(new ItemStack(ModBlocks.dwarvenSign),new ItemStack(Items.sign),new ItemStack(Blocks.iron_block),new ItemStack(ModItems.bifrostShard));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfSignRecipe);
		
		for(int i=0;i<16;i++) {
			dwarfMushroomRecipes[i]=new DwarfTradeSimple(new ItemStack(vazkii.botania.common.block.ModBlocks.mushroom,1,i),0,1,5,new ItemStack(vazkii.botania.common.block.ModBlocks.flower,1,i));
			PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfMushroomRecipes[i]);
		}
		
		dwarfWeedRecipe=new DwarfTradeSimple(new ItemStack(ModBlocks.dwarfWeed),0,1,5,new ItemStack(Blocks.tallgrass));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfWeedRecipe);
		
		dwarfLivingRockRecipe=new DwarfTradeSimple(new ItemStack(vazkii.botania.common.block.ModBlocks.livingrock),0,1,5,new ItemStack(vazkii.botania.common.block.ModBlocks.livingwood));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfLivingRockRecipe);
		
		dwarfDwarfRockRecipe=new DwarfTradeSimple(new ItemStack(ModBlocks.dwarfRock),0,1,5,new ItemStack(vazkii.botania.common.block.ModBlocks.livingrock));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfDwarfRockRecipe);
		
		dwarfdeepQuartzRecipe=new DwarfTradeSimple(new ItemStack(ModItems.deepQuartz),0,1,5,new ItemStack(Items.quartz));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfdeepQuartzRecipe);
		
		dwarfPumpkinToMeadTrade=new DwarfTradeSimple(new ItemStack(ModItems.dwarfMead),4,1,15,new ItemStack(Blocks.pumpkin));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfPumpkinToMeadTrade);
		
		dwarfMelonToMeadTrade=new DwarfTradeSimple(new ItemStack(ModItems.dwarfMead),4,1,15,new ItemStack(Blocks.melon_block));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfMelonToMeadTrade);
		
		dwarfChainTrade=new DwarfTradeSimple(new ItemStack(ModItems.dwarfChain),5,2,15,new ItemStack(Items.golden_apple));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfChainTrade);
		
		dwarfBarrierTrade=new DwarfTradeSimple(new ItemStack(ModItems.dwarfBarrier),4,1,12,new ItemStack(ModBlocks.dwarfRock),new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,1));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfBarrierTrade);
		
		dwarfStoneHorseTrade=new DwarfTradeSimple(new ItemStack(ModItems.stoneHorse),7,5,16,new ItemStack(ModBlocks.dwarfRock), new ItemStack(Items.saddle),new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,5));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfStoneHorseTrade);
		
		dwarfManaCrystalTrade=new DwarfTradeSimple(new ItemStack(ModBlocks.manaCrystal),12,4,22,new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,2),new ItemStack(vazkii.botania.common.item.ModItems.overgrowthSeed));
		PortalRecipes.addRecipe(Dimension.NIDAVELLIR, dwarfManaCrystalTrade);
		
		dwarfMasterBoomMossTrade=new DwarfTradeSimple(new ItemStack(ModBlocks.boomMoss,1,1),11,3,20,new ItemStack(ModBlocks.boomMoss),new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,5),new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,5));
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
		dwarfForgeManasteelRecipes=getForgeRecipes(3,3,4,12,new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,0),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelHelm),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelChest),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelLegs),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelBoots),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelPick),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelShovel),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelAxe),
				new ItemStack(vazkii.botania.common.item.ModItems.manasteelSword));
		dwarfForgeTerrasteelRecipes=getForgeRecipes(5,3,4,12,new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,4),
				new ItemStack(vazkii.botania.common.item.ModItems.terrasteelHelm),
				new ItemStack(vazkii.botania.common.item.ModItems.terrasteelChest),
				new ItemStack(vazkii.botania.common.item.ModItems.terrasteelLegs),
				new ItemStack(vazkii.botania.common.item.ModItems.terrasteelBoots),
				null,
				null,
				null,
				new ItemStack(vazkii.botania.common.item.ModItems.terraSword));
		dwarfForgeElementiumRecipes=getForgeRecipes(4,3,4,12,new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,7),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumHelm),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumChest),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumLegs),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumBoots),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumPick),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumShovel),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumAxe),
				new ItemStack(vazkii.botania.common.item.ModItems.elementiumSword));

		dwarfReforgeRecipes[0]=new DwarfTradeReforging(ReforgeType.HELMET,18,12,40);
		dwarfReforgeRecipes[1]=new DwarfTradeReforging(ReforgeType.CHESTPLATE,18,15,40);
		dwarfReforgeRecipes[2]=new DwarfTradeReforging(ReforgeType.LEGGINGS,18,13,40);
		dwarfReforgeRecipes[3]=new DwarfTradeReforging(ReforgeType.BOOTS,18,10,40);
		dwarfReforgeRecipes[4]=new DwarfTradeReforging(ReforgeType.PICKAXE,21,10,40);
		dwarfReforgeRecipes[5]=new DwarfTradeReforging(ReforgeType.SHOVEL,18,8,40);
		dwarfReforgeRecipes[6]=new DwarfTradeReforging(ReforgeType.AXE,18,6,40);
		dwarfReforgeRecipes[7]=new DwarfTradeReforging(ReforgeType.SWORD,21,9,40);
		
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
		if(helm!=null)
			recipes[0]=new DwarfTradeForging(helm,5*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),4,ingredient.getItemDamage()));
		if(chest!=null)
			recipes[1]=new DwarfTradeForging(chest,10*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),7,ingredient.getItemDamage()));
		if(legs!=null)
			recipes[2]=new DwarfTradeForging(legs,8*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),6,ingredient.getItemDamage()));
		if(boots!=null)
			recipes[3]=new DwarfTradeForging(boots,4*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),3,ingredient.getItemDamage()));
		if(pick!=null)
			recipes[4]=new DwarfTradeForging(pick,5*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()));
		if(shovel!=null)
			recipes[5]=new DwarfTradeForging(shovel,2*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()));
		if(axe!=null)
			recipes[6]=new DwarfTradeForging(axe,4*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),2,ingredient.getItemDamage()));
		if(sword!=null)
			recipes[7]=new DwarfTradeForging(sword,3*cost,minRep,repBoost,maxRep,new ItemStack(ingredient.getItem(),1,ingredient.getItemDamage()));
		return recipes;
	}

	private static void weighDwarfRecipes() {
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[0],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[1],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[2],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[3],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[4],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[5],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[6],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[7],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[8],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[9],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[10],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[11],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[12],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[13],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[14],200));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[15],200));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfWeedRecipe,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfLivingRockRecipe,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfDwarfRockRecipe,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfdeepQuartzRecipe,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfPumpkinToMeadTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMelonToMeadTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfChainTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfBarrierTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfStoneHorseTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfManaCrystalTrade,1600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMasterBoomMossTrade,1600));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[4],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[5],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[6],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeIronRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[4],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[5],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[6],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeDiamondRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[4],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[5],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[6],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeGoldRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[4],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[5],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[6],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeManasteelRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeTerrasteelRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeTerrasteelRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeTerrasteelRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeTerrasteelRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeTerrasteelRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[0],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[1],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[2],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[3],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[4],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[5],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[6],100));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfForgeElementiumRecipes[7],100));

		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[0],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[1],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[2],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[3],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[4],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[5],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[6],600));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfReforgeRecipes[7],600));
	}
	
	public static DwarfTrade getRandomWeighedTrade(int reputation, DwarfTrade[] blacklist) {
		List<DwarfTradeWeight> curList=new ArrayList(dwarfTradeWeights);
		while(!curList.isEmpty()) {
			DwarfTradeWeight trade=(DwarfTradeWeight)WeightedRandom.getRandomItem(new Random(), dwarfTradeWeights);
			if(trade.trade.isReputationSufficent(reputation)) {
				boolean blacklisted=false;
				for(DwarfTrade blackTrade : blacklist) {
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

		DwarfTrade trade;
		public DwarfTradeWeight(DwarfTrade trade, int weight) {
			super(weight);
			this.trade=trade;
		}
	}
	
	public static IPortalRecipe getSampleReforgeRecipe(ReforgeType type) {
		return null;
	}


	private static void indexDwarfRecipes() {
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[7]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[8]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[9]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[10]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[11]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[12]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[13]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[14]);
		dwarfTradesIndex.add((DwarfTrade)dwarfMushroomRecipes[15]);
		
		dwarfTradesIndex.add((DwarfTrade)dwarfWeedRecipe);
		dwarfTradesIndex.add((DwarfTrade)dwarfLivingRockRecipe);
		dwarfTradesIndex.add((DwarfTrade)dwarfDwarfRockRecipe);
		dwarfTradesIndex.add((DwarfTrade)dwarfdeepQuartzRecipe);
		dwarfTradesIndex.add((DwarfTrade)dwarfPumpkinToMeadTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfMelonToMeadTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfChainTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfBarrierTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfStoneHorseTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfManaCrystalTrade);
		dwarfTradesIndex.add((DwarfTrade)dwarfMasterBoomMossTrade);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeIronRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeDiamondRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeGoldRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeManasteelRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeTerrasteelRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeTerrasteelRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeTerrasteelRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeTerrasteelRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeTerrasteelRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfForgeElementiumRecipes[7]);

		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[0]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[1]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[2]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[3]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[4]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[5]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[6]);
		dwarfTradesIndex.add((DwarfTrade)dwarfReforgeRecipes[7]);
	}
}
