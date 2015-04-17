package soundlogic.silva.common.item.block;

import soundlogic.silva.common.lib.LibBlockNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockPylon extends ItemBlockMod{

    public ItemBlockPylon(Block p_i45326_1_)
	   {
	       super(p_i45326_1_);
	       this.setMaxDamage(0);
	       this.setHasSubtypes(true);
	   }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
    	return "tile."+LibBlockNames.PYLON_SUBS[stack.getItemDamage()];
    }
    
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
    @Override
	public int getMetadata(int p_77647_1_)
	{
	   return p_77647_1_;
	}
}
