package soundlogic.silva.common.item.block;

import soundlogic.silva.client.lib.LibResources;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMod extends ItemBlock{

	public ItemBlockMod(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("tile.", "tile." + LibResources.PREFIX_MOD);
	}
}
