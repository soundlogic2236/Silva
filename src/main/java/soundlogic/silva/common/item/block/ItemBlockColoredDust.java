package soundlogic.silva.common.item.block;

import java.awt.Color;

import soundlogic.silva.client.lib.LibResources;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockColoredDust extends ItemBlock{

	public ItemBlockColoredDust(Block block) {
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

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par1ItemStack.getItemDamage() >= EntitySheep.fleeceColorTable.length)
			return 0xFFFFFF;

		float[] color = EntitySheep.fleeceColorTable[par1ItemStack.getItemDamage()];
		return new Color(color[0], color[1], color[2]).getRGB();
	}
}
