package soundlogic.silva.common.core.handler.portal.fate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class VigridrFateSound extends VigridrFateGenericRepeating {

	private final String TAG_SOUND_TO_PLAY = "soundToPlay";
	private final String TAG_VOLUME = "volume";
	private final String TAG_PITCH = "pitch";
	private final String TAG_DISTANCE = "distance";
	
	String soundToPlay;
	float volume;
	float pitch;
	float distance;
	
	public VigridrFateSound() {
		
	}
	
	public VigridrFateSound(String soundToPlay, float volume, float pitch, float distance, int timesToDo, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(timesToDo, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
		this.soundToPlay=soundToPlay;
		this.volume=volume;
		this.pitch=pitch;
		this.distance=distance;
	}
	
	@Override
	public boolean canApplyToEntity(Entity entity) {
		return entity instanceof EntityPlayer;
	}

	@Override
	public boolean canApplyPeaceful(Entity entity) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
		cmp.setString(TAG_SOUND_TO_PLAY, soundToPlay);
		cmp.setFloat(TAG_VOLUME, volume);
		cmp.setFloat(TAG_PITCH, pitch);
		cmp.setFloat(TAG_DISTANCE, distance);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
		soundToPlay=cmp.getString(TAG_SOUND_TO_PLAY);
		volume=cmp.getFloat(TAG_VOLUME);
		pitch=cmp.getFloat(TAG_PITCH);
		distance=cmp.getFloat(TAG_DISTANCE);
	}

	@Override
	public boolean doFate() {
		double[] soundCoords = getSoundCoords();
		entity.worldObj.playSoundEffect(soundCoords[0], soundCoords[1], soundCoords[2], soundToPlay, volume, pitch);
		return true;
	}
	
	public double[] getSoundCoords() {
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		double ang = random.nextDouble()*2*Math.PI;
		x+=distance*Math.sin(ang);
		z+=distance*Math.cos(ang);
		return new double[] {x,y,z};
	}

}
