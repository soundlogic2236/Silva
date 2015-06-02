package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockManaEater;
import soundlogic.silva.common.block.BlockMultiblockProxyLava;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.HUDHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class MultiblockDataCarnilotus extends MultiblockDataBase {
	
	protected boolean[][][] proxyLocationsWithAcid;

	private ArrayList<EntityItem> trackedItems = new ArrayList<EntityItem>();
	private int maxMana = 20000;
	
	IIcon iconSkin;
	IIcon iconStem;
	IIcon iconInside;
	public IIcon iconAcid;
	public IIcon iconTeethLip;
	public IIcon[] iconTeethInner = new IIcon[9];
	IIcon[] iconTransition = new IIcon[7];
	
	public MultiblockDataCarnilotus() {
		super(new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0));
		BlockData core = new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0);
		BlockData livingwood = new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0);
		BlockData purplePetal = new BlockData(vazkii.botania.common.block.ModBlocks.petalBlock,10);
		BlockData blackPetal = new BlockData(vazkii.botania.common.block.ModBlocks.petalBlock,15);
		BlockData ironBar = new BlockData(Blocks.iron_bars,0);
		BlockData manaEater = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				if(!(world.getBlock(x, y, z) instanceof BlockManaEater))
					return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(!(tile instanceof TileManaEater))
					return false;
				TileManaEater eater = (TileManaEater) tile;
				return Math.abs(eater.rotationY % 360 - 90 ) < 20;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.manaEater,0,0);
			}
		};
		BlockData airOrWaterOrAcid = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				if(core==null)
					return BlockData.AIR.isValid(core, world, x, y, z);
				CarnilotusTileData data = (CarnilotusTileData) core.tileData;
				if(data.activated)
					return BlockData.MULTIBLOCK_NO_RENDER_WATER.isValid(core, world, x, y, z);
				return BlockData.WATER.isValid(core, world, x, y, z) ||
						BlockData.AIR.isValid(core, world, x, y, z);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				BlockData.WILDCARD.setBlock(core, world, x, y, z);
			}
		};

		creationRequirementsTemplate = new BlockData[][][] {
					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD},
					 {BlockData.WILDCARD, manaEater, manaEater, manaEater, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, manaEater, manaEater, manaEater, BlockData.WILDCARD, core},
					 {BlockData.WILDCARD, manaEater, manaEater, manaEater, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD}},
					
					{{BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, livingwood},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD}},
						
					{{BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD}},
						
					{{BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {purplePetal, BlockData.AIR, BlockData.AIR, BlockData.AIR, purplePetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, purplePetal, purplePetal, purplePetal, BlockData.WILDCARD,BlockData.WILDCARD}},
						
					{{BlockData.WILDCARD, ironBar, ironBar, ironBar, BlockData.WILDCARD,BlockData.WILDCARD},
					 {ironBar, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {ironBar, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {ironBar, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, ironBar, ironBar, ironBar, BlockData.WILDCARD,BlockData.WILDCARD}},
							
					{{BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD,BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD,BlockData.WILDCARD}},
					 
					{{BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD,BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, blackPetal, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD,BlockData.WILDCARD}},
			};

		templateOrigin = new int[] {2,0,5};
		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK_CORE},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, airOrWaterOrAcid, airOrWaterOrAcid, airOrWaterOrAcid, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK_NO_RENDER, BlockData.MULTIBLOCK_NO_RENDER, BlockData.MULTIBLOCK_NO_RENDER, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK_NO_RENDER, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK_NO_RENDER, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK_NO_RENDER, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK_NO_RENDER, BlockData.MULTIBLOCK_NO_RENDER, BlockData.MULTIBLOCK_NO_RENDER, BlockData.WILDCARD,BlockData.WILDCARD}},
						
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD}},
				 
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD}},
		};
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	

	@Override
	public String getName() {
		return "carnilotus";
	}



	@Override
	public IMultiblockTileData createTileData() {
		return new CarnilotusTileData();
	}

	boolean captureDrops;
	List<EntityItem> capturedDrops = new ArrayList<EntityItem>();
	
	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if(!captureDrops)
			return;
		capturedDrops=new ArrayList<EntityItem>(event.drops);
		event.setCanceled(true);
	}
	
	DamageSource acid = new DamageSource("carnilotus");

	@Override
	public void onTick(TileMultiblockCore core) {
		CarnilotusTileData data = (CarnilotusTileData) core.tileData;
		AxisAlignedBB aabb = getWaterBounds(core);
		if(!data.activated && checkForWater(core)) {
			List<EntityItem> items = core.getWorldObj().getEntitiesWithinAABB(EntityItem.class, aabb);
			for(EntityItem ent : items) {
				ItemStack stack = ent.getEntityItem();
				int count = stack.stackSize;
				int meta = stack.getItemDamage();
				int toRemove = 0;
				if(stack.getItem() == vazkii.botania.common.item.ModItems.rune) {
					if(meta == 0) {
						toRemove = Math.min(count, data.waterRunesNeeded);
						data.waterRunesNeeded-=toRemove;
					}
					else if (meta == 10) {
						toRemove = Math.min(count, data.gluttonyRunesNeeded);
						data.gluttonyRunesNeeded-=toRemove;
					}
					else if (meta == 8) {
						toRemove = Math.min(count, data.manaRunesNeeded);
						data.manaRunesNeeded-=toRemove;
					}
					else if (meta == 13) {
						toRemove = Math.min(count, data.wrathRunesNeeded);
						data.wrathRunesNeeded-=toRemove;
					}
				}
				else if(stack.getItem() == Items.sugar) {
					toRemove = Math.min(count, data.sugarNeeded);
					data.sugarNeeded-=toRemove;
				}
				stack.stackSize-=toRemove;
			}
			if(data.waterRunesNeeded == 0 && data.gluttonyRunesNeeded == 0 && data.manaRunesNeeded == 0 && data.wrathRunesNeeded == 0 && data.sugarNeeded == 0) {
				data.activated=true;
				clearWater(core);
				core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
			}
		}
		else if (data.activated) {
			List<EntityLivingBase> ents = core.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			AxisAlignedBB bite = this.getBiteBounds(core);
			for(EntityLivingBase ent : ents) {
				if(Math.random()<.6) {
					float damage = 1;
					if(ent.boundingBox.intersectsWith(bite))
						damage+=3;
					ent.fallDistance=0;
					if(ent instanceof EntityLiving)
						captureDrops=true;
					ent.attackEntityFrom(acid, damage);
					captureDrops=false;
					if(!capturedDrops.isEmpty()) {
						for(EntityItem drop : capturedDrops) {
							ItemStack stack = drop.getEntityItem();
							ItemStack toAdd = stack.copy();
							toAdd.stackSize=1;
							for(int i = 0 ; i < stack.stackSize ; i ++)
								data.drops.add(toAdd.copy());
						}
						capturedDrops.clear();
					}
				}
			}
			if(data.drops.size()>0) {
				ItemStack stack = data.drops.get(0);
				ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(stack);
				boolean isBone = stack.getItem() == Items.bone;
				boolean isFood = stack.getItem() instanceof ItemFood;
				boolean isSmeltedFood = smelted==null ? false : smelted.getItem() instanceof ItemFood;
				int levelUnsmelted = 0;
				int levelSmelted = 0;
				float satUnsmelted = 0F;
				float satSmelted = 0F;
				if(isBone)
					data.bonemealLevel+=3*300;
				if(isFood) {
					ItemFood item = (ItemFood) stack.getItem();
					levelUnsmelted = item.func_150905_g(stack);
					satUnsmelted = item.func_150906_h(stack);
				}
				if(isSmeltedFood) {
					ItemFood item = (ItemFood) smelted.getItem();
					levelSmelted = item.func_150905_g(smelted);
					satSmelted = item.func_150906_h(smelted);
				}
				if(stack.getItem() == Items.spider_eye) {
					
				}
				int mana = levelUnsmelted*levelUnsmelted*(64+16*levelSmelted);
				if(data.bonemealLevel>0) {
					mana = (int) (mana * (1F+Math.min(5, data.bonemealLevel/300F)));
					data.bonemealLevel=Math.max(0, data.bonemealLevel-30);
				}
				data.currentMana=Math.min(mana+data.currentMana, maxMana);
				data.drops.remove(0);
			}
		}
		if(data.currentMana>0) {
			ForgeDirection[] directions = new ForgeDirection[] {
					ForgeDirection.NORTH,
					ForgeDirection.EAST,
					ForgeDirection.SOUTH,
					ForgeDirection.WEST,
			};
			int tempMana = data.currentMana;
			for(ForgeDirection dir : directions) {
				int x = core.xCoord + dir.offsetX;
				int y = core.yCoord + dir.offsetY;
				int z = core.zCoord + dir.offsetZ;
				
				TileEntity tile = core.getWorldObj().getTileEntity(x, y, z);
				if(tile instanceof IManaPool) {
					IManaPool pool = (IManaPool) tile;
					int mana = Math.min(data.currentMana, Math.max(100, tempMana / 10));
					pool.recieveMana(mana);
					data.currentMana-=mana;
					core.getWorldObj().markBlockForUpdate(x,y,z);
				}
			}
		}
	}



	private boolean checkForWater(TileMultiblockCore core) {
		for(int i = 0; i < 3 ; i++) {
			for( int j = 0 ; j < 3 ; j++) {
				for( int k = 0 ; k < 2 ; k++) {
					int[] coords = getTransformedCoords(core, k+1, i+1, j+1);
					if(core.getWorldObj().getBlock(coords[0], coords[1],coords[2])!=Blocks.water)
						return false;
				}
			}
		}
		return true;
	}

	private void clearWater(TileMultiblockCore core) {
		for(int i = 0; i < 3 ; i++) {
			for( int j = 0 ; j < 3 ; j++) {
				for( int k = 0 ; k < 2 ; k++) {
					int[] coords = getTransformedCoords(core, k+1, i+1, j+1);
					core.getWorldObj().setBlockToAir(coords[0], coords[1],coords[2]);
					setBlock(BlockData.MULTIBLOCK_NO_RENDER_WATER, core, core.getWorldObj(), coords[0], coords[1], coords[2]);
				}
			}
		}
		core.markForRefresh();
	}

	private AxisAlignedBB getWaterBounds(TileMultiblockCore core) {
		int[] coords1 = getTransformedCoords(core, 1, 1, 1);
		int[] coords2 = getTransformedCoords(core, 2, 3, 3);
		int minX=Math.min(coords1[0], coords2[0]);
		int minY=Math.min(coords1[1], coords2[1]);
		int minZ=Math.min(coords1[2], coords2[2]);
		int maxX=Math.max(coords1[0], coords2[0]);
		int maxY=Math.max(coords1[1], coords2[1]);
		int maxZ=Math.max(coords1[2], coords2[2]);
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
	}

	private AxisAlignedBB getBiteBounds(TileMultiblockCore core) {
		int[] coords1 = getTransformedCoords(core, 1, 1, 1);
		int[] coords2 = getTransformedCoords(core, 1, 3, 3);
		int minX=Math.min(coords1[0], coords2[0]);
		int minY=Math.min(coords1[1], coords2[1]);
		int minZ=Math.min(coords1[2], coords2[2]);
		int maxX=Math.max(coords1[0], coords2[0]);
		double maxY=Math.max(coords1[1], coords2[1])+.1;
		int maxZ=Math.max(coords1[2], coords2[2]);
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
	}
	
	@Override
	public void onCollision(TileMultiblockBase tile,
			TileMultiblockCore core, Entity entity) {
		// NO OP
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		int[] coords = this.convertRelativeCoords(core, tile);
		IIcon outside = null;
		if(tile.getBlockType().getMaterial().equals(Material.water)) {
			tile.iconsForSides=new IIcon[]{iconAcid,iconAcid,iconAcid,iconAcid,iconAcid,iconAcid};
		}
		else if(tile.getOriginalBlock()==vazkii.botania.common.block.ModBlocks.livingwood) {
			tile.iconsForSides=new IIcon[]{iconStem,iconStem,iconStem,iconStem,iconStem,iconStem};
		}
		else if(tile.getOriginalBlock()==vazkii.botania.common.block.ModBlocks.petalBlock) {
			int innerFace = -1;
			if(coords[2] == 0)
				switch(core.rotation) {
				case 0: innerFace = 3;break;
				case 1: innerFace = 5;break;
				case 2: innerFace = 2;break;
				case 3: innerFace = 4;break;
				}
			else if(coords[2] == 4)
				switch(core.rotation) {
				case 0: innerFace = 2;break;
				case 1: innerFace = 4;break;
				case 2: innerFace = 3;break;
				case 3: innerFace = 5;break;
				}
			else if(coords[1] == 0)
				switch(core.rotation) {
				case 0: innerFace = 5;break;
				case 1: innerFace = 2;break;
				case 2: innerFace = 4;break;
				case 3: innerFace = 3;break;
				}
			else if(coords[1] == 4)
				switch(core.rotation) {
				case 0: innerFace = 4;break;
				case 1: innerFace = 3;break;
				case 2: innerFace = 5;break;
				case 3: innerFace = 2;break;
				}
			else {
				innerFace = 1;
			}
			tile.iconsForSides=new IIcon[]{iconSkin,iconSkin,iconSkin,iconSkin,iconSkin,iconSkin};
			if(innerFace!=-1) {
				IIcon inside = iconInside;
				if(tile.getOriginalMeta()==15) {
					if(coords[0]==6 && coords[1] == 3)
						inside = iconTransition[0];
					else if(coords[0]==6 && coords[1] == 2)
						inside = iconTransition[1];
					else if(coords[0]==6 && coords[1] == 1)
						inside = iconTransition[2];
					else if(coords[1] == 3)
						inside = iconTransition[3];
					else if(coords[1] == 1)
						inside = iconTransition[4];
				}
				else if(coords[0] == 3) {
					if(coords[2]==4 && coords[1] == 3)
						inside = iconTransition[5];
					else if(coords[2]==4 && coords[1] == 1)
						inside = iconTransition[6];
					else if(coords[2]!=4)
						inside = iconTransition[1];
						
				}
				tile.iconsForSides[innerFace]=inside;
			}
		}
		else if(tile.getOriginalBlock()==ModBlocks.manaEater) {
			int[] cCoords = getTransformedCoords(core, 1, 2, 2);
			int dx = cCoords[0] - tile.xCoord;
			int dz = cCoords[2] - tile.zCoord;
			int slot = (-dx+1)+(-dz+1)*3;
			tile.iconsForSides=new IIcon[]{iconSkin,iconTeethInner[slot],iconSkin,iconSkin,iconSkin,iconSkin};
		}
	}

    @Override
	public void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		if(tile.getBlockType().getMaterial().equals(Material.water)) {
			tile.solid=false;
			tile.lightValue=15;
			int[] coords = this.convertRelativeCoords(core, tile);
			if(coords[0]==2)
				tile.maxBBY=15F/16F;
		}
		if(tile.getOriginalBlock() == Blocks.iron_bars) {
			tile.solid=false;
			tile.maxBBY=.5F;
		}
		if(tile.getOriginalBlock()==vazkii.botania.common.block.ModBlocks.petalBlock || tile.getOriginalBlock() == Blocks.iron_bars) {
			int[] coords = this.convertRelativeCoords(core, tile);
			int innerFace = -1;
			if(coords[2] == 0)
				switch(core.rotation) {
				case 0: innerFace = 3;break;
				case 1: innerFace = 5;break;
				case 2: innerFace = 2;break;
				case 3: innerFace = 4;break;
				}
			else if(coords[2] == 4)
				innerFace = -1;
			else if(coords[1] == 0)
				switch(core.rotation) {
				case 0: innerFace = 5;break;
				case 1: innerFace = 2;break;
				case 2: innerFace = 4;break;
				case 3: innerFace = 3;break;
				}
			else if(coords[1] == 4)
				switch(core.rotation) {
				case 0: innerFace = 4;break;
				case 1: innerFace = 3;break;
				case 2: innerFace = 5;break;
				case 3: innerFace = 2;break;
				}
			if(innerFace!=-1) {
				ForgeDirection dir = ForgeDirection.values()[innerFace];
				tile.minBBX=dir.offsetX>0 ? .5F : 0F;
				tile.minBBZ=dir.offsetZ>0 ? .5F : 0F;
				tile.maxBBX=dir.offsetX<0 ? .5F : 1F;
				tile.maxBBZ=dir.offsetZ<0 ? .5F : 1F;
			}
		}
	}



	@Override
	public void init(TileMultiblockCore core) {
		// NO OP
	}


	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core,
			EntityPlayer player, ItemStack stack) {
		if(player == null)
			return;

		if(!core.getWorldObj().isRemote) {
			CarnilotusTileData data = (CarnilotusTileData) core.tileData;
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			core.writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(data.TAG_KNOWN_MANA, data.currentMana);
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(core.xCoord, core.yCoord, core.zCoord, -999, nbttagcompound));
		}

		core.getWorldObj().playSoundAtEntity(player, "botania:ding", 0.11F, 1F);
	}


	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core,
			Minecraft mc, ScaledResolution res) {
		CarnilotusTileData data = (CarnilotusTileData) core.tileData;
		String name = StatCollector.translateToLocal(LibResources.PREFIX_MOD + this.getUnlocalizedName() + ".name");
		int color = 0x007eff;
		HUDHandler.drawSimpleManaHUD(color, data.knownMana, maxMana, name, res);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}



	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.CARNILOTUS;
	}

	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconSkin = par1IconRegister.registerIcon(LibResources.CARNILOTUS_SKIN);
		iconStem = par1IconRegister.registerIcon(LibResources.CARNILOTUS_STEM);
		iconInside = par1IconRegister.registerIcon(LibResources.CARNILOTUS_INSIDE);
		iconAcid = par1IconRegister.registerIcon(LibResources.CARNILOTUS_ACID);
		iconTeethLip = par1IconRegister.registerIcon(LibResources.CARNILOTUS_TEETH_LIP);
		for(int i = 0 ; i < 9 ; i++)
			iconTeethInner[i] = par1IconRegister.registerIcon(LibResources.CARNILOTUS_TEETH_INNER+i);
		for(int i = 0 ; i < 7 ; i++)
			iconTransition[i] = par1IconRegister.registerIcon(LibResources.CARNILOTUS_TRANSITION+i);
	}

	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) { return !mirrorX && !mirrorZ; }



	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.carnilotus;
	}
}
