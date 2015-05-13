package soundlogic.silva.common.item;

import soundlogic.silva.client.lib.LibResources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;

public class ItemProxy extends ItemMod{

	IIcon[] icons=new IIcon[9];
	private final String[] names=new String[]{
			"questionMark",
			"helmet",
			"chestplate",
			"leggings",
			"boots",
			"pickaxe",
			"shovel",
			"axe",
			"sword"};
	private final String[] resources=new String[]{
			"questionMark",
			"proxyHelmet",
			"proxyChestplate",
			"proxyLeggings",
			"proxyBoots",
			"proxyPickaxe",
			"proxyShovel",
			"proxyAxe",
			"proxySword"};
	
	public ItemProxy(String unLocalizedName) {
		super(unLocalizedName);
	}
	
    @Override
    public String getUnlocalizedName(ItemStack stack) {
    	return super.getUnlocalizedName()+"."+names[stack.getItemDamage()];
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	for(int i=0;i<resources.length;i++)
    		if(resources[i]!=null)
    			icons[i] = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+resources[i]);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
    	return icons[damage];
    }

}
