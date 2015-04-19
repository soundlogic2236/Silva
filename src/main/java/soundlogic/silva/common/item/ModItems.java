package soundlogic.silva.common.item;

import java.util.Map;

import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import soundlogic.silva.common.lib.LibItemNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModItems {
	
	public static Item bifrostShard;
	public static Item questionMark;
	public static Item pageBundle;
	public static Item fakePageBundle;
	public static Item fakeLexicon;
	public static Item stoneHorse;
	public static Item deepQuartz;
	public static Item dwarfMead;
	public static Item dwarfChain;
	public static Item dwarfBarrier;

	public static void preInit() {
	
		bifrostShard=new ItemMod(LibItemNames.BIFROST_SHARD);
		questionMark=new ItemMod(LibItemNames.QUESTION_MARK);
		pageBundle=new ItemPapers(LibItemNames.PAGE_BUNDLE);
		fakePageBundle=new ItemFakePapers(LibItemNames.FAKE_PAPERS);
		fakeLexicon=new ItemFakeLexicon();
		stoneHorse=new ItemStoneHorse();
	}
	
}
