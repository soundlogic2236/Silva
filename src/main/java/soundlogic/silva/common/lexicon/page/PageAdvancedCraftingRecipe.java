package soundlogic.silva.common.lexicon.page;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class PageAdvancedCraftingRecipe extends PageCraftingRecipe{

	PageBackground background;
	
	public PageAdvancedCraftingRecipe(String unlocalizedName, List<IRecipe> recipes, PageBackground background) {
		super(unlocalizedName, recipes);
		this.background=background;
	}

	public PageAdvancedCraftingRecipe(String unlocalizedName, IRecipe recipe, PageBackground background) {
		super(unlocalizedName, recipe);
		this.background=background;
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);
		super.renderScreen(gui, mx, my);
	}
	

}
