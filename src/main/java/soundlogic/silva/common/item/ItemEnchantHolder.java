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

public class ItemEnchantHolder extends ItemMod{

	public static final String TAG_DUMMY_ENCHANT = "dummyEnchant";
	
	IIcon[] icons=new IIcon[11];
	private final String[] names=new String[]{
			"helmet",
			"chestplate",
			"leggings",
			"boots",
			"pickaxe",
			"shovel",
			"axe",
			"sword",
			"bow",
			"hoe",
			"shears"};
	private final String[] resources=new String[]{
			"paradoxHelmet",
			"paradoxChestplate",
			"paradoxLeggings",
			"paradoxBoots",
			"paradoxPickaxe",
			"paradoxShovel",
			"paradoxAxe",
			"paradoxSword",
			"paradoxBow",
			"paradoxHoe",
			"paradoxShears"};
	
	public ItemEnchantHolder(String unLocalizedName) {
		super(unLocalizedName);
		this.setMaxStackSize(1);
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

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(!stack.hasTagCompound())
			return;
		if(stack.getTagCompound().hasKey(TAG_DUMMY_ENCHANT)) {
			addStringToTooltip(Enchantment.enchantmentsList[stack.getTagCompound().getShort(TAG_DUMMY_ENCHANT)].getTranslatedName(1), list);
		}
		if(!EnchantmentMoverHandler.holderHasEnchantmentData(stack))
			return;
		Map<Enchantment, Integer> enchants = EnchantmentMoverHandler.getHolderEnchantments(stack);
		for(Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			addStringToTooltip(enchant.getKey().getTranslatedName(enchant.getValue()), list);
		}
		int curXP = EnchantmentMoverHandler.getHolderCurrentXP(stack);
		int maxXP = EnchantmentMoverHandler.getHolderNeededXP(stack);
		addStringToTooltip(String.format(StatCollector.translateToLocal(LibGUI.ENCHANT_XP_TOOLTIP),curXP,maxXP), list);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < names.length; i++)
			list.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public boolean hasContainerItem() {
		return true;
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return EnchantmentMoverHandler.removeTopEnchantmentFromHolder(stack);
	}

	public boolean canApplyToStack(ItemStack holder, ItemStack enchanted) {
		switch(holder.getItemDamage()) {
		case  0: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.HELMET);
		case  1: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.CHESTPLATE);
		case  2: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.LEGGINGS);
		case  3: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.BOOTS);
		case  4: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.PICKAXE);
		case  5: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.SHOVEL);
		case  6: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.AXE);
		case  7: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.SWORD);
		case  8: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.BOW);
		case  9: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.HOE);
		case 10: return EquipmentHelper.isEquipmentType(enchanted, EquipmentType.SHEARS);
		}
		return false;
	}
	
}
