package soundlogic.silva.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import vazkii.botania.api.mana.IManaSpreader;

public class DarkElfActSpreader implements IDarkElfAct{

	Block inputBlock;
	int inputMetadata;
	Block outputBlock;
	int outputMetadata;
	
	public DarkElfActSpreader(int meta) {
		inputBlock = GameRegistry.findBlock("Botania", "spreader");
		inputMetadata = meta;
		outputBlock = ModBlocks.manaEater;
		outputMetadata = 0;
	}
	
	
	@Override
	public boolean tryApplyToBlock(World world, int x, int y, int z,
			TilePortalCore core) {
		Block block = world.getBlock(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		if(block != inputBlock || inputMetadata != metadata)
			return false;
		IManaSpreader spreader = (IManaSpreader) world.getTileEntity(x, y, z);
		float rotationX = spreader.getRotationX();
		float rotationY = spreader.getRotationY();
		world.setBlock(x, y, z, outputBlock, outputMetadata, 3);
		TileManaEater eater = (TileManaEater) world.getTileEntity(x, y, z);
		eater.rotationX=rotationX;
		eater.rotationY=rotationY;
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


	@Override
	public float chanceOfTriggeringTrap(World world, int x, int y, int z,
			TilePortalCore core) {
		return .1F;
	}

}
