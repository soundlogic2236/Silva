package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import vazkii.botania.common.core.helper.Vector3;

public abstract class MultiblockDataPixieRoomBase extends MultiblockDataBase {

	private static final String TAG_PROCESSED = "_silvaPixieRoomProcessedStack";
	
	private static int[][] tempWallCoords = new int[][] {
		{1,1,4},
		{2,1,4}
	};
	
	public static ArrayList<MultiblockDataPixieRoomBase> pixieRooms = new ArrayList<MultiblockDataPixieRoomBase> ();

	private static class BlockDataInterior extends BlockData {
		
		MultiblockDataPixieRoomBase multiblockData;
		
		public BlockDataInterior(MultiblockDataPixieRoomBase multiblockData) {
			this.multiblockData=multiblockData;
		}
		@Override
		public boolean isValid(TileMultiblockCore core, World world, int x, int y, int z) {
			if(core==null) {
				return world.isAirBlock(x, y, z);
			}
			else if(!((PixiePowerTileData)core.getTileData()).activated) {
				if(isTempWallCoords(core, multiblockData, x, y, z))
					return BlockData.MULTIBLOCK.isValid(core, world, x, y, z);
				else
					return world.isAirBlock(x, y, z);
			}
			else
				return multiblockData.checkInteriorBlock(core, world, x, y, z);
		}
		@Override
		public void setBlock(TileMultiblockCore core, World world, int x, int y, int z) {
			//NO OP
		}
	}
	
	private static boolean isTempWallCoords(TileMultiblockCore core, MultiblockDataPixieRoomBase room, int x, int y, int z) {
		for(int[] coords : tempWallCoords) {
			int[] transCoords = room.getTransformedCoords(core, coords[0], coords[1], coords[2]);
			if(transCoords[0]==x && transCoords[1]==y && transCoords[2]==z)
				return true;
		}
		return false;
	}
	
