package soundlogic.silva.common.item.block;

import soundlogic.silva.common.block.tile.TileManaCrystal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockManaCrystal extends ItemBlockMod{

	public ItemBlockManaCrystal(Block block) {
		super(block);
		this.setNoRepair();
		this.setMaxDamage(TileManaCrystal.MAX_MANA);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		int damage=stack.getItemDamage();
		return (double)(this.getMaxDamage()-damage)/this.getMaxDamage();
	}

}
