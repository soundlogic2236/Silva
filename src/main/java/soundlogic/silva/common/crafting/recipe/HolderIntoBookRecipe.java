package soundlogic.silva.common.crafting.recipe;

import java.util.Map.Entry;

import soundlogic.silva.common.core.handler.EnchantmentMoverHandler;
import soundlogic.silva.common.item.ItemEnchantHolder;
import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.ItemLexicon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class HolderIntoBookRecipe implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundHolder = false;
		boolean foundBook = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(foundHolder && foundBook)
					return false;
				if(stack.getItem() instanceof ItemEnchantHolder && !foundHolder && EnchantmentMoverHandler.holderHasEnchantmentData(stack) && ( EnchantmentMoverHandler.holderMissingXP(stack)<=0 ))
					foundHolder = true;
				else if(!foundBook) {
					if(stack.getItem()==Items.book)
						foundBook = true;
					else return false;
				}
			}
		}
		
		return foundHolder && foundBook;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack holderStack=null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ItemEnchantHolder)
					holderStack = stack;
			}
		}
		
		Entry<Enchantment, Integer> enchant = EnchantmentMoverHandler.getTopEnchantment(holderStack);
		
		ItemStack result = new ItemStack(Items.enchanted_book);
		Items.enchanted_book.addEnchantment(result, new EnchantmentData(enchant.getKey(), enchant.getValue()));

		return result;
	}
	
	
	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
}
