package soundlogic.silva.common.lexicon;

import soundlogic.silva.common.lib.LibLexicon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class SLexiconEntry extends LexiconEntry {

	public SLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
		BotaniaAPI.addEntry(this, category);
	}

	@Override
	public LexiconEntry setLexiconPages(LexiconPage... pages) {
		for(LexiconPage page : pages) {
			page.unlocalizedName = LibLexicon.PAGE_PREFIX + getLazyUnlocalizedName() + page.unlocalizedName;
			if(page instanceof ITwoNamedPage) {
				ITwoNamedPage dou = (ITwoNamedPage) page;
				dou.setSecondUnlocalizedName(LibLexicon.PAGE_PREFIX + getLazyUnlocalizedName() + dou.getSecondUnlocalizedName());
			}
		}

		return super.setLexiconPages(pages);
	}
	@Override
	public String getUnlocalizedName() {
		return LibLexicon.ENTRY_PREFIX + super.getUnlocalizedName();
	}

	public String getLazyUnlocalizedName() {
		return super.getUnlocalizedName();
	}

}
