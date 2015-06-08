package soundlogic.silva.common.core.handler;

import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.mana.ManaItemHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class BifrostCreationHandler {

	private final int MANA_COST = 2000;
	
	@SubscribeEvent
	public void onUse(PlayerInteractEvent event) {
		if(event.world.isRemote)
			return;
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack!=null && stack.getItem() == GameRegistry.findItem("Botania", "rainbowRod") && event.action == Action.RIGHT_CLICK_BLOCK) {
			Block block=event.world.getBlock(event.x, event.y, event.z);
			int metadata=event.world.getBlockMetadata(event.x, event.y, event.z);
			if(block == GameRegistry.findBlock("Botania", "storage") && metadata == 4) {
				if(stack.getItemDamage() == 0 && ManaItemHandler.requestManaExact(stack, event.entityPlayer, MANA_COST, false)) {
					ManaItemHandler.requestManaExact(stack, event.entityPlayer, MANA_COST, true);
					stack.setItemDamage(stack.getMaxDamage());
					makeBifrostShards(event.x,event.y,event.z, event.world);
					event.setCanceled(true);
				}
			}
		}
	}

	private void makeBifrostShards(int x, int y, int z, World world) {
		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(ModItems.simpleResource,18,0)));
	}
}
