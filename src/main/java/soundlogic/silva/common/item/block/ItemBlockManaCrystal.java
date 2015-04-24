package soundlogic.silva.common.item.block;

import soundlogic.silva.common.block.tile.TileManaCrystal;
import vazkii.botania.api.mana.IManaItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemBlockManaCrystal extends ItemBlockMod implements IManaItem{

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

	@Override
	public void addMana(ItemStack stack, int mana) {
		stack.setItemDamage(Math.min(getMana(stack) + mana, getMaxMana(stack)));
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack pool) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack pool) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public int getMana(ItemStack stack) {
		return stack.getItemDamage();
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return TileManaCrystal.MAX_MANA;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return true;
	}

}
