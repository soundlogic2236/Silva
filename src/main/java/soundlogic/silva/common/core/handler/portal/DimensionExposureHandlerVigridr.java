package soundlogic.silva.common.core.handler.portal;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import net.minecraft.entity.EntityLivingBase;

public class DimensionExposureHandlerVigridr extends DimensionExposureHandlerBase{

	@Override
	public void onEntityUpdate(EntityLivingBase entity, int level) {
		super.onEntityUpdate(entity, level);
		if(entity.worldObj.isRemote)
			return;
		if(random.nextFloat()<( (float)level / (level * 2 + 20*30))) {
//			FateHandler.applyRandomFate(entity, level);
		}
	}
}
