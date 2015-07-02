package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.multiblocks.darkenedtheater.IDarkenedTheaterPlay;
import soundlogic.silva.common.entity.EntityPixieProxy.EquipmentRenderDataSimple;
import soundlogic.silva.common.entity.EntityPixieProxy.IEquipmentRenderData;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieData;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieGroupHandler;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lib.LibGUI;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;

public class MultiblockDataPixieRoomDarkenedTheater extends MultiblockDataPixieRoomSimple {

	private static final String PIXIE_PART_DIRECTOR_NAME = "#Director";
	private static final String PIXIE_PART_PLAYWRIGHT_PREFIX = "#Playwright_";
	
	private static final double[] director_center_location = new double[] {0, 1.2, 0};
	private static final double[] director_side_location = new double[] {3, 1.2, -3};
	
	private IIcon iconBlack;
	private IIcon iconFloor;
	private IIcon iconFloorDim;
	private IIcon iconFloorVeryDim;
	
	public static HashMap<String, IDarkenedTheaterPlay> stringToPlay = new HashMap<String, IDarkenedTheaterPlay>();
	public static List<IDarkenedTheaterPlay> playList = new ArrayList<IDarkenedTheaterPlay>();

	public MultiblockDataPixieRoomDarkenedTheater() {
		super(new ItemStack(Items.wooden_sword),new ItemStack(Items.golden_helmet),new ItemStack(Items.iron_helmet),new ItemStack(Items.book),new ItemStack(Items.paper),new ItemStack(Items.stick),new ItemStack(Items.string),new ItemStack(Items.writable_book),new ItemStack(Items.bow));
	}
	
	@Override
	protected boolean checkInteriorBlock(TileMultiblockCore core, World world,
			int x, int y, int z, int i, int j, int k) {
		return world.isAirBlock(x, y, z);
	}

	@Override
	protected boolean isTemplateBlockValid(TileMultiblockCore core,
			World world, int x, int y, int z) {
		return world.getBlock(x, y, z)==Blocks.coal_block;
	}

	@Override
	public String getName() {
		return "pixieRoomDarkenedTheater";
	}

