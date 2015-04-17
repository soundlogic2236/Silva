package soundlogic.silva.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.relic.ItemThorRing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBifrostSparkling extends BlockBifrost {

	protected BlockBifrostSparkling() {
		super();
		setBlockName(LibBlockNames.BIFROST_BLOCK_SPARKLING);
		setLightLevel(1F);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(LibResources.BLOCK_BIFROST_SPARKLING);
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.bifrost;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        for(int i=0;i<4;i++) {
        	int face=random.nextInt(6);
	        double d0 = 0.5D + ((double)random.nextFloat() - 0.5D) * 0.9D;
	        double d1 = 0.5D + ((double)random.nextFloat() - 0.5D) * 0.9D;
        	double nx=x;
        	double ny=y;
        	double nz=z;
        	switch(face) {
        	case 0:nx+=d0;ny+=d1;break;
        	case 1:nx+=d0;ny+=d1;nz++;break;
        	case 2:ny+=d0;nz+=d1;break;
        	case 3:ny+=d0;nz+=d1;nx++;;break;
        	case 4:nx+=d0;nz+=d1;break;
        	case 5:nx+=d0;nz+=d1;ny++;break;
        	}
        	double r0 = i==0 ? .8F : .1F;
	        double a1 = Math.random()*2*Math.PI;
	        double a2 = Math.random()*2*Math.PI;
	        double r = Math.abs(Math.sin(a1)*Math.cos(a2))*(r0+Math.random()*r0);
	        double g = Math.abs(Math.sin(a1)*Math.sin(a2))*(r0+Math.random()*r0);
	        double b = Math.abs(Math.cos(a1))*(.1F+Math.random()*.1F);
	        Botania.proxy.sparkleFX(world, nx, ny, nz, (float)r, (float)g, (float)b, 0.8F + random.nextFloat() * 0.65F, 12);
        }
    }

}
