package soundlogic.silva.common.item;

import soundlogic.silva.common.block.BlockModDoor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemModDoor extends ItemMod {

	BlockModDoor door;
	
    public ItemModDoor(String unLocalizedName, BlockModDoor door)
    {
    	super(unLocalizedName);
        this.maxStackSize = 1;
        this.door=door;
        door.toDrop=this;
    }
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side != 1)
        {
            return false;
        }
        else
        {
            ++y;
            Block block = getDoor(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
            {
                if (!block.canPlaceBlockAt(world, x, y, z))
                {
                    return false;
                }
                else
                {
                    int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    placeDoorBlock(world, x, y, z, i1, block);
                    --stack.stackSize;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
    
    public BlockModDoor getDoor(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    	return this.door;
    }

    public static void placeDoorBlock(World world, int x, int y, int z, int direction, Block block)
    {
        byte b0 = 0;
        byte b1 = 0;

        if (direction == 0)
        {
            b1 = 1;
        }

        if (direction == 1)
        {
            b0 = -1;
        }

        if (direction == 2)
        {
            b1 = -1;
        }

        if (direction == 3)
        {
            b0 = 1;
        }

        int i1 = (world.getBlock(x - b0, y, z - b1).isNormalCube() ? 1 : 0) + (world.getBlock(x - b0, y + 1, z - b1).isNormalCube() ? 1 : 0);
        int j1 = (world.getBlock(x + b0, y, z + b1).isNormalCube() ? 1 : 0) + (world.getBlock(x + b0, y + 1, z + b1).isNormalCube() ? 1 : 0);
        boolean flag = world.getBlock(x - b0, y, z - b1) == block || world.getBlock(x - b0, y + 1, z - b1) == block;
        boolean flag1 = world.getBlock(x + b0, y, z + b1) == block || world.getBlock(x + b0, y + 1, z + b1) == block;
        boolean flag2 = false;

        if (flag && !flag1)
        {
            flag2 = true;
        }
        else if (j1 > i1)
        {
            flag2 = true;
        }

        world.setBlock(x, y, z, block, direction, 2);
        world.setBlock(x, y + 1, z, block, 8 | (flag2 ? 1 : 0), 2);
        world.notifyBlocksOfNeighborChange(x, y, z, block);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
    }

}
