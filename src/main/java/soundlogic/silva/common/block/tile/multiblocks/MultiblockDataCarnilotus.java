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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
	
	private ArrayList<EntityItem> trackedItems = new ArrayList<EntityItem>();
	private int maxMana = 20000;
	
	IIcon iconSkin;
	IIcon iconStem;
	IIcon iconInside;
	
	public MultiblockDataCarnilotus() {
		super(new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0));
		BlockData core = new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0);
		BlockData livingwood = new BlockData(vazkii.botania.common.block.ModBlocks.livingwood,0);
		BlockData purplePetal = new BlockData(vazkii.botania.common.block.ModBlocks.petalBlock,10);
		BlockData blackPetal = new BlockData(vazkii.botania.common.block.ModBlocks.petalBlock,15);
		BlockData ironBar = new BlockData(Blocks.iron_bars,0);
		BlockData manaEater = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				if(!(world.getBlock(x, y, z) instanceof BlockManaEater))
					return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(!(tile instanceof TileManaEater))
					return false;
				TileManaEater eater = (TileManaEater) tile;
				return Math.abs(eater.rotationY % 360 - 90 ) < 20;
			}
			public void setBlock(World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.manaEater,0,0);
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
		proxyLocations = new boolean[][][] {
				{{false, false, false, false, false,false},
				 {false, true, true, true, false, false},
				 {false, true, true, true, false, true},
				 {false, true, true, true, false, false},
				 {false, false, false, false, false, false}},
					
				{{false, true, true, true, false, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, true},
				 {true, false, false, false, true, false},
				 {false, true, true, true, false, false}},
					
				{{false, true, true, true, false, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {false, true, true, true, false, false}},
					
				{{false, true, true, true, false, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {false, true, true, true, false, false}},
					
				{{false, true, true, true, false, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {true, false, false, false, true, false},
				 {false, true, true, true, false, false}},
						
				{{false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false}},
				 
				{{false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false},
				 {false, false, false, false, true, false}},
		};
		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK_CORE},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD,BlockData.WILDCARD}},
						
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
	List<EntityItem> capturedDrops;
	
	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if(!captureDrops)
			return;
		capturedDrops=event.drops;
		event.setCanceled(true);
	}
	

	@Override
	public void onTick(TileMultiblockCore core) {
		boolean hasWater=checkForWater(core);
		CarnilotusTileData data = (CarnilotusTileData) core.tileData;
		if(hasWater) {
			AxisAlignedBB aabb = getWaterBounds(core);
			if(!data.activated) {
				List<EntityItem> items = core.getWorldObj().getEntitiesWithinAABB(EntityItem.class, aabb);
				for(EntityItem ent : items) {
					ItemStack stack = ent.getEntityItem();
					int count = stack.stackSize;
					int meta = stack.getItemDamage();
					int toRemove = 0;
					System.out.println(stack);
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
				}
			}
			else {
				List<EntityLiving> ents = core.getWorldObj().getEntitiesWithinAABB(EntityLiving.class, aabb);
				for(EntityLiving ent : ents) {
					if(Math.random()<.2) {
						captureDrops=true;
						ent.attackEntityFrom(DamageSource.magic, 1);
						captureDrops=false;
						if(ent.isDead) {
							for(EntityItem drop : capturedDrops) {
								ItemStack stack = drop.getEntityItem();
								ItemStack toAdd = stack.copy();
								toAdd.stackSize=1;
								for(int i = 0 ; i < stack.stackSize ; i ++)
									data.drops.add(toAdd.copy());
							}
						}
					}
				}
				if(data.drops.size()>0) {
					ItemStack stack = data.drops.get(0);
					ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(stack);
					boolean isBone = stack.getItem() == Items.bone;
					boolean isFood = stack.getItem() instanceof ItemFood;
					boolean isSmeltedFood = smelted.getItem() instanceof ItemFood;
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
					int mana = levelUnsmelted+levelUnsmelted*(64+16*levelSmelted);
					if(data.bonemealLevel>0) {
						mana = (int) (mana * (1F+Math.min(5, data.bonemealLevel/300F)));
						data.bonemealLevel=Math.max(0, data.bonemealLevel-30);
					}
					data.currentMana=Math.min(mana+data.currentMana, maxMana);
					data.drops.remove(0);
				}
			}
		}
/*		int lavaX = core.xCoord;
		int lavaY = core.yCoord+4;
		int lavaZ = core.zCoord;
		LavashroomTileData data = (LavashroomTileData) core.tileData;
		Block block = core.getWorldObj().getBlock(lavaX, lavaY, lavaZ);
		if(data.activated && block == Blocks.lava) {
			if(data.lavaAmount<=maxLava-1000) {
				data.lavaAmount += 1000;
				if(data.lavaAmount==1000)
					core.markForVisualUpdate();
				core.getWorldObj().setBlockToAir(lavaX, lavaY, lavaZ);
			}
		}
		else if(!data.activated && block == Blocks.red_mushroom_block) {
			core.getWorldObj().setBlockToAir(lavaX, lavaY, lavaZ);
		}
		else if(!data.activated && block == Blocks.lava) {
			AxisAlignedBB aabb= AxisAlignedBB.getBoundingBox(lavaX,lavaY,lavaZ,lavaX+1,lavaY+1,lavaZ+1);
			List<EntityItem> items = core.getWorldObj().getEntitiesWithinAABB(EntityItem.class, aabb);
			for(EntityItem ent : items) {
				ItemStack stack = ent.getEntityItem();
				if(stack.getItem() != vazkii.botania.common.item.ModItems.rune)
					continue;
				int count = stack.stackSize;
				int meta = stack.getItemDamage();
				int toRemove = 0;
				if(meta == 1) {
					toRemove = Math.min(count, data.fireRunesNeeded);
					data.fireRunesNeeded-=toRemove;
				}
				else if (meta == 2) {
					toRemove = Math.min(count, data.earthRunesNeeded);
					data.earthRunesNeeded-=toRemove;
				}
				else if (meta == 8) {
					toRemove = Math.min(count, data.manaRunesNeeded);
					data.manaRunesNeeded-=toRemove;
				}
				else if (meta == 13) {
					toRemove = Math.min(count, data.wrathRunesNeeded);
					data.wrathRunesNeeded-=toRemove;
				}
				stack.stackSize-=toRemove;
			}
			if(data.fireRunesNeeded == 0 && data.earthRunesNeeded == 0 && data.manaRunesNeeded == 0 && data.wrathRunesNeeded == 0) {
				data.activated=true;
			}
		}
		if(data.activated) {
			if(data.lavaAmount>0) {
				data.lavaAmount = Math.max(0, data.lavaAmount-2);
				if(data.currentMana <= maxMana) {
					if(data.fuels.size()>0 && data.lavaAmount>0) {
						data.lavaAmount = Math.max(0, data.lavaAmount-10);
						data.fuelTicks+=100;
						data.currentMana= Math.min(maxMana, data.currentMana + data.fuels.get(0)/20);
						if(data.fuelTicks>=data.fuels.get(0))
							data.fuels.remove(0);
					}
				}
				if(data.lavaAmount==0) {
					core.markForVisualUpdate();
				}
			}
			else {
				data.fuels.clear();
				data.fuelTicks=0;
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
		}*/
	}



	private boolean checkForWater(TileMultiblockCore core) {
		for(int i = 0; i < 3 ; i++) {
			for( int j = 0 ; j < 3 ; j++) {
				int[] coords = getTransformedCoords(core, 1, i+1, j+1);
				if(core.getWorldObj().getBlock(coords[0], coords[1],coords[2])!=Blocks.water)
					return false;
			}
		}
		return true;
	}

	private AxisAlignedBB getWaterBounds(TileMultiblockCore core) {
		int[] coords1 = getTransformedCoords(core, 1, 1, 1);
		int[] coords2 = getTransformedCoords(core, 1, 3, 3);
		int minX=Math.min(coords1[0], coords2[0]);
		int minY=Math.min(coords1[1], coords2[1]);
		int minZ=Math.min(coords1[2], coords2[2]);
		int maxX=Math.max(coords1[0], coords2[0]);
		int maxY=Math.max(coords1[1], coords2[1]);
		int maxZ=Math.max(coords1[2], coords2[2]);
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX+1, maxY+1, maxZ+1);
	}

	@Override
	public void onCollision(TileMultiblockBase tile,
			TileMultiblockCore core, Entity entity) {
		//NO OP
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		int[] coords = this.convertRelativeCoords(core, tile);
		IIcon outside = null;
		IIcon[] genIcons = new IIcon[] {
				Blocks.iron_ore.getIcon(0, 0),
				Blocks.redstone_block.getIcon(0, 0),
				Blocks.redstone_lamp.getIcon(0, 0),
				Blocks.iron_block.getIcon(0, 0),
				Blocks.bookshelf.getIcon(0, 0),
				Blocks.tnt.getIcon(0, 0),
				};
		if(tile.getOriginalBlock()==vazkii.botania.common.block.ModBlocks.livingwood) {
			outside = iconStem;
		}
		else if(tile.getOriginalBlock()==vazkii.botania.common.block.ModBlocks.petalBlock) {
			outside = iconSkin;
		}
		if(outside!=null) {
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
			tile.iconsForSides=new IIcon[]{outside,outside,outside,outside,outside,outside};
			if(innerFace!=-1)
				tile.iconsForSides[innerFace]=iconInside;
		}
//		tile.iconsForSides=genIcons;
/*		LavashroomTileData data = (LavashroomTileData) core.tileData;
		IIcon icon = data.lavaAmount > 0 ? iconLava : iconDullLava;
		IIcon[] lavaIcons = new IIcon[] {icon,icon,icon,icon,icon,icon};
		IIcon skinNonEdgeIcon = data.lavaAmount > 0 ? iconMushroom_skin : iconMushroom_dullSkin;
		IIcon skinEdgeIcon = data.lavaAmount > 0 ? iconMushroom_skinEdge : iconMushroom_dullSkinEdge;
		int lavaLight = data.lavaAmount > 0 ? 15 : 10;
		if(y<=1 && (x!=0 || z!=0)) {
			tile.iconsForSides=lavaIcons;
			tile.lightValue = lavaLight;
		}
		else if (x != 0 || y != 0 || z != 0){
			for(int side = 0 ; side < 6 ; side++) {
				int meta = tile.getOriginalMeta();
				IIcon skinIcon = skinNonEdgeIcon;
				IIcon insideIcon = this.iconMushroom_inside;
				if(y==2) {
					skinIcon=skinEdgeIcon;
					insideIcon=data.lavaAmount> 0 ? this.iconMushroom_inside_edge : this.iconMushroom_inside_edge_dull;
				}
				IIcon current = ( meta == 10 && side > 1 ? iconMushroom_stem : (meta >= 1 && meta <= 9 && side == 1 ? skinIcon : (meta >= 1 && meta <= 3 && side == 2 ? skinIcon : (meta >= 7 && meta <= 9 && side == 3 ? skinIcon : ((meta == 1 || meta == 4 || meta == 7) && side == 4 ? skinIcon : ((meta == 3 || meta == 6 || meta == 9) && side == 5 ? skinIcon : (meta == 14 ? skinIcon : (meta == 15 ? iconMushroom_stem : insideIcon))))))));
				tile.iconsForSides[side]=current;
			}
		}
		else {
			for(int side = 0 ; side < 6 ; side++) {
				int meta = 10;
				IIcon skinIcon = skinNonEdgeIcon;
				IIcon current = ( meta == 10 && side > 1 ? iconMushroom_stem : (meta >= 1 && meta <= 9 && side == 1 ? skinIcon : (meta >= 1 && meta <= 3 && side == 2 ? skinIcon : (meta >= 7 && meta <= 9 && side == 3 ? skinIcon : ((meta == 1 || meta == 4 || meta == 7) && side == 4 ? skinIcon : ((meta == 3 || meta == 6 || meta == 9) && side == 5 ? skinIcon : (meta == 14 ? skinIcon : (meta == 15 ? iconMushroom_stem : iconMushroom_inside))))))));
				tile.iconsForSides[side]=current;
			}
		}*/
	}

    @Override
	public void onLoad(TileMultiblockCore core) {
/*		LavashroomTileData data = (LavashroomTileData) core.tileData;
		for(TileMultiblockProxy proxy : core.proxies) {
			int[] pos = proxy.getRelativePos();
			int x = pos[0];
			int y = pos[1];
			int z = pos[2];
			if(y<=2 && (x!=0 || z!=0)) {
				proxy.solid=false;
			}
		}*/
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
	}



	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) { return !mirrorX && !mirrorZ; }



	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.lavaShroom;
	}
}
