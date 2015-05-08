package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

public class BlockDarkenedDust extends BlockDust implements ILexiconable{

	protected BlockDarkenedDust() {
        super();
        this.setHardness(-1);
		setBlockName(LibBlockNames.DARKENED_DUST);
		setCreativeTab(Silva.creativeTab);
	}
	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister Reg)
    {
        blockIcon = Reg.registerIcon(LibResources.DARKENED_DUST);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0x000000;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> output=new ArrayList<ItemStack>();
        return output;
    }

    @Override
    public boolean canSilkHarvest() {
    	return true;
    }

    @Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        float hardness = 1F*100F/30F;
        if (hardness < 0.0F)
        {
            return 0.0F;
        }

        if (!(player.getCurrentEquippedItem() !=null && player.getCurrentEquippedItem().getItem().getToolClasses(player.getCurrentEquippedItem()).contains("pickaxe")))
        {
            return player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100F;
        }
        else
        {
            return player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30F;
        }
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.darkenedDust;
	}
}
