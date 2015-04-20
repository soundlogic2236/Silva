package soundlogic.silva.common.item;

import soundlogic.silva.common.lib.LibGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemBrokenDwarvenWrapper extends ItemMod{

	private final static String TAG_BROKEN_ITEM = "brokenItem";
	
	public ItemBrokenDwarvenWrapper(String unLocalizedName) {
		super(unLocalizedName);
		
	}
	
	public static ItemStack makeFromStack(ItemStack input) {
		ItemStack stack=new ItemStack(ModItems.brokenDwarvenWrapper);
		NBTTagCompound cmp=new NBTTagCompound();
		NBTTagCompound stackcmp=new NBTTagCompound();
		ItemStack inside=input.copy();
		inside.stackSize++;
		inside.setItemDamage(1);
		inside.writeToNBT(stackcmp);
		cmp.setTag(TAG_BROKEN_ITEM, stackcmp);
		stack.setTagCompound(cmp);
		return stack;
	}
	
	public static ItemStack getFromStack(ItemStack input) {
		return ItemStack.loadItemStackFromNBT(input.getTagCompound().getCompoundTag(TAG_BROKEN_ITEM));
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return String.format(StatCollector.translateToLocal(LibGUI.BROKEN_ITEM_NAME), getFromStack(stack).getDisplayName());
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		ItemStack renderStack=getFromStack(stack);
		IIcon result=renderStack.getItem().getIcon(renderStack, renderPass,player,usingItem,useRemaining);
		return result;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int renderPass) {
		ItemStack renderStack=getFromStack(stack);
		IIcon result=renderStack.getItem().getIcon(renderStack, renderPass);
		return result;
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		ItemStack renderStack=getFromStack(stack);
		IIcon result=renderStack.getItem().getIconIndex(renderStack);
		return result;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1;
	}
	
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
}
