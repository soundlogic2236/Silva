package soundlogic.silva.common.core.handler.portal.fate;

import java.util.ArrayList;

import soundlogic.silva.common.entity.EntityFenrirEcho;
import soundlogic.silva.common.entity.EntityPhantomEndermanEcho;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class VigridrFateSpawnPhantomEndermen extends VigridrFateSpawnEntitySimple {

	int numMen = 0;
	
	public VigridrFateSpawnPhantomEndermen() {
		
	}
	
	public VigridrFateSpawnPhantomEndermen(int enderMenToSpawn, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(new EntityPhantomEndermanEcho(null), enderMenToSpawn, 0, 0, 1, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
	}

	public boolean trySpawnEntity(World world) {
		double x = this.entity.posX;
		double y = this.entity.posY;
		double z = this.entity.posZ;
		for(int i = 0 ; i < trialsPerSpawnAttempt ; i ++) {
			Entity entity = tryCreateEntity(world);
			y+=this.entity.boundingBox.minY-entity.boundingBox.minY;
			entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
			trySpawnEntity(world, x, y, z, entity);
		}
		return true;
	}
	
	protected Entity tryCreateEntity(World world) {
		EntityPhantomEndermanEcho entity = (EntityPhantomEndermanEcho) super.tryCreateEntity(world);
		entity.setAngle((float) ((((float)numMen)/(float)this.trialsPerSpawnAttempt)*2F*Math.PI));
		numMen++;
		numMen=numMen%trialsPerSpawnAttempt;
		return entity;
	}

	protected boolean entityCanSpawn(World world, Entity entity, double x, double y, double z) {
		return true;
	}
}
