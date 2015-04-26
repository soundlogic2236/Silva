package soundlogic.silva.common.core.handler.portal;

import net.minecraft.entity.EntityLivingBase;

public class DimensionExposureHandlerHelheim extends DimensionExposureHandlerBase{

	@Override
	public void onEntityUpdate(EntityLivingBase entity, int level) {
		super.onEntityUpdate(entity, level);
		if(level>20)
			if(entity.isEntityUndead()) {
				entity.captureDrops=true;
				entity.setDead();
			}
	}
}
