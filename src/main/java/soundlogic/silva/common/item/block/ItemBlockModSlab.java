package soundlogic.silva.common.item.block;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockModSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockModSlab extends ItemSlab{

	public ItemBlockModSlab(Block par1) {
		super(par1, ((BlockModSlab)par1).getSingleBlock(), ((BlockModSlab)par1).getFullBlock(), false);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return field_150939_a.getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD);
	}

	
}
