package soundlogic.silva.common.core.handler.portal;

import net.minecraft.entity.EntityLivingBase;

public class DimensionExposureHandlerMuspelheim extends DimensionExposureHandlerBase{

	@Override
	public void onEntityUpdate(EntityLivingBase entity, int level) {
		super.onEntityUpdate(entity, level);
		if(random.nextFloat()<( (float)level / (level * 2 + 20)))
			entity.setFire(2);
	}
}
