package soundlogic.silva.common.core.handler.portal.fate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class VigridrFateGenericRepeating extends VigridrFateGeneric {

	private final String TAG_TIMES_TO_DO = "timesToDo";
	private final String TAG_MIN_TICKS_BETWEEN_DOING = "minTicksBetweenDoing";
	private final String TAG_DO_CHANCE = "doChance";
	
	int timesToDo;
	int minTicksBetweenDoing;
	float doChance;
	
	private final String TAG_TIMES_DONE = "timesDone";
	private final String TAG_TICKS_SINCE_LAST_DONE = "ticksSinceLastDone";
	
	int timesDone;
	int ticksSinceLastDone;

	public VigridrFateGenericRepeating() {
		
	}
	
	public VigridrFateGenericRepeating(int timesToDo, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(minTicksBeforeStart, startChance, weight);
		this.timesToDo=timesToDo;
		this.minTicksBetweenDoing=minTicksBetweenDoing;
		this.doChance=doChance;
	}
	
	@Override
	public boolean isFateFinished() {
		return timesDone>=timesToDo;
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
		cmp.setInteger(TAG_TIMES_TO_DO, timesToDo);
		cmp.setInteger(TAG_MIN_TICKS_BETWEEN_DOING, minTicksBetweenDoing);
		cmp.setFloat(TAG_DO_CHANCE, doChance);
		cmp.setInteger(TAG_TICKS_SINCE_LAST_DONE, ticksSinceLastDone);
		cmp.setInteger(TAG_TIMES_DONE, timesDone);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
		timesToDo=cmp.getInteger(TAG_TIMES_TO_DO);
		minTicksBetweenDoing=cmp.getInteger(TAG_MIN_TICKS_BETWEEN_DOING);
		doChance=cmp.getFloat(TAG_DO_CHANCE);
		ticksSinceLastDone=cmp.getInteger(TAG_TICKS_SINCE_LAST_DONE);
		timesDone=cmp.getInteger(TAG_TIMES_DONE);
	}

	@Override
	public void tickFate() {
		super.tickFate();
		ticksSinceLastDone++;
		if(hasStarted() && ticksSinceLastDone>=minTicksBetweenDoing) {
			if(random.nextFloat()<doChance)
				doAction();
		}
	}
	
	@Override
	public void start() {
		super.start();
		doAction();
	}
	
	private void doAction() {
		if(timesDone>=timesToDo)
			return;
		if(doFate()) {
			ticksSinceLastDone=0;
			timesDone++;
		}
	}
	
	public abstract boolean doFate();
	

	@Override
	public int getStateCode() {
		return timesDone;
	}
	
}
