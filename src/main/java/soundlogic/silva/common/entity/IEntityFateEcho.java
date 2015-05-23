package soundlogic.silva.common.entity;

import net.minecraft.world.World;

public interface IEntityFateEcho {
	public void setKey(int key);
	public int getKey();
	public float getMaxRangeFromSource();
	public double getDistanceSq(double x, double y, double z);
	public World getWorldObj();
}
