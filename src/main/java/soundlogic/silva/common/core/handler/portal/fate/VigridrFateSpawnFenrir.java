package soundlogic.silva.common.core.handler.portal.fate;

import java.util.ArrayList;

import soundlogic.silva.common.entity.EntityFenrirEcho;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class VigridrFateSpawnFenrir extends VigridrFateSpawnEntitySimple {

	public VigridrFateSpawnFenrir() {
		
	}
	
	public VigridrFateSpawnFenrir(int trialsPerSpawnAttempt, float minDistance, float maxDistance, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(new EntityFenrirEcho(null), trialsPerSpawnAttempt, minDistance, maxDistance, 1, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
	}
	
	protected Entity tryCreateEntity(World world) {
		EntityFenrirEcho entity = (EntityFenrirEcho) super.tryCreateEntity(world);
		entity.setPlayerTarget((EntityPlayer) this.entity);
		return entity;
	}
}
