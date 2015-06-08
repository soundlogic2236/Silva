package soundlogic.silva.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.subtile.BasicSignature;
import soundlogic.silva.common.block.subtile.functional.SubTileBlazeBloom;
import soundlogic.silva.common.block.subtile.functional.SubTileGlitterelle;
import soundlogic.silva.common.block.subtile.functional.SubTileRhododender;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TileEnchantPlate;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import soundlogic.silva.common.block.tile.TilePortalUpgradeInhibit;
import soundlogic.silva.common.block.tile.TilePortalUpgradeRedstone;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockProxy;
import soundlogic.silva.common.core.handler.DustHandler;
import soundlogic.silva.common.item.block.ItemBlockBoomMoss;
import soundlogic.silva.common.item.block.ItemBlockColoredDust;
import soundlogic.silva.common.item.block.ItemBlockDwarvenSign;
import soundlogic.silva.common.item.block.ItemBlockManaCrystal;
import soundlogic.silva.common.item.block.ItemBlockMod;
import soundlogic.silva.common.item.block.ItemBlockModMultiple;
import soundlogic.silva.common.item.block.ItemBlockModSlab;
import soundlogic.silva.common.item.block.ItemBlockPylon;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.tileentity.TileEntity;

public class ModBlocks {

	// Darkened Blocks
	public static Block darkenedStone;
	public static Block darkenedWood;
	public static Block darkenedDust;
	public static Block drainedSoulsand;

	// Enhanced Portal
	public static Block portalCore;
	public static Block portalUpgradeRedstone;
	public static Block portalUpgradeInhibit;
	public static Block portalUpgradeCharge;
	public static Block pixieDust;
	public static Block glowstoneDust;
	public static Block blazeDust;

	// Pylons
	public static Block dimensionalPylon;
	
	// Multiblocks
	public static Block multiblockProxy;
	public static Block multiblockCore;
	public static Block multiblockProxyLava;
	public static Block multiblockProxyWater;
	public static Block multiblockProxyNoRender;
	public static Block multiblockProxyNoRenderWater;

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

	public static Block manaEater;
	public static Block dwarvenSign;
	public static Block dwarfRock;
	public static Block manaCrystal;
	public static Block dwarvenManaPool;
	public static Block enchantPlate;
	public static Block blazeFire;
	public static Block coloredDust;
	public static Block darkElfTrap;
	
