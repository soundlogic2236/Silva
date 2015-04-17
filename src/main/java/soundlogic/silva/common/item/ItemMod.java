package soundlogic.silva.common.item;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibItemNames;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemMod extends Item{

	private boolean namedMetadata=false;
	private static String[] creativelyHiddenNames=new String[]{LibItemNames.QUESTION_MARK, LibItemNames.FAKE_PAPERS};
	
	public ItemMod(String unLocalizedName) {
		super();
		if(!(Arrays.asList(creativelyHiddenNames).contains(unLocalizedName)))
			setCreativeTab(Silva.creativeTab);
		this.setUnlocalizedName(unLocalizedName);
	}
	
	public Item setNamedMetadata(boolean namedMetadata) {
		this.namedMetadata=namedMetadata;
		return this;
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		if(namedMetadata) {
			return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item\\.", "item." + LibResources.PREFIX_MOD)+"."+par1ItemStack.getItemDamage();
		}
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item\\.", "item." + LibResources.PREFIX_MOD);
	}


	public void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	String resourceName=LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "");
		itemIcon = par1IconRegister.registerIcon(resourceName);
	}
}
