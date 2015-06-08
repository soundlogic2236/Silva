package soundlogic.silva.common.item;

import java.util.List;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.lib.LibItemNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemSimpleResource extends ItemMod {

	final int types = LibItemNames.SIMPLE_RESOURCE_NAMES.length;
	IIcon[] icons;
	
	public ItemSimpleResource() {
		super(LibItemNames.SIMPLE_RESOURCE);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < types; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[types];
		for(int i = 0; i < icons.length; i++) {
	    	String resourceName=LibResources.PREFIX_MOD+this.getUnlocalizedName(i).replaceAll("item\\.", "");
			icons[i] = par1IconRegister.registerIcon(resourceName);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedName(par1ItemStack.getItemDamage());
	}
	
	private String getUnlocalizedName(int meta) {
		return "item." + LibItemNames.SIMPLE_RESOURCE_NAMES[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return icons[par1];
	}
}
