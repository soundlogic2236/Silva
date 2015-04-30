package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePortalUpgradeInhibit;
import soundlogic.silva.common.block.tile.TilePortalUpgradeRedstone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalUpgradeInhibit extends BlockPortalUpgradeBase{

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePortalUpgradeInhibit();
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
    	blockIcon = par1IconRegister.registerIcon(LibResources.PORTAL_UPGRADE_INHIBIT);
	}
}
