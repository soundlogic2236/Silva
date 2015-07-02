package soundlogic.silva.common.block;

import java.util.Random;

import soundlogic.silva.client.lib.LibResources;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockModDoor  extends BlockDoor implements ILexiconable {

	LexiconEntry entry;
	
	boolean respondToPlayer;
	boolean respondToRedstone;
	boolean floats;
	
	public Item toDrop;
	
	protected BlockModDoor(Material material, boolean respondToPlayer, boolean respondToRedstone, boolean floats) {
        super(material);
        this.respondToPlayer=respondToPlayer;
        this.respondToRedstone=respondToRedstone;
        this.floats=floats;
	}

	public String getTextureName() {
		return LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "");
	}

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (!shouldRespondToPlayer(world, player, x, y, z))
        {
            return false; //Allow items to interact with the door
        }
        else
        {
            int i1 = this.func_150012_g(world, x, y, z);
            int j1 = i1 & 7;
            j1 ^= 4;

            if ((i1 & 8) == 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, j1, 2);
                world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y - 1, z, j1, 2);
                world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
            }

            world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
            return true;
        }
    }

	public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
    {
        int l = world.getBlockMetadata(x, y, z);

        if ((l & 8) == 0)
        {
            boolean flag = false;

            if (world.getBlock(x, y + 1, z) != this)
            {
                world.setBlockToAir(x, y, z);
                flag = true;
            }

            if (!this.floats && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z))
            {
                world.setBlockToAir(x, y, z);
                flag = true;

                if (world.getBlock(x, y + 1, z) == this)
                {
                    world.setBlockToAir(x, y + 1, z);
                }
            }

            if (flag)
            {
                if (!world.isRemote)
                {
                    this.dropBlockAsItem(world, x, y, z, l, 0);
                }
            }
            else if (shouldRespondToRedstone(world, x, y, z))
            {
                boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);

                if ((flag1 || p_149695_5_.canProvidePower()) && p_149695_5_ != this)
                {
                    this.func_150014_a(world, x, y, z, flag1);
                }
            }
        }
        else
        {
            if (world.getBlock(x, y - 1, z) != this)
            {
                world.setBlockToAir(x, y, z);
            }

            if (p_149695_5_ != this)
            {
                this.onNeighborBlockChange(world, x, y - 1, z, p_149695_5_);
            }
        }
    }

	public boolean shouldRespondToPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return this.respondToPlayer;
	}
	
    public boolean shouldRespondToRedstone(World world, int x, int y, int z) {
		return this.respondToRedstone;
	}

    public Item getItemDropped(int meta, Random rand, int fortune) {
    	return toDrop;
    }
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
    	return toDrop;
    }

	public BlockModDoor setEntry(LexiconEntry entry) {
		this.entry=entry;
		return this;
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return entry;
	}

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return p_149742_3_ >= p_149742_1_.getHeight() - 1 ? false : (floats || World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_)) && super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_) && super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_ + 1, p_149742_4_);
    }
}
