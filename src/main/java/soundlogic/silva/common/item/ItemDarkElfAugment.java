package soundlogic.silva.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.helper.EquipmentHelper;
import soundlogic.silva.common.core.helper.EquipmentHelper.EquipmentType;
import soundlogic.silva.common.crafting.recipe.PapersCloneRecipe;
import soundlogic.silva.common.crafting.recipe.PapersIntoLexiconRecipe;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibGUI;
import soundlogic.silva.common.lib.LibItemNames;
import soundlogic.silva.common.lib.LibNBT;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;

public class ItemDarkElfAugment extends ItemMod{

	
	public static class AugmentData {

		IIcon icon;
		String iconName;
		
		boolean helmet=false;
		boolean chestplate=false;
		boolean leggings=false;
		boolean boots=false;
		boolean amulet=false;
		boolean ring=false;
		boolean belt=false;

		ArrayList crafting = null;
		
		public AugmentData(String iconName) {
			this.iconName=iconName;
		}
		
		public AugmentData(EquipmentType type, String iconName) {
			this(iconName);
			switch(type) {
			case HELMET: this.helmet=true; break;
			case CHESTPLATE: this.chestplate=true; break;
			case LEGGINGS: this.leggings=true; break;
			case BOOTS: this.boots=true; break;
			case AMULET: this.amulet=true; break;
			case RING: this.ring=true; break;
			case BELT: this.belt=true; break;
			}
		}
		
		public AugmentData(boolean helmet, boolean chestplate, boolean leggings, boolean boots, boolean amulet, boolean ring, boolean belt, String iconName) {
			this(iconName);
			this.helmet=helmet;
			this.chestplate=chestplate;
			this.leggings=leggings;
			this.boots=boots;
			this.amulet=amulet;
			this.ring=ring;
			this.belt=belt;
		}
		
		public boolean isStackValid(ItemStack stack) {
			if(helmet && EquipmentHelper.isEquipmentType(stack, EquipmentType.HELMET))
				return true;
			if(chestplate && EquipmentHelper.isEquipmentType(stack, EquipmentType.CHESTPLATE))
				return true;
			if(leggings && EquipmentHelper.isEquipmentType(stack, EquipmentType.LEGGINGS))
				return true;
			if(boots && EquipmentHelper.isEquipmentType(stack, EquipmentType.BOOTS))
				return true;
			if(amulet && EquipmentHelper.isEquipmentType(stack, EquipmentType.AMULET))
				return true;
			if(ring && EquipmentHelper.isEquipmentType(stack, EquipmentType.RING))
				return true;
			if(belt && EquipmentHelper.isEquipmentType(stack, EquipmentType.BELT))
				return true;
			return false;
		}
		
		public List<String> getCraftingTooltips() {
			if(crafting!=null)
				return crafting;
			crafting = new ArrayList();
			if(helmet)
				crafting.add("helmet");
			if(chestplate)
				crafting.add("chestplate");
			if(leggings)
				crafting.add("leggings");
			if(boots)
				crafting.add("boots");
			if(amulet)
				crafting.add("amulet");
			if(ring)
				crafting.add("ring");
			if(belt)
				crafting.add("belt");
			return crafting;
		}
	}
	
	private static HashMap<String, AugmentData> augments = new HashMap<String, AugmentData>();
	
	public static void registerAugment(String key, AugmentData data) {
		augments.put(key, data);
	}
	public static void registerAugment(String key, EquipmentType type, String iconName) {
		registerAugment(key, new AugmentData(type, iconName));
	}
	public static void registerAugment(String key, EquipmentType type) {
		registerAugment(key, type, "darkElfAugment"+key.replace(".", ""));
	}

	public ItemDarkElfAugment(String unLocalizedName) {
		super(unLocalizedName);
		this.hasSubtypes=true;
	}
	
	public static ItemStack forAugment(String key) {
		return setAugment(new ItemStack(ModItems.augments), key);
	}

	public static ItemStack setAugment(ItemStack stack,String key) {
		NBTTagCompound compound=new NBTTagCompound();
		if(stack.hasTagCompound())
			compound=stack.getTagCompound();
		compound.setString(LibNBT.AUGMENT_KEY, key);
		stack.setTagCompound(compound);
		return stack;
	}
	
	public static String getAugmentKey(ItemStack stack) {
		NBTTagCompound compound=stack.getTagCompound();
		if(compound==null)
			return "";
		String key;
		if(compound.hasKey(LibNBT.AUGMENT_KEY))
			return compound.getString(LibNBT.AUGMENT_KEY);
		else
			return "";
	}

	public static AugmentData getAugment(ItemStack stack) {
		String key = getAugmentKey(stack);
		if(key.equals(""))
			return null;
		return augments.get(key);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + "." + getAugmentKey(par1ItemStack);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(GuiScreen.isShiftKeyDown()) {
			String key = getAugmentKey(par1ItemStack);
			AugmentData augment=null;
			if(key!=null)
				augment = augments.get(key);
			if(key==null || augment==null)
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.AUGMENT_ERROR), par3List);
			else {
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.DARK_ELF_PREFIX+key), par3List);
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.DARK_ELF_CRAFT), par3List);
				List<String> crafts = augment.getCraftingTooltips();
				for(String string : crafts) {
					addStringToTooltip(StatCollector.translateToLocal(LibGUI.DARK_ELF_CRAFT_PREFIX+string), par3List);
				}
			}
		}
		else
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), par3List);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(String key : augments.keySet())
			list.add(forAugment(key));
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	String resourceName=LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "");
		for(AugmentData data : augments.values())
			data.icon=par1IconRegister.registerIcon(data.iconName);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
		String key = getAugmentKey(stack);
		AugmentData augment=null;
		if(key!=null)
			augment = augments.get(key);
		if(augment!=null)
			return augment.icon; 
		return Items.rotten_flesh.getIconFromDamage(0);
    }

}
