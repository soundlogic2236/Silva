package soundlogic.silva.common.lexicon.page;

import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.common.lexicon.page.PageText;

public class PageAdvancedText extends PageText{

	PageBackground background;
	
	public PageAdvancedText(String unlocalizedName, PageBackground background) {
		super(unlocalizedName);
		this.background=background;
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);
		super.renderScreen(gui, mx, my);
	}
	

}
