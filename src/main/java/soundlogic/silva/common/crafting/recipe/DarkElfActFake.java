package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActFake implements IDarkElfAct{

	ItemStack input;
	ItemStack output;
	
	public DarkElfActFake(ItemStack output, ItemStack input) {
		this.input=input;
		this.output=output;
	}
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		return false;
	}

	@Override
	public List<ItemStack> getDisplayInputs() {
		return Arrays.asList(input);
	}

	@Override
	public List<ItemStack> getDisplayOutputs() {
		return Arrays.asList(output);
	}

}
