package soundlogic.silva.common.item.block;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.entity.EntityItemPixieFlower;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class ItemBlockPixieFlower extends ItemBlockMod {

	Random random = new Random();
	
	public static float dustChance = .03F;
	
	public ItemBlockPixieFlower(Block block) {
		super(block);
        this.setMaxDamage(0);
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
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer) {
		if(!entityPlayer.worldObj.isRemote) {
			givePlayerItem(new ItemStack(BotaniaAccessHandler.findBlock("flower"),1,stack.getItemDamage()),entityPlayer);
			if(random.nextFloat()<dustChance)
				givePlayerItem(new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,8),entityPlayer);
		}
		stack.stackSize--;
		return stack;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity oldEntity, ItemStack stack) {
		if(oldEntity instanceof EntityItemPixieFlower)
			return null;
		return new EntityItemPixieFlower((EntityItem)oldEntity);
	}
	
	private void givePlayerItem(ItemStack stack, EntityPlayer player) {
		if(!player.inventory.addItemStackToInventory(stack))
			player.dropPlayerItemWithRandomChoice(stack, false);
	}

}
