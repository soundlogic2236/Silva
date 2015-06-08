package soundlogic.silva.common.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.EnchantmentMoverHandler;
import soundlogic.silva.common.core.helper.EquipmentHelper;
import soundlogic.silva.common.core.helper.EquipmentHelper.EquipmentType;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.lexicon.KnowledgeType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public abstract class ItemModMultiple extends ItemMod{

	IIcon[] icons=new IIcon[getCount()];

	public ItemModMultiple(String unLocalizedName) {
		super(unLocalizedName);
		this.hasSubtypes=true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < getCount(); i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	public abstract int getCount();

	@Override
    public String getUnlocalizedName(ItemStack stack) {
    	return super.getUnlocalizedName()+"."+stack.getItemDamage();
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	String resourceName=LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "");
    	for(int i=0;i<icons.length;i++)
   			icons[i] = par1IconRegister.registerIcon(resourceName+i);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
    	return icons[damage];
    }

}
