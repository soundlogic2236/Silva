package soundlogic.silva.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.DustHandler;
import soundlogic.silva.common.entity.EntityEnderPearlRedirected;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.decor.IFloatingFlower;

public class SubTileGlitterelle extends SubTileFunctional {

	private final static int tickRate = 20*30;
	private final static int lightLevelEmitted = 14;
	private final static int lightLevelSpawn = 7;
	private final static int spawnRange = 7;
	private final static int searchRange = 14;
	private final static int yRange = 1;
	private final static int manaForSpawn = 5000;
	private final static int maxMana = 1000000;
	private final static int time_midnight = 18000;
	private final static int time_day = 24000;
	
	private static final boolean[] rule_live = new boolean[]{
		false, //0
		false, //1
		true,  //2
		true,  //3
		false, //4
		false, //5
		false, //6
		false, //7
		false, //8
	};

	private static final boolean[] rule_spawn = new boolean[]{
		false, //0
		false, //1
		false, //2
		true,  //3
		false, //4
		false, //5
		false, //6
		false, //7
		false, //8
	};
	
	private static final int[][] spawn_search_offsets_1d = new int[][] {
		{ 1, 0,-1},
		{ 0, 0,-1},
		{-1, 0,-1},
		{ 1, 0, 0},
		{-1, 0, 0},
		{ 1, 0, 1},
		{ 0, 0, 1},
		{-1, 0, 1}
	};

	private static final int[][] spawn_search_offsets = new int[][] {
		{ 1,-1,-1},
		{ 0,-1,-1},
		{-1,-1,-1},
		{ 1,-1, 0},
		{-1,-1, 0},
		{ 1,-1, 1},
		{ 0,-1, 1},
		{-1,-1, 1},

		{ 1, 0,-1},
		{ 0, 0,-1},
		{-1, 0,-1},
		{ 1, 0, 0},
		{-1, 0, 0},
		{ 1, 0, 1},
		{ 0, 0, 1},
		{-1, 0, 1},

		{ 1, 1,-1},
		{ 0, 1,-1},
		{-1, 1,-1},
		{ 1, 1, 0},
		{-1, 1, 0},
		{ 1, 1, 1},
		{ 0, 1, 1},
		{-1, 1, 1},
	};
	
	ForgeDirection[] mainDirs = new ForgeDirection[] {
			ForgeDirection.NORTH,
			ForgeDirection.SOUTH,
			ForgeDirection.EAST,
			ForgeDirection.WEST,
	};
	
	private static final String TAG_ACTIVATED = "activated";
	private static final String TAG_ALIVE = "alive";
	private static final String TAG_ALIVE_PREVTICK = "alivePrevTick";
	private static final String TAG_ALIVE_PREV2TICK = "alivePrev2Tick";
	private static final String TAG_ALIVE_MIDNIGHT = "aliveMidnight";
	
	boolean activated=false;
	boolean alive=false;
	boolean alivePrevTick=false;
	boolean alivePrev2Tick=false;
	boolean aliveSinceMidnight=true;
	int ticksSearched=0;
	List<SubTileGlitterelle> nearby = new ArrayList<SubTileGlitterelle>();
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		World world = supertile.getWorldObj();
		if(world.isRemote)
			return;
		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;
		long time = world.getWorldTime();
		boolean midnight = (time%time_day)==time_midnight;
		boolean premidnight = time_midnight-(time%time_day)<10;
		if(premidnight) {
			if(!activated) {
				alivePrevTick=true;
				alivePrev2Tick=true;
			}
		}
		if(midnight) {
			if(!activated) {
				alive=true;
				alivePrevTick=true;
				alivePrev2Tick=true;
				aliveSinceMidnight=true;
			}
			activated=true;
			if(!aliveSinceMidnight)
				world.setBlockToAir(x, y, z);
			aliveSinceMidnight=false;
		}
		
		aliveSinceMidnight = aliveSinceMidnight || alive;
		
