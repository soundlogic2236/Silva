package soundlogic.silva.common.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredDust extends BlockDust {

	protected BlockColoredDust() {
        super(false);
		setBlockName(LibBlockNames.COLORED_DUST);
		setCreativeTab(Silva.creativeTab);
	}
	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister Reg)
    {
        blockIcon = Reg.registerIcon(LibResources.COLORED_DUST);
    }

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < 16; i++)
			par3.add(new ItemStack(par1, 1, i));
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return getColorFromMeta(world.getBlockMetadata(x, y, z));
    }
    
    public int getColorFromMeta(int meta) {
    	float[] col = EntitySheep.fleeceColorTable[meta];
    	return (new Color(col[0],col[1],col[2])).getRGB();
    }

}
