package soundlogic.silva.common.item.block;

import soundlogic.silva.client.lib.LibResources;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockModMultiple extends ItemBlock{

	public ItemBlockModMultiple(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack)+par1ItemStack.getItemDamage();
	}
    @Override
	public int getMetadata(int p_77647_1_)
	{
	   return p_77647_1_;
	}
}
