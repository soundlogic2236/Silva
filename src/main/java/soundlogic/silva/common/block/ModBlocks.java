package soundlogic.silva.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.item.block.ItemBlockBoomMoss;
import soundlogic.silva.common.item.block.ItemBlockDwarvenSign;
import soundlogic.silva.common.item.block.ItemBlockMod;
import soundlogic.silva.common.item.block.ItemBlockModSlab;
import soundlogic.silva.common.item.block.ItemBlockPylon;
import soundlogic.silva.common.lib.LibBlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.tileentity.TileEntity;

public class ModBlocks {

	// Darkened Blocks
	public static Block darkenedStone;
	public static Block darkenedStoneBrick;
	public static Block darkenedWood;
	public static Block darkenedWoodPlank;
	public static Block darkenedPortalFrame;
	public static Block darkenedPortalFrameCore;
	public static Block darkenedDust;
	public static Block drainedSoulsand;
	public static Block darkenedQuartz;

	// Enhanced Portal
	public static Block portalCore;
	public static Block pixieDust;

	// Pylons
	public static Block dimensionalPylon;

	public static Block consumingWeed;
	public static Block dwarfWeed;
	public static Block boomMoss;

	// Bifrost
	public static Block bifrostBlock;
	public static Block bifrostBlockStairs;
	public static Block bifrostBlockWall;
	public static Block bifrostBlockSlab;
	public static Block bifrostBlockDoubleSlab;
	public static Block bifrostBlockSparkling;

	// Paradox Blocks
	public static Block paradoxStone;
	public static Block paradoxStoneBrick;
	public static Block paradoxWood;
	public static Block paradoxWoodPlank;
	public static Block manaEater;
	public static Block dwarvenSign;
	public static Block dwarfRock;
	public static Block manaCrystal;
	
	public static void preInit() {
		
		dimensionalPylon=new BlockPylon();
		GameRegistry.registerBlock(dimensionalPylon, ItemBlockPylon.class, LibBlockNames.PYLON);
		
		manaEater=new BlockManaEater();
		GameRegistry.registerBlock(manaEater, ItemBlockMod.class, LibBlockNames.MANA_EATER);
		
		boomMoss=new BlockBoomMoss();
		GameRegistry.registerBlock(boomMoss, ItemBlockBoomMoss.class, LibBlockNames.BOOM_MOSS);

		bifrostBlock=new BlockBifrost();
		GameRegistry.registerBlock(bifrostBlock, ItemBlockMod.class, LibBlockNames.BIFROST_BLOCK);

		bifrostBlockStairs=new BlockBifrostStairs();
		GameRegistry.registerBlock(bifrostBlockStairs, ItemBlockMod.class, LibBlockNames.BIFROST_BLOCK_STAIRS);
		
		bifrostBlockWall=new BlockBifrostWall();
		GameRegistry.registerBlock(bifrostBlockWall, ItemBlockMod.class, LibBlockNames.BIFROST_BLOCK_WALL);
		
		bifrostBlockSlab=new BlockBifrostSlab(false);
		bifrostBlockDoubleSlab=new BlockBifrostSlab(true);
		GameRegistry.registerBlock(bifrostBlockSlab, ItemBlockModSlab.class, LibBlockNames.BIFROST_BLOCK_SLAB);
		GameRegistry.registerBlock(bifrostBlockDoubleSlab, ItemBlockModSlab.class, LibBlockNames.BIFROST_BLOCK_DOUBLE_SLAB);
		
		bifrostBlockSparkling=new BlockBifrostSparkling();
		GameRegistry.registerBlock(bifrostBlockSparkling, ItemBlockMod.class, LibBlockNames.BIFROST_BLOCK_SPARKLING);
		
		portalCore=new BlockPortalCore();
		GameRegistry.registerBlock(portalCore, ItemBlockMod.class, LibBlockNames.PORTAL_CORE);
		
		pixieDust=new BlockPixieDust();
		GameRegistry.registerBlock(pixieDust, ItemBlockMod.class, LibBlockNames.PIXIE_DUST);

		darkenedDust=new BlockDarkenedDust();
		GameRegistry.registerBlock(darkenedDust, ItemBlockMod.class, LibBlockNames.DARKENED_DUST);
		
		dwarvenSign=new BlockDwarvenSign();
		GameRegistry.registerBlock(dwarvenSign, ItemBlockDwarvenSign.class, LibBlockNames.DWARVEN_SIGN);
		
		dwarfWeed=new BlockDwarfWeed();
		GameRegistry.registerBlock(dwarfWeed, ItemBlockMod.class, LibBlockNames.DWARF_WEED);
		
		manaCrystal=new BlockManaCrystal();
		GameRegistry.registerBlock(manaCrystal, ItemBlockMod.class, LibBlockNames.MANA_CRYSTAL);
		
		
		initTileEntities();
	}

	private static void initTileEntities() {

		registerTile(TilePylon.class, LibBlockNames.PYLON);
		registerTile(TileManaEater.class, LibBlockNames.MANA_EATER);
		registerTile(TileBoomMoss.class, LibBlockNames.BOOM_MOSS);
		registerTile(TilePortalCore.class, LibBlockNames.PORTAL_CORE);
		registerTile(TileDwarvenSign.class, LibBlockNames.DWARVEN_SIGN);
		registerTile(TileManaCrystal.class, LibBlockNames.MANA_CRYSTAL);
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntityWithAlternatives(clazz, LibResources.PREFIX_MOD + key, key);
	}

}
