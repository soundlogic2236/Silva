package soundlogic.silva.common.core.handler;

import java.util.HashMap;
import java.util.Map.Entry;

import soundlogic.silva.common.block.IDustBlock;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.mana.ManaItemHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class DustHandler {

	static HashMap<ItemStack, Block> overrideDustBlocks = new HashMap<ItemStack, Block>();
	static HashMap<Block, ItemStack> stacks = new HashMap<Block, ItemStack>();
	
	@SubscribeEvent
	public void onUse(PlayerInteractEvent event) {
		if(event.world.isRemote || event.action!=event.action.RIGHT_CLICK_BLOCK)
			return;
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack==null)
			return;
		Block block = getBlockForStack(stack);
		if(block==null)
			return;
		int x=event.x+ForgeDirection.VALID_DIRECTIONS[event.face].offsetX;
		int y=event.y+ForgeDirection.VALID_DIRECTIONS[event.face].offsetY;
		int z=event.z+ForgeDirection.VALID_DIRECTIONS[event.face].offsetZ;
		ItemStack itemBlock=new ItemStack(block);
		itemBlock.tryPlaceItemIntoWorld(event.entityPlayer, event.world, x, y, z, 0, 0F, 0F, 0F);
		if(itemBlock.stackSize==0 && !event.entityPlayer.capabilities.isCreativeMode) {
			event.entityPlayer.getCurrentEquippedItem().stackSize--;
			if(event.entityPlayer.getCurrentEquippedItem().stackSize==0)
				event.entityPlayer.setCurrentItemOrArmor(0,null);
		}
		event.setCanceled(true);
	}
	
	public static void registerBlock(ItemStack stack, Block block) {
		overrideDustBlocks.put(stack, block);
	}
	
	public static void registerBlock(IDustBlock block) {
		overrideDustBlocks.put(block.getDustStack(), (Block) block);
	}
	
	public static ItemStack getStackFromBlock(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.redstone_wire)
			return new ItemStack(Items.redstone);
		else if(block instanceof IDustBlock)
			return ((IDustBlock) block).getDustStack(world, x, y, z);
		return null;
	}
	
	public static boolean isDust(IBlockAccess world, int x, int y, int z) {
		return getStackFromBlock(world, x, y, z)!=null;
	}
	
	public static boolean isMagicDust(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.redstone_wire)
			return true;
		else if(block instanceof IDustBlock)
			return ((IDustBlock) block).isDustMagic();
		return false;
	}

	public static Block getBlockForStack(ItemStack stack) {
		for(Entry<ItemStack,Block> entry : overrideDustBlocks.entrySet()) {
			if(entry.getKey().isItemEqual(stack))
				return entry.getValue();
		}
		return null;
	}
}
