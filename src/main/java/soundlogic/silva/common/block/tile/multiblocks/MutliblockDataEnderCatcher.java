package soundlogic.silva.common.block.tile.multiblocks;

import java.util.List;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockManaEater;
import soundlogic.silva.common.block.BlockPylon;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.helper.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class MutliblockDataEnderCatcher extends MultiblockDataBase {

	private IIcon iconBase;

	public MutliblockDataEnderCatcher() {
		super(new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick"),1));
		BlockData core = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick"),1);
		BlockData obsidian = new BlockData(Blocks.obsidian, 0);
		BlockData quartz = new BlockData(Blocks.quartz_block, 2);
		BlockData pylon = new BlockData(ModBlocks.dimensionalPylon, BlockPylon.getMetadataForDimension(Dimension.GINNUNGAGAP));
		BlockData hopper = new BlockData(Blocks.hopper, 0);
		BlockData stairs0 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),0) {
			
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				int meta = world.getBlockMetadata(x, y, z);
				return world.getBlock(x, y, z)==block && (meta==0 || meta==2);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		BlockData stairs1 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),0);
		BlockData stairs2 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),2) {
			
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				int meta = world.getBlockMetadata(x, y, z);
				return world.getBlock(x, y, z)==block && (meta==0 || meta==3);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		BlockData stairs3 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),2);
		BlockData stairs4 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),3);
		BlockData stairs5 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),5) {
			
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				int meta = world.getBlockMetadata(x, y, z);
				return world.getBlock(x, y, z)==block && (meta==1 || meta==2);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		BlockData stairs6 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),1);
		BlockData stairs7 = new BlockData(BotaniaAccessHandler.findBlock("endStoneBrick0Stairs"),7) {
			
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				int meta = world.getBlockMetadata(x, y, z);
				return world.getBlock(x, y, z)==block && (meta==1 || meta==3);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};

		creationRequirementsTemplate = new BlockData[][][] {
				{{quartz, obsidian, obsidian, obsidian, quartz},
				 {obsidian, obsidian, obsidian, obsidian, obsidian},
				 {obsidian, obsidian, hopper, obsidian, obsidian},
				 {obsidian, obsidian, obsidian, obsidian, obsidian},
				 {quartz, obsidian, obsidian, obsidian, quartz}},
				 
				{{quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz},
				 {BlockData.WILDCARD, stairs0, stairs1, stairs2, BlockData.WILDCARD},
				 {BlockData.WILDCARD, stairs3, core, stairs4, BlockData.WILDCARD},
				 {BlockData.WILDCARD, stairs5, stairs6, stairs7, BlockData.WILDCARD},
				 {quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz}},
				 
				{{quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz}},

				{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}}};

		templateOrigin = new int[] {2,1,2};
		
		persistanceAndCreationBlocks = new BlockData[][][] {
				{{quartz, obsidian, obsidian, obsidian, quartz},
				 {obsidian, obsidian, obsidian, obsidian, obsidian},
				 {obsidian, obsidian, hopper, obsidian, obsidian},
				 {obsidian, obsidian, obsidian, obsidian, obsidian},
				 {quartz, obsidian, obsidian, obsidian, quartz}},
				 
				{{quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz},
				 {BlockData.WILDCARD, stairs0, stairs1, stairs2, BlockData.WILDCARD},
				 {BlockData.WILDCARD, stairs3, BlockData.MULTIBLOCK_CORE, stairs4, BlockData.WILDCARD},
				 {BlockData.WILDCARD, stairs5, stairs6, stairs7, BlockData.WILDCARD},
				 {quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz}},
				 
				{{quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {quartz, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, quartz}},

				{{pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
				 {pylon, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, pylon}}};
	}

	@Override
	public String getName() {
		return "enderCatcher";
	}

	@Override
	public IMultiblockTileData createTileData() {
		return new BlankTileData();
	}

	@Override
	public void onTick(TileMultiblockCore core) {
		World worldObj = core.getWorldObj();
		if(worldObj.isRemote)
			return;
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(core.xCoord-2, core.yCoord+1, core.zCoord-2, core.xCoord+3, core.yCoord+5, core.zCoord+3);
		List<EntityEnderPearl> pearls = worldObj.getEntitiesWithinAABB(EntityEnderPearl.class, bounds);
		if(pearls.isEmpty())
			return;
		IInventory hopperInventory = (IInventory) worldObj.getTileEntity(core.xCoord, core.yCoord-1, core.zCoord);
		for(EntityEnderPearl pearl : pearls) {
			if(InventoryHelper.testInventoryInsertion(hopperInventory, new ItemStack(Items.ender_pearl), ForgeDirection.DOWN)!=1)
				return;
			EntityLivingBase thrower = pearl.getThrower();
            if (thrower != null && thrower instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)thrower;
                
                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == worldObj)
                {
                	double tx = core.xCoord+.5;
                	double ty = core.yCoord+1+thrower.posY-thrower.boundingBox.minY;
                	double tz = core.zCoord+.5;
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, tx, ty, tz, 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event))
                    {
	                    if (thrower.isRiding())
	                    {
	                        thrower.mountEntity((Entity)null);
	                    }

	                    thrower.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
	                    thrower.fallDistance = 0.0F;
	                    
                    }
                }
            }
            InventoryHelper.insertItemIntoInventory(hopperInventory, new ItemStack(Items.ender_pearl));
            pearl.setDead();
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
		tile.iconsForSides=new IIcon[] {iconBase, iconBase, iconBase, iconBase, iconBase, iconBase};
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
		return LibMultiblockNames.ENDER_CATCHER;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBase = par1IconRegister.registerIcon(LibResources.ENDER_CATCHER_BASE);
	}
	
	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX,
			boolean mirrorZ, int rot) {
		return trial==0;
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.enderCatcher;
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
		// NO OP
	}
}
