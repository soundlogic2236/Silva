package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.lib.LibAugments;
import soundlogic.silva.common.network.MessageEntityData;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.Botania;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EyeHandler {

	private abstract static class SearchType {
		
		private static ArrayList<SearchType> types = new ArrayList<SearchType>();
		
		private static final SearchType INVENTORY = new SearchTypeInventory("", LibAugments.FIND_EYE);
		private static final SearchType REDSTONE_SIGNAL = new SearchTypeRedstone("power", LibAugments.REDSTONE_EYE);
		
		private String word;
		private String tag;
		
		private SearchType(String word, String tag) {
			this.word=word;
			this.tag=tag;
			types.add(this);
		}
		
		public void renderAtBlock(String request, World world, int x, int y, int z) {
			float m = 0.02F;
			BotaniaAccessHandler.setWispFXDepthTest(false);
			BotaniaAccessHandler.wispFX(world, x + (float) Math.random(), y + (float) Math.random(), z + (float) Math.random(), 1F, 0F, 0F, 0.15F + 0.05F * (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		public void renderAtEntity(String request, World world, Entity e) {
			BotaniaAccessHandler.setWispFXDepthTest(Math.random() < 0.6);
			BotaniaAccessHandler.wispFX(world, e.posX + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.posY + e.height, e.posZ + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 1F, 0F, 0F, 0.15F + 0.05F * (float) Math.random(), -0.05F - 0.03F * (float) Math.random());
		}
		
		public abstract boolean checkBlock(String request, World world, int x, int y, int z);
		
		private static SearchType getType(String tag) {
			for(SearchType search : types) {
				if(search.tag.equals(tag))
					return search;
			}
			return null;
		}

		private static SearchType getTypeForWord(String word) {
			for(SearchType search : types) {
				if(search.word.equals(word))
					return search;
			}
			return INVENTORY;
		}
		
		private static String getTagForWord(String word) {
			return getTypeForWord(word).tag;
		}

		private static class SearchTypeInventory extends SearchType {
			private SearchTypeInventory(String word, String tag) {
				super(word, tag);
			}

			@Override
			public boolean checkBlock(String request, World world, int x, int y, int z) {
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile != null && tile instanceof IInventory) {
					IInventory inv = (IInventory) tile;
					if(scanInventory(inv, request))
						return true;
				}
				return false;
			}
		}
		private static class SearchTypeRedstone extends SearchType {
			private SearchTypeRedstone(String word, String tag) {
				super(word, tag);
			}
			public void renderAtBlock(String request, World world, int x, int y, int z) {
				float m = 0.02F;
				BotaniaAccessHandler.setWispFXDepthTest(false);
				if(Math.random()<.3)
					BotaniaAccessHandler.wispFX(world, x + .3F + (float) Math.random() * .4F, y + .3F + (float) Math.random() * .4F, z + .3F + (float) Math.random() * .4F, 1F, 0F, 0F, 0.15F + 0.05F * (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
			}
			@Override
			public boolean checkBlock(String request, World world, int x, int y, int z) {
				for(int side = 0; side < 6; side++) {
					if(world.getIndirectPowerOutput(x, y, z, side)) {
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public static ResourceLocation shader_bumpy = new ResourceLocation("minecraft:shaders/post/bumpy.json");

	private final static String TAG_EYE = "silvaEye";
	
	private final static int SEARCH_DURATION = 20 * 60;
	private final static int SEARCH_RANGE = 24;
	
	private final static Pattern regex = Pattern.compile("eye spy (.+)");
	
	private static boolean lastTickWasEye=false;

	public static class EyeData implements IExtendedEntityProperties {
		private final static String TAG_REQUEST = "request";
		private final static String TAG_REQUEST_TIME = "requestTime";
		private final static String TAG_POSITIONS = "highlightPositions";
		private final static String TAG_EYE_TAG = "eyeRequirement";
		private final static String TAG_LAST_REQUEST = "lastRequest";

		private String request = "";
		private int request_time = 0;
		private String positions = "";
		private String eyeTag = "";
		private SearchType type;
		private String lastRequest = "";
		
		private Entity entity;
		
		@Override
		public void saveNBTData(NBTTagCompound cmp) {
			cmp.setString(TAG_REQUEST, request);
			cmp.setInteger(TAG_REQUEST_TIME, request_time);
			cmp.setString(TAG_POSITIONS, positions);
			cmp.setString(TAG_EYE_TAG, eyeTag);
			cmp.setString(TAG_LAST_REQUEST, lastRequest);
		}
		@Override
		public void loadNBTData(NBTTagCompound cmp) {
			request=cmp.getString(TAG_REQUEST);
			request_time=cmp.getInteger(TAG_REQUEST_TIME);
			positions=cmp.getString(TAG_POSITIONS);
			eyeTag=cmp.getString(TAG_EYE_TAG);
			type=SearchType.getType(eyeTag);
			lastRequest=cmp.getString(TAG_LAST_REQUEST);
		}
		@Override
		public void init(Entity entity, World world) {
			this.entity=entity;
		}
		public void clear() {
			this.request="";
			this.request_time=0;
			this.positions="";
			this.eyeTag="";
			send();
		}
		public void tick() {
			if(this.request_time>0)
				this.request_time--;
			if(this.request_time==0)
				clear();
		}
		public void send() {
			MessageEntityData.updateExtendedEntityData(entity, TAG_EYE);
		}
		public void setRequest(String request, int searchDuration) {
			this.request=request;
			this.request_time=searchDuration;
			this.eyeTag=SearchType.getTagForWord(request);
			this.type=SearchType.getType(eyeTag);
			this.lastRequest=this.request;
		}
	}

	public static boolean hasEye(EntityPlayer player, EyeData data) {
		return hasEye(player, data.eyeTag);
	}
	
	public static boolean hasEye(EntityPlayer player, String tag) {
		ItemStack stack = player.getEquipmentInSlot(4);
		if(stack==null)
			return false;
		return DarkElfAugmentHandler.getAugments(stack).contains(tag);
	}
	
	public static void updatePlayerServer(EntityPlayer player) {
		EyeData data = getEyeData(player);
		data.tick();
		if(!data.request.equals("") && !hasEye(player,data)) {
			data.clear();
			return;
		}
		if(data.type==null) {
			data.clear();
			return;
		}
		
		StringBuilder positionsBuilder = new StringBuilder();

		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		int range = SEARCH_RANGE/2;
		for(int i = -range; i < range + 1; i++) {
			for(int j = -range; j < range + 1; j++) {
				for(int k = -range; k < range + 1; k++) {
					int xp = x + i;
					int yp = y + j;
					int zp = z + k;
					if(data.type.checkBlock(data.request, player.worldObj, xp, yp, zp))
						positionsBuilder.append(xp).append(",").append(yp).append(",").append(zp).append(";");
				}
			}
		}
		String positions = positionsBuilder.toString();
		if(!data.positions.equals(positions)) {
			data.positions=positions;
			data.send();
		}
	}
	public static void updatePlayerClient(EntityPlayer player) {
		EyeData data = getEyeData(player);
		data.tick();
		if(data.request=="" || !hasEye(player,data)) {
			if(lastTickWasEye)
				Silva.proxy.setShader(null);
			lastTickWasEye=false;
			return;
		}
		lastTickWasEye=true;
		
		SearchType type = data.type;
		
		Silva.proxy.setShader(shader_bumpy);
		String[] tokens = data.positions.split(";");

		for(String token : tokens) {
			if(token.isEmpty())
				continue;

			if(token.contains(",")) {
				String[] tokens_ = token.split(",");
				int x = Integer.parseInt(tokens_[0]);
				int y = Integer.parseInt(tokens_[1]);
				int z = Integer.parseInt(tokens_[2]);
				type.renderAtBlock(data.request, player.worldObj,x,y,z);
			} else {
				int id = Integer.parseInt(token);
				Entity e = player.worldObj.getEntityByID(id);

				if(e != null && Math.random() < 0.6) {
					type.renderAtEntity(data.request, player.worldObj,e);
				}
			}
		}
		BotaniaAccessHandler.setWispFXDepthTest(true);
	}
		
	private static boolean scanInventory(IInventory inv, String request) {
		for(int l = 0; l < inv.getSizeInventory(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			if(CorporeaHelper.stacksMatch(istack, request))
				return true;
		}
		return false;
	}

	public static EyeData getEyeData(EntityPlayer player) {
		return (EyeData) player.getExtendedProperties(TAG_EYE);
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if(!(event.entityLiving instanceof EntityPlayer))
			return;
		if(event.entityLiving.worldObj.isRemote)
			updatePlayerClient((EntityPlayer) event.entityLiving);
		else
			updatePlayerServer((EntityPlayer) event.entityLiving);
	}
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
	    if (event.entity instanceof EntityPlayer)
	    {
	         event.entity.registerExtendedProperties(TAG_EYE, new EyeData());
	    }
	}
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChatMessage(ServerChatEvent event) {
		EyeData data = getEyeData(event.player);
		String message = event.message.toLowerCase().trim();
		Matcher matcher = regex.matcher(message);
		if(!matcher.matches())
			return;
		String q = matcher.group(1);
		if(q.equals("again")) {
			if(data.lastRequest.equals(""))
				return;
			q=data.lastRequest;
		}
		if(q.equals("stop")) {
			if(data.request.equals(""))
				return;
			data.clear();
			event.setCanceled(true);
			return;
		}
		if(q.equals("this")) {
			ItemStack stack = event.player.getCurrentEquippedItem();
			if(stack==null)
				return;
			q = stack.getDisplayName().toLowerCase().trim();
		}
		SearchType type = SearchType.getTypeForWord(q);
		if(!hasEye(event.player,type.tag))
			return;
		else {
			data.setRequest(q, SEARCH_DURATION);
			data.send();
		}
		
		event.setCanceled(true);
	}

}
