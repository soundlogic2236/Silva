package soundlogic.silva.common.core.handler;

import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.mana.ManaItemHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PixieDustHandler {

	private final int MANA_COST = 2000;
	
	@SubscribeEvent
	public void onUse(PlayerInteractEvent event) {
		if(event.world.isRemote)
			return;
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack!=null && stack.getItem() == vazkii.botania.common.item.ModItems.manaResource && stack.getItemDamage() == 8 && event.action == Action.RIGHT_CLICK_BLOCK) {
			int x=event.x+ForgeDirection.VALID_DIRECTIONS[event.face].offsetX;
			int y=event.y+ForgeDirection.VALID_DIRECTIONS[event.face].offsetY;
			int z=event.z+ForgeDirection.VALID_DIRECTIONS[event.face].offsetZ;
			ItemStack pixieBlock=new ItemStack(ModBlocks.pixieDust);
			pixieBlock.tryPlaceItemIntoWorld(event.entityPlayer, event.world, x, y, z, 0, 0F, 0F, 0F);
			if(pixieBlock.stackSize==0 && !event.entityPlayer.capabilities.isCreativeMode) {
				event.entityPlayer.getCurrentEquippedItem().stackSize--;
				if(event.entityPlayer.getCurrentEquippedItem().stackSize==0)
					event.entityPlayer.setCurrentItemOrArmor(0,null);
			}
			event.setCanceled(true);
		}
	}
}
