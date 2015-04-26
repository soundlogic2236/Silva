package soundlogic.silva.common.core.handler.potion;

import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface IPotionHandler {
	public void onUseItem(PlayerInteractEvent event);
	public void onPlayerTick(PlayerTickEvent event);
	public void onLivingHurt(LivingHurtEvent event);
	public Potion getPotion();
}
