package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.tile.TilePortalCore;

public class DarkElfActOreDict implements IDarkElfAct{

	String input;
	Block outputBlock;
	int outputMetadata;
	
	public DarkElfActOreDict(Block outputBlock, int outputMetadata, String input) {
		this.outputBlock=outputBlock;
		this.outputMetadata=outputMetadata;
		this.input=input;
	}
	
	public DarkElfActOreDict(ItemStack output, String input) {
		this((ItemBlock)output.getItem(), output, input);
	}
	
	private DarkElfActOreDict(ItemBlock outputItem, ItemStack output, String input) {
		this(outputItem.field_150939_a, outputItem.getMetadata(output.getItemDamage()), input);
	}
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		ItemStack stack = new ItemStack(block, 1, metadata);
		if(!isOreDict(stack,input))
			return false;
		world.setBlock(x, y, z, outputBlock, outputMetadata, 3);
		return true;
	}

	public boolean isOreDict(ItemStack stack, String entry) {
		if(stack == null || stack.getItem() == null)
			return false;

		for(ItemStack ostack : OreDictionary.getOres(entry)) {
			ItemStack cstack = ostack.copy();
			if(cstack.getItemDamage() == Short.MAX_VALUE)
				cstack.setItemDamage(stack.getItemDamage());

			if(stack.isItemEqual(cstack))
				return true;
		}

		return false;
	}
	
	@Override
	public List<ItemStack> getDisplayInputs() {
		return Arrays.asList(OreDictionary.getOres(input).get(0));
	}

	@Override
	public List<ItemStack> getDisplayOutputs() {
		return Arrays.asList(new ItemStack(outputBlock, 1, outputMetadata));
	}

}
