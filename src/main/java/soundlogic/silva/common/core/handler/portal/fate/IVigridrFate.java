package soundlogic.silva.common.core.handler.portal.fate;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

public interface IVigridrFate {

	public boolean canApplyToEntity(Entity entity);
	public boolean isFateFinished();
	public void writeToNBT(NBTTagCompound cmp);
	public void readFromNBT(NBTTagCompound cmp);
	public void tickFate();
	public void init();
	public int getWeight(Entity entity, int exposureLevel);
	public void setEntity(Entity entity);
	public void endFate();
	public int getStateCode();
	public void setName(String name);
	public String getName();
	public void setKey(int key);
	public int getKey();
	public IChatComponent getStartMessage();
}
