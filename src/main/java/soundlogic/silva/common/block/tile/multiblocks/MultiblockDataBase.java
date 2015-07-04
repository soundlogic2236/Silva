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
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.api.mana.IManaPool;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
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
	
	private MultiblockSet multiblockSet;
	
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
			boolean mirrorX = getMirrorXForTrial(trial);
			boolean mirrorZ = getMirrorZForTrial(trial);
			int rotation = getRotationForTrial(trial);
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
					if(!data.isValid(null, world, transCoords[0], transCoords[1], transCoords[2])) {
						return false;
					}
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
		Block curBlock = Blocks.air;
		int curMeta = 0;
		float curHardness = 0;
		TileEntity tempTile=null;
		NBTTagCompound curCompound = null;
		if(!world.isAirBlock(x, y, z)) {
			curBlock = world.getBlock(x, y, z);
			curMeta = world.getBlockMetadata(x, y, z);
			curHardness = curBlock.getBlockHardness(world, x, y, z);
			tempTile = world.getTileEntity(x,y,z);
			if(tempTile!=null) {
				curCompound = new NBTTagCompound();
				tempTile.writeToNBT(curCompound);
			}
		}
		data.setBlock(core, world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile!=null && tile instanceof TileMultiblockProxy) {
			TileMultiblockProxy proxy = (TileMultiblockProxy)world.getTileEntity(x, y, z);
			if(proxy!=null) {
				if(tempTile instanceof TileMultiblockProxy) {
					proxy.readFromNBT(curCompound);
				}
				else {
					proxy.setOriginalBlock(curBlock, curMeta, curCompound, curHardness);
					proxy.relativeX=core.xCoord-x;
					proxy.relativeY=core.yCoord-y;
					proxy.relativeZ=core.zCoord-z;
					proxy.getCore();
					proxy.markDirty();
				}
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
	private float[] getSimpleOffset(float i, float j, float k) {
		return new float[] {
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
	private float[] rotate90(float[] input) {
		return new float[] {
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				MultiblockComponent component = new MultiblockComponentFromBlockData(this, x, y, z, isCore);
				mb.addComponent(component);
			}
			
			public Block forMultiblockGetBlock() {
				return BotaniaAccessHandler.findBlock("pool");
			}
			
			public int forMultiblockGetMeta() {
				return (int) (BotaniaAPI.internalHandler.getWorldElapsedTicks() / 20) % 3;
			}
			@Override
			public ItemStack[] forMultiblockGetMaterials() {
				return new ItemStack[] { new ItemStack(BotaniaAccessHandler.findBlock("pool")) };
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
			@Override
			public Block forMultiblockGetBlock() {
				return Blocks.water;
			}
			@Override
			public int forMultiblockGetMeta() {
				return 0;
			}
			@Override
			public ItemStack[] forMultiblockGetMaterials() {
				return new ItemStack[] { new ItemStack(Items.water_bucket) };
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			@Override
			public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
				// NO OP
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
			return world.getBlock(x, y, z)==block && (metadata==-1 || world.getBlockMetadata(x, y, z)==metadata);
		}

		public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
			if(metadata!=-1)
				world.setBlock(x, y, z, block, metadata, 0);
		}

		public void addToMultiblock(Multiblock mb, int x, int y, int z, boolean isCore) {
			MultiblockComponent component = new MultiblockComponentFromBlockData(this, x, y, z, isCore);
			mb.addComponent(component);
		}
		
		public Block forMultiblockGetBlock() {
			return block;
		}
		
		public int forMultiblockGetMeta() {
			return metadata;
		}

		public ItemStack[] forMultiblockGetMaterials() {
			return new ItemStack[] { new ItemStack(block, 1, metadata) };
		}

		public void forMultiblockDoRotation(MultiblockComponent multiblockComponentFromBlockData, double angle) {
			// NO OP
		}
		
		protected static class MultiblockComponentFromBlockData extends MultiblockComponent {

			BlockData data;
			boolean isCore;
			
			public MultiblockComponentFromBlockData(BlockData data, ChunkCoordinates relPos, boolean isCore) {
				super(relPos, data.block, data.metadata);
				this.data=data;
				this.isCore=isCore;
			}

			public MultiblockComponentFromBlockData(BlockData data, int x, int y, int z, boolean isCore) {
				this(data, new ChunkCoordinates(x, y, z), isCore);
			}

			@Override
			public Block getBlock() {
				return data.forMultiblockGetBlock();
			}

			@Override
			public int getMeta() {
				return data.forMultiblockGetMeta();
			}
			
			public ItemStack[] getMaterials() {
				return data.forMultiblockGetMaterials();
			}
			@Override
			public MultiblockComponent copy() {
				return new MultiblockComponentFromBlockData(data, relPos, isCore);
			}

			@Override
			public boolean matches(World world, int x, int y, int z) {
				return data.isValid(null, world, x, y, z);
			}

			@Override
			public void rotate(double angle) {
				super.rotate(angle);
				data.forMultiblockDoRotation(this, angle);
			}
		}

	}
	
	public abstract String getName();
	
	public abstract IMultiblockTileData createTileData();
	
	public abstract void onTick(TileMultiblockCore core);
	public abstract void onClientTick(TileMultiblockCore core);
	public abstract void onBreak(TileMultiblockCore core);
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

	public abstract void onInvalidate(TileMultiblockCore core);

	public void onWalking(TileMultiblockBase tile, TileMultiblockCore core, Entity ent) {
		// NO OP
	}
	
	public int getTrialToUseForMultiblockDisplay() {
		int trial = 0;
		while(trial < 16) {
			boolean mirrorX = getMirrorXForTrial(trial);
			boolean mirrorZ = getMirrorZForTrial(trial);
			int rotation = getRotationForTrial(trial);
			if(shouldTryTransform(trial,mirrorX, mirrorZ, rotation))
				return trial;
			trial++;
		}
		return -1;
	}
	
	public static final boolean getMirrorXForTrial(int trial) {
		return ( trial & 1 ) > 0;
	}
	
	public static final boolean getMirrorZForTrial(int trial) {
		return ( trial & 2 ) > 0;
	}
	
	public static final int getRotationForTrial(int trial) {
		boolean rotation1 = ( trial & 4 ) > 0;
		boolean rotation2 = ( trial & 8 ) > 0;
		return ( rotation1 ? 1 : 0 ) + ( rotation2 ? 2 : 0 );
	}
	
	public static class SilvaMultiblock extends Multiblock {
		MultiblockDataBase silvaMultiblock;
		public SilvaMultiblock(MultiblockDataBase silvaMultiblock) {
			super();
			this.silvaMultiblock=silvaMultiblock;
		}
		public Multiblock[] createRotations() {
			Multiblock[] blocks = new Multiblock[4];
			blocks[0] = this;
			blocks[1]=blocks[0].copy();
			blocks[2]=blocks[0].copy();
			blocks[3]=blocks[0].copy();
			if(silvaMultiblock.shouldTryTransform(4, false, false, 1))
				blocks[1].rotate(Math.PI / 2);
			if(silvaMultiblock.shouldTryTransform(8, false, false, 2)) {
				blocks[2].rotate(Math.PI / 2);
				blocks[2].rotate(Math.PI / 2);
			}
			if(silvaMultiblock.shouldTryTransform(12, false, false, 3)) {
				blocks[3].rotate(Math.PI / 2);
				blocks[3].rotate(Math.PI / 2);
				blocks[3].rotate(Math.PI / 2);
			}
			return blocks;
		}
	}
	
	protected MultiblockSet makeMultiblockSet() {
		Multiblock mb = new SilvaMultiblock(this);
		int renderOffsetX;
		int renderOffsetY;
		int renderOffsetZ;
		
		int trial = getTrialToUseForMultiblockDisplay();
		
		boolean mirrorX = getMirrorXForTrial(trial);
		boolean mirrorZ = getMirrorZForTrial(trial);
		int rotation = getRotationForTrial(trial);
		
		for(int i = 0;i<creationRequirementsTemplate.length;i++) {
			BlockData[][] templateSlice1=creationRequirementsTemplate[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData data = templateSlice2[k];
					int[] transCoords = getTransformedCoords(0,0,0,i,j,k,mirrorX,mirrorZ,rotation);
					boolean isCore = transCoords[0]==0 && transCoords[1]==0 && transCoords[2]==0;
					data.addToMultiblock(mb, transCoords[2], transCoords[1], -transCoords[0], isCore);
				}
			}
		}
		mb.setRenderOffset(0, this.templateOrigin[1], 0);
		return mb.makeSet();
	}

	public final MultiblockSet getMultiblockSet() {
		if(multiblockSet==null)
			multiblockSet=makeMultiblockSet();
		return multiblockSet;
	}
	
	
}
