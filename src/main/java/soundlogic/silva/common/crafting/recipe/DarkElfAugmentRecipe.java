package soundlogic.silva.common.crafting.recipe;

import soundlogic.silva.common.core.handler.DarkElfAugmentHandler;
import soundlogic.silva.common.item.ItemDarkElfAugment;
import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.ItemLexicon;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class DarkElfAugmentRecipe implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		ItemStack augment=null;
		ItemStack target=null;
		
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(augment!=null && target!=null)
					return false;
				if(stack.getItem() instanceof ItemDarkElfAugment && augment==null)
					augment=stack;
				else if(target==null)
					target=stack;
				else
					return false;
			}
		}
		if(augment==null || target==null)
			return false;
		return ItemDarkElfAugment.getAugment(augment).isStackValid(target);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack augment=null;
		ItemStack target=null;
		
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ItemDarkElfAugment && augment==null)
					augment=stack;
				else
					target=stack;
			}
		}
		return DarkElfAugmentHandler.augmentStack(target, ItemDarkElfAugment.getAugmentKey(augment));
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
