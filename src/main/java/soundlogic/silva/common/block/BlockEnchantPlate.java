package soundlogic.silva.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileEnchantPlate;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockEnchantPlate extends BlockContainer implements ILexiconable {

	public static IIcon overlay;
	IIcon[] icons;

	public BlockEnchantPlate() {
		super(Material.iron);
		setBlockBounds(0F, 0F, 0F, 1F, 3F / 16F, 1F);
		setHardness(3F);
		setResistance(10F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.ENCHANT_PLATE);
		setCreativeTab(Silva.creativeTab);
	}	

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[3];
		for(int i = 0; i < icons.length; i++)
			icons[i] = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+i);;
		overlay = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+"Overlay");
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return icons[Math.min(2, par1)];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEnchantPlate();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.enchantmentMoving;
	}

}
