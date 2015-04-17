package soundlogic.silva.common.crafting.recipe;

import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.ItemLexicon;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class PapersIntoLexiconRecipe implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundPapers = false;
		boolean foundLexicon = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(foundPapers && foundLexicon)
					return false;
				if(stack.getItem() == ModItems.pageBundle && !foundPapers)
					foundPapers = true;
				else if(!foundLexicon) {
					if(stack.getItem() instanceof ILexicon)
						foundLexicon = true;
					else return false;
				}
			}
		}

		return foundPapers && foundLexicon;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack item = null;
		ItemStack pageStack=null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ILexicon && item == null)
					item = stack;
				else pageStack = stack;
			}
		}

		KnowledgeType knowledgeType=ItemPapers.getKnowledgeType(pageStack);
		LexiconEntry openingEntry=ItemPapers.getOpeningEntry(pageStack);
		
		ILexicon lexicon = (ILexicon) item.getItem();
		if(lexicon.isKnowledgeUnlocked(item, knowledgeType))
			return null;

		ItemStack copy = item.copy();
		lexicon.unlockKnowledge(copy, knowledgeType);
		ItemLexicon.setForcedPage(copy, openingEntry.unlocalizedName);
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
