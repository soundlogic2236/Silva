package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
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

public class BlockPixieDust extends BlockDust implements ILexiconable{

	protected BlockPixieDust() {
        super(new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,8),true);
		setBlockName(LibBlockNames.PIXIE_DUST);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        for(int i=0;i<3;i++) {
	        double d0 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
	        double d1 = (double)((float)y + 0.0625F);
	        double d2 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
	
	        Botania.proxy.sparkleFX(world, d0, d1, d2, 1F, 0.25F, 0.9F, 0.1F + random.nextFloat() * 0.25F, 12);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xf51fff;
    }

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.elvenResources;
	}

}
