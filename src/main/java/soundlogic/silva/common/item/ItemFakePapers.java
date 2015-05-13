package soundlogic.silva.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.lexicon.KnowledgeType;

public class ItemFakePapers extends ItemMod{

	public ItemFakePapers(String unLocalizedName) {
		super(unLocalizedName);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(GuiScreen.isShiftKeyDown()) {
			addStringToTooltip(StatCollector.translateToLocal(LibGUI.PAPERS_CRAFT), par3List);
			addStringToTooltip(StatCollector.translateToLocal(LibGUI.PAPERS_KNOWLEDGE_TYPE), par3List);
			addStringToTooltip(StatCollector.translateToLocal(LibGUI.FAKE_KNOWLEDGE_TOOLTIP), par3List);
		}
		else
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), par3List);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		//NO OP
	}

}
