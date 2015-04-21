package soundlogic.silva.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockSimpleMultiple extends BlockSimple{

	int subCount;
	IIcon[] blockIcons;
	
	protected BlockSimpleMultiple(Material material, int count) {
		super(material);
		subCount=count;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < subCount; i++)
			par3.add(new ItemStack(par1, 1, i));
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcons=new IIcon[subCount];
		for(int i = 0; i < subCount; i++)
			blockIcons[i] = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", "")+i);
	}
	

	@Override
	public IIcon getIcon(int side, int meta) {
		return blockIcons[meta];
	}
	
}