	public static void preInit() {
		
		dimensionalPylon=new BlockPylon();
		GameRegistry.registerBlock(dimensionalPylon, ItemBlockPylon.class, LibBlockNames.PYLON);
		
		manaEater=new BlockManaEater();
		GameRegistry.registerBlock(manaEater, ItemBlockModMultiple.class, LibBlockNames.MANA_EATER);
		
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
		DustHandler.registerBlock((IDustBlock) pixieDust);

		glowstoneDust=new BlockGlowstoneDust();
		GameRegistry.registerBlock(glowstoneDust, ItemBlockMod.class, LibBlockNames.GLOWSTONE_DUST);
		DustHandler.registerBlock((IDustBlock) glowstoneDust);

		blazeDust=new BlockBlazeDust();
		GameRegistry.registerBlock(blazeDust, ItemBlockMod.class, LibBlockNames.BLAZE_DUST);
		DustHandler.registerBlock((IDustBlock) blazeDust);

		darkenedDust=new BlockDarkenedDust();
		GameRegistry.registerBlock(darkenedDust, ItemBlockMod.class, LibBlockNames.DARKENED_DUST);
		
		dwarvenSign=new BlockDwarvenSign();
		GameRegistry.registerBlock(dwarvenSign, ItemBlockDwarvenSign.class, LibBlockNames.DWARVEN_SIGN);
		
		dwarfWeed=new BlockDwarfWeed();
		GameRegistry.registerBlock(dwarfWeed, ItemBlockMod.class, LibBlockNames.DWARF_WEED);
		
		manaCrystal=new BlockManaCrystal();
		GameRegistry.registerBlock(manaCrystal, ItemBlockManaCrystal.class, LibBlockNames.MANA_CRYSTAL);
		
		dwarfRock=new BlockSimpleMultiple(Material.rock,2).setEntry(LexiconData.basicDwarvenResources).setBlockName(LibBlockNames.DWARF_ROCK);
		GameRegistry.registerBlock(dwarfRock, ItemBlockModMultiple.class, LibBlockNames.DWARF_ROCK);
		
		dwarvenManaPool=new BlockDwarvenPool().setBlockName(LibBlockNames.DWARVEN_POOL);
		GameRegistry.registerBlock(dwarvenManaPool, ItemBlockMod.class, LibBlockNames.DWARVEN_POOL);
		
		portalUpgradeRedstone=new BlockPortalUpgradeRedstone().setBlockName(LibBlockNames.PORTAL_UPGRADE_REDSTONE);
		GameRegistry.registerBlock(portalUpgradeRedstone, ItemBlockMod.class, LibBlockNames.PORTAL_UPGRADE_REDSTONE);
		
		portalUpgradeInhibit=new BlockPortalUpgradeInhibit().setBlockName(LibBlockNames.PORTAL_UPGRADE_INHIBIT);
		GameRegistry.registerBlock(portalUpgradeInhibit, ItemBlockMod.class, LibBlockNames.PORTAL_UPGRADE_INHIBIT);
		
		portalUpgradeCharge=new BlockPortalUpgradeCharge().setBlockName(LibBlockNames.PORTAL_UPGRADE_CHARGE);
		GameRegistry.registerBlock(portalUpgradeCharge, ItemBlockMod.class, LibBlockNames.PORTAL_UPGRADE_CHARGE);
		
		multiblockProxy=new BlockMultiblockProxy(Material.glass).setBlockName(LibBlockNames.MULTIBLOCK_PROXY);
		GameRegistry.registerBlock(multiblockProxy, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_PROXY);
		
		multiblockCore=new BlockMultiblockCore(Material.glass).setBlockName(LibBlockNames.MULTIBLOCK_CORE);
		GameRegistry.registerBlock(multiblockCore, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_CORE);

		multiblockProxyLava=new BlockMultiblockProxyLava().setBlockName(LibBlockNames.MULTIBLOCK_PROXY_LAVA);
		GameRegistry.registerBlock(multiblockProxyLava, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_PROXY_LAVA);
		
		multiblockProxyWater=new BlockMultiblockProxyWater().setBlockName(LibBlockNames.MULTIBLOCK_PROXY_WATER);
		GameRegistry.registerBlock(multiblockProxyWater, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_PROXY_WATER);
		
		multiblockProxyNoRender=new BlockMultiblockProxyNoRender(Material.glass).setBlockName(LibBlockNames.MULTIBLOCK_PROXY_NO_RENDER);
		GameRegistry.registerBlock(multiblockProxyNoRender, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_PROXY_NO_RENDER);
		
		multiblockProxyNoRenderWater=new BlockMultiblockProxyNoRenderWater().setBlockName(LibBlockNames.MULTIBLOCK_PROXY_NO_RENDER_WATER);
		GameRegistry.registerBlock(multiblockProxyNoRenderWater, ItemBlockMod.class, LibBlockNames.MULTIBLOCK_PROXY_NO_RENDER_WATER);
		
		darkenedStone=new BlockContraryMultiple(Material.rock,5).setEntry(LexiconData.darkElfResources).setBlockName(LibBlockNames.DARKENED_STONE);
		GameRegistry.registerBlock(darkenedStone, ItemBlockModMultiple.class, LibBlockNames.DARKENED_STONE);
		
		darkenedWood=new BlockContraryMultiple(Material.wood,5).setEntry(LexiconData.darkElfResources).setBlockName(LibBlockNames.DARKENED_WOOD);
		GameRegistry.registerBlock(darkenedWood, ItemBlockModMultiple.class, LibBlockNames.DARKENED_WOOD);
		
		paradoxStone=new BlockSimpleMultiple(Material.rock,5).setEntry(LexiconData.darkElfResources).setBlockName(LibBlockNames.PARADOX_STONE);
		GameRegistry.registerBlock(paradoxStone, ItemBlockModMultiple.class, LibBlockNames.PARADOX_STONE);
		
		paradoxWood=new BlockSimpleMultiple(Material.wood,5).setEntry(LexiconData.darkElfResources).setBlockName(LibBlockNames.PARADOX_WOOD);
		GameRegistry.registerBlock(paradoxWood, ItemBlockModMultiple.class, LibBlockNames.PARADOX_WOOD);
		
		enchantPlate=new BlockEnchantPlate();
		GameRegistry.registerBlock(enchantPlate, ItemBlockMod.class, LibBlockNames.ENCHANT_PLATE);
		
		blazeFire=new BlockBlazeFire().setBlockName(LibBlockNames.BLAZE_FIRE);
		GameRegistry.registerBlock(blazeFire, LibBlockNames.BLAZE_FIRE);
		
		coloredDust=new BlockColoredDust().setBlockName(LibBlockNames.COLORED_DUST);
		GameRegistry.registerBlock(coloredDust, ItemBlockColoredDust.class, LibBlockNames.COLORED_DUST);
		
		darkElfTrap=new BlockDarkElfTrap().setBlockName(LibBlockNames.DARK_ELF_TRAP);
		GameRegistry.registerBlock(darkElfTrap, ItemBlockMod.class, LibBlockNames.DARK_ELF_TRAP);
		
		initTileEntities();
	}

