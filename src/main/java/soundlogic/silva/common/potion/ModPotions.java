package soundlogic.silva.common.potion;

import java.util.Map;

import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.lib.LibItemNames;
import soundlogic.silva.common.lib.LibPotionNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;

public class ModPotions {
	
	public static Potion potionMead;
	
	public static void preInit() {
		potionMead=new PotionMod(ConfigHandler.potionIDMead, false, 0).setIconIndex(0, 0).setPotionName(LibPotionNames.MEAD);
	}
	
}
