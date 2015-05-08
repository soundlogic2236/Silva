package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlazeDust extends BlockDust {

	protected BlockBlazeDust() {
        super(new ItemStack(Items.blaze_powder));
		setBlockName(LibBlockNames.BLAZE_DUST);
		setLightLevel(.25F);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        double d0 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
        double d1 = (double)((float)y + 0.0625F);
        double d2 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;

        world.spawnParticle("flame", d0, d1, d2, (random.nextDouble() - .5D)/10D, (random.nextDouble() - .5D)/10D, (random.nextDouble() - .5D)/10D);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xffa300;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> output=new ArrayList<ItemStack>();
    	output.add(new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8));
        return output;
    }
    
}
