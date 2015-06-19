package soundlogic.silva.common.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDwarvenSign extends BlockContainer implements ILexiconable{

	IIcon iconOn, iconOff;
	public static IIcon arrowOn, arrowOff;
	
	protected BlockDwarvenSign() {
		super(Material.rock);
		setCreativeTab(Silva.creativeTab);
		setBlockName(LibBlockNames.DWARVEN_SIGN);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if(world.isRemote)
			return true;
		((TileDwarvenSign)world.getTileEntity(x, y, z)).activate();
		return true;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		if(meta<4)
			return iconOff;
		return iconOn;
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", ""));
		iconOn = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+"On");
		iconOff = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+"Off");
		arrowOn = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+"IconOn");
		arrowOff = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+"IconOff");
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 3;
        if (l == 0)
        {
            this.setBlockBounds(1F/16F, 	1F/16F, 	15F/16F, 
            					15F/16F, 	15F/16F,	1F);
        }

        if (l == 1)
        {
            this.setBlockBounds(1F/16F, 	1F/16F, 	0F, 
            					15F/16F, 	15F/16F, 	1F/16F);
        }

        if (l == 2)
        {
            this.setBlockBounds(15F/16F, 	1F/16F, 	1F/16F, 
            					1F, 		15F/16F, 	15F/16F);
        }

        if (l == 3)
        {
            this.setBlockBounds(0F, 		1F/16F, 	1F/16F, 
            					1F/16F, 	15F/16F, 	15F/16F);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
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

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int l = world.getBlockMetadata(x, y, z) & 3;
        if (!canPlaceBlockOnSide(world, x, y, z, l+2))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == DOWN  && world.isSideSolid(x, y + 1, z, DOWN )) ||
               (dir == UP    && world.isSideSolid(x, y - 1, z, UP   )) ||
               (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH)) ||
               (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH)) ||
               (dir == WEST  && world.isSideSolid(x + 1, y, z, WEST )) ||
               (dir == EAST  && world.isSideSolid(x - 1, y, z, EAST ));
    }
    

    @Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileDwarvenSign();
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.dwarfIntro;
	}
}