	@Override
	public void setActivatedVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		if(y==0) {
			DarkenedTheaterTileData data = (DarkenedTheaterTileData) core.getTileData();
			int brightness = 0;
			if(data.floorBrightTicks>0)
				brightness++;
			if(data.currentPlayString.equals(""))
				brightness++;
			switch(brightness) {
			case 0:this.setInteriorIcon(core, tile, x, y, z, iconFloorVeryDim);break;
			case 1:this.setInteriorIcon(core, tile, x, y, z, iconFloorDim);break;
			case 2:this.setInteriorIcon(core, tile, x, y, z, iconFloor);break;
			}
		}
		else
			this.setInteriorIcon(core, tile, x, y, z, iconBlack);
	}

	@Override
	public void setActivatedPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		// NO OP
	}
	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.PIXIE_ROOM_DARKENED_THEATER;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBlack = par1IconRegister.registerIcon(LibResources.ICON_BLACK);
		iconFloor = par1IconRegister.registerIcon(LibResources.DARKENED_THEATER_FLOOR);
		iconFloorDim = par1IconRegister.registerIcon(LibResources.DARKENED_THEATER_FLOOR+"Dim");
		iconFloorVeryDim = par1IconRegister.registerIcon(LibResources.DARKENED_THEATER_FLOOR+"VeryDim");
	}
	
	@Override
	protected boolean setInteriorBlock(TileMultiblockCore core, World world, int x, int y, int z, int i, int j, int k) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivate(TileMultiblockCore core) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivatedTick(TileMultiblockCore core) {
		DarkenedTheaterTileData data = (DarkenedTheaterTileData) core.getTileData();
		AxisAlignedBB interior =  AxisAlignedBB.getBoundingBox(
				core.xCoord+-3, 
				core.yCoord+1, 
				core.zCoord+-3, 
				core.xCoord+4, 
				core.yCoord+7, 
				core.zCoord+4);
		World world = core.getWorldObj();
		List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, interior);
		if(checkForMotion(entities))
			markMotion(core, data);
		if(data.floorBrightTicks>0) {
			data.floorBrightTicks--;
			if(data.floorBrightTicks==0)
				core.markForVisualUpdate();
		}
		if(data.pixieGroup.pixieDiedLastTick) {
			data.missingPositionsDirty=true;
			data.allRequiredPositionsFilled=false;
		}
		if(data.selectingPlay) {
			if(data.decreasePageTriggered) {
				data.selectionPage--;
				data.selectionCount=-1;
			}
			if(data.increasePageTriggered) {
				data.selectionPage++;
				data.selectionCount=-1;
			}
			if(data.selectionCount>0) {
				data.selectionCount--;
				if(data.selectionCount==0) {
					data.selectionCount=-1;
					data.preSelectedPlay=-1;
				}
			}
			if(data.selectionCount==-2) {
				data.selectionCount=-1;
				IDarkenedTheaterPlay play = getPlayInPixieSlot(core, data, data.preSelectedPlay);
				data.preSelectedPlay=-1;
				if(play.canSelect(core, data)) {
					stopSelectingPlay(core, data);
					startPlay(core, data, play);
				}
			}
			if(world.getEntitiesWithinAABB(EntityPlayer.class, interior).isEmpty()) {
				data.ticksWithoutPlayer++;
				if(data.ticksWithoutPlayer>200)
					stopSelectingPlay(core, data);
			}
			
		}
		if(data.currentPlay!=null) {
			if(data.selectionCount>0)
				data.selectionCount--;
			if(data.selectionCount==0)
				data.selectionCount=-1;
			if(data.selectionCount==-2)
				stopPlay(core, data);
		}
		data.decreasePageTriggered=false;
		data.increasePageTriggered=false;
		boolean requirementsMightHaveChanged=data.requiredPositionsDirty;
		if(!data.allRequiredPositionsFilled) {
			for(String position : getMissingPositionList(core, data)) {
				if(world.rand.nextFloat()<getSpawnRateForPosition(core, data, position, data.currentPlay)) {
					if(drainPowerToCreatePixie(core, data, position, data.currentPlay)) {
						fillPosition(core, data, position);
					}
				}
			}
		}
		getMissingPositionList(core, data);
		if(requirementsMightHaveChanged) {
			boolean b = data.allRequiredPositionsFilled;
			List<String> allPositions = new ArrayList<String>(getRequiredPositionList(core, data));
			for(ActorPixieData pixie : data.pixieGroup.pixies) {
				if(!allPositions.contains(pixie.position)) {
					pixie.setDead(data.pixieGroup);
				}
				allPositions.remove(pixie.position);
			}
			data.allRequiredPositionsFilled = b;
		}
		if(data.currentPlay!=null) {
			data.currentPlayProgress++;
			data.currentPlay.setProgress(core, data, data.currentPlayProgress);
		}
		for(ActorPixieData pixie : data.pixieGroup.pixies) {
			tickPosition(core, data, pixie, pixie.position);
			if(pixie.moveToTarget) {
				double[] targetCoords = convertPixieCoords(core, data, pixie.unconvertedTargetPosX, pixie.unconvertedTargetPosY, pixie.unconvertedTargetPosZ);
				pixie.targetPosX=targetCoords[0];
				pixie.targetPosY=targetCoords[1];
				pixie.targetPosZ=targetCoords[2];
				System.out.println(targetCoords[0]+","+targetCoords[1]+","+targetCoords[2]);
			}
			if(pixie.setRotation) {
				pixie.rotation=convertPixieRotation(core, data, pixie.unconvertedRotation);
				pixie.setRotation=false;
			}
			pixie.triggered=false;
		}
		data.pixieGroup.tick(world);
		core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
	}
	
	private void stopPlay(TileMultiblockCore core, DarkenedTheaterTileData data) {
		data.setPlay("");
	}

	private void startPlay(TileMultiblockCore core, DarkenedTheaterTileData data, IDarkenedTheaterPlay play) {
		data.setPlay(play.getKey());
	}

	private void toggleSelectingPlay(TileMultiblockCore core, DarkenedTheaterTileData data) {
		if(data.selectingPlay==true)
			stopSelectingPlay(core, data);
		else
			startSelectingPlay(core, data);
	}

	private void stopSelectingPlay(TileMultiblockCore core, DarkenedTheaterTileData data) {
		data.selectingPlay=false;
		data.allRequiredPositionsFilled=false;
		data.missingPositionsDirty=true;
		data.requiredPositionsDirty=true;
		data.selectionCount=-1;
	}
	
	private void startSelectingPlay(TileMultiblockCore core, DarkenedTheaterTileData data) {
		data.selectingPlay=true;
		data.allRequiredPositionsFilled=false;
		data.missingPositionsDirty=true;
		data.requiredPositionsDirty=true;
	}
	
	private void tickPosition(TileMultiblockCore core, DarkenedTheaterTileData data, ActorPixieData pixie, String position) {
		if(position.startsWith("#")) {
			if(position.equals(PIXIE_PART_DIRECTOR_NAME)) {
				if(data.currentPlay==null) {
					if(pixie.triggered && pixie.atTarget) {
						toggleSelectingPlay(core, data);
					}
				}
				else {
					if(pixie.triggered && pixie.atTarget) {
						if(data.selectionCount>0)
							data.selectionCount=-2;
						else
							data.selectionCount=20*10;
					}
					if(data.selectionCount>0)
						pixie.displayName=EnumChatFormatting.GOLD+StatCollector.translateToLocal(LibGUI.STOP_DARKENED_THEATER);
					else
						pixie.displayName="";
				}
				pixie.canTrigger=pixie.atTarget;
				if(pixie.atTarget)
					pixie.setRotation(getDirectorAngle(core, data));
				pixie.setTargetPosition(getDirectorLocation(core, data));
				return;
			}
			if(position.startsWith(PIXIE_PART_PLAYWRIGHT_PREFIX)) {
				if(pixie.playwrightSlot==-1) {
					pixie.playwrightSlot=getSlotForPlaywright(position);
				}
				if(pixie.playwrightSlot==0) {
					if(data.selectionPage>0) {
						pixie.displayName="<<<";
						pixie.canTrigger=true;
						if(pixie.triggered)
							data.decreasePageTriggered=true;
					}
					else {
						pixie.displayName=EnumChatFormatting.RED+"<<<";
						pixie.canTrigger=false;
					}
				}
				else if(pixie.playwrightSlot==6) {
					if(data.selectionPage<(playList.size()/5)) {
						pixie.displayName=">>>";
						pixie.canTrigger=true;
						if(pixie.triggered)
							data.increasePageTriggered=true;
					}
					else {
						pixie.displayName=EnumChatFormatting.RED+">>>";
						pixie.canTrigger=false;
					}
				}
				else {
					IDarkenedTheaterPlay play = getPlayInPixieSlot(core, data, pixie.playwrightSlot);
					if(play==null) {
						pixie.displayName=EnumChatFormatting.RED+"---";
						pixie.canTrigger=false;
						pixie.playwrightLevel=-1;
					}
					else {
						boolean canSelect = play.canSelect(core, data);
						if(!canSelect)
							pixie.displayName=EnumChatFormatting.RED+play.getDisplayName();
						else if(data.preSelectedPlay==pixie.playwrightSlot)
							pixie.displayName=EnumChatFormatting.GOLD+play.getDisplayName();
						else
							pixie.displayName=play.getDisplayName();
						pixie.canTrigger=canSelect;
						pixie.playwrightLevel=play.getPlaywrightLevel();
						if(pixie.triggered) {
							if(data.preSelectedPlay!=pixie.playwrightSlot) {
								data.preSelectedPlay=pixie.playwrightSlot;
								data.selectionCount=20*15;
							}
							else
								data.selectionCount=-2;
						}
					}
				}
				return;
			}
		}
		else {
			if(data.currentPlay==null)
				return;
			data.currentPlay.tickPixie(core, data, pixie, position);
		}
	}
	
	private IDarkenedTheaterPlay getPlayInPixieSlot(TileMultiblockCore core, DarkenedTheaterTileData data, int playwrightSlot) {
		int slot = data.selectionPage*5+playwrightSlot-1;
		if(slot>=0 && slot<playList.size())
			return playList.get(slot);
		return null;
	}

	private float getSpawnRateForPosition(TileMultiblockCore core, DarkenedTheaterTileData data, String position, IDarkenedTheaterPlay currentPlay) {
		// TODO Auto-generated method stub
		return 0.01F;
	}

	private boolean drainPowerToCreatePixie(TileMultiblockCore core, DarkenedTheaterTileData data, String position, IDarkenedTheaterPlay currentPlay) {
		// TODO Auto-generated method stub
		return true;
	}

	private void fillPosition(TileMultiblockCore core, DarkenedTheaterTileData data, String position) {
		if(position.startsWith("#")) {
			if(position.equals(PIXIE_PART_DIRECTOR_NAME)) {
				makeDirector(core, data);
				return;
			}
			if(position.startsWith(PIXIE_PART_PLAYWRIGHT_PREFIX)) {
				makePlaywright(core, data, getSlotForPlaywright(position));
				return;
			}
		}
		else {
			
		}
	}
	
	private int getSlotForPlaywright(String position) {
		return Integer.valueOf(position.substring(PIXIE_PART_PLAYWRIGHT_PREFIX.length()));
	}
	
	private double[] getDirectorLocation(TileMultiblockCore core, DarkenedTheaterTileData data) {
		if(data.currentPlay==null)
			return director_center_location;
		return director_side_location;
	}
	
	private float getDirectorAngle (TileMultiblockCore core, DarkenedTheaterTileData data) {
		if(data.currentPlay==null)
			return 0;
		return 180+45;
	}
	
	private void makeDirector(TileMultiblockCore core, DarkenedTheaterTileData data) {
		double[] coords = getDirectorLocation(core, data);
		fillPosition(core, data, coords[0], coords[1], coords[2], getDirectorAngle(core, data), PIXIE_PART_DIRECTOR_NAME);
	}
	
	private void makePlaywright(TileMultiblockCore core, DarkenedTheaterTileData data, Integer slot) {
		fillPosition(core, data, slot-3, 1.2F, 3, 0, PIXIE_PART_PLAYWRIGHT_PREFIX+slot);
	}
	
	private ActorPixieData fillPosition(TileMultiblockCore core, DarkenedTheaterTileData data, double x, double y, double z, float rotation, String position) {
		ActorPixieData pixie = new ActorPixieData(data.pixieGroup);
		double[] coords = convertPixieCoords(core, data, x, y, z);
		pixie.posX=coords[0];
		pixie.posY=coords[1];
		pixie.posZ=coords[2];
		pixie.rotation=convertPixieRotation(core, data, rotation);
		pixie.position=position;
		data.missingPositionsDirty=true;
		return pixie;
	}
	
	private float convertPixieRotation(TileMultiblockCore core, DarkenedTheaterTileData data, float rotation) {
		return core.rotation*90-90 + rotation;
	}
	
	private double[] convertPixieCoords(TileMultiblockCore core, DarkenedTheaterTileData data, double x, double y, double z) {
		int[] coords1 = convertPixieCoordsInt(core, data, (int)x, (int)y, (int)z);
		int[] coords2 = convertPixieCoordsInt(core, data, (int)x+1, (int)y+1, (int)z+1);
		return new double[] {
			interpolateConversion(coords1[0], coords2[0], x) + .5,
			interpolateConversion(coords1[1], coords2[1], x),
			interpolateConversion(coords1[2], coords2[2], x) + .5,
		};
	}
	private int[] convertPixieCoordsInt(TileMultiblockCore core, DarkenedTheaterTileData data, int x, int y, int z) {
		return this.getTransformedCoords(core, y+1, z+4, x+4);
	}
	
	private double interpolateConversion(double convX1, double convX2, double originalX) {
		return convX1 + (convX2 - convX1) * fpart(originalX);
	}
	
	private double fpart(double x) {
		return x - (int)x;
	}
	
	private void markMotion(TileMultiblockCore core, DarkenedTheaterTileData data) {
		data.floorBrightTicks=20*6;
		core.markForVisualUpdate();
	}

	private boolean checkForMotion(List<EntityLiving> entities) {
		for(EntityLiving ent : entities) {
			double totalMovement=0;
			totalMovement+=(ent.posX-ent.prevPosX)*(ent.posX-ent.prevPosX);
			totalMovement+=(ent.posY-ent.prevPosY)*(ent.posY-ent.prevPosY);
			totalMovement+=(ent.posZ-ent.prevPosZ)*(ent.posZ-ent.prevPosZ);
			if(totalMovement>0.02)
				return true;
		}
		return false;
	}

	protected List<String> getMissingPositionList(TileMultiblockCore core, DarkenedTheaterTileData data) {
		if(data.missingPositionsDirty) {
			data.missingPositions.clear();
			data.missingPositions.addAll(getRequiredPositionList(core, data));
			for(ActorPixieData pixie : data.pixieGroup.pixies) {
				data.missingPositions.remove(pixie.position);
			}
			if(data.missingPositions.isEmpty())
				data.allRequiredPositionsFilled=true;
			data.missingPositionsDirty=false;
		}
		return data.missingPositions;
	}
	
	protected List<String> getRequiredPositionList(TileMultiblockCore core,  DarkenedTheaterTileData data) {
		if(data.requiredPositionsDirty) {
			data.requiredPositions.clear();
			data.requiredPositions.add(PIXIE_PART_DIRECTOR_NAME);
			if(data.selectingPlay) {
				for(int i = 0; i < 7; i++)
					data.requiredPositions.add(PIXIE_PART_PLAYWRIGHT_PREFIX+i);
			}
			if(data.currentPlay!=null)
				data.requiredPositions.addAll(data.currentPlay.getRequiredPositionList());
			data.missingPositionsDirty=true;
			data.requiredPositionsDirty=false;
		}
		return data.requiredPositions;
	}
	
	public void onWalking(TileMultiblockBase tile, TileMultiblockCore core, Entity ent) {
		if(ent instanceof EntityPlayer) {
			if(!ent.isSneaking()) {
				markMotion(core, (DarkenedTheaterTileData) core.getTileData());
			}
		}
	}

	@Override
	public void onActivatedClientTick(TileMultiblockCore core) {
		DarkenedTheaterTileData data = (DarkenedTheaterTileData) core.getTileData();
		data.pixieGroup.clientTick(core.getWorldObj());
	}

	@Override
	public IMultiblockTileData createTileData() {
		return new DarkenedTheaterTileData();
	}

	@Override
	public void onCollision(TileMultiblockBase tile, TileMultiblockCore core, Entity ent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(TileMultiblockCore core) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core,
			Minecraft mc, ScaledResolution res) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core,
			EntityPlayer player, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInvalidate(TileMultiblockCore core) {
		// TODO Auto-generated method stub
		
	}

	public static class ActorPixieData extends PixieData {
		EquipmentRenderDataSimple equipmentRenderData = new EquipmentRenderDataSimple();
		IDarkenedTheaterPlay play = null;
		String position="";
		String displayName = "";
		
		boolean triggered;
		boolean canTrigger = false;
		
		int playwrightLevel = -1;
		int playwrightSlot = -1;

		double unconvertedTargetPosX = 0;
		double unconvertedTargetPosY = 0;
		double unconvertedTargetPosZ = 0;
		
		float unconvertedRotation = 0;
		boolean setRotation = false;
		
		double targetPosX = 0;
		double targetPosY = 0;
		double targetPosZ = 0;
		boolean moveToTarget = false;
		boolean atTarget = true;
		
		static ItemStack directorStack;
		static ItemStack[] playwrightStack;

		public ActorPixieData(PixieGroupHandler data) {
			super(data);
		}
		@Override
		public void tick(World world, PixieGroupHandler data) {
			handleTargetMovement();
			super.tick(world, data);
			setEquipment();
			equipmentRenderData.tick();
		}
		
		private void handleTargetMovement() {
			if(!moveToTarget)
				return;
			this.moveToTarget=false;
			double dx=targetPosX-this.posX;
			double dy=targetPosY-this.posY;
			double dz=targetPosZ-this.posZ;
			System.out.println(Math.abs(dx)+Math.abs(dy)+Math.abs(dz));
			if(Math.abs(dx)<0.01 && Math.abs(dy)<0.01 && Math.abs(dz)<0.01) {
				this.posX=targetPosX;
				this.posY=targetPosY;
				this.posZ=targetPosZ;
				this.motionX=0;
				this.motionY=0;
				this.motionZ=0;
				atTarget=true;
				return;
			}
			this.motionX=dx;
			this.motionY=dy;
			this.motionZ=dz;
			this.normVelocity();
			if(Math.abs(motionX)>dx)
				this.motionX=dx;
			if(Math.abs(motionY)>dy)
				this.motionY=dy;
			if(Math.abs(motionZ)>dz)
				this.motionZ=dz;
		}
		private void setEquipment() {
			if(position.equals(PIXIE_PART_DIRECTOR_NAME)) {
				if(directorStack==null)
					directorStack=new ItemStack(Items.book);
				equipmentRenderData.setEquipment(directorStack);
			}
			else if(position.startsWith(PIXIE_PART_PLAYWRIGHT_PREFIX)) {
				if(playwrightStack==null) {
					playwrightStack=new ItemStack[3];
					playwrightStack[0] = new ItemStack(Items.paper);
					playwrightStack[1] = new ItemStack(ModItems.simpleResource,1,1);
					playwrightStack[2] = new ItemStack(Items.enchanted_book);
				}
				if(playwrightLevel!=-1)
					equipmentRenderData.setEquipment(playwrightStack[playwrightLevel]);
				else
					equipmentRenderData.setEquipment(null);
			}
			else if (play!=null) {
				equipmentRenderData.setEquipment(play.getEquipmentForPosition(position));
				equipmentRenderData.setHelmet(play.getHelmetForPosition(position));
			}
		}
		@Override
		public void clientTick(World world, PixieGroupHandler data) {
			super.clientTick(world, data);
			equipmentRenderData.tick();
		}
		@Override
		public IEquipmentRenderData getEquipment() {
			return equipmentRenderData;
		}
		@Override
		public boolean interact(EntityPlayer player) {
			if(canTrigger) {
				triggered=true;
				return true;
			}
			return false;
		}
		
		public boolean shouldDispayName() {
			return displayName.length()>0;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public void setTargetPosition(double[] coords) {
			setTargetPosition(coords[0], coords[1], coords[2]);
		}
		public void setTargetPosition(double x, double y, double z) {
			unconvertedTargetPosX = x;
			unconvertedTargetPosY = y;
			unconvertedTargetPosZ = z;
			moveToTarget=true;
			atTarget=false;
		}
		
		public void setRotation(float rotation) {
			this.unconvertedRotation=rotation;
			this.setRotation=true;
		}
		
		private final String POSITION="position";
		private final String PLAYWRIGHTLEVEL="playwrightLevel";
		private final String DISPLAYNAME="displayName";
		
		@Override
		public void writeToNBT(NBTTagCompound cmp) {
			super.writeToNBT(cmp);
			equipmentRenderData.writeToNBT(cmp);
			cmp.setString(POSITION, position);
			if(playwrightLevel!=-1)
				cmp.setInteger(PLAYWRIGHTLEVEL, playwrightLevel);
			cmp.setString(DISPLAYNAME, displayName);
		}

		@Override
		public void readFromNBT(NBTTagCompound cmp) {
			super.readFromNBT(cmp);
			equipmentRenderData.readFromNBT(cmp);
			position=cmp.getString(POSITION);
			if(cmp.hasKey(PLAYWRIGHTLEVEL))
				playwrightLevel=cmp.getInteger(PLAYWRIGHTLEVEL);
			else
				playwrightLevel=-1;
			displayName=cmp.getString(DISPLAYNAME);
		}

	}
}
