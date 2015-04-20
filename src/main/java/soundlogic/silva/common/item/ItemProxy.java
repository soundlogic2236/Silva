package soundlogic.silva.common.item;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.crafting.recipe.DwarfTradeReforging.ReforgeType;
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

	public static ItemStack getStackForReforgeType(ReforgeType type) {
		switch(type) {
		case HELMET:
			return new ItemStack(ModItems.proxyItem,1,1);
		case CHESTPLATE:
			return new ItemStack(ModItems.proxyItem,1,2);
		case LEGGINGS:
			return new ItemStack(ModItems.proxyItem,1,3);
		case BOOTS:
			return new ItemStack(ModItems.proxyItem,1,4);
		case PICKAXE:
			return new ItemStack(ModItems.proxyItem,1,5);
		case SHOVEL:
			return new ItemStack(ModItems.proxyItem,1,6);
		case AXE:
			return new ItemStack(ModItems.proxyItem,1,7);
		case SWORD:
			return new ItemStack(ModItems.proxyItem,1,8);
		}
		return null;
	}

}
