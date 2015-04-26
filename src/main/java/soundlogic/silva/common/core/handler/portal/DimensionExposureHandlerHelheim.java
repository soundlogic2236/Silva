package soundlogic.silva.common.core.handler.portal;

import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class DimensionExposureHandlerHelheim extends DimensionExposureHandlerBase{

	@Override
	public void onEntityUpdate(EntityLivingBase entity, int level) {
		super.onEntityUpdate(entity, level);
		if(level>20)
			if(entity.isEntityUndead()) {
				entity.captureDrops=true;
				entity.setDead();
				World worldObj = entity.worldObj;
				Vector3 entityPoint=new Vector3(entity.posX,entity.posY+entity.height/2,entity.posZ);
				for(int i = 0 ; i< 4 ; i++) {
					Vector3 point=new Vector3(
							entity.posX+random.nextFloat()*3-1.5F,
							entity.posY+random.nextFloat()*3-1.5F+entity.height/2,
							entity.posZ+random.nextFloat()*3-1.5F);
					LightningHandler.spawnLightningBolt(worldObj, entityPoint, point, 2, worldObj.rand.nextLong(), 0x1d2457, 0x1d2457);
				}
			}
	}
}
