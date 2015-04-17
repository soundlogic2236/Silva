package soundlogic.silva.common.lexicon.page;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageText;

public class PageAdvancedImage extends PageImage{

	PageBackground background;
	
	public PageAdvancedImage(String unlocalizedName, String image, PageBackground background) {
		super(unlocalizedName, image);
		this.background=background;
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);
		super.renderScreen(gui, mx, my);
	}
	

}
