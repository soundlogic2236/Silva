package soundlogic.silva.common.block.tile;

import net.minecraft.world.World;

public interface ICustomSpreader {

	World getWorldObj();
	
	void prepBurst();

	public int getBurstParticleTick();

	public void setBurstParticleTick(int burstParticleTick);

	public int getLastBurstDeathTick();

	public void setLastBurstDeathTick(int lastBurstDeathTick);
}
