package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import soundlogic.silva.common.core.helper.EquipmentHelper;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ItemRegenIvy;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DarkElfAugmentHandler {
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
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipEvent(ItemTooltipEvent event) {
		if(event.itemStack == null)
			return;
		List<String> augments = getAugments(event.itemStack);
		for(String augment : augments) {
			event.toolTip.add(StatCollector.translateToLocal(LibGUI.DARK_ELF_PREFIX+augment));
		}
	}
}
