package soundlogic.silva.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDustBlock {
	public ItemStack getDustStack(World world, int x, int y, int z);

	public ItemStack getDustStack();

}
