package soundlogic.silva.common.core.handler.portal.fate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler.StoredEntityData;
import soundlogic.silva.common.entity.IEntityFateEcho;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class VigridrFateSpawnEntitySimple extends VigridrFateGenericRepeating implements IVigridrFateSpawning {

	private final String TAG_TRIALS = "trials";
	private final String TAG_MIN_DISTANCE = "minDistance";
	private final String TAG_MAX_DISTANCE = "maxDistance";
	private final String TAG_ENTITY_NAME = "entityName";
	private final String TAG_ENTITY_NBT = "entityNBT";
	private final String TAG_TOTAL_SPAWNED = "totalSpawned";
	private final String TAG_STORED = "entitiesSpawned";
	private final String TAG_MOB = "entityIsMob";
	
	int trialsPerSpawnAttempt;
	float minDistance;
	float maxDistance;
	String toSpawnName;
	NBTTagCompound toSpawnNBT;
	boolean isMob;
	
	int totalSpawned = 0;
	
	List<StoredEntityData> stored = new ArrayList<StoredEntityData>();
	
	public VigridrFateSpawnEntitySimple() {
		
	}

	@Override
	public boolean isFateFinished() {
		return super.isFateFinished() && FateHandler.countAllEchosStoredAndUnstored(this)==0 && this.ticksSinceLastDone>3;
	}
	
	@Override
	public int getStateCode() {
		return super.getStateCode() + FateHandler.countAllEchosStoredAndUnstored(this)*timesToDo;
	}
	
	@Override
	public void endFate() {
		if(FateHandler.activeEchos.containsKey(key))
			for(IEntityFateEcho entity : FateHandler.activeEchos.get(key)) {
				((Entity) entity).setDead();
			}
	}
	
	public VigridrFateSpawnEntitySimple(Entity toSpawn, int trialsPerSpawnAttempt, float minDistance, float maxDistance, int timesToDo, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(timesToDo, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
		this.trialsPerSpawnAttempt=trialsPerSpawnAttempt;
		this.minDistance=minDistance;
		this.maxDistance=maxDistance;
		this.toSpawnName=(String) EntityList.classToStringMapping.get(toSpawn.getClass());
		this.toSpawnNBT=new NBTTagCompound();
		toSpawn.writeToNBT(this.toSpawnNBT);
		this.isMob=toSpawn instanceof IMob;
	}
	
	@Override
	public boolean canApplyToEntity(Entity entity) {
		return entity instanceof EntityPlayer;
	}

	@Override
	public boolean canApplyPeaceful(Entity entity) {
		return !isMob;
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
		cmp.setInteger(TAG_TRIALS, trialsPerSpawnAttempt);
		cmp.setFloat(TAG_MIN_DISTANCE, minDistance);
		cmp.setFloat(TAG_MAX_DISTANCE, maxDistance);
		cmp.setString(TAG_ENTITY_NAME, toSpawnName);
		cmp.setTag(TAG_ENTITY_NBT, toSpawnNBT);
		cmp.setInteger(TAG_TOTAL_SPAWNED, totalSpawned);
		cmp.setBoolean(TAG_MOB, isMob);
		
		if(!stored.isEmpty()) {
			cmp.setInteger(TAG_STORED, stored.size());
			for(int i = 0 ; i < stored.size() ; i++) {
				NBTTagCompound str = new NBTTagCompound();
				stored.get(i).saveNBTData(str);
				cmp.setTag(TAG_STORED+i, str);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
		trialsPerSpawnAttempt=cmp.getInteger(TAG_TRIALS);
		minDistance=cmp.getFloat(TAG_MIN_DISTANCE);
		maxDistance=cmp.getFloat(TAG_MAX_DISTANCE);
		toSpawnName=cmp.getString(TAG_ENTITY_NAME);
		toSpawnNBT=cmp.getCompoundTag(TAG_ENTITY_NBT);
		totalSpawned=cmp.getInteger(TAG_TOTAL_SPAWNED);
		isMob=cmp.getBoolean(TAG_MOB);
		
		stored.clear();
		if(cmp.hasKey(TAG_STORED)) {
			int size = cmp.getInteger(TAG_STORED);
			for(int i = 0 ; i < size ; i++) {
				NBTTagCompound str = cmp.getCompoundTag(TAG_STORED+i);
				StoredEntityData data = new StoredEntityData();
				data.loadNBTData(str);
				stored.add(data);
			}
		}
	}

	@Override
	public boolean doFate() {
		if(entity.worldObj.isRemote)
			return false;
		if(trySpawnEntity(entity.worldObj))
			return true;
		return false;
	}

	public boolean trySpawnEntity(World world) {
		double x = this.entity.posX;
		double y = this.entity.posY;
		double z = this.entity.posZ;
		double r = (maxDistance-minDistance)*Math.random()+minDistance;
		double a1 = Math.random()<.3D ? 0 : Math.random()*Math.PI*2;
		double a2 = Math.random()*Math.PI*2;
		Entity entity = tryCreateEntity(world);
		for(int i = 0 ; i < trialsPerSpawnAttempt ; i ++) {
			x+=r*Math.cos(a1)*Math.sin(a2);
			y+=r*Math.sin(a1);
			z+=r*Math.cos(a2);
			if(a1==0) {
				entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
				y+=this.entity.boundingBox.minY-entity.boundingBox.minY;
				entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
			}
			if(trySpawnEntity(world, x, y, z, entity)!=null)
				return true;
		}
		return false;
	}

	protected Entity tryCreateEntity(World world) {
		Entity entity = EntityList.createEntityByName(toSpawnName, world);
		entity.readFromNBT(toSpawnNBT);
		if(entity instanceof IEntityFateEcho) {
			((IEntityFateEcho) entity).setKey(this.getKey());
		}
		return entity;
	}

	protected Entity trySpawnEntity(World world, double x, double y, double z, Entity entity) {
		entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
		if(entityCanSpawn(world, entity, x, y, z)) {
			if(world.spawnEntityInWorld(entity)) {
				totalSpawned++;
				return entity;
			}
			return null;
		}
		else
			return null;
	}

	protected boolean entityCanSpawn(World world, Entity entity, double x, double y, double z) {
		return (entity instanceof EntityLiving ? ((EntityLiving) entity).getCanSpawnHere() : true) && World.doesBlockHaveSolidTopSurface(world, (int)x, (int)entity.boundingBox.minY-1, (int)z);
	}

	@Override
	public void storeEntityData(StoredEntityData data) {
		stored.add(data);
	}

	@Override
	public List<StoredEntityData> getStoredEntityData() {
		return stored;
	}

	@Override
	public void setStoredEntityData(List<StoredEntityData> data) {
		stored=data;
	}

}
