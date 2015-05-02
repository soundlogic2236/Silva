package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soundlogic.silva.common.block.BlockMultiblockBase;
import soundlogic.silva.common.block.BlockMultiblockCore;
import soundlogic.silva.common.block.BlockMultiblockProxyLava;
import soundlogic.silva.common.block.ModBlocks;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class MultiblockDataBase {

	protected BlockData[][][] creationRequirementsTemplate;
	protected boolean[][][] proxyLocations;
	protected BlockData[][][] persistanceAndCreationBlocks;
	protected int[] templateOrigin;
	
	BlockData requiredBlock;
	
	public static HashMap<Block, List<MultiblockDataBase>> multiBlocks = new HashMap<Block, List<MultiblockDataBase>>();
	public static HashMap<String, MultiblockDataBase> multiBlocksByName = new HashMap<String, MultiblockDataBase> ();
	
	int targetProxyCount = -1;
	
	public MultiblockDataBase(BlockData rootBlock) {
		super();
		List<MultiblockDataBase> list;
		if(multiBlocks.containsKey(rootBlock.block))
			list=multiBlocks.get(rootBlock.block);
		else {
			list=new ArrayList<MultiblockDataBase>();
			multiBlocks.put(rootBlock.block, list);
		}
		list.add(this);
		this.requiredBlock=rootBlock;
		multiBlocksByName.put(this.getName(), this);
	}
	
	public boolean tryCreate(World world, int x, int y, int z) {
		int trial = 0;
		while(trial < 10) {
			boolean mirrorX = ( trial & 1 ) > 0;
			boolean mirrorZ = ( trial & 2 ) > 0;
			boolean rotation1 = ( trial & 4 ) > 0;
			boolean rotation2 = ( trial & 8 ) > 0;
			int rotation = ( rotation1 ? 1 : 0 ) + ( rotation2 ? 2 : 0 );
			if(shouldTryTransform(trial,mirrorX, mirrorZ, rotation)) {
				if(matchesTemplateForCreation(world,x,y,z,mirrorX,mirrorZ,rotation)) {
					create(world,x,y,z,mirrorX,mirrorZ,rotation);
					return true;
				}
			}
		}
		return false;
	}
		
	protected boolean matchesTemplateForPersistance(World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		for(int i = 0;i<proxyLocations.length;i++) {
			BlockData[][] templateSlice1=persistanceAndCreationBlocks[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] coords = getTransformedCoords(x,y,z,i,j,k,mirrorX,mirrorZ,rotation);
					if(!data.isValid(world, coords[0], coords[1], coords[2]))
						return false;
				}
			}
		}
		return true;
	}

	protected boolean matchesTemplateForCreation(World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		if(!requiredBlock.isValid(world, x, y, z))
			return false;
		for(int i = 0;i<creationRequirementsTemplate.length;i++) {
			BlockData[][] templateSlice1=creationRequirementsTemplate[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] transCoords = getTransformedCoords(x,y,z,i,j,k,mirrorX,mirrorZ,rotation);
					if(!data.isValid(world, transCoords[0], transCoords[1], transCoords[2]))
						return false;
				}
			}
		}
		return true;
	}

	public void create(World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		Block curBlock = world.getBlock(x,y,z);
		int curMeta = world.getBlockMetadata(x,y,z);
		world.setBlock(x,y,z, ModBlocks.multiblockCore,0,0);
		TileMultiblockCore core=(TileMultiblockCore)world.getTileEntity(x,y,z);
		core.setOriginalBlock(curBlock, curMeta);
		core.setData(this);
		core.mirrorX=mirrorX;
		core.mirrorZ=mirrorZ;
		core.rotation=rotation;
		core.markDirty();
		for(int i = 0;i<persistanceAndCreationBlocks.length;i++) {
			BlockData[][] templateSlice1=persistanceAndCreationBlocks[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] coords = getTransformedCoords(core,i,j,k);
					curBlock = world.getBlock(coords[0], coords[1], coords[2]);
					curMeta = world.getBlockMetadata(coords[0], coords[1], coords[2]);
					if(coords[0] != x || coords[1] != y || coords[2] != z) {
						data.setBlock(world, coords[0], coords[1], coords[2]);
						TileEntity tile = world.getTileEntity(coords[0], coords[1], coords[2]);
						if(tile!=null && tile instanceof TileMultiblockProxy) {
							TileMultiblockProxy proxy = (TileMultiblockProxy)world.getTileEntity(coords[0], coords[1], coords[2]);
							if(proxy!=null) {
								proxy.setOriginalBlock(curBlock, curMeta);
								proxy.relativeX=x-coords[0];
								proxy.relativeY=y-coords[1];
								proxy.relativeZ=z-coords[2];
								proxy.getCore();
								proxy.markDirty();
							}
						}
					}
				}
			}
		}
		init(core);
		markDirty(core);
		onLoad(core);
	}
	
	private void markDirty(TileMultiblockCore core) {
		World world = core.getWorldObj();
		for(int i = 0;i<proxyLocations.length;i++) {
			boolean[][] templateSlice1=proxyLocations[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				boolean[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					if(templateSlice2[k]) {
						int[] coords = getTransformedCoords(core,i,j,k);
						world.markBlockForUpdate(coords[0], coords[1], coords[2]);
					}
				}
			}
		}
	}
	
	private int[] getTransformedCoords(TileMultiblockCore core, int i, int j, int k) {
		return getTransformedCoords(core.xCoord,core.yCoord,core.zCoord,i,j,k,core.mirrorX,core.mirrorZ,core.rotation);
	}
	
	private int[] getTransformedCoords(int x, int y, int z, int i, int j, int k, boolean mirrorX, boolean mirrorZ, int rotation) {
		int[] targetOffset = getTargetOffset(i,j,k,mirrorX,mirrorZ,rotation);
		return new int[]{
			x+targetOffset[0],
			y+targetOffset[1],
			z+targetOffset[2]
		};
	}

	private int[] getTargetOffset(int i, int j, int k, boolean mirrorX, boolean mirrorZ, int rotation) {
		int[] offset = getSimpleOffset(i,j,k);
		if(mirrorX)
			offset[0]=-offset[0];
		if(mirrorZ)
			offset[2]=-offset[2];
		if(rotation>0)
			offset = rotate90(offset);
		if(rotation>1)
			offset = rotate90(offset);
		if(rotation>2)
			offset = rotate90(offset);
		return offset;
	}
	
	private int[] getSimpleOffset(int i, int j, int k) {
		return new int[] {
				j-templateOrigin[0],
				i-templateOrigin[1],
				k-templateOrigin[2],
			};
	}
	
	private int[] rotate90(int[] input) {
		return new int[] {
				input[2],
				input[1],
				-input[0]
		};
	}
	
	protected static class BlockData {
		Block block;
		int metadata;
		public static BlockData WILDCARD = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				return true;
			}
			public void setBlock(World world, int x, int y, int z) {
				//NO OP
			}
		};
		public static BlockData AIR = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				return world.isAirBlock(x, y, z);
			}
			public void setBlock(World world, int x, int y, int z) {
				world.setBlockToAir(x, y, z);
			}
		};
		
		public static BlockData MULTIBLOCK = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockBase;
			}
			public void setBlock(World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxy,0,0);
			}
		};

		public static BlockData MULTIBLOCK_CORE = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockCore;
			}
			public void setBlock(World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockCore,0,0);
			}
		};
		
		public static BlockData MULTIBLOCK_LAVA = new BlockData() {
			public boolean isValid(World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxyLava;
			}
			public void setBlock(World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxyLava,0,0);
			}
		};

		private BlockData()
		{
		}
		
		public BlockData(Block block, int meta) {
			this.block=block;
			this.metadata=meta;
		}
		
		public boolean isValid(World world, int x, int y, int z) {
			return world.getBlock(x, y, z)==block && world.getBlockMetadata(x, y, z)==metadata;
		}

		public void setBlock(World world, int x, int y, int z) {
			world.setBlock(x, y, z, block, metadata, 0);
		}
	}
	
	protected void checkProxyList(TileMultiblockCore core) {
		if(targetProxyCount==-1) {
			targetProxyCount=0;
			for(int i = 0;i<proxyLocations.length;i++) {
				boolean[][] templateSlice1=proxyLocations[i];
				for(int j = 0; j<templateSlice1.length;j++) {
					boolean[] templateSlice2=templateSlice1[j];
					for(int k = 0;k<templateSlice2.length;k++) {
						if(templateSlice2[k]) {
							targetProxyCount++;
						}
					}
				}
			}
		}
		if(core.proxies.size()!=targetProxyCount) {
			if(targetProxyCount==-1) {
				targetProxyCount=0;
				for(int i = 0;i<proxyLocations.length;i++) {
					boolean[][] templateSlice1=proxyLocations[i];
					for(int j = 0; j<templateSlice1.length;j++) {
						boolean[] templateSlice2=templateSlice1[j];
						for(int k = 0;k<templateSlice2.length;k++) {
							if(templateSlice2[k]) {
								int[] coords = getTransformedCoords(core,i,j,k);
								TileEntity tile = core.getWorldObj().getTileEntity(coords[0], coords[1], coords[2]);
								if(tile instanceof TileMultiblockProxy)
									((TileMultiblockProxy) tile).getCore();
							}
						}
					}
				}
			}
		}
	}
	
	public abstract String getName();
	
	public abstract IMultiblockTileData createTileData();
	
	public abstract void onTick(TileMultiblockCore core);
	public abstract void onCollision(TileMultiblockBase tile, TileMultiblockCore core, Entity ent);
	public abstract void init(TileMultiblockCore core);
	public abstract void onLoad(TileMultiblockCore core);
	public abstract void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z);
	public abstract void renderHUD(TileMultiblockBase tile, TileMultiblockCore core, Minecraft mc, ScaledResolution res);
	public abstract void onWanded(TileMultiblockBase tile, TileMultiblockCore core, EntityPlayer player, ItemStack stack);
	public abstract String getUnlocalizedName();

	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO OP
	}
	
	public abstract boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot);

	public abstract LexiconEntry getLexiconEntry();
	
}
