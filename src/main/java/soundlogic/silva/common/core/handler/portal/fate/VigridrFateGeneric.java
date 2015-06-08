package soundlogic.silva.common.core.handler.portal.fate;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.common.lib.LibGUI;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCache;

public abstract class VigridrFateGeneric implements IVigridrFate {

	private final String TAG_NAME = "name";
	private final String TAG_KEY = "key";
	
	String name;
	int key;

	private final String TAG_START_CHANCE = "startChance";
	private final String TAG_MIN_TICKS_BEFORE_START = "minTicksBeforeStart";
	private final String TAG_TICKS = "ticks";
	private final String TAG_STARTED = "started";

	public Random random = new Random();
	int weight;
	
	public Entity entity;
	float startChance;
	int minTicksBeforeStart;
	int ticks;
	boolean started;
	
	public VigridrFateGeneric() {
		
	}
	
	public VigridrFateGeneric(int minTicksBeforeStart, float startChance, int weight) {
		this.startChance=startChance;
		this.minTicksBeforeStart=minTicksBeforeStart;
		this.weight=weight;
		this.ticks=-1;
		this.started=false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setFloat(TAG_START_CHANCE, startChance);
		cmp.setInteger(TAG_MIN_TICKS_BEFORE_START, minTicksBeforeStart);
		cmp.setInteger(TAG_TICKS, ticks);
		cmp.setBoolean(TAG_STARTED, started);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		startChance = cmp.getFloat(TAG_START_CHANCE);
		minTicksBeforeStart = cmp.getInteger(TAG_MIN_TICKS_BEFORE_START);
		ticks = cmp.getInteger(TAG_TICKS);
		started = cmp.getBoolean(TAG_STARTED);
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public void start() {
		started=true;
	}
	
	@Override
	public void tickFate() {
		ticks++;
		if(!hasStarted() && ticks>=minTicksBeforeStart) {
			if(random.nextFloat()<startChance)
				start();
		}
	}
	

	@Override
	public void init() {
		//NO OP
	}

	@Override
	public void setEntity(Entity entity) {
		this.entity=entity;
	}
	
	@Override
	public void endFate() {
		// NO OP
	}

	@Override
	public int getWeight(Entity entity, int exposureLevel) {
		return weight;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setKey(int key) {
		this.key=key;
	}
	public int getKey() {
		return key;
	}
	public String getUnlocalizedStartMessage() {
		return LibGUI.FATE_MESSAGE_PREFIX+this.getName();
	}
	public IChatComponent getStartMessage() {
		IChatComponent message = new ChatComponentTranslation(getUnlocalizedStartMessage(), new Object[0]);
		message.getChatStyle().setItalic(true);
		return message;
	}
	
	public boolean tryForceStart(int tries) {
		int i = 0;
		while(!this.hasStarted() && i<tries) {
			this.start();
		}
		return this.hasStarted();
	}
	
	@SideOnly(Side.CLIENT)
	public void renderWorld(WorldRenderer renderer, RenderBlocks renderBlocks, ChunkCache chunkCache, int pass) {
		// NO OP
	}
	@SideOnly(Side.CLIENT)
	public void renderWorldForPlayer(WorldRenderer renderer, RenderBlocks renderBlocks, ChunkCache chunkCache, int pass) {
		// NO OP
	}
	@SideOnly(Side.CLIENT)
	public boolean renderEntity(EntityLivingBase entity, RendererLivingEntity renderer, double x, double y, double z) {
		return false; // NO OP
	}
	@SideOnly(Side.CLIENT)
	public boolean renderPlayer(Entity entity, RenderPlayer renderer, float partialRenderTick) {
		return false; // NO OP
	}
}