	private static void initTileEntities() {

		registerTile(TilePylon.class, LibBlockNames.PYLON);
		registerTile(TileManaEater.class, LibBlockNames.MANA_EATER);
		registerTile(TileBoomMoss.class, LibBlockNames.BOOM_MOSS);
		registerTile(TilePortalCore.class, LibBlockNames.PORTAL_CORE);
		registerTile(TileDwarvenSign.class, LibBlockNames.DWARVEN_SIGN);
		registerTile(TileManaCrystal.class, LibBlockNames.MANA_CRYSTAL);
		registerTile(TileDwarvenPool.class, LibBlockNames.DWARVEN_POOL);
		registerTile(TilePortalUpgradeRedstone.class, LibBlockNames.PORTAL_UPGRADE_REDSTONE);
		registerTile(TilePortalUpgradeInhibit.class, LibBlockNames.PORTAL_UPGRADE_INHIBIT);
		registerTile(TilePortalUpgradeCharge.class, LibBlockNames.PORTAL_UPGRADE_CHARGE);
		registerTile(TileMultiblockProxy.class, LibBlockNames.MULTIBLOCK_PROXY);
		registerTile(TileMultiblockCore.class, LibBlockNames.MULTIBLOCK_CORE);
		registerTile(TileMultiblockProxy.class, LibBlockNames.MULTIBLOCK_PROXY_LAVA);
		registerTile(TileMultiblockProxy.class, LibBlockNames.MULTIBLOCK_PROXY_NO_RENDER);
		registerTile(TileEnchantPlate.class, LibBlockNames.ENCHANT_PLATE);
		registerTile(TileDarkElfTrap.class, LibBlockNames.DARK_ELF_TRAP);
		
		registerSubTile(SubTileRhododender.class, LibBlockNames.SUBTILE_RHODODENDER);
		registerSubTile(SubTileBlazeBloom.class, LibBlockNames.SUBTILE_BLAZEBLOOM);
		registerSubTile(SubTileGlitterelle.class, LibBlockNames.SUBTILE_GLITTERELLE);
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntityWithAlternatives(clazz, LibResources.PREFIX_MOD + key, key);
	}
	
	private static void registerSubTile(Class<? extends SubTileEntity> clazz, String key) {
		BotaniaAPI.registerSubTile(key, clazz);
		BotaniaAPI.registerSubTileSignature(clazz, new BasicSignature(key));
		BotaniaAPI.addSubTileToCreativeMenu(key);
	}

}
