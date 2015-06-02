package soundlogic.silva.common.block;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockContrary extends BlockSimple implements ILexiconable {

	LexiconEntry entry;
	
	protected BlockContrary(Material material) {
		super(material);
        this.setHardness(-1);
	}
	
    @Override
    public boolean canSilkHarvest() {
    	return true;
    }


    @Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        float hardnessDefault = 3F;
        float hardnessSilk = 50F;
        
        float hardness = EnchantmentHelper.getSilkTouchModifier(player) ? hardnessSilk : hardnessDefault;

        if (!ForgeHooks.canHarvestBlock(this, player, metadata))
        {
            return player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100F;
        }
        else
        {
            return player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30F;
        }
	}

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> output=new ArrayList<ItemStack>();
        return output;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+getUnlocalizedName().replaceAll("tile\\.", ""));
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return entry;
	}
}
