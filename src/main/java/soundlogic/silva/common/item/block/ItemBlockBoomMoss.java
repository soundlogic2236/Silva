package soundlogic.silva.common.item.block;

import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.lib.LibBlockNames;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockBoomMoss extends ItemBlockMod{


    public ItemBlockBoomMoss(Block block) {
		super(block);
	}

	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return ModBlocks.boomMoss.getRenderColor(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
    	return super.getUnlocalizedName()+"."+stack.getItemDamage();
    }
	
	@Override
	public int getMetadata(int p_77647_1_)
	{
	   return p_77647_1_;
	}
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return par1ItemStack.getItemDamage()==1;
    }
}
