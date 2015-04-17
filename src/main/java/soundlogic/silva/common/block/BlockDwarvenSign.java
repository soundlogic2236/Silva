package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.lib.LibBlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDwarvenSign extends BlockContainer{

	protected BlockDwarvenSign() {
		super(Material.iron);
		setCreativeTab(Silva.creativeTab);
		setBlockName(LibBlockNames.DWARVEN_SIGN);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		((TileDwarvenSign)world.getTileEntity(x, y, z)).activate();
		return true;
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
        float f = 0.28125F;
        float f1 = 0.78125F;
        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

        if (l == 2)
        {
            this.setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
        }

        if (l == 3)
        {
            this.setBlockBounds(f2, f, 0.0F, f3, f1, f4);
        }

        if (l == 4)
        {
            this.setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
        }

        if (l == 5)
        {
            this.setBlockBounds(0.0F, f, f2, f4, f1, f3);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        boolean flag = false;

        int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
        flag = true;

        if (l == 2 && p_149695_1_.getBlock(p_149695_2_, p_149695_3_, p_149695_4_ + 1).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 3 && p_149695_1_.getBlock(p_149695_2_, p_149695_3_, p_149695_4_ - 1).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 4 && p_149695_1_.getBlock(p_149695_2_ + 1, p_149695_3_, p_149695_4_).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 5 && p_149695_1_.getBlock(p_149695_2_ - 1, p_149695_3_, p_149695_4_).getMaterial().isSolid())
        {
            flag = false;
        }

        if (flag)
        {
            this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }

        super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
    }
     @Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileDwarvenSign();
	}
}
