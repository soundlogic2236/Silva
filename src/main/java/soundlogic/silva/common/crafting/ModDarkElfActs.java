package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.crafting.recipe.DarkElfActDarkenedDust;
import soundlogic.silva.common.crafting.recipe.DarkElfActFake;
import soundlogic.silva.common.crafting.recipe.DarkElfActFurnace;
import soundlogic.silva.common.crafting.recipe.DarkElfActNoteBlock;
import soundlogic.silva.common.crafting.recipe.DarkElfActOreDict;
import soundlogic.silva.common.crafting.recipe.DarkElfActRedstoneControl;
import soundlogic.silva.common.crafting.recipe.DarkElfActSimple;
import soundlogic.silva.common.crafting.recipe.DarkElfActSpreader;
import soundlogic.silva.common.crafting.recipe.DarkElfActTNT;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;

public class ModDarkElfActs {

	public static List<IDarkElfAct> spreaderConversions;
	public static IDarkElfAct stoneConversion;
	public static IDarkElfAct woodConversion;
	
	public static List<IDarkElfAct> fakeDustConversions;
	
	public static void preInit() {
		spreaderConversions = new ArrayList<IDarkElfAct>();
		spreaderConversions.add(new DarkElfActSpreader(0));
		spreaderConversions.add(new DarkElfActSpreader(1));
		spreaderConversions.add(new DarkElfActSpreader(2));
		spreaderConversions.add(new DarkElfActSpreader(3));
		DarkElfActs.addActs(spreaderConversions);
		
		stoneConversion = new DarkElfActOreDict(ModBlocks.darkenedStone,0,"stone");
		DarkElfActs.addAct(stoneConversion);

		woodConversion = new DarkElfActOreDict(ModBlocks.darkenedWood,0,"logWood");
		DarkElfActs.addAct(woodConversion);
		
		fakeDustConversions = new ArrayList<IDarkElfAct>();
		fakeDustConversions.add(new DarkElfActFake(new ItemStack(ModBlocks.darkenedDust), new ItemStack(Items.redstone)));
		fakeDustConversions.add(new DarkElfActFake(new ItemStack(ModBlocks.darkenedDust), new ItemStack(Items.glowstone_dust)));
		fakeDustConversions.add(new DarkElfActFake(new ItemStack(ModBlocks.darkenedDust), new ItemStack(Items.blaze_powder)));
		fakeDustConversions.add(new DarkElfActFake(new ItemStack(ModBlocks.darkenedDust), new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8)));
		
		DarkElfActs.addAct(new DarkElfActFurnace());
		DarkElfActs.addAct(new DarkElfActNoteBlock());
		DarkElfActs.addAct(new DarkElfActRedstoneControl());
		DarkElfActs.addAct(new DarkElfActTNT());
		DarkElfActs.addAct(new DarkElfActDarkenedDust());
	}
	
}
