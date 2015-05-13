package soundlogic.silva.common.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.common.item.ItemEnchantHolder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class EnchantmentMoverHandler {

	
	private static final String TAG_XP_REQUIREMENT = "xpRequirement";
	private static final String TAG_XP_CURRENT = "xpCurrent";
	private static final String TAG_ENCHANTS_SIZE = "enchantsSize";
	private static final String TAG_ENCHANT_ID = "enchantID";
	private static final String TAG_ENCHANT_LEVEL = "enchantLevel";

	public static int getManaRequirement(ItemStack enchanted, ItemStack holder) {
		int mana = 5000;
		Map<Enchantment, Integer> enchants = getEnchantments(enchanted);
		for(Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			Enchantment ench=enchant.getKey();
			int level=enchant.getValue();
			mana += (int) (5000F * ((15 - Math.min(15, ench.getWeight())) * 1.05F) * ((3F + level * level) * 0.25F) * (0.9F + enchants.size() * 0.05F));
		}
		return mana;
	}
	public static int getXPRequirement(ItemStack enchanted, ItemStack holder) {
		int value = 0;
		Map<Enchantment, Integer> enchants = getEnchantments(enchanted);
		for(Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			Enchantment ench=enchant.getKey();
			int level=enchant.getValue();
			value += (int) (100F * ((15 - Math.min(15, ench.getWeight())) * 1.05F) * ((3F + level * level) * 0.25F) * (0.9F + enchants.size() * 0.05F));
			if(ench.getWeight()==0)
				value+=30*30*30;
			else
				value += ench.getMinEnchantability(level)*ench.getMinEnchantability(level)*ench.getMinEnchantability(level);
		}
		int xp = (int) (Math.sqrt(value)*4);
		return xp;
	}
	public static boolean canApplyToStack(ItemStack holder, ItemStack enchanted) {
		if(holderHasEnchantmentData(holder))
			return false;
		return ((ItemEnchantHolder)holder.getItem()).canApplyToStack(holder, enchanted);
	}
	
	public static ItemStack applyToStack(ItemStack holder, ItemStack enchanted) {
		int xp = getXPRequirement(enchanted, holder);

		Map<Enchantment, Integer> enchants = getEnchantments(enchanted);
		
		return applyToStack(holder,enchants,xp,0);
	}
	
	private static ItemStack applyToStack(ItemStack holder, Map<Enchantment, Integer> enchants, int xp, int xp_current) {
		ItemStack result = holder.copy();

		NBTTagCompound cmp = new NBTTagCompound();
		
		if(result.hasTagCompound())
			cmp=result.getTagCompound();
		
		cmp.setInteger(TAG_XP_REQUIREMENT, xp);
		cmp.setInteger(TAG_XP_CURRENT, xp_current);
		
		writeEnchantsToNBT(enchants, cmp);
		
		result.setTagCompound(cmp);
		
		return result;
	}
	
	private static void writeEnchantsToNBT(Map<Enchantment, Integer> enchants, NBTTagCompound cmp) {
		cmp.setInteger(TAG_ENCHANTS_SIZE, enchants.size());

		int i = 0;
		for(Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			cmp.setShort(TAG_ENCHANT_ID+i, (short) entry.getKey().effectId);
			cmp.setShort(TAG_ENCHANT_LEVEL+i, (short) (int) entry.getValue());
			i++;
		}
	}
	
	public static Map<Enchantment, Integer> getEnchantments(ItemStack enchanted) {
		Map map = EnchantmentHelper.getEnchantments(enchanted);
		Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		for(Object o : map.entrySet()) {
			Entry<Integer, Integer> entry = (Entry<Integer, Integer>) o;
			enchants.put(Enchantment.enchantmentsList[entry.getKey()], entry.getValue());
		}
		return enchants;
	}
	
	public static boolean holderHasEnchantmentData(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().hasKey(TAG_XP_CURRENT) : false;
	}
	
	public static int getHolderCurrentXP(ItemStack stack) {
		return stack.getTagCompound().getInteger(TAG_XP_CURRENT);
	}
	
	public static int getHolderNeededXP(ItemStack stack) {
		return stack.getTagCompound().getInteger(TAG_XP_REQUIREMENT);
	}
	
	public static int holderMissingXP(ItemStack stack) {
		return getHolderNeededXP(stack)-getHolderCurrentXP(stack);
	}
	
	public static void setHolderCurrentXP(ItemStack stack, int xp) {
		stack.getTagCompound().setInteger(TAG_XP_CURRENT, xp);
	}
	
	public static Map<Enchantment, Integer> getHolderEnchantments(ItemStack stack) {
		Map<Enchantment, Integer> result = new HashMap<Enchantment, Integer>();
		NBTTagCompound cmp = stack.getTagCompound();
		int size = cmp.getInteger(TAG_ENCHANTS_SIZE);
        for (int i = 0; i < size; ++i)
        {
            short short1 = cmp.getShort(TAG_ENCHANT_ID+i);
            short short2 = cmp.getShort(TAG_ENCHANT_LEVEL+i);
            result.put(Enchantment.enchantmentsList[Integer.valueOf(short1)], Integer.valueOf(short2));
        }
        return result;
	}
	
	public static int getFirstValidHolderInPlayerInventory(EntityPlayer player) {
		for(int i = 0; i<player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack!=null && stack.getItem() instanceof ItemEnchantHolder && holderHasEnchantmentData(stack) && holderMissingXP(stack)>0)
				return i;
		}
		return -1;
	}
	
	public static Entry<Enchantment, Integer> getTopEnchantment(ItemStack stack) {
		Map<Enchantment, Integer> enchants = getHolderEnchantments(stack);
		return enchants.entrySet().iterator().next();
	}
	
	public static ItemStack removeTopEnchantmentFromHolder(ItemStack stack) {
		Map<Enchantment, Integer> enchants = getHolderEnchantments(stack);
		if(enchants.size()==1)
			return null;
		enchants.remove(getTopEnchantment(stack).getKey());
		return applyToStack(stack, enchants, getHolderNeededXP(stack), getHolderCurrentXP(stack));
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if(event.source.getEntity() instanceof EntityPlayer) {
			EntityLivingBase entity = event.entityLiving;
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			if(entity instanceof EntityPlayer)
				return;
			int slot = getFirstValidHolderInPlayerInventory(player);
			if(slot!=-1) {
				int xp = getExperiencePoints(event.entityLiving, player);
				while(slot!=-1 && xp>0) {
					ItemStack stack = player.inventory.getStackInSlot(slot);
					int xpToAdd = Math.min(xp, holderMissingXP(stack));
					xp-=xpToAdd;
					setHolderCurrentXP(stack,getHolderCurrentXP(stack)+xpToAdd);
					if(xp>0)
						slot = getFirstValidHolderInPlayerInventory(player);
				}
			}
		}
	}
	
	private int getExperiencePoints(EntityLivingBase entity, EntityPlayer player) {
		try {
			return tryGetExperiencePoints(entity, player);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	private int tryGetExperiencePoints(EntityLivingBase entity, EntityPlayer player) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class clazz = entity.getClass();
		while(true) {
			try {
				Method getExperiencePoints = clazz.getDeclaredMethod("getExperiencePoints", EntityPlayer.class);
				getExperiencePoints.setAccessible(true);
				return (Integer) getExperiencePoints.invoke(entity, player);
			} catch(NoSuchMethodException e) {
				clazz=clazz.getSuperclass();
			}
		}
	}
}
