package soundlogic.silva.common.block;

import java.util.Random;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibBlockNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPortalUpgradeBase extends BlockContainer implements IBifrostBlock, IPortalFocus {

	protected BlockPortalUpgradeBase() {
		super(Material.glass);
		setHardness(-1F);
		setResistance(4000F);
		setCreativeTab(Silva.creativeTab);
		setStepSound(soundTypeGlass);
		setLightLevel(1F);
	}
	
	@Override
	public boolean isPortalFocus(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		ModBlocks.bifrostBlock.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
		return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		ModBlocks.bifrostBlock.onNeighborBlockChange(world, x, y, z, block);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		return ModBlocks.bifrostBlock.getPlayerRelativeBlockHardness(player, world, x, y, z);
	}
	
	@Override
	public boolean isBifrost(World world, int x, int y, int z) {
		return true;
	}
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
    	ModBlocks.bifrostBlockSparkling.randomDisplayTick(world, x, y, z, random);
    }

    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
    	return true;
    }
}
