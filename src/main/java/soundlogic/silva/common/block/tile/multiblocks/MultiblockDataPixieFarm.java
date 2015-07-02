package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockPylon;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.entity.EntityPixieProxy;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieData;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieGroupHandler;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityPixie;
import cpw.mods.fml.common.registry.GameRegistry;

public class MultiblockDataPixieFarm extends MultiblockDataBase {

	public static ArrayList<TileMultiblockCore> activeFarms = new ArrayList<TileMultiblockCore>();
	
	private static final int MAX_PIXIES = 20;
	private static final int COOKIE_LEVEL_FOR_SPAWN = 300;
	private static final int COOKIE_LEVEL_PER_COOKIE = 1000;
	private static final int MANA_FOR_DUST = 2000;
	static Block pixieFlower;
	static Block storage;
	static Item manaCookie;
	static ItemStack pixieDust;
	private IIcon iconPillar;

	public MultiblockDataPixieFarm() {
		super(new BlockData(BotaniaAccessHandler.findBlock("quartzTypeElf"),2));
		pixieFlower = ModBlocks.pixieFlower;
		manaCookie = BotaniaAccessHandler.findItem("manaCookie");
		pixieDust = new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,8);
		storage = BotaniaAccessHandler.findBlock("storage");
		BlockData core = new BlockData(BotaniaAccessHandler.findBlock("quartzTypeElf"),2);
		BlockData quartz = new BlockData(BotaniaAccessHandler.findBlock("quartzTypeElf"),2);
		BlockData storage = new BlockData(BotaniaAccessHandler.findBlock("storage"),2);
		BlockData pylon = new BlockData(ModBlocks.dimensionalPylon, BlockPylon.getMetadataForDimension(Dimension.ALFHEIM));
		BlockData dust = new BlockData(ModBlocks.pixieDust, 0);
		BlockData grass = new BlockData(Blocks.grass, 0);
		BlockData airFlower = new BlockData(pixieFlower, 0) {
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
				 {grass, grass, grass, grass, grass, BlockData.WILDCARD, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, BlockData.WILDCARD, grass, BlockData.WILDCARD, grass, grass, grass, grass},
				 {grass, grass, BlockData.WILDCARD, grass, grass, BlockData.WILDCARD, grass, grass, BlockData.WILDCARD, grass, grass},
				 {grass, grass, grass, grass, BlockData.WILDCARD, grass, BlockData.WILDCARD, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
				 {grass, grass, grass, grass, grass, BlockData.WILDCARD, grass, grass, grass, grass, grass},
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
					 {grass, grass, grass, grass, grass, BlockData.WILDCARD, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, BlockData.WILDCARD, grass, BlockData.WILDCARD, grass, grass, grass, grass},
					 {grass, grass, BlockData.WILDCARD, grass, grass, BlockData.WILDCARD, grass, grass, BlockData.WILDCARD, grass, grass},
					 {grass, grass, grass, grass, BlockData.WILDCARD, grass, BlockData.WILDCARD, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, grass, grass, grass, grass, grass, grass},
					 {grass, grass, grass, grass, grass, BlockData.WILDCARD, grass, grass, grass, grass, grass},
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
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
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
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
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
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
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
		if(!activeFarms.contains(core))
			activeFarms.add(core);
		PixieFarmTileData data = (PixieFarmTileData)core.getTileData();
		AxisAlignedBB aabb = getPixieBoundingBox(core);
		World world = core.getWorldObj();
		prepTickPixies(world, core, data,aabb);
		updateFlowerCount(world, core, data);
		processCookies(world, core, data);
		spawnPixiesFromBlocks(world, core,data,aabb);
		data.pixieGroup.tick(world);
		spreadFlowersAndPixies(world, core,data,aabb);
		shedDust(world, core, data);
		adjustPowerLevel(data);
		core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);

/*		ArrayList<Integer> gens = new ArrayList<Integer>();
		for(Pixie pixie : data.pixies) {
			int gen = pixie.generation;
			while(gen>=gens.size()) {
				gens.add(0);
			}
			gens.set(gen, gens.get(gen)+1);
		}
		String res = "";
		for(int gen : gens) {
			if(gen<10)
				res=res+"0"+gen+",";
			else
				res=res+gen+",";
		}
		System.out.println(res);*/
	}
	
	private void updateFlowerCount(World world, TileMultiblockCore core, PixieFarmTileData data) {
		if(data.flowercount!=-1 && world.rand.nextFloat()>.1F)
			return;
		int y = core.yCoord;
		data.flowercount=0;
		for(int x = core.xCoord-4; x <= core.xCoord+5 ; x++) {
			for(int z = core.zCoord-4; z <= core.zCoord+5 ; z++) {
				if(world.getBlock(x, y, z)==pixieFlower)
					data.flowercount++;
			}
		}
	}

