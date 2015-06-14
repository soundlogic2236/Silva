package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileSlingshot;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSlingshot extends BlockContainer implements ILexiconable, IWireframeAABBProvider, IWandable {

	public static boolean noCollide = false;
	
	protected BlockSlingshot() {
		super(Material.wood);
		setHardness(1.0F);
		setResistance(10.0F);
		setStepSound(soundTypeWood);
		setBlockBounds(3F/16F, 0F, 3F/16F, 13F/16F, 13F/16F, 13F/16F);
		setCreativeTab(Silva.creativeTab);
	}
	
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float sx, float sy, float sz) {
    	return ((TileSlingshot)world.getTileEntity(x, y, z)).fireProjectile(player);
    }

    
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSlingshot();
	}
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return LibRenderIDs.idSlingshot;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_)
    {
        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean canCollideCheck(int meta, boolean boat) {
    	return !noCollide;
    }
        
	@Override
	public AxisAlignedBB getWireframeAABB(World world, int x, int y, int z) {
		float f = 1F / 16F;
		return AxisAlignedBB.getBoundingBox(x + f, y + f, z + f, x + 1 - f, y + 1 - f, z + 1 - f);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack stack) {
		return LexiconData.slingshot;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer arg0, ItemStack arg1, World arg2, int arg3, int arg4, int arg5, int arg6) {
		return true;
	}

}
