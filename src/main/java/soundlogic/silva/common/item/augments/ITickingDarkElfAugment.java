package soundlogic.silva.common.item.augments;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITickingDarkElfAugment {

	public void onTick(EntityLivingBase entity, ItemStack augmentedStack, int slot);
}
