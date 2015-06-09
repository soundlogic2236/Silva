package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import baubles.api.BaublesApi;
import soundlogic.silva.common.core.helper.EquipmentHelper;
import soundlogic.silva.common.item.augments.ITickingDarkElfAugment;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemRegenIvy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DarkElfAugmentHandler {
	
	private static HashMap<String, ITickingDarkElfAugment> tickingAugments = new HashMap<String, ITickingDarkElfAugment>();
	
	public static final String TAG_DARK_ELF = "Silva_Dark_Elf";
	
	public static Random random = new Random();
	
	public static ItemStack augmentStack(ItemStack stack, String tag) {
		if(stack==null)
			return null;
		ItemStack result=stack.copy();
		NBTTagCompound cmp=new NBTTagCompound();
		if(result.hasTagCompound())
			cmp=result.getTagCompound();
		NBTTagList augments = new NBTTagList();
		if(cmp.hasKey(TAG_DARK_ELF))
			augments = cmp.getTagList(TAG_DARK_ELF, 8);
		augments.appendTag(new NBTTagString(tag));
		cmp.setTag(TAG_DARK_ELF, augments);
		result.setTagCompound(cmp);
		return result;
	}
	
	public static List<String> getAugments(ItemStack stack) {
		ArrayList<String> result = new ArrayList<String>();
		if(!stack.hasTagCompound())
			return result;
		if(!stack.getTagCompound().hasKey(TAG_DARK_ELF))
			return result;
		NBTTagList augments = stack.getTagCompound().getTagList(TAG_DARK_ELF, 8);
		for(int i = 0 ; i < augments.tagCount() ; i++) {
			result.add(augments.getStringTagAt(i));
		}
		return result;
	}
	
	public static void registerTickingAugment(String tag, ITickingDarkElfAugment augment) {
		tickingAugments.put(tag, augment);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipEvent(ItemTooltipEvent event) {
		if(event.itemStack == null)
			return;
		List<String> augments = getAugments(event.itemStack);
		for(String augment : augments) {
			event.toolTip.add(StatCollector.translateToLocal(LibGUI.DARK_ELF_PREFIX+augment));
		}
	}
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event) {
		doAugmentTick(event.entityLiving.getEquipmentInSlot(1),event.entityLiving,0);
		doAugmentTick(event.entityLiving.getEquipmentInSlot(2),event.entityLiving,1);
		doAugmentTick(event.entityLiving.getEquipmentInSlot(3),event.entityLiving,2);
		doAugmentTick(event.entityLiving.getEquipmentInSlot(4),event.entityLiving,3);
		if(event.entityLiving instanceof EntityPlayer) {
			IInventory baubles = BaublesApi.getBaubles((EntityPlayer) event.entityLiving);
			doAugmentTick(baubles.getStackInSlot(0),event.entityLiving,4);
			doAugmentTick(baubles.getStackInSlot(1),event.entityLiving,5);
			doAugmentTick(baubles.getStackInSlot(2),event.entityLiving,5);
			doAugmentTick(baubles.getStackInSlot(3),event.entityLiving,6);
		}
	}
	
	public void doAugmentTick(ItemStack stack, EntityLivingBase entity, int slot) {
		if(stack==null)
			return;
		List<String> augments = getAugments(stack);
		for(String str : augments) {
			if(tickingAugments.containsKey(str))
				tickingAugments.get(str).onTick(entity, stack, slot);
		}
	}
	
}
