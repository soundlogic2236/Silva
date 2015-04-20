package soundlogic.silva.common.core.handler;

import java.util.Random;

import soundlogic.silva.common.item.ItemBrokenDwarvenWrapper;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemRegenIvy;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DwarfForgedHandler {
	public static final String TAG_DWARF_FORGED = "Silva_Dwarf_Forged";
	private static final int MANA_PER_DAMAGE = 100;
	
	public static Random random = new Random();
	
	public static ItemStack dwarfForgeStack(ItemStack stack) {
		if(stack==null)
			return null;
		ItemStack result=stack.copy();
		NBTTagCompound cmp=new NBTTagCompound();
		if(result.hasTagCompound())
			cmp=result.getTagCompound();
		cmp.setBoolean(TAG_DWARF_FORGED, true);
		result.setTagCompound(cmp);
		return result;
	}
	
	public static boolean isDwarfForged(ItemStack stack) {
		return stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_DWARF_FORGED);
	}
	
	public static boolean isWeaponDwarfForged(EntityPlayer player) {
		return isDwarfForged(player.getCurrentEquippedItem()) && player.getCurrentEquippedItem().getItem() instanceof ItemSword;
	}
	
	public static boolean isToolDwarfForged(EntityPlayer player) {
		return isDwarfForged(player.getCurrentEquippedItem()) && player.getCurrentEquippedItem().getItem() instanceof ItemTool;
	}
	
	public static int dwarfForgedArmorCount(EntityPlayer player) {
		return 
				(isDwarfForged(player.getEquipmentInSlot(1)) ? 1 : 0) +
				(isDwarfForged(player.getEquipmentInSlot(2)) ? 1 : 0) +
				(isDwarfForged(player.getEquipmentInSlot(3)) ? 1 : 0) +
				(isDwarfForged(player.getEquipmentInSlot(4)) ? 1 : 0);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipEvent(ItemTooltipEvent event) {
		if(event.itemStack != null && event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().getBoolean(TAG_DWARF_FORGED))
			event.toolTip.add(StatCollector.translateToLocal(LibGUI.DWARF_FORGED_TOOLTIP));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTick(PlayerTickEvent event) {
		if(event.phase == Phase.END && !event.player.worldObj.isRemote) {
			if(random.nextFloat()<.2F)
				for(int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
					ItemStack stack = event.player.inventory.getStackInSlot(i);
					if(isDwarfForged(stack) && stack.getTagCompound().getBoolean(ItemRegenIvy.TAG_REGEN) && stack.getItemDamage() > 0) {
						if(ManaItemHandler.requestManaExact(stack, event.player, MANA_PER_DAMAGE, true))
							stack.setItemDamage(stack.getItemDamage() - 1);
					}
				}
		}
	}
	
	@SubscribeEvent
	public void onDestroyedItem(PlayerDestroyItemEvent event) {
		System.out.println("d");
		if(isDwarfForged(event.original) && event.original.getItem().isRepairable()) {
			System.out.println("e");
			ItemStack broken = ItemBrokenDwarvenWrapper.makeFromStack(event.original);
			System.out.println(broken);
            if (!event.entityPlayer.inventory.addItemStackToInventory(broken))
            {
        		System.out.println("stroy");
            	event.entityPlayer.dropPlayerItemWithRandomChoice(broken, false);
            }
		}
	}

	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			if(event.source.isDamageAbsolute() || event.source.isUnblockable())
				return;
			int count = dwarfForgedArmorCount((EntityPlayer) event.entityLiving);
			float chance = (float)count/80F;
			if(random.nextFloat()<chance)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(LivingHurtEvent event) {
		if(event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			if(isWeaponDwarfForged(player)) {
				if(random.nextFloat()<.2F)
					event.ammount *=1.1F;
				if(random.nextFloat()<.05F)
					event.ammount *=2F;
			}
		}
	}
}
