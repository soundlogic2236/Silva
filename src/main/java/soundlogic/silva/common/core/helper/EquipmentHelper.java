package soundlogic.silva.common.core.helper;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import soundlogic.silva.common.core.helper.EquipmentHelper.EquipmentType;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class EquipmentHelper {

	public enum EquipmentType {
		ANY_ARMOR,
		ANY_TOOL,
		ANY_DIGGING_TOOL,
		ANY_WEAPON,
		ANY_MELEE_WEAPON,
		ANY_RANGED_WEAPON,
		ANY_BAUBLE,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS,
		PICKAXE,
		SHOVEL,
		AXE,
		SWORD,
		BOW,
		HOE,
		SHEARS,
		AMULET,
		RING,
		BELT,
	};
	
	public static boolean isEquipmentType(ItemStack stack, EquipmentType type) {
		return isEquipmentTypeSimple(stack, type);
	}

	private static boolean isEquipmentTypeSimple(ItemStack stack, EquipmentType type) {
		ItemTool tool=null;
		if(stack.getItem() instanceof ItemTool)
			tool=(ItemTool)stack.getItem();
		switch(type) {
		case ANY_ARMOR:
			return	isEquipmentType(stack, EquipmentType.HELMET) ||
					isEquipmentType(stack, EquipmentType.CHESTPLATE) ||
					isEquipmentType(stack, EquipmentType.LEGGINGS) ||
					isEquipmentType(stack, EquipmentType.BOOTS);
		case ANY_TOOL:
			return	isEquipmentType(stack, EquipmentType.PICKAXE) ||
					isEquipmentType(stack, EquipmentType.SHOVEL) ||
					isEquipmentType(stack, EquipmentType.AXE) ||
					isEquipmentType(stack, EquipmentType.HOE) ||
					isEquipmentType(stack, EquipmentType.SHEARS);
		case ANY_DIGGING_TOOL:
			return	isEquipmentType(stack, EquipmentType.PICKAXE) ||
					isEquipmentType(stack, EquipmentType.SHOVEL) ||
					isEquipmentType(stack, EquipmentType.AXE) ||
					isEquipmentType(stack, EquipmentType.SHEARS);
		case ANY_WEAPON:
			return	isEquipmentType(stack, EquipmentType.ANY_MELEE_WEAPON) ||
					isEquipmentType(stack, EquipmentType.ANY_RANGED_WEAPON);
		case ANY_MELEE_WEAPON:
			return	isEquipmentType(stack, EquipmentType.SWORD);
		case ANY_RANGED_WEAPON:
			return	isEquipmentType(stack, EquipmentType.BOW);
		case ANY_BAUBLE:
			return	isEquipmentType(stack, EquipmentType.AMULET) ||
					isEquipmentType(stack, EquipmentType.BELT) ||
					isEquipmentType(stack, EquipmentType.RING);
		case HELMET:
			return stack.getItem().isValidArmor(stack, 0, null);
		case CHESTPLATE:
			return stack.getItem().isValidArmor(stack, 1, null);
		case LEGGINGS:
			return stack.getItem().isValidArmor(stack, 2, null);
		case BOOTS:
			return stack.getItem().isValidArmor(stack, 3, null);
		case PICKAXE:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "pickaxe") != -1;
		case SHOVEL:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "shovel") != -1;
		case AXE:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "axe") != -1;
		case SWORD:
			return stack.getItem() instanceof ItemSword;
		case HOE:
			return stack.getItem() instanceof ItemHoe;
		case SHEARS:
			return stack.getItem() instanceof ItemShears;
		case BOW:
			return stack.getItem() instanceof ItemBow;
		case AMULET:
			return stack.getItem() instanceof IBauble && ((IBauble)stack.getItem()).getBaubleType(stack)==BaubleType.AMULET;
		case BELT:
			return stack.getItem() instanceof IBauble && ((IBauble)stack.getItem()).getBaubleType(stack)==BaubleType.BELT;
		case RING:
			return stack.getItem() instanceof IBauble && ((IBauble)stack.getItem()).getBaubleType(stack)==BaubleType.RING;
		}
		return false;
	}

	public static ItemStack getProxyStackForType(EquipmentType type) {
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
