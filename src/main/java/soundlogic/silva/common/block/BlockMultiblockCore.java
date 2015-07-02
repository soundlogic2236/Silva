package soundlogic.silva.common.block;

import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMultiblockCore extends BlockMultiblockProxy{

	protected BlockMultiblockCore(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileMultiblockCore();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for(Entry<String,MultiblockDataBase> entry : MultiblockDataBase.multiBlocksByName.entrySet()) {
			entry.getValue().registerBlockIcons(par1IconRegister);
		}
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idMultiblockCore;
	}
}
