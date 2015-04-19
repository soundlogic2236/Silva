package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.DwarfTrade;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSimple;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.PortalRecipeSimple;
import soundlogic.silva.common.item.ModItems;

public class ModPortalTradeRecipes {

	public static List<DwarfTrade> dwarfTradesIndex=new ArrayList<DwarfTrade>();
	private static List<DwarfTradeWeight> dwarfTradeWeights=new ArrayList<DwarfTradeWeight>();

	public static IPortalRecipe dwarfSignRecipe;
	public static IPortalRecipe[] dwarfMushroomRecipes=new IPortalRecipe[16];
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
	
		indexDwarfRecipes();
		weighDwarfRecipes();
	}


	private static void weighDwarfRecipes() {
		int divisor=200;
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[0],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[1],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[2],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[3],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[4],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[5],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[6],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[7],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[8],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[9],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[10],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[11],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[12],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[13],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[14],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMushroomRecipes[15],200/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfWeedRecipe,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfLivingRockRecipe,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfDwarfRockRecipe,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfdeepQuartzRecipe,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfPumpkinToMeadTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMelonToMeadTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfChainTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfBarrierTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfStoneHorseTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfManaCrystalTrade,1600/divisor));
		dwarfTradeWeights.add(new DwarfTradeWeight((DwarfTrade)dwarfMasterBoomMossTrade,1600/divisor));
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
	}
}
