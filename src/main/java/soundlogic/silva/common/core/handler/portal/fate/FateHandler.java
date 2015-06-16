package soundlogic.silva.common.core.handler.portal.fate;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.relauncher.Side;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerBase;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.entity.EntityFenrirEcho;
import soundlogic.silva.common.entity.IEntityFateEcho;
import soundlogic.silva.common.item.ItemFatePearl;
import soundlogic.silva.common.lib.LibGUI;
import soundlogic.silva.common.network.MessageEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class FateHandler {

	private final static String TAG_FATE = "silvaFate";

	public final static double FATE_MESSAGE_RANGE = 40;
	
	static HashMap<String, IVigridrFate> fatesOriginals = new HashMap<String, IVigridrFate>();
	static HashMap<String, Class<IVigridrFate>> fatesClasses = new HashMap<String, Class<IVigridrFate>>();
	static HashMap<String, NBTTagCompound> fatesData = new HashMap<String, NBTTagCompound>();
	static ArrayList<String> fatesNames = new ArrayList<String>();
	static HashMap<Integer,EntityLivingBase> activeFateKeys = new HashMap<Integer,EntityLivingBase>();
	static HashMap<Integer,ArrayList<IEntityFateEcho>> activeEchos = new HashMap<Integer,ArrayList<IEntityFateEcho>>();
	static ArrayList<IVigridrFate> allActiveFates = new ArrayList<IVigridrFate>();
	
	protected static class StoredEntityData {

		private static final String TAG_NAME = "name";
		private static final String TAG_DATA = "data";
		private static final String TAG_DIM = "dim";
		private static final String TAG_DISTANCE = "distance";
		private static final String TAG_X = "x";
		private static final String TAG_Y = "y";
		private static final String TAG_Z = "z";
		
		String className;
		NBTTagCompound entityData;
		int dimension;
		float distance;
		double x;
		double y;
		double z;
		public StoredEntityData(Entity entity) {
			storeEntity(entity);
		}
		public StoredEntityData() {
			// NO OP
		}

		public void storeEntity(Entity entity) {
			className=(String) EntityList.classToStringMapping.get(entity.getClass());
			entityData=new NBTTagCompound();
			entity.writeToNBT(entityData);
			dimension=entity.worldObj.provider.dimensionId;
			distance = -1;
			if(entity instanceof IEntityFateEcho) {
				distance = ((IEntityFateEcho) entity).getMaxRangeFromSource();
			}
			x = entity.posX;
			y = entity.posY;
			z = entity.posZ;
			if(entity instanceof IEntityFateEcho) {
				((IEntityFateEcho) entity).setStored();
			}
			else
				entity.setDead();
		}
		public boolean unStoreEntity(World world) {
			if(!isRightWorld(world))
				return false;
			Entity entity = EntityList.createEntityByName(className, world);
			if(entity==null)
				return false;
			entity.readFromNBT(entityData);
			return world.spawnEntityInWorld(entity);
		}
		
		public void saveNBTData(NBTTagCompound cmp) {
			cmp.setString(TAG_NAME, className);
			cmp.setTag(TAG_DATA, entityData);
			cmp.setInteger(TAG_DIM, dimension);
			cmp.setFloat(TAG_DISTANCE, distance);
			cmp.setDouble(TAG_X, x);
			cmp.setDouble(TAG_Y, y);
			cmp.setDouble(TAG_Z, z);
		}

		public void loadNBTData(NBTTagCompound cmp) {
			className = cmp.getString(TAG_NAME);
			entityData = cmp.getCompoundTag(TAG_DATA);
			dimension = cmp.getInteger(TAG_DIM); 
			distance = cmp.getFloat(TAG_DISTANCE);
			x = cmp.getDouble(TAG_X);
			y = cmp.getDouble(TAG_Y);
			z = cmp.getDouble(TAG_Z);
		}
		
		public float getMaxRangeFromSource() {
			return distance;
		}

		public double getDistanceSq(double x, double y, double z) {
			return 
					(this.x-x)*(this.x-x)+
					(this.y-y)*(this.y-y)+
					(this.z-z)*(this.z-z);
		}
		
		public boolean isRightWorld(World world) {
			return world.provider.dimensionId==dimension;
		}
	}
	
	private static class FateExtendedEntityProperties implements IExtendedEntityProperties {

		private final static int MAX_COLOR = 0xFFFFFF;
		
		private final static String TAG_FATE_NAME="fateName";
		private final static String TAG_FATE_DATA="fateData";
		private final static String TAG_FATE_COLOR1="fateColor1";
		private final static String TAG_FATE_COLOR2="fateColor2";
		private final static String TAG_FATE_KEY="fateCreateTime";
		
		IVigridrFate fate=null;
		String fateName="";
		EntityLivingBase entity;
		int fateColor1 = -1;
		int fateColor2 = -1;
		int prevStateCode = -1;
		
		@Override
		public void saveNBTData(NBTTagCompound cmp) {
			if(fate==null)
				return;
			cmp.setString(TAG_FATE_NAME, fateName);
			NBTTagCompound fateData = new NBTTagCompound();
			fate.writeToNBT(fateData);
			cmp.setTag(TAG_FATE_DATA, fateData);
			cmp.setInteger(TAG_FATE_COLOR1, fateColor1);
			cmp.setInteger(TAG_FATE_COLOR2, fateColor2);
		}

		@Override
		public void loadNBTData(NBTTagCompound cmp) {
			if(!cmp.hasKey(TAG_FATE_NAME)) {
				fateName = "";
				fate = null;
				if(entity.worldObj.isRemote) {
					if(cmp.hasKey(TAG_FATE_COLOR1)) {
						fateColor1 = cmp.getInteger(TAG_FATE_COLOR1);
						fateColor2 = cmp.getInteger(TAG_FATE_COLOR2);
						ItemFatePearl.setRenderColors(fateColor1,fateColor2, false);
					}
					else
						ItemFatePearl.setRenderColors(-1,-1, true);
				}
				return;
			}
			NBTTagCompound fateData = cmp.getCompoundTag(TAG_FATE_DATA);
			String newFateName = cmp.getString(TAG_FATE_NAME);
			if(newFateName != fateName) {
				setFate(newFateName);
			}
			if(fate!=null) {
				fate.readFromNBT(fateData);
				if(entity.worldObj.isRemote) {
					fateColor1 = cmp.getInteger(TAG_FATE_COLOR1);
					fateColor2 = cmp.getInteger(TAG_FATE_COLOR2);
					ItemFatePearl.setRenderColors(fateColor1,fateColor2, false);
				}
				FateHandler.addKey(fate.getKey(), entity);
			}
			else {
				fateName="";
				if(entity.worldObj.isRemote) {
					ItemFatePearl.setRenderColors(-1,-1, true);
				}
			}
		}

		@Override
		public void init(Entity entity, World world) {
			this.entity=(EntityLivingBase) entity;
		}
		
		public void setFate(String fateName) {
			clearFate();
			this.fate=FateHandler.tryCreateFate(fateName);
			if(fate!=null) {
				fate.setEntity(entity);
				fate.init();
				this.fateName=fateName;
			}
			setFateColors();
		}
		
		public void setFateColors() {
			World world = entity.worldObj;
			if(world.isRemote)
				return;
			if(fate==null) {
				this.fateColor1=-1;
				this.fateColor2=-1;
				ItemFatePearl.setRenderColors(fateColor1,fateColor2, true);
				return;
			}
			if(fateColor1==-1) {
				String entNameTag="";
				if(entity instanceof EntityPlayer) {
					entNameTag=((EntityPlayer) entity).getGameProfile().getId().toString();
				}
				else {
					entNameTag=entity.getUniqueID().toString();
				}
				String toHash = entNameTag+Long.toString(world.getSeed())+fateName;
				int hash = toHash.hashCode();
				Color col = new Color(hash%MAX_COLOR);
				Color col2 = new Color(col.getRed()/2+128,col.getGreen()/2+128,col.getBlue()/2+128);
				Color col3 = saturateColor(col2);				
				this.fateColor1=col3.getRGB()+MAX_COLOR;
			}
			fateColor2=fateColor1;
			for(int i = 0 ; i < fate.getStateCode() ; i++) {
				fateColor2*=fateColor1;
				fateColor2=fateColor2%MAX_COLOR;
			}
			while(fateColor2<0)
				fateColor2+=MAX_COLOR;
			ItemFatePearl.setRenderColors(fateColor1,fateColor2, false);
		}

		private Color saturateColor(Color col) {
			float[] hsv = Color.RGBtoHSB(col.getRed(), col.getBlue(), col.getGreen(), null);
			hsv[1]=hsv[1]/2.0F+.5F;
			return new Color(Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
		}

		public void clearFate() {
			if(fate==null)
				return;
			if(!entity.worldObj.isRemote) {
				FateHandler.removeKey(fate.getKey(), entity);
				this.fate.endFate();
			}
			this.fate=null;
			this.fateName="";
			this.fateColor1=-1;
			this.fateColor2=-1;
			this.prevStateCode=-1;
			MessageEntityData.updateExtendedEntityData(entity, TAG_FATE);
		}
		
		public IVigridrFate getFate() {
			return fate;
		}
		
		public void updateProgress() {
			if(entity.worldObj.isRemote)
				return;
			if(fate.isFateFinished()) {
				if(prevStateCode==0)
					sendFateMessage();
				this.clearFate();
				return;
			}
			int stateCode = fate.getStateCode();
			if(!entity.worldObj.isRemote && stateCode!=this.prevStateCode) {
				setFateColors();
				if(prevStateCode==0) {
					sendFateMessage();
				}
				this.prevStateCode=stateCode;
				MessageEntityData.updateExtendedEntityData(entity, TAG_FATE);
			}
		}

		public void sendFateMessage() {
			World world = entity.worldObj;
			if(world.isRemote)
				return;
			AxisAlignedBB box = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ).expand(FATE_MESSAGE_RANGE, FATE_MESSAGE_RANGE, FATE_MESSAGE_RANGE);
			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, box);
			for(EntityPlayer player : players) {
				IChatComponent message = this.fate.getStartMessage();
				player.addChatComponentMessage(message);
			}
		}

		public void applyFate(String name) {
			this.setFate(name);
			if(this.fate!=null) {
				String toHash = "";
				toHash+=fateName;
				String entNameTag="";
				if(entity instanceof EntityPlayer) {
					entNameTag=((EntityPlayer) entity).getGameProfile().getId().toString();
				}
				else {
					entNameTag=entity.getUniqueID().toString();
				}
				toHash+=entNameTag;
				toHash+=entity.worldObj.getTotalWorldTime();
				int key = toHash.hashCode();
				this.fate.setKey(key);
				FateHandler.addKey(key, entity);
			}
			MessageEntityData.updateExtendedEntityData(entity, TAG_FATE);
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
	    if (event.entity instanceof EntityLivingBase)
	    {
	         event.entity.registerExtendedProperties(TAG_FATE, new FateExtendedEntityProperties());
	    }
	}
	
	
	
	@SubscribeEvent
	public void applyGeneralTick(LivingUpdateEvent event) {
		if(hasFate(event.entityLiving)) {
			IVigridrFate fate = getFate(event.entityLiving);
			if(event.entityLiving.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && !fate.canApplyPeaceful(event.entityLiving))
				clearFate(event.entityLiving);
			else {
				fate.tickFate();
				tryPlaceStoredEntities(event.entityLiving);
				updateFateProgress(event.entityLiving);
			}
		}
	}
	
	public static IVigridrFate registerFate(IVigridrFate original, String fateName) {
		fatesOriginals.put(fateName, original);
		fatesClasses.put(fateName, (Class<IVigridrFate>) original.getClass());
		NBTTagCompound cmp = new NBTTagCompound();
		original.writeToNBT(cmp);
		fatesData.put(fateName, cmp);
		fatesNames.add(fateName);
		return original;
	}
	
	public static int getFateWeight(String fateName, EntityLivingBase entity, int exposureLevel) {
		return fatesOriginals.get(fateName).getWeight(entity, exposureLevel);
	}
	
	public static boolean canFateApplyToEntity(String fateName, EntityLivingBase entity) {
		IVigridrFate fate = fatesOriginals.get(fateName);
		return !(entity instanceof IEntityFateEcho) && fate.canApplyToEntity(entity) && (entity.worldObj.difficultySetting != EnumDifficulty.PEACEFUL || fate.canApplyPeaceful(entity));
	}
	
	public static boolean applyFateToEntity(String fateName, EntityLivingBase entity) {
		if(hasFate(entity))
			return false;
		if(!canFateApplyToEntity(fateName, entity))
			return false;
		getFateData(entity).applyFate(fateName);
		if(hasFate(entity))
			return true;
		return false;
	}
	
	public static boolean applyRandomFate(EntityLivingBase entity, int exposureLevel) {
		if(hasFate(entity))
			return false;
		int totalWeight = 0;
		ArrayList<String> validFates = new ArrayList<String>();
		for(String fate : fatesNames) {
			if(canFateApplyToEntity(fate, entity)) {
				totalWeight+=getFateWeight(fate, entity, exposureLevel);
				validFates.add(fate);
			}
		}
		if(validFates.isEmpty())
			return false;
		Collections.shuffle(validFates);
		for(String fate : validFates) {
			totalWeight-=getFateWeight(fate, entity, exposureLevel);
			if(totalWeight<=0) {
				return applyFateToEntity(fate, entity);
			}
		}
		return false;
	}
	
	public static boolean hasFate(EntityLivingBase entity) {
		return getFate(entity)!=null;
	}

	public static void clearFate(EntityLivingBase entity) {
		getFateData(entity).clearFate();
	}

	public static IVigridrFate getFate(EntityLivingBase entity) {
		return entity==null ? null : getFateData(entity).getFate();
	}

	public static void updateFateProgress(EntityLivingBase entity) {
		getFateData(entity).updateProgress();
	}

	public static String getFateKey(EntityLivingBase entity) {
		return getFateData(entity).fateName;
	}
	
	public static EntityLivingBase getEntityFromKey(int key) {
		return activeFateKeys.get(key);
	}
	
	private static FateExtendedEntityProperties getFateData(EntityLivingBase entity) {
		return (FateExtendedEntityProperties) entity.getExtendedProperties(TAG_FATE);
	}
	
	private static IVigridrFate tryCreateFate(String fateName) {
		try {
			return createFate(fateName);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static IVigridrFate createFate(String fateName) throws InstantiationException, IllegalAccessException {
		if(fateName=="")
			return null;
		Class<IVigridrFate> fateClass = fatesClasses.get(fateName);
		IVigridrFate createdFate = fateClass.newInstance();
		NBTTagCompound originalData = fatesData.get(fateName);
		createdFate.readFromNBT(originalData);
		createdFate.setName(fateName);
		return createdFate;
	}

	public static void init() {
//		registerFate(new VigridrFateSound("botania:spreaderFire", 0.05F, 0.85F, 5, 3, 20*30, .001F, .1F, 20*5, 5), "randomSpreaderFire");
		registerFate(new VigridrFateSpawnFenrir(5, 3, 8, 20*30, .001F, .1F, 20*5, 5), "fenrir");
		registerFate(new VigridrFateSpawnPhantomEndermen(8, 20*30, .001F, .1F, 20*5, 5), "endermen");
		registerFate(new VigridrFateFireBlade(20,50,4,8,0,5, 1, 20*30, .001F, .1F, 20*5, 5), "fireblade");
//		registerFate(new VigridrFateSpawnNidhogg(5, 3, 8, 20*30, .001F, .1F, 20*5, 5), "nidhogg");
	}
	public static void printDebugData(EntityLivingBase entity, EntityPlayer player) {
		FateExtendedEntityProperties data = getFateData(entity);
		if(!player.worldObj.isRemote) {
				player.addChatComponentMessage(new ChatComponentText("Entity Name:  "+entity.getCommandSenderName()));
			if(data.fate!=null) {
				player.addChatComponentMessage(new ChatComponentText("Fate Name:    "+data.fateName));
				player.addChatComponentMessage(new ChatComponentText("Fate:         "+data.fate));
				player.addChatComponentMessage(new ChatComponentText("Fate Color 1: "+Integer.toHexString(data.fateColor1)));
				player.addChatComponentMessage(new ChatComponentText("Fate Color 2: "+Integer.toHexString(data.fateColor2)));
				player.addChatComponentMessage(new ChatComponentText("Fate State:   "+data.fate.getStateCode()));
			}
			else
				player.addChatComponentMessage(new ChatComponentText("Fate Name:    NO FATE"));
		}
	}
	
	
	public static void addEcho(IEntityFateEcho echo) {
		int key = echo.getKey();
		ArrayList<IEntityFateEcho> echos;
		if(activeEchos.containsKey(key))
			echos=activeEchos.get(key);
		else {
			echos=new ArrayList<IEntityFateEcho>();
			activeEchos.put(key, echos);
		}
		if(!echos.contains(echo))
			echos.add(echo);
	}
	public static void removeEcho(IEntityFateEcho echo) {
		if(activeEchos.containsKey(echo.getKey()))
			activeEchos.get(echo.getKey()).remove(echo);
	}
	public static List<IEntityFateEcho> getEchos(int key) {
		if(activeEchos.containsKey(key))
			return new ArrayList<IEntityFateEcho>(activeEchos.get(key));
		return new ArrayList<IEntityFateEcho>();
	}
	public static int countEchos(int key) {
		return getEchos(key).size();
	}
	public static void storeEntity(EntityLivingBase entity, int key) {
		IVigridrFate fate = getFate(activeFateKeys.get(key));
		if(fate==null) {
			entity.setDead();
			return;
		}
		IVigridrFateSpawning spawning = (IVigridrFateSpawning) fate;
		spawning.storeEntityData(new StoredEntityData(entity));;
	}
	
	public static List<IVigridrFate> getAllActiveFates() {
		return new ArrayList(allActiveFates);
	}
	
	public static void storeEntity(EntityLivingBase entity) {
		storeEntity(entity, ((IEntityFateEcho) entity).getKey());
	}
	public static void storeEntity(IEntityFateEcho entity) {
		storeEntity((EntityLivingBase) entity, entity.getKey());
	}
	public static boolean checkIfEchoInRange(IEntityFateEcho echo, World world, double x, double y, double z) {
		float maxDist = echo.getMaxRangeFromSource();
		if(maxDist == -2)
			return true;
		if(world.provider.dimensionId!=echo.getWorldObj().provider.dimensionId)
			return false;
		if(maxDist == -1)
			return true;
		return echo.getDistanceSq(x, y, z)<=maxDist*maxDist;
	}
	public static boolean checkIfStoredEchoInRange(StoredEntityData echo, World world, double x, double y, double z) {
		float maxDist = echo.getMaxRangeFromSource();
		if(maxDist == -2)
			return true;
		if(!echo.isRightWorld(world))
			return false;
		if(maxDist == -1)
			return true;
		return echo.getDistanceSq(x, y, z)<=maxDist*maxDist;
	}

	public static boolean checkIfEchoInRange(IEntityFateEcho echo) {
		int key = echo.getKey();
		EntityLivingBase source = activeFateKeys.get(key);
		if(source==null || source.isDead)
			return false;
		return checkIfEchoInRange(echo, source.worldObj, source.posX, source.posY, source.posZ);
	}
	public static void storeEntityIfNessisary(IEntityFateEcho echo) {
		if(!checkIfEchoInRange(echo))
			storeEntity(echo);
	}
	public static int countAllEchosStoredAndUnstored(IVigridrFateSpawning fate) {
		return countEchos(fate.getKey())+fate.getStoredEntityData().size();
	}

	public static void doUpdateForEcho(IEntityFateEcho echo) {
		if(echo.getWorldObj().isRemote)
			return;
		Entity entity = (Entity) echo;
		if(!echo.getStored()) {
			addEcho(echo);
			storeEntityIfNessisary(echo);
		}
		else
			entity.setDead();
	}
	public static void setDead(IEntityFateEcho echo) {
		if(!echo.getWorldObj().isRemote)
			removeEcho(echo);
	}
	
	protected static void addKey(int key, EntityLivingBase entity) {
		activeFateKeys.put(key, entity);
		allActiveFates.add(getFate(entity));
	}
	protected static void removeKey(int key, EntityLivingBase entity) {
		activeFateKeys.remove(key);
		allActiveFates.remove(getFate(entity));
		if(activeEchos.containsKey(key)) {
			List<IEntityFateEcho> ents = new ArrayList<IEntityFateEcho>(activeEchos.get(key));
			for(IEntityFateEcho ent : ents)
				((Entity) ent).setDead();
			activeEchos.remove(key);
		}
	}
	
	public static void tryPlaceStoredEntities(EntityLivingBase source) {
		if(source.worldObj.isRemote)
			return;
		IVigridrFate fate = getFate(source);
		if(fate instanceof IVigridrFateSpawning) {
			IVigridrFateSpawning spawning=(IVigridrFateSpawning) fate;
			List<StoredEntityData> ents = spawning.getStoredEntityData();
			ArrayList<StoredEntityData> newEnts = new ArrayList<StoredEntityData>();
			for(StoredEntityData echo : ents) {
				if(!tryPlaceStoredEntity(source, echo))
					newEnts.add(echo);
			}
			spawning.setStoredEntityData(newEnts);
		}
	}
	
	public static boolean tryPlaceStoredEntity(Entity source, StoredEntityData echo) {
		if(!checkIfStoredEchoInRange(echo, source.worldObj, source.posX, source.posY, source.posZ))
			return false;
		return echo.unStoreEntity(source.worldObj);
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event) {
		for(List list : event.getChunk().entityLists) {
			for(Object o : list) {
				Entity ent = (Entity) o;
				onEntityUnload(ent);
			}
		}
	}
	
	protected void onEntityUnload(Entity entity) {
		if(entity instanceof EntityLivingBase) {
			EntityLivingBase entLiving = (EntityLivingBase) entity;
			if(hasFate(entLiving)) {
				int key = getFate(entLiving).getKey();
				if(activeEchos.containsKey(key)) {
					ArrayList<IEntityFateEcho> echos = new ArrayList<IEntityFateEcho>(activeEchos.get(key));
					for(IEntityFateEcho echo : echos) {
						storeEntity(echo);
					}
				}
			}
			if(entity instanceof IEntityFateEcho) {
				storeEntity(entLiving);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if(event.entity.worldObj.isRemote)
			return;
		if((!(event.entity instanceof EntityPlayer)) && hasFate(event.entityLiving)) {
			int key = getFate(event.entityLiving).getKey();
			removeKey(key, event.entityLiving);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogOut(PlayerLoggedOutEvent event) {
		onEntityUnload(event.player);
	}
}
