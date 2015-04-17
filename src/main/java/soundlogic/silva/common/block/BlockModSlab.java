package soundlogic.silva.common.block;

import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class BlockModSlab extends BlockSlab{

	public BlockModSlab(boolean full, Material material) {
		super(full, material);
	}
	public abstract BlockSlab getFullBlock();

	public abstract BlockSlab getSingleBlock();

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(getSingleBlock());
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(getSingleBlock());
	}

	@Override
	public ItemStack createStackedBlock(int par1) {
		return new ItemStack(getSingleBlock());
	}
	
	@Override
	public String func_150002_b(int p_150002_1_) {
		return super.getUnlocalizedName();
	}

}
