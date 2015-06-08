package soundlogic.silva.common.crafting.recipe;

import java.util.List;

import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.crafting.DarkElfLoot;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDarkElfActSpecialLoot extends IDarkElfAct {

	public List<DarkElfLoot.LootEntry> modifyLootList(TilePortalCore core, List<DarkElfLoot.LootEntry> loot);
}
