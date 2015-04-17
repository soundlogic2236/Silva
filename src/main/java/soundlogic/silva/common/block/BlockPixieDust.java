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

public class BlockPixieDust extends Block implements ILexiconable{

	private static ItemStack pixieStack;
	
	protected BlockPixieDust() {
        super(Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		setBlockName(LibBlockNames.PIXIE_DUST);
		pixieStack=new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8);
	}
	
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return LibRenderIDs.idPixieDust;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        int strength = getStrength(world,x,y,z);

        for(int i=0;i<strength;i++) {
	        double d0 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
	        double d1 = (double)((float)y + 0.0625F);
	        double d2 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
	
	        Botania.proxy.sparkleFX(world, d0, d1, d2, 1F, 0.25F, 0.9F, 0.1F + random.nextFloat() * 0.25F, 12);
        }
    }
    
    public int getStrength(IBlockAccess world, int x, int y, int z) {
    	return 3;
    }
    
    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) || p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_) == Blocks.glowstone;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xf51fff;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> output=new ArrayList<ItemStack>();
    	output.add(new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8));
        return output;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
    	return new ItemStack(vazkii.botania.common.item.ModItems.manaResource,1,8);
    }

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.elvenResources;
	}

}
