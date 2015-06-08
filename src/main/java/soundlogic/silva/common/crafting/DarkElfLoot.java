package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;
import soundlogic.silva.common.crafting.recipe.IDarkElfActSpecialLoot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class DarkElfLoot {

	static Random random = new Random();
	
	public static ArrayList<LootEntry> loot = new ArrayList<LootEntry>();
	
	public static void addLoot(LootEntry entry) {
		loot.add(entry);
	}
	public static void addLoot(LootResult result, int weight) {
		loot.add(new LootEntry(result, weight));
	}
	public static void addLoot(ItemStack stack, int spookLevel, float spookChance, int weight) {
		loot.add(new LootEntry(stack, spookLevel, spookChance, weight));
	}
	public static LootResult getLoot(TilePortalCore core, IDarkElfAct act) {
		List<LootEntry> loot=DarkElfLoot.loot;
		if(act instanceof IDarkElfActSpecialLoot) {
			loot=((IDarkElfActSpecialLoot) act).modifyLootList(core, new ArrayList(loot));
		}
		LootEntry entry = (LootEntry) WeightedRandom.getRandomItem(random, loot);
		return entry.getResult();
	}
	
	public static class LootResult {
		ItemStack stack;
		float spookChance;
		int spookLevel;
		public LootResult(ItemStack stack, int spookLevel, float spookChance) {
			this.stack=stack;
			this.spookChance=spookChance;
			this.spookLevel=spookLevel;
		}
		public ItemStack getStack() {
			return stack;
		}
		public float getSpookChance() {
			return spookChance;
		}
		public int getSpookLevel() {
			return spookLevel;
		}
	}
	
	public static class LootEntry extends WeightedRandom.Item {

		LootResult result;
		
		public LootEntry(LootResult result, int weight) {
			super(weight);
			this.result=result;
		}
		public LootEntry(ItemStack stack, int spookLevel, float spookChance, int weight) {
			this(new LootResult(stack, spookLevel, spookChance), weight);
		}
		public LootResult getResult() {
			return result;
		}
	}
}
