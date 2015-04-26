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
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        return ModBlocks.boomMoss.getRenderColor(stack.getItemDamage());
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
	
}
