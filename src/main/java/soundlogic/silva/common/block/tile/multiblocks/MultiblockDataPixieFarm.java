package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockPylon;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityPixie;
import cpw.mods.fml.common.registry.GameRegistry;

public class MultiblockDataPixieFarm extends MultiblockDataBase {

	private static final int MAX_PIXIES = 20;
	private static final int COOKIE_LEVEL_FOR_SPAWN = 300;
	private static final int COOKIE_LEVEL_PER_COOKIE = 1000;
	private static final int MANA_FOR_DUST = 2000;
	static Block shinyFlower;
	static Item manaCookie;
	static ItemStack pixieDust;
	
	public MultiblockDataPixieFarm() {
		super(new BlockData(GameRegistry.findBlock("Botania", "quartzTypeElf"),2));
		shinyFlower = GameRegistry.findBlock("Botania", "shinyFlower");
		manaCookie = GameRegistry.findItem("Botania", "manaCookie");
		pixieDust = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,8);
		BlockData core = new BlockData(GameRegistry.findBlock("Botania", "quartzTypeElf"),2);
		BlockData quartz = new BlockData(GameRegistry.findBlock("Botania", "quartzTypeElf"),2);
		BlockData storage = new BlockData(GameRegistry.findBlock("Botania", "storage"),2);
		BlockData pylon = new BlockData(ModBlocks.dimensionalPylon, BlockPylon.getMetadataForDimension(Dimension.ALFHEIM));
		BlockData dust = new BlockData(ModBlocks.pixieDust, 0);
		BlockData grass = new BlockData(Blocks.grass, 0);
		BlockData airFlower = new BlockData(GameRegistry.findBlock("Botania", "shinyFlower"), 0) {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.isAirBlock(x, y, z) || world.getBlock(x, y, z)==block;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		
		creationRequirementsTemplate = new BlockData[][][] {
				{{BlockData.WILDCARD, grass, grass, grass, grass, grass, grass, grass, grass, grass, BlockData.WILDCARD},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, storage, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, storage, grass, grass, BlockData.WILDCARD, grass, grass, storage, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, storage, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {BlockData.WILDCARD, grass, grass, grass, grass, grass, grass, grass, grass, grass, BlockData.WILDCARD}},

				{{storage, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, storage},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, dust, airFlower, airFlower, airFlower, airFlower, airFlower, dust, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, dust, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, BlockData.POOL, dust, BlockData.POOL, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, dust, dust, core, dust, dust, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, BlockData.POOL, dust, BlockData.POOL, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, dust, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, dust, airFlower, airFlower, airFlower, airFlower, airFlower, dust, airFlower, BlockData.WILDCARD},
				 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
				 {storage, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, storage}},

				{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}}};

		templateOrigin = new int[] {5,1,5};
		
		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.WILDCARD, grass, grass, grass, grass, grass, grass, grass, grass, grass, BlockData.WILDCARD},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, storage, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, storage, grass, grass, BlockData.WILDCARD, grass, grass, storage, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, storage, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {BlockData.WILDCARD, grass, grass, grass, grass, grass, grass, grass, grass, grass, BlockData.WILDCARD}},

					{{storage, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, storage},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, dust, airFlower, airFlower, airFlower, airFlower, airFlower, dust, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, dust, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, BlockData.POOL, dust, BlockData.POOL, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, dust, dust, BlockData.MULTIBLOCK_CORE, dust, dust, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, BlockData.POOL, dust, BlockData.POOL, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, dust, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, dust, airFlower, airFlower, airFlower, airFlower, airFlower, dust, airFlower, BlockData.WILDCARD},
					 {BlockData.WILDCARD, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, airFlower, BlockData.WILDCARD},
					 {storage, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, storage}},

					{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}},

					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},

					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}}};
	}

	@Override
	public String getName() {
		return "pixieFarm";
	}

	@Override
	public IMultiblockTileData createTileData() {
		return new PixieFarmTileData();
	}

	@Override
	public void onTick(TileMultiblockCore core) {
		PixieFarmTileData data = (PixieFarmTileData)core.getTileData();
		AxisAlignedBB aabb = getPixieBoundingBox(core);
		World world = core.getWorldObj();
		processCookies(world, core, data);
		spawnPixiesFromBlocks(world, core,data,aabb);
		tickPixies(world, core, data,aabb);
		cleanDeadPixies(data);
		spawnPixiesFromBlocks(world, core,data,aabb);
		spreadFlowersAndPixies(world, core,data,aabb);
		shedDust(world, core, data);
		core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
	}

	private void shedDust(World world, TileMultiblockCore core, PixieFarmTileData data) {
		if(world.rand.nextFloat()>.1F)
			return;
		for(Pixie pixie : data.pixies) {
			if(world.rand.nextFloat()<.1F/((300-pixie.ticks)*(300-pixie.ticks)+20)) {
				IManaPool pool = getRandomManaPool(world, core);
				if(pool.getCurrentMana()>MANA_FOR_DUST)
					pool.recieveMana(-MANA_FOR_DUST);
				else
					return;
				EntityItem ent = new EntityItem(world, pixie.posX, pixie.posY, pixie.posZ, pixieDust.copy());
				world.spawnEntityInWorld(ent);
			}
		}
	}

	private IManaPool getRandomManaPool(World world, TileMultiblockCore core) {
		switch(world.rand.nextInt(4)) {
		case 0:return (IManaPool) world.getTileEntity(core.xCoord-1, core.yCoord, core.zCoord-1);
		case 1:return (IManaPool) world.getTileEntity(core.xCoord-1, core.yCoord, core.zCoord+1);
		case 2:return (IManaPool) world.getTileEntity(core.xCoord+1, core.yCoord, core.zCoord-1);
		case 3:return (IManaPool) world.getTileEntity(core.xCoord+1, core.yCoord, core.zCoord+1);
		}
		return null;
	}

	private void processCookies(World world, TileMultiblockCore core, PixieFarmTileData data) {
		data.cookieLevel = MathHelper.floor_double(.9D * data.cookieLevel);
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, getCookieBoundingBox(core, 0));
		for(EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			if(stack.getItem()==manaCookie) {
				if(cookieInSpot(item, core)) {
					data.cookieLevel+=stack.stackSize*COOKIE_LEVEL_PER_COOKIE;
					item.setDead();
				}
			}
		}
	}

	private boolean cookieInSpot(EntityItem item, TileMultiblockCore core) {
		if(item.boundingBox.intersectsWith(getCookieBoundingBox(core,1)))
			return true;
		if(item.boundingBox.intersectsWith(getCookieBoundingBox(core,2)))
			return true;
		if(item.boundingBox.intersectsWith(getCookieBoundingBox(core,3)))
			return true;
		if(item.boundingBox.intersectsWith(getCookieBoundingBox(core,4)))
			return true;
		return false;
	}

	private AxisAlignedBB getCookieBoundingBox(TileMultiblockCore core, int i) {
		double height = .1;
		switch(i) {
		case 0: return AxisAlignedBB.getBoundingBox(core.xCoord-3, core.yCoord, core.zCoord-3, core.xCoord+4, core.yCoord+height, core.zCoord+4);
		case 1: return AxisAlignedBB.getBoundingBox(core.xCoord-3, core.yCoord, core.zCoord, core.xCoord-2, core.yCoord+height, core.zCoord+1);
		case 2: return AxisAlignedBB.getBoundingBox(core.xCoord+3, core.yCoord, core.zCoord, core.xCoord+4, core.yCoord+height, core.zCoord+1);
		case 3: return AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord, core.zCoord-3, core.xCoord+1, core.yCoord+height, core.zCoord-2);
		case 4: return AxisAlignedBB.getBoundingBox(core.xCoord, core.yCoord, core.zCoord+3, core.xCoord+1, core.yCoord+height, core.zCoord+4);
		}
		return null;
	}

	private void cleanDeadPixies(PixieFarmTileData data) {
		if(!data.pixieDied)
			return;
		Iterator<Pixie> pixies = data.pixies.iterator();
		while(pixies.hasNext()) {
			if(pixies.next().isDead)
				pixies.remove();
		}
	}

	private void spreadFlowersAndPixies(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
		if(world.rand.nextFloat()<.5F)
			return;
		List<Pixie> curPixies = new ArrayList<Pixie>(data.pixies);
		for(Pixie pixie : curPixies) {
			if(world.rand.nextFloat()>.1F)
				return;
			if((pixie.posY-core.yCoord)<2.3) {
				int x = (int) pixie.posX;
				int y = core.yCoord;
				int z = (int) pixie.posZ;
				if(world.rand.nextFloat()<chanceForSpawnPixie(world, core, data, x, y, z))
					spawnPixieFromBlock(world, core, data, x, y, z, aabb);
			}
			pixie.spawnFlower(world, core, data, core.yCoord, aabb);
		}
	}

	private void spawnPixiesFromBlocks(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
		if(data.cookieLevel < COOKIE_LEVEL_FOR_SPAWN)
			return;
		int minX = MathHelper.floor_double(aabb.minX);
		int minZ = MathHelper.floor_double(aabb.minZ);
		int rangeX = MathHelper.floor_double(aabb.maxX)-MathHelper.floor_double(aabb.minX)-1;
		int rangeZ = MathHelper.floor_double(aabb.maxZ)-MathHelper.floor_double(aabb.minZ)-1;
		int x = minX+core.getWorldObj().rand.nextInt(rangeX);
		int y = core.yCoord;
		int z = minZ+core.getWorldObj().rand.nextInt(rangeZ);
		if(spawnPixieFromBlock(world, core, data, x, y, z, aabb)) {
			data.cookieLevel-=COOKIE_LEVEL_FOR_SPAWN;
		}
	}

	private boolean spawnPixieFromBlock(World world, TileMultiblockCore core, PixieFarmTileData data, int x, int y, int z, AxisAlignedBB aabb) {
		if(world.rand.nextFloat()<chanceForSpawnPixie(world, core, data, x, y, z))
			return spawnPixie(world, core, data, x, y+1.5, z, aabb);
		return false;
	}
	
	private boolean spawnPixie(World world, TileMultiblockCore core, PixieFarmTileData data, double x, double y, double z, AxisAlignedBB aabb) {
		if(data.pixies.size()<MAX_PIXIES) {
			Pixie pixie = new Pixie(x, y, z, aabb);
			data.pixies.add(pixie);
			return true;
		}
		return false;
	}

	private float chanceForSpawnPixie(World world, TileMultiblockCore core, PixieFarmTileData data, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block==shinyFlower)
			return .1F + .05F * countAirBlocksAround(world, x, y, z);
		if(block==ModBlocks.pixieDust) {
			if(x==core.xCoord)
				return 0F;
			if(z==core.zCoord)
				return 0F;
			return countAirBlocksAround(world, x, y, z)==4 ? .05F : 0;
		}
		return 0F;
	}
	private int countAirBlocksAround(World world, int x, int y, int z) {
		int count = 0;
		if(world.isAirBlock(x+1, y, z))
			count++;
		if(world.isAirBlock(x-1, y, z))
			count++;
		if(world.isAirBlock(x, y, z+1))
			count++;
		if(world.isAirBlock(x, y, z-1))
			count++;
		return count;
	}

	private void tickPixies(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
		for(Pixie pixie : data.pixies) {
			pixie.tick(world, core, data, aabb);
		}
	}
	
	private AxisAlignedBB getPixieBoundingBox(TileMultiblockCore core) {
		return AxisAlignedBB.getBoundingBox(core.xCoord-4, core.yCoord+1, core.zCoord-4, core.xCoord+5, core.yCoord+4, core.zCoord+5);
	}

	private void doParticles(World world, TileMultiblockCore core, PixieFarmTileData data) {
		if(!core.getWorldObj().isRemote)
			return;
		for(Pixie pixie : data.pixies) {
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(core.getWorldObj(), pixie.posX + (Math.random() - 0.5) * 0.25, pixie.posY + 0.5  + (Math.random() - 0.5) * 0.25, pixie.posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
		}
	}

	@Override
	public void onCollision(TileMultiblockBase tile, TileMultiblockCore core, Entity ent) {
		// NO OP
	}

	@Override
	public void init(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		// NO OP
	}

	@Override
	public void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		// NO OP
	}

	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core, Minecraft mc, ScaledResolution res) {
		// NO OP
	}

	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core, EntityPlayer player, ItemStack stack) {
		// NO OP
	}

	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.PIXIE_FARM;
	}

	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) {
		return trial==0;
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.PIXIE_FARM;
	}

	public static class Pixie {
		final static double default_speed = 0.08F;
		final static AxisAlignedBB default_boundingBox = AxisAlignedBB.getBoundingBox(-0.2, -0.2, -0.2, 0.2, 0.2, 0.2);
		final static double bounds_acceleration = 0.04F;
		final static double random_acceleration = 0.08F;
		public double posX, posY, posZ;
		public double prevPosX, prevPosY, prevPosZ;
		public double motionX, motionY, motionZ;
		public float rotation;
		public float prevRotation;
		public int ticks=0;
		boolean isDead = false;
		public int type = 0;

		public Pixie() {
		}
		
		public void spawnFlower(World world, TileMultiblockCore core, PixieFarmTileData data, int y, AxisAlignedBB aabb) {
			if(posY-y>1.1)
				return;
			int x = MathHelper.floor_double(posX);
			int z = MathHelper.floor_double(posZ);
			if(!world.isAirBlock(x, y, z))
				return;
			if(shinyFlower.canPlaceBlockAt(world, x, y, z))
				world.setBlock(x, y, z, shinyFlower, world.rand.nextInt(16), 3);
		}

		public void tick(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
			ticks++;
			checkDead(world, data, aabb);
			if(isDead)
				return;
			this.prevPosX=this.posX;
			this.prevPosY=this.posY;
			this.prevPosZ=this.posZ;
			this.prevRotation=this.rotation;
			avoidBounds(aabb);
			randomMotion(world);
			tickMotion(world, aabb);
		}
		
		private void tickMotion(World world, AxisAlignedBB aabb) {
			double speed = getSpeed();
			double newX = posX+motionX*speed;
			double newY = posY+motionY*speed;
			double newZ = posZ+motionZ*speed;
			boolean shouldMove = true;
			boolean newBlocked = positionBlocked(world, newX, newY, newZ);
			if(newBlocked) {
				boolean curBlocked = positionBlocked(world, posX, posY, posZ);
				if(!curBlocked)
					shouldMove=false;
			}
			if(shouldMove) {
				this.posX=newX;
				this.posY=newY;
				this.posZ=newZ;
			}
			else {
				this.motionX*=-1;
				this.motionY*=-1;
				this.motionZ*=-1;

				newX = posX+motionX*speed;
				newY = posY+motionY*speed;
				newZ = posZ+motionZ*speed;
				
				newBlocked = positionBlocked(world, newX, newY, newZ);
				
				if(!newBlocked) {
					this.posX=newX;
					this.posY=newY;
					this.posZ=newZ;
				}
			}
		}
		
		private boolean positionBlocked(World world, double x, double y, double z) {
			if(world.isAirBlock((int)x, (int)y, (int)z))
				return false;
			else if(world.getBlock((int)x, (int)y, (int)z).isNormalCube())
				return true;
			else
				return world.func_147461_a(getBoundingBox(x,y,z)).isEmpty();
		}

		private void randomMotion(World world) {
			this.motionX+=world.rand.nextGaussian()*random_acceleration;
			this.motionY+=world.rand.nextGaussian()*random_acceleration;
			this.motionZ+=world.rand.nextGaussian()*random_acceleration;
			normVelocity();
		}

		private void avoidBounds(AxisAlignedBB aabb) {
			double minXDist = Math.max(0, posX - aabb.minX);
			double minYDist = Math.max(0, posY - aabb.minY);
			double minZDist = Math.max(0, posZ - aabb.minZ);
			double maxXDist = Math.max(0, aabb.maxX - posX);
			double maxYDist = Math.max(0, aabb.maxY - posY);
			double maxZDist = Math.max(0, aabb.maxZ - posZ);
			double buffer = .01;
			double accX = Math.max(1D/(buffer+minXDist), 1D/(buffer+maxXDist))*bounds_acceleration;
			double accY = Math.max(1D/(buffer+minYDist), 1D/(buffer+maxYDist))*bounds_acceleration;
			double accZ = Math.max(1D/(buffer+minZDist), 1D/(buffer+maxZDist))*bounds_acceleration;
			accX *= minXDist>maxXDist ? -1 : 1;
			accY *= minYDist>maxYDist ? -1 : 1;
			accZ *= minZDist>maxZDist ? -1 : 1;
			this.motionX+=accX;
			this.motionY+=accY;
			this.motionZ+=accZ;
			normVelocity();
		}

		private void checkDead(World world, PixieFarmTileData data, AxisAlignedBB aabb) {
			if(this.ticks>getLifeSpan()) {
				setDead(data);
				return;
			}
			if(!aabb.copy().expand(.5, .5, .5).isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
				setDead(data);
				return;
			}
			if(world.getBlock((int)posX, (int)posY, (int)posZ).isNormalCube()) {
				setDead(data);
				return;
			}
		}

		private int getLifeSpan() {
			return 20*60*2;
		}

		public Pixie(double x, double y, double z, AxisAlignedBB aabb) {
			this();
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			setStartingVelocity(aabb);
		}
		
		private void setStartingVelocity(AxisAlignedBB aabb) {
			double minXDist = posX - aabb.minX;
			double minYDist = posY - aabb.minY;
			double minZDist = posZ - aabb.minZ;
			double maxXDist = aabb.maxX - posX;
			double maxYDist = aabb.maxY - posY;
			double maxZDist = aabb.maxZ - posZ;
			double buffer = 2;
			this.motionX = Math.max(1D/(buffer+minXDist), 1D/(buffer+maxXDist));
			this.motionY = Math.max(1D/(buffer+minYDist), 1D/(buffer+maxYDist));
			this.motionZ = Math.max(1D/(buffer+minZDist), 1D/(buffer+maxZDist));
			this.motionX *= minXDist>maxXDist ? -1 : 1;
			this.motionY *= minYDist>maxYDist ? -1 : 1;
			this.motionZ *= minZDist>maxZDist ? -1 : 1;
			normVelocity();
		}
		private void normVelocity() {
			double vel = Math.sqrt(motionX*motionX+motionY*motionY+motionZ*motionZ);
			if(vel!=0) {
				this.motionX = this.motionX/vel;
				this.motionY = this.motionY/vel;
				this.motionZ = this.motionZ/vel;
				this.rotation=(float) (Math.atan2(motionX, motionZ) / Math.PI * 180F);
			}
			else {
				this.motionX=Math.random()-.5;
				this.motionY=Math.random()-.5;
				this.motionZ=Math.random()-.5;
				normVelocity();
			}
		}
		
		private void setDead(PixieFarmTileData data) {
			data.pixieDied=true;
			this.isDead=true;
		}
		
		public AxisAlignedBB getBoundingBox(double x, double y, double z) {
			return getBoundingBox().copy().offset(x, y, z);
		}
		private AxisAlignedBB getBoundingBox() {
			return default_boundingBox;
		}
		
		public double getSpeed() {
			return default_speed;
		}
	}
}