	private void adjustPowerLevel(PixieFarmTileData data) {
		for(FarmPixieData pixie : data.pixieGroup.pixies) {
			data.pixiePowerLevel+=Math.max(5, 10*pixie.generation*pixie.generation);
		}
		data.pixiePowerLevel=Math.min(data.pixiePowerLevel, data.maxPixiePower);
		data.pixiePowerLevel-=data.pixiePowerLevel/2000;
		data.pixiePowerLevel=Math.max(data.pixiePowerLevel, 0);
	}

	@Override
	public void onClientTick(TileMultiblockCore core) {
		PixieFarmTileData data = (PixieFarmTileData)core.getTileData();
		data.pixieGroup.clientTick(core.getWorldObj());
	}

	private void shedDust(World world, TileMultiblockCore core, PixieFarmTileData data) {
		for(FarmPixieData pixie : data.pixieGroup.pixies) {
			if(world.rand.nextFloat()<getDustChanceForPixie(pixie)) {
				IManaPool pool = getRandomManaPool(world, core);
				if(pool.getCurrentMana()>MANA_FOR_DUST)
					pool.recieveMana(-MANA_FOR_DUST);
				else
					return;
				EntityItem ent = new EntityItem(world, pixie.posX, pixie.posY, pixie.posZ, pixieDust.copy());
				ent.lifespan=20*15;
				world.spawnEntityInWorld(ent);
			}
		}
	}
	
