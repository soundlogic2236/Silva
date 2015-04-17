package soundlogic.silva.common.item;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.common.item.ItemLexicon;

public class ItemFakeLexicon extends ItemLexicon {

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		if(GuiScreen.isShiftKeyDown()) {
			par3List.add((" \u2022 " + StatCollector.translateToLocal(LibGUI.FAKE_KNOWLEDGE_TOOLTIP)).replaceAll("&", "\u00a7"));
		}
	}
}
