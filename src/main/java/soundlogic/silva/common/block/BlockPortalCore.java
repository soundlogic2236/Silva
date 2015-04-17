package soundlogic.silva.common.block;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalCore extends BlockContainer implements IWandable, ILexiconable{

	public IIcon iconOff;
	public IIcon iconOn;
	public IIcon portalTex;

	protected BlockPortalCore() {
		super(Material.wood);
		setBlockName(LibBlockNames.PORTAL_CORE);
		setHardness(10F);
		setStepSound(soundTypeWood);
		setCreativeTab(Silva.creativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TilePortalCore();
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconOff = par1IconRegister.registerIcon(LibResources.PORTAL_OFF);
		iconOn = par1IconRegister.registerIcon(LibResources.PORTAL_ON);
		portalTex = par1IconRegister.registerIcon(LibResources.PORTAL_INSIDE);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return iconOff;
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return ((TilePortalCore) world.getTileEntity(x, y, z)).getDimension()==null ? iconOff : iconOn;
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		return ((TilePortalCore) world.getTileEntity(x, y, z)).onWanded();
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.advancedPortals;
	}

}
