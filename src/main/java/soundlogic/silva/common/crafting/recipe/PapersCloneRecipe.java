package soundlogic.silva.common.crafting.recipe;

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

public class PapersCloneRecipe implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundPapers = false;
		boolean foundPaper = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(foundPapers && foundPaper)
					return false;
				if(stack.getItem() == ModItems.pageBundle && !foundPapers)
					foundPapers = true;
				else if(!foundPaper) {
					if(stack.getItem()==Items.paper)
						foundPaper = true;
					else return false;
				}
			}
		}

		return foundPapers && foundPaper;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack pageStack=null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() == ModItems.pageBundle)
					pageStack = stack;
			}
		}

		ItemStack copy = pageStack.copy();
		copy.stackSize=2;
		return copy;
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
