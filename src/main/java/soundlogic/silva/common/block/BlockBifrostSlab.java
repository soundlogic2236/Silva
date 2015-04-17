package soundlogic.silva.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.relic.ItemThorRing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBifrostSlab extends BlockModSlab implements IBifrostBlock, ILexiconable {

	public BlockBifrostSlab(boolean full) {
		super(full, ModBlocks.bifrostBlock.getMaterial());
		if(!full) {
			setBlockName(LibBlockNames.BIFROST_BLOCK_SLAB);
			setCreativeTab(Silva.creativeTab);
			useNeighborBrightness = true;
		}
		else
			setBlockName(LibBlockNames.BIFROST_BLOCK_DOUBLE_SLAB);
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

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return ModBlocks.bifrostBlock.getIcon(p_149691_1_, 0);
    }

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.bifrost;
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.bifrostBlockDoubleSlab;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.bifrostBlockSlab;
	}

}
