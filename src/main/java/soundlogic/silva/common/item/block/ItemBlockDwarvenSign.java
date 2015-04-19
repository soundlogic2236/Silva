package soundlogic.silva.common.item.block;

import soundlogic.silva.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockDwarvenSign extends ItemBlockMod {

	public ItemBlockDwarvenSign(Block block) {
		super(block);
	}
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xs, float xy, float xz)
    {
        if (side == 0)
        {
            return false;
        }
        else if (!world.getBlock(x, y, z).getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }

            if (!player.canPlayerEdit(x, y, z, side, stack))
            {
                return false;
            }
            else if (world.isRemote)
            {
                return true;
            }
            else
            {
                if (side == 1)
                {
                    return false;
                }
                else
                {
                    world.setBlock(x, y, z, ModBlocks.dwarvenSign, side, 3);
                }

                --stack.stackSize;

                return true;
            }
        }
    }

}
