package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActSimple implements IDarkElfAct{

	Block inputBlock;
	int inputMetadata;
	Block outputBlock;
	int outputMetadata;
	
	public DarkElfActSimple(Block outputBlock, int outputMetadata, Block inputBlock, int inputMetadata) {
		this.outputBlock=outputBlock;
		this.outputMetadata=outputMetadata;
		this.inputBlock=inputBlock;
		this.inputMetadata=inputMetadata;
	}
	
	public DarkElfActSimple(ItemStack output, ItemStack input) {
		this((ItemBlock)output.getItem(), output, (ItemBlock)input.getItem(), input);
	}
	
	private DarkElfActSimple(ItemBlock outputItem, ItemStack output, ItemBlock inputItem, ItemStack input) {
		this(outputItem.field_150939_a, outputItem.getMetadata(output.getItemDamage()), inputItem.field_150939_a, inputItem.getMetadata(input.getItemDamage()));
	}
	
	@Override
	public boolean blockIsValidTarget(Block block) {
		return block==inputBlock;
	}

	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		if(block != inputBlock || inputMetadata != metadata)
			return false;
		world.setBlock(x, y, z, outputBlock, outputMetadata, 3);
		return true;
	}

	@Override
	public List<ItemStack> getDisplayInputs() {
		return Arrays.asList(new ItemStack(inputBlock, 1, inputMetadata));
	}

	@Override
	public List<ItemStack> getDisplayOutputs() {
		return Arrays.asList(new ItemStack(outputBlock, 1, outputMetadata));
	}

}