		if(!activated) {
			world.setBlock(x, y+1, z, Blocks.air, 0, 3);
		}
		else if(alive) {
			world.setBlock(x, y+1, z, Blocks.glowstone, 0, 3);
		}
		else {
			world.setBlock(x, y+1, z, Blocks.stone, 0, 3);
		}
		
		alivePrev2Tick=alivePrevTick;
		alivePrevTick=alive;
		
		int[] coords = searchTick(world,x,y,z,ticksSearched);
		ticksSearched++;
		for(int i=0;i<2;i++) {
			if(coords!=null) {
				coords = searchTick(world,x,y,z,ticksSearched);
				ticksSearched++;
			}
		}
		
		if(!activated)
			return;

		boolean forceAlive=false;
		forceAlive = world.getBlock(x, y-1, z)==Blocks.glowstone;
		
		if(time % tickRate==0) {
			System.out.println("Ticking Flower!");
			if(!forceAlive) {
				while(coords!=null) {
					coords = searchTick(world,x,y,z,ticksSearched);
					ticksSearched++;
				}
				nearby.clear();
				for(int[] offset2 : spawn_search_offsets) {
					int sx = x+offset2[0]*spawnRange;
					int sy = y+offset2[1];
					int sz = z+offset2[2]*spawnRange;
					TileEntity tile = world.getTileEntity(sx, sy, sz);
					if(tile!=null && tile instanceof ISubTileContainer) {
						SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
						if(subtile instanceof SubTileGlitterelle)
							nearby.add((SubTileGlitterelle) subtile);
					}
				}
				int livingCount = 0;
				for(SubTileGlitterelle tile : nearby) {
					if(tile.isAlive())
						livingCount++;
				}
				System.out.println(livingCount);
				if(alive)
					alive=rule_live[livingCount%rule_live.length];
				else
					alive=rule_spawn[livingCount%rule_spawn.length];
			}
			else
				alive=true;
			if(isAlive() && (mana>=manaForSpawn || false )) {
				for(int[] offset : spawn_search_offsets_1d) {
					boolean foundFlower=false;
					int foundY = 0;
					for(int i = -yRange ; i<=yRange ; i++) {
						int nx=x+offset[0]*spawnRange;
						int ny=y+i;
						int nz=z+offset[2]*spawnRange;
						TileEntity tile = world.getTileEntity(nx, ny, nz);
						if(tile!=null && tile instanceof ISubTileContainer) {
							SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
							if(subtile instanceof SubTileGlitterelle) {
								foundFlower=true;
								break;
							}
						}
					}
					System.out.println(foundFlower);
					if(!foundFlower) {
						for(int i = -yRange ; i<=yRange ; i++) {
							int nx=x+offset[0]*spawnRange;
							int ny=y+i;
							int nz=z+offset[2]*spawnRange;
							Block block = world.getBlock(nx, ny, nz);
							if(block.isAir(world, nx, ny, nz) || block.isReplaceable(world, nx, ny, nz)) {
								int livingCount2=0;
								System.out.println("Searching for flower spawn");
								for(int[] offset2 : spawn_search_offsets) {
									int sx = nx+offset2[0]*spawnRange;
									int sy = ny+offset2[1];
									int sz = nz+offset2[2]*spawnRange;
									TileEntity tile = world.getTileEntity(sx, sy, sz);
									if(tile!=null && tile instanceof ISubTileContainer) {
										SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
										if(subtile instanceof SubTileGlitterelle) {
											System.out.println(subtile);
											if(((SubTileGlitterelle) subtile).isAlive())
												livingCount2++;
										}
									}
								}
								System.out.println(livingCount2);
								if(rule_spawn[livingCount2%rule_spawn.length]) {
									Block myBlock = world.getBlock(x, y, z);
									int myMeta = world.getBlockMetadata(x, y, z);
									
									world.setBlock(nx, ny, nz, myBlock, myMeta, 3);
									TileEntity tile = world.getTileEntity(nx, ny, nz);
									((ISubTileContainer)tile).setSubTile(LibBlockNames.SUBTILE_GLITTERELLE);
									((SubTileGlitterelle)(((ISubTileContainer)tile).getSubTile())).setSpawned();
									break;
								}
							}
						}
					}
				}
			}
			nearby.clear();
			ticksSearched=0;
		}
		if(mana>getMaxMana()) {
			for(int[] offset : spawn_search_offsets) {
				int nx=x+offset[0]*spawnRange;
				int ny=y+offset[1];
				int nz=z+offset[2]*spawnRange;
				TileEntity tile = world.getTileEntity(nx, ny, nz);
				if(tile!=null && tile instanceof ISubTileContainer) {
					SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
					if(subtile instanceof SubTileGlitterelle) {
						SubTileGlitterelle glit = (SubTileGlitterelle)subtile;
						int manaToAdd = Math.max(0, glit.getMaxMana()-glit.mana);
						glit.mana+=manaToAdd;
						mana-=manaToAdd;
					}
				}
			}
			mana=getMaxMana();
		}
	}
	
	private int[] searchTick(World world, int x, int y, int z, int tick) {
		int[] coords = convertTickToSearchCoords(tick);
		if(coords!=null) {
			int sx=x+coords[0];
			int sy=y+coords[1];
			int sz=z+coords[2];
			TileEntity tile = world.getTileEntity(sx, sy, sz);
			if(tile!=null && tile instanceof ISubTileContainer) {
				SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
				if(subtile instanceof SubTileGlitterelle)
					nearby.add((SubTileGlitterelle) subtile);
			}
		}
		return coords;
	}
	
	private int[] convertTickToSearchCoords(int tick) {
		int depth = yRange*2+1;
		int y = (tick%depth)-yRange;
		if(tick<(depth-1)) {
			return new int[] {
				0,
				y+(tick<yRange?0:1),
				0
			};
		}
		int remTicks = (tick-depth)/depth;
		
		int ticksSign = (remTicks%2)*2-1;

		if(remTicks<searchRange*searchRange*2) {
			int triangleTicks = remTicks/2;
			
			int row = (int) Math.floor(Math.sqrt(triangleTicks));
			int column = triangleTicks - row*row-row;
			return new int[] {
					(searchRange-row)*ticksSign,
					y,
					column,
			};
		}
		else if(tick<(2*searchRange*searchRange+2*searchRange+1)*depth) {
			int lineTicks = (remTicks-searchRange*searchRange*2)/2+1;
			return new int[] {
					0,
					y,
					lineTicks * ticksSign
			};
		}
		else
			return null;
	}
	
	public boolean isAlive() {
		return alivePrev2Tick && !supertile.isInvalid();
	}
	public void setSpawned() {
		activated=true;
		alive=true;
		alivePrevTick=false;
		alivePrev2Tick=false;
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.glitterelle;
	}

	@Override
	public int getMaxMana() {
		return alive || !activated ? maxMana : 0;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setBoolean(TAG_ACTIVATED, activated);
		cmp.setBoolean(TAG_ALIVE, alive);
		cmp.setBoolean(TAG_ALIVE_MIDNIGHT, aliveSinceMidnight);
		cmp.setBoolean(TAG_ALIVE_PREV2TICK, alivePrev2Tick);
		cmp.setBoolean(TAG_ALIVE_PREVTICK, alivePrevTick);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		activated = cmp.getBoolean(TAG_ACTIVATED);
		alive = cmp.getBoolean(TAG_ALIVE);
		aliveSinceMidnight = cmp.getBoolean(TAG_ALIVE_MIDNIGHT);
		alivePrev2Tick = cmp.getBoolean(TAG_ALIVE_PREV2TICK);
		alivePrevTick = cmp.getBoolean(TAG_ALIVE_PREVTICK);
	}	
}
