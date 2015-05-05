package soundlogic.silva.common.core.handler.potion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.BookHandler;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.item.ItemDwarvenMead;
import soundlogic.silva.common.potion.ModPotions;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PotionMeadHandler {

	static Random random = new Random();
	HashMap<EntityPlayer,Integer> prev1SwingTime=new HashMap<EntityPlayer,Integer>();
	HashMap<EntityPlayer,Integer> prev2SwingTime=new HashMap<EntityPlayer,Integer>();
	
	private boolean lastTickWasMead=false;
	
	@SubscribeEvent
	public void onUseItem(PlayerInteractEvent event) {
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack==null)
			return;
		if(stack.getItem()==Items.writable_book) {
			if(ConfigHandler.devBooks)
				Silva.proxy.convertBooks();
			else if(event.entityPlayer.isPotionActive(ModPotions.potionMead)){
				event.entityPlayer.setCurrentItemOrArmor(0, BookHandler.getRandomBookForPlayer(event.entityPlayer));
				event.setCanceled(true);
			}
		}
	}
	@SubscribeEvent
	public void onTick(PlayerTickEvent event) {
		if(event.phase == Phase.END) {
			if(!event.player.worldObj.isRemote) {
				if(event.player.isPotionActive(ModPotions.potionMead)) {
					if(event.player.swingProgressInt==1) {
						prev2SwingTime.put(event.player, prev1SwingTime.get(event.player));
						prev1SwingTime.put(event.player, event.player.ticksExisted);
					}
		        	PotionEffect effect = event.player.getActivePotionEffect(ModPotions.potionMead);
		        	if(effect.getAmplifier()>=3)
		        		event.player.attackEntityFrom(ItemDwarvenMead.ALCOHOL_DAMAGE, Float.MAX_VALUE);
				}
				else {
					prev1SwingTime.put(event.player, null);
				}
			}
			else {
				if(event.player.isPotionActive(ModPotions.potionMead)) {
		        	PotionEffect effect = event.player.getActivePotionEffect(ModPotions.potionMead);
		        	switch(effect.getAmplifier()) {
		        	case 0:break;
		        	case 1:Silva.proxy.setShader(ItemDwarvenMead.shader_blur);break;
		        	case 2:Silva.proxy.setShader(ItemDwarvenMead.shader_phosphor);break;
		        	default:Silva.proxy.setShader(ItemDwarvenMead.shader_phosphor);break;
		        	}
					lastTickWasMead=true;
				}
				else if(lastTickWasMead) {
					Silva.proxy.setShader(null);
					lastTickWasMead=false;
				}
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
	public static void onEnd() {
		Silva.proxy.setShader(null);
	}
	
}
