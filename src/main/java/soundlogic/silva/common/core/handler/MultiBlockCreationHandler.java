package soundlogic.silva.common.core.handler;

import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataCarnilotus;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataLavashroom;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataMysticalGrinder;
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

public class MultiBlockCreationHandler {

	public MultiBlockCreationHandler() {
		new MultiblockDataLavashroom();
		new MultiblockDataCarnilotus();
		new MultiblockDataMysticalGrinder();
	}
	
	@SubscribeEvent
	public void onUse(PlayerInteractEvent event) {
		if(event.world.isRemote)
			return;
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack!=null && stack.getItem() == GameRegistry.findItem("Botania", "twigWand") && event.action == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.world.getBlock(event.x, event.y, event.z);
			if(!MultiblockDataBase.multiBlocks.containsKey(block))
				return;
			List<MultiblockDataBase> list = MultiblockDataBase.multiBlocks.get(block);
			for(MultiblockDataBase multiblock : list)
				if(multiblock.tryCreate(event.world, event.x, event.y, event.z)) {
					event.setCanceled(true);
					return;
				}
		}
	}
}
