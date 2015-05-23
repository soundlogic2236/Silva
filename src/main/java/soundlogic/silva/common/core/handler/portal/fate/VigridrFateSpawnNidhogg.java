package soundlogic.silva.common.core.handler.portal.fate;

import java.util.ArrayList;

import soundlogic.silva.common.entity.EntityFenrirEcho;
import soundlogic.silva.common.entity.EntityNidhoggEcho;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class VigridrFateSpawnNidhogg extends VigridrFateSpawnEntitySimple {

	boolean done=false;
	int lifespan=20*60*3;
	
	public VigridrFateSpawnNidhogg() {
		
	}
	
	public VigridrFateSpawnNidhogg(int trialsPerSpawnAttempt, float minDistance, float maxDistance, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(new EntityNidhoggEcho(null), trialsPerSpawnAttempt, minDistance, maxDistance, 1, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
	}

	@Override
	public boolean isFateFinished() {
		return done;
	}
	
	public void tickFate() {
		super.tickFate();
		if(hasStarted() && ticksSinceLastDone>=lifespan)
			done=true;
	}
	
	protected Entity tryCreateEntity(World world) {
		EntityNidhoggEcho entity = (EntityNidhoggEcho) super.tryCreateEntity(world);
		return entity;
	}
	protected boolean entityCanSpawn(World world, Entity entity, double x, double y, double z) {
		return true;
	}
}
