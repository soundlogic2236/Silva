package soundlogic.silva.common.core.handler;

import java.util.HashMap;
import java.util.Random;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.potion.ModPotions;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PotionEffectHandler {

	static Random random = new Random();
	HashMap<EntityPlayer,Integer> prev1SwingTime=new HashMap<EntityPlayer,Integer>();
	HashMap<EntityPlayer,Integer> prev2SwingTime=new HashMap<EntityPlayer,Integer>();
	
	@SubscribeEvent
	public void onTick(PlayerTickEvent event) {
		if(event.phase == Phase.END && !event.player.worldObj.isRemote) {
			if(event.player.isPotionActive(ModPotions.potionMead)) {
				if(event.player.swingProgressInt==1) {
					prev2SwingTime.put(event.player, prev1SwingTime.get(event.player));
					prev1SwingTime.put(event.player, event.player.ticksExisted);
				}
				if(!event.player.isPotionActive(Potion.confusion))
					event.player.addPotionEffect(new PotionEffect(Potion.confusion.id,event.player.getActivePotionEffect(ModPotions.potionMead).getDuration()));
			}
			else {
				prev1SwingTime.put(event.player, null);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(LivingHurtEvent event) {
		if(event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			if(player.isPotionActive(ModPotions.potionMead)) {
				prev1SwingTime.put(player, null);
				Integer ticks=prev2SwingTime.get(player);
				boolean justMissed=ticks !=null && ( player.ticksExisted-ticks ) < 1000;
				boolean air=player.isAirBorne;

				if(justMissed) {
					event.ammount *= 3F;
				}
				
				if(air) {
					event.ammount *= 1.2F;
				}
				
				if(air && justMissed) {
					event.ammount *= 2F;
				}
			}
		}
	}
	
}
