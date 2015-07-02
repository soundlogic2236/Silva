package soundlogic.silva.common.block.tile.multiblocks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockPylon;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import soundlogic.silva.core.FakeWorld;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiblockDataMysticalGrinder extends MultiblockDataBase {

	private static final String TAG_GRINDER_TICKS = "_mysticalGrinderTicks";

	private static final int MAX_MANA = getManaCostPerTick(14,14);
	
	private static HashMap<ItemStack, DropCacheResult> dropCache = new HashMap<ItemStack, DropCacheResult>();
	private static class DropCacheResult {
		boolean override = false;
		boolean isTested = false;
		boolean isBlock = false;
		int totalTests = 0;
		int sameTests = 0;
		int oneTests = 0;
		int changeTests = 0;
		int noDropTests = 0;
		int multipleTests = 0;
		int changedByFortuneTests = 0;
		int crashes = 0;
		int maxTestResult = 0;
		boolean finalized = false;
		boolean shouldTest = false;
		private void incrementSame() {
			this.totalTests++;
			this.sameTests++;
			if(this.sameTests>maxTestResult)
				this.maxTestResult=sameTests;
		}
		private void incrementOne() {
			this.totalTests++;
			this.oneTests++;
			this.maxTestResult=Math.max(this.maxTestResult, this.oneTests);
		}
		private void incrementChange() {
			this.totalTests++;
			this.changeTests++;
			this.maxTestResult=Math.max(this.maxTestResult, this.changeTests);
		}
		private void incrementNone() {
			this.totalTests++;
			this.noDropTests++;
			this.maxTestResult=Math.max(this.maxTestResult, this.noDropTests);
		}
		private void incrementMultiple() {
			this.totalTests++;
			this.multipleTests++;
			this.maxTestResult=Math.max(this.maxTestResult, this.multipleTests);
		}
		private void incrementCrashes() {
			this.totalTests++;
			this.crashes++;
		}
		public boolean shouldRunTests() {
			if(maxTestResult>=50)
				return false;
			if(crashes>=(totalTests/2+2)) {
				maxTestResult=50;
				return false;
			}
			if(maxTestResult>=(totalTests/2+2)) {
				maxTestResult=50;
				return false;
			}
			return true;
		}
		public boolean shouldProcess() {
			if(finalized)
				return shouldTest;
			shouldTest=statsForProcessing();
			return shouldTest;
		}
		private boolean statsForProcessing() {
			if(override)
				return true;
			if(multipleTests>10*totalTests/50)
				return true;
			if(changeTests>20*totalTests/50)
				return true;
			return false;
		}
	}
	private static HashMap<ItemStack, ItemStack> lookupCache = new HashMap<ItemStack, ItemStack>();

	public static HashMap<ItemStack, HashMap<List<ItemStack>,Integer>> extraGrindResults = new HashMap<ItemStack, HashMap<List<ItemStack>,Integer>>();
	public static HashMap<ItemStack, Float> extraGrindResultsHardness = new HashMap<ItemStack, Float>();

	private IIcon iconBase;

	private IIcon iconBaseInert;
	
	public MultiblockDataMysticalGrinder() {
		super(new BlockData(Blocks.sticky_piston, 0));
		BlockData core = new BlockData(Blocks.sticky_piston, 0);
		BlockData dirt = new BlockData(Blocks.dirt, 1);
		BlockData crystal = new BlockData(ModBlocks.manaCrystal, 0);
		BlockData weed = new BlockData(ModBlocks.dwarfWeed, 0);
		BlockData pylon = new BlockData(ModBlocks.dimensionalPylon, BlockPylon.getMetadataForDimension(Dimension.NIDAVELLIR));
		BlockData brick = new BlockData(ModBlocks.dwarfRock,1);

		creationRequirementsTemplate = new BlockData[][][] {
				{{BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, dirt, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, crystal, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, core, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, crystal, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD}},

				{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}},
		};
		
		templateOrigin = new int[] {3,3,2};

		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD},
				 {BlockData.WILDCARD, brick, brick, brick, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, dirt, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD},
				 {BlockData.WILDCARD, dirt, BlockData.WILDCARD, dirt, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, crystal, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.MULTIBLOCK_CORE, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, crystal, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD},
				 {BlockData.WILDCARD, weed, BlockData.WILDCARD, weed, BlockData.WILDCARD}},

				{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}},
		};
	}

	private static int getManaCostPerTick(int activeStacks, int activeWeeds) {
		return activeStacks*1+activeWeeds*2;
	}

	@Override
	public String getName() {
		return "mysticalGrinder";
	}

	@Override
	public IMultiblockTileData createTileData() {
		return new MysticalGrinderTileData();
	}

	@Override
	public void onTick(TileMultiblockCore core) {
		MysticalGrinderTileData data = (MysticalGrinderTileData) core.getTileData();
		takeMana(core, data);
		int manaCost = getManaCostPerTick(data.getActiveStackCount(), data.getActiveWeedCount());
		boolean prevActive = data.active;
		if(data.currentMana>=manaCost) {
			data.active=true;
			data.currentMana-=manaCost;
			List<EntityItem> items = core.getWorldObj().getEntitiesWithinAABB(EntityItem.class, getSearchBoundingBox(core));
			AxisAlignedBB direct = getDirectBoundingBox(core);
			for(EntityItem item : items) {
				boolean colliding = item.boundingBox.intersectsWith(direct);
				processEntity(core, item, data, colliding);
			}
			data.tick();
			for(int i = 0 ; i < data.toDropStacksCount ; i ++) {
				ItemStack stack = data.toDropStacks[i];
				List<ItemStack> stacks = getDrops(stack, 6);
				if(stacks.isEmpty()) {
					int j = 0;
					while(j<10 && stacks.isEmpty()) {
						stacks=getDrops(stack,6);
						j++;
					}
				}
				for(ItemStack drop : stacks) {
					EntityItem item = new EntityItem(core.getWorldObj(), core.xCoord + 0.5, core.yCoord - 1.5, core.zCoord + 0.5, drop);
					item.getEntityData().setInteger(TAG_GRINDER_TICKS, -1);
					core.getWorldObj().spawnEntityInWorld(item);
				}
				data.clearDrops();
			}
		}
		else
			data.active=false;
		if(data.active!=prevActive)
			core.markForVisualUpdate();
		if(data.dirty)
			core.markDirty();
		if(data.visualDirty)
			core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
		data.dirty=false;
		data.visualDirty=false;
	}
	
	private void takeMana(TileMultiblockCore core, MysticalGrinderTileData data) {
		int missingMana = MAX_MANA - data.currentMana;
		takeMana(core, data, missingMana/2, ForgeDirection.NORTH);
		takeMana(core, data, missingMana/2, ForgeDirection.SOUTH);
		takeMana(core, data, missingMana/2, ForgeDirection.EAST);
		takeMana(core, data, missingMana/2, ForgeDirection.WEST);
	}
	
	private void takeMana(TileMultiblockCore core, MysticalGrinderTileData data, int amount, ForgeDirection direction) {
		TileEntity tile = core.getWorldObj().getTileEntity(core.xCoord+direction.offsetX, core.yCoord+direction.offsetY, core.zCoord+direction.offsetZ);
		if(tile instanceof TileManaCrystal) {
			IManaPool crystal = (TileManaCrystal) tile;
			int manaToTake=Math.min(crystal.getCurrentMana(),amount);
			crystal.recieveMana(-manaToTake);
			data.currentMana+=manaToTake;
		}
	}

	private void processEntity(TileMultiblockCore core, EntityItem entity, MysticalGrinderTileData data, boolean colliding) {
		ItemStack stack = entity.getEntityItem();
		DropCacheResult cache = getCacheForStack(stack);
		if(colliding) {
			int ticks = entity.getEntityData().getInteger(TAG_GRINDER_TICKS);
			if(ticks>50) {
				if(cache.maxTestResult<50)
					testStack(cache, stack, 100-cache.maxTestResult, true);
				if(!cache.shouldProcess())
					return;
				ItemStack tryAdd = stack.copy();
				tryAdd.stackSize=1;
				float hardness = 1;
				if(cache.override) {
					for(ItemStack stack2 : extraGrindResults.keySet()) {
						if(stack.isItemEqual(stack2)) {
							hardness=extraGrindResultsHardness.get(stack2);
							break;
						}
					}
				}
				else {
					ItemBlock itemBlock = (ItemBlock) stack.getItem(); 
					Block block = itemBlock.field_150939_a;
					int metadata = itemBlock.getMetadata(stack.getItemDamage());
					try {
					hardness = block.getBlockHardness(FakeWorld.INSTANCE, 0, 0, 0);
					}
					catch(Exception e) {}
				}
				boolean adding=true;
				while(adding) {
					if(data.addStack(tryAdd, hardness)) {
						ItemStack originalStack = entity.getEntityItem().copy();
						originalStack.stackSize--;
						if(originalStack.stackSize>0)
							entity.setEntityItemStack(originalStack);
						else {
							entity.setDead();
							adding=false;
						}
					}
					else
						adding=false;
				}
			}
		}
		else {
			testStack(cache, stack, 10, false);
			NBTTagCompound cmp = entity.getEntityData();
			int ticks = cmp.getInteger(TAG_GRINDER_TICKS);
			if(ticks!=-1 && ticks<=50) {
				Vector3 pos = new Vector3(entity.posX, entity.posY, entity.posZ);
				Vector3 prevPos = new Vector3(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
				Vector3 target = new Vector3(core.xCoord + .5, core.yCoord - 1.5, core.zCoord + .5);
				if(pos.subtract(target).magSquared()<prevPos.subtract(target).magSquared()) {
					cmp.setInteger(TAG_GRINDER_TICKS, ticks+1);
				}
			}
		}
	}
	
	private static void testStack(DropCacheResult cache, ItemStack stack, int tests, boolean checkEveryTest) {
		if(!cache.shouldRunTests())
			return;
		if(!cache.isTested) {
			for(ItemStack stack2 : extraGrindResults.keySet()) {
				if(stack.isItemEqual(stack2)) {
					cache.override=true;
					return;
				}
			}
			cache.isBlock=isStackBlock(stack);
			cache.isTested=true;
		}
		if(cache.override)
			return;
		if(!cache.isBlock)
			return;
		ItemBlock itemBlock = (ItemBlock) stack.getItem(); 
		Block block = itemBlock.field_150939_a;
		int metadata = itemBlock.getMetadata(stack.getItemDamage());
		int test = 0;
		while (test<tests) {
			test++;
			if(checkEveryTest && !cache.shouldRunTests())
				return;
			testStack(cache, stack, block, metadata);
		}
	}
	private static void testStack(DropCacheResult cache, ItemStack stack, Block block, int metadata) {
		List<ItemStack> drops = getDrops(stack, 100, true);
		if(drops==null) {
			cache.incrementCrashes();
			return;
		}
		if(drops.size()==0) {
			cache.incrementNone();
			return;
		}
		if(drops.size()==1) {
			ItemStack drop = drops.get(0);
			if(drop.isItemEqual(stack)) {
				cache.incrementSame();
				return;
			}
			if(shouldTestForFortuneChange(cache))
				testStackForFortuneChange(cache, stack, block, metadata);
			else
				increaseOneDropResult(cache);
			return;
		}
		cache.incrementMultiple();
	}
	private static boolean shouldTestForFortuneChange(DropCacheResult cache) {
		return cache.changedByFortuneTests<11 || Math.abs(cache.oneTests-cache.changeTests)<4;
	}

	private static void increaseOneDropResult(DropCacheResult cache) {
		if(cache.oneTests>cache.changeTests)
			cache.incrementOne();
		else
			cache.incrementChange();
	}

	private static void testStackForFortuneChange(DropCacheResult cache, ItemStack stack, Block block, int metadata) {
		List<ItemStack> drops0 = new ArrayList<ItemStack>();
		List<ItemStack> drops1 = new ArrayList<ItemStack>();
		List<ItemStack> drops3 = new ArrayList<ItemStack>();
		List<ItemStack> drops6 = new ArrayList<ItemStack>();
		List<ItemStack> drops100 = new ArrayList<ItemStack>();
		for(int i = 0 ; i < 10 ; i ++) {
			drops0.addAll(getDrops(block, metadata, 0));
			drops1.addAll(getDrops(block, metadata, 1));
			drops3.addAll(getDrops(block, metadata, 3));
			drops6.addAll(getDrops(block, metadata, 6));
			drops100.addAll(getDrops(block, metadata, 100));
		}
		List<ItemStack> dropsCombined = new ArrayList<ItemStack>();
		dropsCombined.addAll(drops0);
		dropsCombined.addAll(drops1);
		dropsCombined.addAll(drops3);
		dropsCombined.addAll(drops6);
		dropsCombined.addAll(drops100);
		List<ItemStack> dropsAll = new ArrayList<ItemStack>();
		for(ItemStack dropStack : dropsCombined) {
			boolean in = false;
			for(ItemStack stack2 : dropsAll) {
				if(checkStacksEqual(dropStack, stack2)) {
					in=true;
					break;
				}
			}
			if(!in) {
				dropsAll.add(dropStack);
			}
		}
		float[] drops0d = getDistribution(drops0, dropsAll);
		float[] drops1d = getDistribution(drops1, dropsAll);
		float[] drops3d = getDistribution(drops3, dropsAll);
		float[] drops6d = getDistribution(drops6, dropsAll);
		float[] drops100d = getDistribution(drops100, dropsAll);
		float totalDiff=0;
		for(int i = 0 ; i < drops0d.length-1 ; i++) {
			totalDiff+=Math.abs(drops0d[i]-drops1d[i]);
			totalDiff+=Math.abs(drops0d[i]-drops3d[i]);
			totalDiff+=Math.abs(drops0d[i]-drops6d[i]);
			totalDiff+=Math.abs(drops0d[i]-drops100d[i]);
			totalDiff+=Math.abs(drops1d[i]-drops3d[i]);
			totalDiff+=Math.abs(drops1d[i]-drops6d[i]);
			totalDiff+=Math.abs(drops1d[i]-drops100d[i]);
			totalDiff+=Math.abs(drops3d[i]-drops6d[i]);
			totalDiff+=Math.abs(drops3d[i]-drops100d[i]);
			totalDiff+=Math.abs(drops6d[i]-drops100d[i]);
		}
		float averageDiff = totalDiff/10F*drops0d.length;
		if(averageDiff>.5F)
			cache.incrementChange();
		else
			cache.incrementOne();
	}
	private static float[] getDistribution(List<ItemStack> val, List<ItemStack> ids) {
		float[] result = new float[ids.size()];
		float total = 0;
		for(ItemStack dropStack : val) {
			for(int i = 0 ; i < ids.size() ; i ++) {
				ItemStack stack = ids.get(i);
				if(checkStacksEqual(dropStack, stack)) {
					total+=stack.stackSize;
					result[i]+=stack.stackSize;
				}
			}
		}
		for(int i = 0 ; i < result.length ; i ++) {
			result[i]/=total;
		}
		return result;
	}
	
	private static DropCacheResult getCacheForStack(ItemStack stack) {
		ItemStack lookup = lookupCache.get(stack);
		if(lookup!=null)
			return dropCache.get(lookup);
		for(ItemStack stack2 : dropCache.keySet()) {
			if(checkStacksEqual(stack, stack2)) {
				lookupCache.put(stack, stack2);
				return dropCache.get(stack2);
			}
		}
		DropCacheResult result = new DropCacheResult();
		lookupCache.put(stack, stack);
		dropCache.put(stack, result);
		return result;
	}
	
	private static boolean checkStacksEqual(ItemStack stack1, ItemStack stack2) {
		if(!stack1.isItemEqual(stack2))
			return false;
		if(stack1.hasTagCompound())
			return ItemStack.areItemStackTagsEqual(stack1, stack2);
		return true;
	}
	
	private static boolean isStackBlock(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock;
	}

	private static List<ItemStack> getDrops(ItemStack stack, int fortune) {
		return getDrops(stack, fortune, false);
	}

	private static List<ItemStack> getDrops(ItemStack stack, int fortune, boolean nullOnError) {
		if(!isStackBlock(stack))
			return new ArrayList<ItemStack>();
		try {
		ItemBlock itemBlock = (ItemBlock) stack.getItem(); 
		Block block = itemBlock.field_150939_a;
		int metadata = itemBlock.getMetadata(stack.getItemDamage());
		return getDrops(block, metadata, fortune, nullOnError);
		}
		catch(Exception e) {}
		return nullOnError ? null : new ArrayList<ItemStack>();
	}
	
	private static List<ItemStack> getDrops(Block block, int metadata, int fortune) {
		return getDrops(block, metadata, fortune, false);
	}
	
	private static List<ItemStack> getDrops(Block block, int metadata, int fortune, boolean nullOnError) {
		try {
		return block.getDrops(FakeWorld.INSTANCE, 0, 0, 0, metadata, fortune);
		}
		catch(Exception e) {}
		return nullOnError ? null : new ArrayList<ItemStack>();
	}
	
	protected AxisAlignedBB getSearchBoundingBox(TileMultiblockCore core) {
		if(isXAxis(core))
			return AxisAlignedBB.getBoundingBox(core.xCoord-3, core.yCoord-2, core.zCoord, core.xCoord+4, core.yCoord-1, core.zCoord+1);
		else
			return AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord-2, core.zCoord-3, core.xCoord+1, core.yCoord-1, core.zCoord+4);
	}
	
	protected AxisAlignedBB getDirectBoundingBox(TileMultiblockCore core) {
		return AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord-2, core.zCoord, core.xCoord+1, core.yCoord-1, core.zCoord+1);
	}
	
	protected boolean isXAxis(TileMultiblockCore core) {
		return core.rotation==0 || core.rotation == 2;
	}

	public int[] getOffsetForWeed(TileMultiblockCore core, int weed) {
		int i = (weed%7)-3;
		int j = 0;
		int k = weed>=7 ? -1 : 1;

		if(isXAxis(core)) {
			return new int[]{i,j,k};
		}
		else {
			return new int[]{k,j,i};
		}
	}
	
	
	@Override
	public void onCollision(TileMultiblockBase tile, TileMultiblockCore core,
			Entity ent) {
		// NO OP
	}

	@Override
	public void init(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile,
			int x, int y, int z) {
		MysticalGrinderTileData data = (MysticalGrinderTileData) core.getTileData();
		if(data.active)
			tile.iconsForSides=new IIcon[] {iconBase, iconBase, iconBase, iconBase, iconBase, iconBase};
		else
			tile.iconsForSides=new IIcon[] {iconBaseInert, iconBaseInert, iconBaseInert, iconBaseInert, iconBaseInert, iconBaseInert};
	}

	@Override
	public void setPhysicalData(TileMultiblockCore core,
			TileMultiblockBase tile, int x, int y, int z) {
		// NO OP
	}

	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core,
			Minecraft mc, ScaledResolution res) {
		// NO OP
	}

	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core,
			EntityPlayer player, ItemStack stack) {
		// NO OP
	}

	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.MYSTICAL_GRINDER;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBase = par1IconRegister.registerIcon(LibResources.GRINDER_BASE);
		iconBaseInert = par1IconRegister.registerIcon(LibResources.GRINDER_BASE+"Inert");
	}

	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX,
			boolean mirrorZ, int rot) {
		return !mirrorX && !mirrorZ;
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.mysticalGrinder;
	}

	@Override
	public void onClientTick(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void onInvalidate(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void onBreak(TileMultiblockCore core) {
		MysticalGrinderTileData data = (MysticalGrinderTileData) core.getTileData();
		for(ItemStack drop : data.stacks) {
			if(drop==null)
				continue;
			EntityItem item = new EntityItem(core.getWorldObj(), core.xCoord + 0.5, core.yCoord - 1.5, core.zCoord + 0.5, drop);
			item.getEntityData().setInteger(TAG_GRINDER_TICKS, -1);
			core.getWorldObj().spawnEntityInWorld(item);
		}
	}
}