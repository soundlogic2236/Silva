package soundlogic.silva.common.core.handler.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public abstract class PotionHandler implements IPotionHandler {

	@Override
	public void onUseItem(PlayerInteractEvent event) {
		// NO OP
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		//NO OP
	}

	@Override
	public void onLivingHurt(LivingHurtEvent event) {
		//NO OP
	}
}
