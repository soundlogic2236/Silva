package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePortalUpgradeRedstone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalUpgradeRedstone extends BlockPortalUpgradeBase{

	IIcon iconOn;
	IIcon iconOff;
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePortalUpgradeRedstone();
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
    	iconOn = par1IconRegister.registerIcon(LibResources.PORTAL_UPGRADE_REDSTONE_ON);
    	iconOff = par1IconRegister.registerIcon(LibResources.PORTAL_UPGRADE_REDSTONE_OFF);
    	blockIcon = iconOff;
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TilePortalUpgradeRedstone tile = ((TilePortalUpgradeRedstone) world.getTileEntity(x, y, z));
		return tile.core==null ? iconOff : tile.core.getDimension()==null ? iconOff : iconOn;
	}
}
