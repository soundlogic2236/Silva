package soundlogic.silva.common.core.handler;

import java.util.List;

import net.minecraft.entity.item.EntityEnderPearl;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnderPearlPortalHandler {

	@SubscribeEvent
	public void onPortalTick(ElvenPortalUpdateEvent event) {
		if(!event.open)
			return;
		List<EntityEnderPearl> pearls=event.portalTile.getWorldObj().getEntitiesWithinAABB(EntityEnderPearl.class, event.aabb);
		if(pearls.size()!=0)
			System.out.println("pnk!");
	}
}