	public MultiblockDataPixieRoomBase() {
		super(new BlockData(ModBlocks.pixieStone, 2));
		pixieRooms.add(this);
		BlockData core = new BlockData(ModBlocks.pixieStone, 2);
		BlockData brick = new BlockData(ModBlocks.pixieStone, 1);
		BlockData chiseled = new BlockData(ModBlocks.pixieStone, 2);
		BlockData door = new BlockData(ModBlocks.pixieDoor, -1);
		BlockData interior = new BlockDataInterior(this);

		templateOrigin = new int[] {4,0,4};

		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK_CORE, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, door, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, door, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
			
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},

				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, interior, interior, interior, interior, interior, interior, interior, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},

				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}}};
	}

	private boolean checkInteriorBlock(TileMultiblockCore core, World world, int x, int y, int z) {
		int[] coords = convertRelativeCoords(core, x-core.xCoord, y-core.xCoord, z-core.xCoord);
		return checkInteriorBlock(core, world, x, y, z, coords[0], coords[1], coords[2]);
	}
	
	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) {
		return false;
	}
	
	public void onTick(TileMultiblockCore core) {
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		World world = core.getWorldObj();
		validateFarm(core, world, data);
		if(!isTemplateBlockValid(core, world, data.patternX,data.patternY,data.patternZ)) {
			core.breakMultiblock();
			return;
		}
		if(!data.activated) {
			absorbItems(core, world, data);
			if(canActivate(core, world, data, data.inventory)) {
				data.activated=true;
				for(int[] coords : tempWallCoords) {
					int[] transCoords = getTransformedCoords(core, coords[0], coords[1], coords[2]);
					world.setBlockToAir(transCoords[0], transCoords[1], transCoords[2]);
				}
				setInterior(core, world, data);
				onActivate(core);
				core.markForVisualUpdate();
			}
		}
		else
			onActivatedTick(core);
	}

	@Override
	public void onClientTick(TileMultiblockCore core) {
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		if(data.hasFarm) {
			TileEntity tile = core.getWorldObj().getTileEntity(data.farmX, data.farmY, data.farmZ);
			if(tile instanceof TileMultiblockCore) {
				if(((TileMultiblockCore) tile).getData() instanceof MultiblockDataPixieFarm) {
					PixieFarmTileData farmData = (PixieFarmTileData) ((TileMultiblockCore) tile).getTileData();
					float power = 0;
					if(data.activated)
						power = ((float)farmData.getPowerLevel())/((float)farmData.getMaxPowerLevel());
					doPowerBeam(core, data.farmX, data.farmY, data.farmZ, power, ClientTickHandler.forestWandRendering);
				}
			}
		}
		if(data.activated)
			onActivatedClientTick(core);
	}

	protected static void doPowerBeam(TileMultiblockCore core, int farmX, int farmY, int farmZ, float power, boolean wand) {
		if(power>0 || wand) {
			ForgeDirection dir = getDirection(core);
			double startX = core.xCoord+0.5+4.5*dir.offsetX;
			double startY = core.yCoord+3.5;
			double startZ = core.zCoord+0.5+4.5*dir.offsetZ;
			double endX = farmX+0.5;
			double endY = farmY+5.3;
			double endZ = farmZ+0.5;
			Vector3 diff = new Vector3(endX-startX, endY-startY, endZ-startZ);
			double length = diff.mag();
			diff.normalize();
			double interval = .2F;
			for(double distance = 0 ; distance < length ; distance+=interval) {
				doPowerDot(core.getWorldObj(), startX, startY, startZ, diff.x, diff.y, diff.z, distance, power, wand);
			}
			doPowerDot(core.getWorldObj(), startX, startY, startZ, diff.x, diff.y, diff.z, length, power, wand);
		}
	}
	
	private static void doPowerDot(World world, double startX, double startY, double startZ, double diffX, double diffY, double diffZ, double distance, float power, boolean wand) {
		double x=startX+diffX*distance;
		double y=startY+diffY*distance;
		double z=startZ+diffZ*distance;
		float r=1F;
		float g=.25F;
		float b=.9F;
		float size = .8F;
		double ang=distance+(power * .1F + .3F) * (ClientTickHandler.ticksInGame+ClientTickHandler.partialTicks);
		float sizeAdjustment = (float) ((Math.sin(ang)+(wand ? 1F: .8F*power))/(2F)*((wand ? .5F: 0F)+2F*power));
		if(sizeAdjustment>0) {
			size = size * sizeAdjustment;
			BotaniaAccessHandler.sparkleFX(world, x, y, z, r, g, b, size, 1, true);
		}
	}
	
	private void setInterior(TileMultiblockCore core, World world, PixiePowerTileData data) {
		for(int i = 0;i<persistanceAndCreationBlocks.length;i++) {
			BlockData[][] templateSlice1=persistanceAndCreationBlocks[i];
			for(int j = 0; j<templateSlice1.length;j++) {
				BlockData[] templateSlice2=templateSlice1[j];
				for(int k = 0;k<templateSlice2.length;k++) {
					BlockData blockData = templateSlice2[k];
					int[] coords = getTransformedCoords(core,i,j,k);
					if(blockData instanceof BlockDataInterior) {
						setInteriorBlock(core, world, coords[0],coords[1],coords[2],i,j,k);
					}
				}
			}
		}
	}

	private void absorbItems(TileMultiblockCore core, World world, PixiePowerTileData data) {
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, getDoorBoundingBox(core));
		for(EntityItem item : items) {
			if(item.isDead)
				continue;
			if(item.getEntityData().getBoolean(TAG_PROCESSED))
				continue;
			item.getEntityData().setBoolean(TAG_PROCESSED, true);
			int count = shouldAbsorb(core, data, data.inventory, item);
			if(count>0) {
				onAbsorb(core, data, item, count);
				ItemStack original = item.getEntityItem();
				ItemStack addStack = original.copy();
				addStack.stackSize=1;
				for(int i = 0 ; i < count ; i++)
					data.inventory.add(addStack);
				if(count==original.stackSize)
					item.setDead();
				else
					original.stackSize-=count;
				core.markForVisualUpdate();
			}
		}
	}

	private AxisAlignedBB getDoorBoundingBox(TileMultiblockCore core) {
		ForgeDirection dir=getDirection(core);
		return AxisAlignedBB.getBoundingBox(
				core.xCoord+dir.offsetX*4, 
				core.yCoord+1, 
				core.zCoord+dir.offsetZ*4, 
				core.xCoord+dir.offsetX*4+1, 
				core.yCoord+3, 
				core.zCoord+dir.offsetZ*4+1);
	}

	protected static ForgeDirection getDirection(TileMultiblockCore core) {
		if(core.rotation==0)
			return ForgeDirection.WEST;
		if(core.rotation==1)
			return ForgeDirection.SOUTH;
		if(core.rotation==2)
			return ForgeDirection.EAST;
		if(core.rotation==3)
			return ForgeDirection.NORTH;
		return ForgeDirection.UNKNOWN;
	}
	
	private void validateFarm(TileMultiblockCore core, World world, PixiePowerTileData data) {
		TileEntity tile = world.getTileEntity(data.farmX, data.farmY, data.farmZ);
		if(tile instanceof TileMultiblockCore) {
			if(((TileMultiblockCore) tile).getData() instanceof MultiblockDataPixieFarm) {
				data.farmData=(PixieFarmTileData) ((TileMultiblockCore) tile).getTileData();
				data.hasFarm=true;
				return;
			}
		}
		data.hasFarm=false;
		
	}

	public boolean drawPower(TileMultiblockCore core, PixiePowerTileData data, int power) {
		if(data.hasFarm)
			return data.farmData.drawPower(power);
		return false;
	}
	
	public void setInteriorIcon(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z, IIcon icon) {
		setIconsForWall(core, tile, x, y, z, true, icon, false, null);
	}
	public void setExteriorIcon(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z, IIcon icon) {
		setIconsForWall(core, tile, x, y, z, false, null, true, icon);
	}
	
	public void setIconsForWall(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z, boolean doInterior, IIcon interior, boolean doExterior, IIcon exterior) {
		int[] coords = tile.getRelativePos();
		boolean negX=false;
		boolean posX=false;
		boolean negY=false;
		boolean posY=false;
		boolean negZ=false;
		boolean posZ=false;
		int wallCount=0;
		if(coords[0]==-4) {
			negX=true;
			wallCount++;
		}
		if(coords[0]==4) {
			posX=true;
			wallCount++;
		}
		if(coords[1]==0) {
			negY=true;
			wallCount++;
		}
		if(coords[1]==8) {
			posY=true;
			wallCount++;
		}
		if(coords[2]==-4) {
			negZ=true;
			wallCount++;
		}
		if(coords[2]==4) {
			posZ=true;
			wallCount++;
		}
		if(doInterior) {
			if(wallCount==1) {
				if(negX)
					tile.iconsForSides[5]=interior;
				else if(posX)
					tile.iconsForSides[4]=interior;
				else if(negY)
					tile.iconsForSides[1]=interior;
				else if(posY)
					tile.iconsForSides[0]=interior;
				else if(negZ)
					tile.iconsForSides[3]=interior;
				else if(posZ)
					tile.iconsForSides[2]=interior;
			}
		}
		if(doExterior) {
			if(negX)
				tile.iconsForSides[4]=exterior;
			if(posX)
				tile.iconsForSides[5]=exterior;
			if(negY)
				tile.iconsForSides[0]=exterior;
			if(posY)
				tile.iconsForSides[1]=exterior;
			if(negZ)
				tile.iconsForSides[2]=exterior;
			if(posZ)
				tile.iconsForSides[3]=exterior;
		}
	}
	
	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		if(data.activated)
			setActivatedVisualData(core, tile, x, y, z);
		else if(tile.getOriginalBlock()==Blocks.air) {
			IIcon iconBlack = MultiblockDataPixieRoomEmpty.iconBlack;
			tile.iconsForSides=new IIcon[]{iconBlack,iconBlack,iconBlack,iconBlack,iconBlack,iconBlack};
		}
	}

	@Override
	public void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		tile.lightValue=1;
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		if(data.activated)
			setActivatedPhysicalData(core, tile, x, y, z);
	}

	@Override
	public void onBreak(TileMultiblockCore core) {
		if(core.getWorldObj().isRemote)
			return;
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		for(ItemStack drop : data.inventory) {
			EntityItem item = new EntityItem(core.getWorldObj(), core.xCoord + 0.5, core.yCoord + 1.5, core.zCoord + 0.5, drop);
			core.getWorldObj().spawnEntityInWorld(item);
		}
		data.inventory.clear();
	}

	public void onStartRoomUnactivated(TileMultiblockCore core) {
		for(int[] coords : tempWallCoords) {
			int[] transCoords = getTransformedCoords(core, coords[0], coords[1], coords[2]);
			this.setBlock(BlockData.MULTIBLOCK, core, core.getWorldObj(), transCoords[0], transCoords[1], transCoords[2]);
		}
	}

	protected int shouldAbsorb(TileMultiblockCore core, PixiePowerTileData data, ArrayList<ItemStack> inventory, EntityItem item) {
		return shouldAbsorb(core, data, inventory, item.getEntityItem());
	}
	
	public Object getNextRequestedObject(TileMultiblockCore core) {
		PixiePowerTileData data = (PixiePowerTileData) core.getTileData();
		return getNextRequestedObject(core, data, data.inventory);
	}

	public int[] getOffsetForRequirements(TileMultiblockCore core) {
		return getTransformedCoords(core, 3, -1, 4);
	}
	
	protected abstract void onAbsorb(TileMultiblockCore core, PixiePowerTileData data, EntityItem item, int count);
	protected abstract void setActivatedVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z);
	protected abstract void setActivatedPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z);
	protected abstract boolean setInteriorBlock(TileMultiblockCore core, World world, int x, int y, int z, int i, int j, int k);
	protected abstract boolean checkInteriorBlock(TileMultiblockCore core, World world, int x, int y, int z, int i, int j, int k);
	protected abstract boolean isTemplateBlockValid(TileMultiblockCore core, World world, int x, int y, int z);
	protected abstract boolean canActivate(TileMultiblockCore core, World world, PixiePowerTileData data, ArrayList<ItemStack> inventory);
	protected abstract void onActivate(TileMultiblockCore core);
	protected abstract void onActivatedTick(TileMultiblockCore core);
	protected abstract void onActivatedClientTick(TileMultiblockCore core);
	protected abstract int shouldAbsorb(TileMultiblockCore core, PixiePowerTileData data, ArrayList<ItemStack> inventory, ItemStack item);
	protected abstract Object getNextRequestedObject(TileMultiblockCore core, PixiePowerTileData data, ArrayList<ItemStack> inventory);

}