	private float getDustChanceForPixie(FarmPixieData pixie) {
		float val = 1F/3600F * (1F + (((float)pixie.generation * 2F)/((float)pixie.generation + 2F)));
		return val;
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
		for(int i = 0 ; i < 4; i++) {
			int[] coords = getCoordsForSlot(core, i);
			if(world.getBlock(coords[0], coords[1], coords[2]) != storage || world.getBlockMetadata(coords[0], coords[1], coords[2])!=2)
				continue;
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, getCookieBoundingBox(core, i));
			for(EntityItem item : items) {
				ItemStack stack = item.getEntityItem();
				if(stack.getItem()==manaCookie) {
					data.cookieLevel+=stack.stackSize*COOKIE_LEVEL_PER_COOKIE;
					item.setDead();
				}
			}
		}
	}


	private AxisAlignedBB getCookieBoundingBox(TileMultiblockCore core, int slot) {
		double height = .1;
		int[] coords = getCoordsForSlot(core, slot);
		return AxisAlignedBB.getBoundingBox(coords[0], coords[1]+1, coords[2], coords[0]+1, coords[1]+1+height, coords[2]+1);
	}
	
	public int[] getCoordsForSlot(TileMultiblockCore core, int slot) {
		switch(slot) {
		case 0: return new int[]{core.xCoord-3, core.yCoord-1, core.zCoord};
		case 1: return new int[]{core.xCoord+3, core.yCoord-1, core.zCoord};
		case 2: return new int[]{core.xCoord, core.yCoord-1, core.zCoord-3};
		case 3: return new int[]{core.xCoord, core.yCoord-1, core.zCoord+3};
		}
		return null;
	}

	private void spreadFlowersAndPixies(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
		List<FarmPixieData> curPixies = new ArrayList<FarmPixieData>(data.pixieGroup.pixies);
		for(FarmPixieData pixie : curPixies) {
			if((pixie.posY-core.yCoord)<2.3) {
				int x = (int) pixie.posX;
				int y = core.yCoord;
				int z = (int) pixie.posZ;
				if(world.rand.nextFloat()<chanceForSpawnPixie(world, core, data, x, y, z, pixie.generation+1))
					spawnPixieFromBlock(world, core, data, x, y, z, aabb, pixie.generation+1);
			}
			pixie.spawnFlower(world, core, data, core.yCoord);
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
		if(spawnPixieFromBlock(world, core, data, x, y, z, aabb,0)) {
			data.cookieLevel-=COOKIE_LEVEL_FOR_SPAWN;
		}
	}

	private boolean spawnPixieFromBlock(World world, TileMultiblockCore core, PixieFarmTileData data, int x, int y, int z, AxisAlignedBB aabb, int generation) {
		if(world.rand.nextFloat()<chanceForSpawnPixie(world, core, data, x, y, z, 0))
			return spawnPixie(world, core, data, x, y+1.5, z, aabb, generation);
		return false;
	}
	
	private boolean spawnPixie(World world, TileMultiblockCore core, PixieFarmTileData data, double x, double y, double z, AxisAlignedBB aabb, int generation) {
		if(data.pixieGroup.pixies.size()<MAX_PIXIES) {
			FarmPixieData pixie = new FarmPixieData(x, y, z, aabb, generation, data);
			return true;
		}
		return false;
	}

	private float chanceForSpawnPixie(World world, TileMultiblockCore core, PixieFarmTileData data, int x, int y, int z, int generation) {
		return baseChanceForSpawnPixie(world, core, data, x, y, z) * (3-(((float)generation+2)/((float)generation+1))) * (generation==0 ? 1 : .05F);
	}
	private float baseChanceForSpawnPixie(World world, TileMultiblockCore core, PixieFarmTileData data, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block==pixieFlower)
			return .1F * countAirBlocksAround(world, x, y, z);
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

	private void prepTickPixies(World world, TileMultiblockCore core, PixieFarmTileData data, AxisAlignedBB aabb) {
		for(FarmPixieData pixie : data.pixieGroup.pixies) {
			pixie.aabb=aabb;
		}
	}
	
	private AxisAlignedBB getPixieBoundingBox(TileMultiblockCore core) {
		return AxisAlignedBB.getBoundingBox(core.xCoord-4, core.yCoord+1, core.zCoord-4, core.xCoord+5, core.yCoord+4, core.zCoord+5);
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
		tile.iconsForSides = new IIcon[] {null, null, iconPillar, iconPillar, iconPillar, iconPillar};
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
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconPillar = par1IconRegister.registerIcon(LibResources.PIXIE_FARM_PILLAR);
	}
	
	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) {
		return trial==0;
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.pixie_farm;
	}

	public static class FarmPixieData extends PixieData {

		final static double bounds_acceleration = 0.04F;
		final static double random_acceleration = 0.08F;
		
		AxisAlignedBB aabb;
		
		public int generation = 0;
		
		public FarmPixieData(double x, double y, double z, AxisAlignedBB aabb, int generation, PixieFarmTileData data) {
			super(data.pixieGroup);
			this.generation=generation;
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			setStartingVelocity(aabb);
			this.aabb=aabb;
		}
		
		public FarmPixieData(PixieGroupHandler data) {
			super(data);
		}

		public void spawnFlower(World world, TileMultiblockCore core, PixieFarmTileData data, int y) {
			if(this.motionY<=0 || this.prevMotionY>=0)
				return;
			if(posY-y>1.6)
				return;
			if(world.rand.nextFloat()<((((float)data.flowercount)/20F)*(((float)data.flowercount)/20F)))
					return;
			if(world.rand.nextFloat()>(((float)generation)+.5F)/(((float)generation)+1.5F))
				return;
			int x = MathHelper.floor_double(posX);
			int z = MathHelper.floor_double(posZ);
			if(!world.isAirBlock(x, y, z))
				return;
			if(pixieFlower.canPlaceBlockAt(world, x, y, z))
				world.setBlock(x, y, z, pixieFlower, world.rand.nextInt(16), 3);
		}

		@Override
		public void tick(World world, PixieGroupHandler data) {
			checkDeadBoundingBox(world, data);
			if(this.isDead())
				return;
			randomMotion(world);
			avoidBounds();
			super.tick(world, data);
		}
		
		
		private void randomMotion(World world) {
			this.motionX+=world.rand.nextGaussian()*random_acceleration;
			this.motionY+=world.rand.nextGaussian()*random_acceleration;
			this.motionZ+=world.rand.nextGaussian()*random_acceleration;

			normVelocity();
		}

		private void avoidBounds() {
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

		private void checkDeadBoundingBox(World world, PixieGroupHandler data) {
			if(!aabb.copy().expand(.5, .5, .5).isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
				setDead(data);
				return;
			}
		}

		@Override
		protected int getLifeSpan() {
			return 20*60*(generation+1);
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
		private static final String TAG_PIXIE_GENERATION = "pixiesGeneration";

		@Override
		public void writeToNBT(NBTTagCompound cmp) {
			super.writeToNBT(cmp);
			cmp.setInteger(TAG_PIXIE_GENERATION, generation);
		}

		@Override
		public void readFromNBT(NBTTagCompound cmp) {
			super.readFromNBT(cmp);
			generation=cmp.getInteger(TAG_PIXIE_GENERATION);
		}

	}

	@Override
	public void onInvalidate(TileMultiblockCore core) {
		this.activeFarms.remove(core);
	}

	@Override
	public void onBreak(TileMultiblockCore tileMultiblockCore) {
		// NO OP
	}
}
