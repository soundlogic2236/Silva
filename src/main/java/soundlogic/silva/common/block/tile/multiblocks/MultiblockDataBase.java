package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soundlogic.silva.common.block.BlockMultiblockBase;
import soundlogic.silva.common.block.BlockMultiblockCore;
import soundlogic.silva.common.block.BlockMultiblockProxy;
import soundlogic.silva.common.block.BlockMultiblockProxyLava;
import soundlogic.silva.common.block.BlockMultiblockProxyNoRender;
import soundlogic.silva.common.block.BlockMultiblockProxyNoRenderWater;
import soundlogic.silva.common.block.BlockMultiblockProxyWater;
import soundlogic.silva.common.block.ModBlocks;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class MultiblockDataBase {

	protected BlockData[][][] creationRequirementsTemplate;
	protected BlockData[][][] persistanceAndCreationBlocks;
	protected int[] templateOrigin;
	
	BlockData requiredBlock;
	
	public static HashMap<Block, List<MultiblockDataBase>> multiBlocks = new HashMap<Block, List<MultiblockDataBase>>();
	public static HashMap<String, MultiblockDataBase> multiBlocksByName = new HashMap<String, MultiblockDataBase> ();
	
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
		while(trial < 16) {
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
			trial++;
		}
		return false;
	}
		
	protected boolean matchesTemplateForPersistance(TileMultiblockCore core, World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		for(int i = 0;i<persistanceAndCreationBlocks.length;i++) {
			BlockData[][] templateSlice1=persistanceAndCreationBlocks[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] coords = getTransformedCoords(x,y,z,i,j,k,mirrorX,mirrorZ,rotation);
					if(!data.isValid(core, world, coords[0], coords[1], coords[2])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean matchesTemplateForCreation(World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		if(!requiredBlock.isValid(null, world, x, y, z))
			return false;
		for(int i = 0;i<creationRequirementsTemplate.length;i++) {
			BlockData[][] templateSlice1=creationRequirementsTemplate[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] transCoords = getTransformedCoords(x,y,z,i,j,k,mirrorX,mirrorZ,rotation);
					if(!data.isValid(null, world, transCoords[0], transCoords[1], transCoords[2]))
						return false;
				}
			}
		}
		return true;
	}

	public void create(World world, int x, int y, int z, boolean mirrorX, boolean mirrorZ, int rotation) {
		Block curBlock = world.getBlock(x,y,z);
		int curMeta = world.getBlockMetadata(x,y,z);
		float curHardness = curBlock.getBlockHardness(world, x, y, z);
		TileEntity tempTile = world.getTileEntity(x, y, z);
		NBTTagCompound curCompound = null;
		if(tempTile!=null) {
			curCompound = new NBTTagCompound();
			tempTile.writeToNBT(curCompound);
		}
		world.setBlock(x,y,z, ModBlocks.multiblockCore,0,0);
		TileMultiblockCore core=(TileMultiblockCore)world.getTileEntity(x,y,z);
		core.setOriginalBlock(curBlock, curMeta, curCompound, curHardness);
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
					if(coords[0] != x || coords[1] != y || coords[2] != z) {
						setBlock(data, core, world, coords[0], coords[1], coords[2]);
					}
				}
			}
		}
		init(core);
	}
	
	public void setBlock(BlockData data, TileMultiblockCore core, World world, int x, int y, int z) {
		Block curBlock = world.getBlock(x, y, z);
		int curMeta = world.getBlockMetadata(x, y, z);
		float curHardness = curBlock.getBlockHardness(world, x, y, z);
		TileEntity tempTile = world.getTileEntity(x,y,z);
		NBTTagCompound curCompound = null;
		if(tempTile!=null) {
			curCompound = new NBTTagCompound();
			tempTile.writeToNBT(curCompound);
		}
		data.setBlock(core, world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile!=null && tile instanceof TileMultiblockProxy) {
			TileMultiblockProxy proxy = (TileMultiblockProxy)world.getTileEntity(x, y, z);
			if(proxy!=null) {
				proxy.setOriginalBlock(curBlock, curMeta, curCompound, curHardness);
				proxy.relativeX=core.xCoord-x;
				proxy.relativeY=core.yCoord-y;
				proxy.relativeZ=core.zCoord-z;
				proxy.getCore();
				proxy.markDirty();
			}
		}
	}
	
	protected int[] convertRelativeCoords(TileMultiblockCore core, TileMultiblockBase tile) {
		int[] relative = tile.getRelativePos();
		return convertRelativeCoords(core,relative[0],relative[1],relative[2]);
	}
	protected int[] convertRelativeCoords(TileMultiblockCore core, int x, int y, int z) {
		int[] offset = new int[]{x,y,z};
		if(core.rotation<1)
			offset = rotate90(offset);
		if(core.rotation<2)
			offset = rotate90(offset);
		if(core.rotation<3)
			offset = rotate90(offset);
		offset = rotate90(offset);
		if(core.mirrorX)
			offset[0]=-offset[0];
		if(core.mirrorZ)
			offset[2]=-offset[2];
		return  new int[]{
				templateOrigin[1]+offset[1],
				templateOrigin[0]+offset[0],
				templateOrigin[2]+offset[2],
		};
	}
	
	public int[] getTransformedCoords(TileMultiblockCore core, int i, int j, int k) {
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
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return true;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		public static BlockData AIR = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.isAirBlock(x, y, z);
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlockToAir(x, y, z);
			}
		};
		public static BlockData POOL = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile==null)
					return false;
				return tile instanceof IManaPool;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		};
		
		public static BlockData WATER = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore multiblock, World world, int x, int y, int z) {
				Block block = world.getBlock(x, y, z);
				return 	block==Blocks.water ||
						block==Blocks.flowing_water;
			}
			@Override
			public void setBlock(TileMultiblockCore multiblock, World world, int x, int y, int z) {
				world.setBlock(x, y, z, Blocks.water, 0, 0);
			}
		};
		
		public static BlockData MULTIBLOCK = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxy;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxy,0,0);
			}
		};

		public static BlockData MULTIBLOCK_CORE = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockCore;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockCore,0,0);
			}
		};
		
		public static BlockData MULTIBLOCK_LAVA = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxyLava;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxyLava,0,0);
			}
		};

		public static BlockData MULTIBLOCK_WATER = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxyWater;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxyWater,0,0);
			}
		};

		public static BlockData MULTIBLOCK_NO_RENDER = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxyNoRender;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxyNoRender,0,0);
			}
		};
		
		public static BlockData MULTIBLOCK_NO_RENDER_WATER = new BlockData() {
			@Override
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				return world.getBlock(x, y, z) instanceof BlockMultiblockProxyNoRenderWater;
			}
			@Override
			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				world.setBlock(x, y, z, ModBlocks.multiblockProxyNoRenderWater,0,0);
			}
		};
		
		protected BlockData()
		{
		}
		
		public static class BlockDataOreDict extends BlockData {
			String oreDict;
			public BlockDataOreDict(String oreDict) {
				this.oreDict=oreDict;
			}
			public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
				Block block = world.getBlock(x, y, z);
				if(block.equals(Blocks.air))
					return false;
				ItemStack stack = new ItemStack(block, 1, block.getDamageValue(world, x, y, z));
				List<ItemStack> ores = OreDictionary.getOres(oreDict);
				for(ItemStack stack2 : ores) {
					if(stack2.isItemEqual(stack))
						return true;
				}
				return false;
			}

			public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
				//NO OP
			}
		}
		
		public BlockData(Block block, int meta) {
			this.block=block;
			this.metadata=meta;
		}
		
		public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
			return world.getBlock(x, y, z)==block && world.getBlockMetadata(x, y, z)==metadata;
		}

		public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
			world.setBlock(x, y, z, block, metadata, 0);
		}
	}
	
	public abstract String getName();
	
	public abstract IMultiblockTileData createTileData();
	
	public abstract void onTick(TileMultiblockCore core);
	public abstract void onCollision(TileMultiblockBase tile, TileMultiblockCore core, Entity ent);
	public abstract void init(TileMultiblockCore core);
	public abstract void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z);
	public abstract void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z);
	public abstract void renderHUD(TileMultiblockBase tile, TileMultiblockCore core, Minecraft mc, ScaledResolution res);
	public abstract void onWanded(TileMultiblockBase tile, TileMultiblockCore core, EntityPlayer player, ItemStack stack);
	public abstract String getUnlocalizedName();

	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO OP
	}
	
	public abstract boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot);

	public abstract LexiconEntry getLexiconEntry();
	
}
