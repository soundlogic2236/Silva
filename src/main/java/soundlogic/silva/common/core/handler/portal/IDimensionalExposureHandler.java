package soundlogic.silva.common.core.handler.portal;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import net.minecraft.entity.EntityLivingBase;

public interface IDimensionalExposureHandler {

	public void init(Dimension dim);
	public void onEntityUpdate(EntityLivingBase entity, int level);
}
