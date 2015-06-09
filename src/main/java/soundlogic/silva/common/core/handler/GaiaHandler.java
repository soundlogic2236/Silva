package soundlogic.silva.common.core.handler;

import soundlogic.silva.common.block.BlockPylon;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import vazkii.botania.common.entity.EntityDoppleganger;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class GaiaHandler {

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if(!(event.entityLiving instanceof EntityDoppleganger))
			return;
		EntityDoppleganger boss = (EntityDoppleganger) event.entityLiving;
		doPylonTuning(boss);
	}
	
	protected void doPylonTuning(EntityDoppleganger boss) {
		if(!boss.isHardMode())
			return;
		ChunkCoordinates coords = boss.getSource();
		int x = coords.posX;
		int y = coords.posY;
		int z = coords.posZ;
		int radius = 12;
		int minX = -radius;
		int minY = 0;
		int minZ = -radius;
		int maxX = radius;
		int maxY = 6;
		int maxZ = radius;
		Dimension dim = DimensionHandler.getDimensionFromWorld(boss.worldObj);
		if(dim==null)
			return;
		if(!dim.canTunePylonWithGaia())
			return;
		for(int i = minX; i <= maxX; i++) {
			for(int j = minY; j <= maxY; j++) {
				for(int k = minZ; k <= maxZ; k++) {
					int px = x+i;
					int py = y+j;
					int pz = z+k;
					if(i*i+k*k<=12*12)
						BlockPylon.tryTunePylon(boss.worldObj, px,py,pz, dim);
				}
			}
		}
	}
}
