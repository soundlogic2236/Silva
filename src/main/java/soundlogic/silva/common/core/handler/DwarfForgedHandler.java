package soundlogic.silva.common.core.handler;

import soundlogic.silva.common.lib.LibGUI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DwarfForgedHandler {
	public static final String TAG_DWARF_FORGED = "Silva_Dwarf_Forged";
	
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
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipEvent(ItemTooltipEvent event) {
		if(event.itemStack != null && event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().getBoolean(TAG_DWARF_FORGED))
			event.toolTip.add(StatCollector.translateToLocal(LibGUI.DWARF_FORGED_TOOLTIP));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTick(PlayerTickEvent event) {
		if(event.phase == Phase.END && !event.player.worldObj.isRemote)
			for(int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
				ItemStack stack = event.player.inventory.getStackInSlot(i);
				if(stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_DWARF_FORGED))
					System.out.println("Dwarf forged "+stack);
			}
	}
}
