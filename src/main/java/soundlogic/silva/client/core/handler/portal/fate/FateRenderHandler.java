package soundlogic.silva.client.core.handler.portal.fate;

import java.util.List;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import soundlogic.silva.common.core.handler.portal.fate.IVigridrFate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FateRenderHandler {

	@SubscribeEvent
	public void renderLiving(RenderLivingEvent.Pre event) {
		EntityLivingBase entity = event.entity;
		IVigridrFate fate = FateHandler.getFate(entity);
		if(fate!=null) {
			if(fate.renderEntity(event.entity, event.renderer, event.x, event.y, event.z))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Pre event) {
		EntityLivingBase entity = event.entityLiving;
		IVigridrFate fate = FateHandler.getFate(entity);
		if(fate!=null) {
			if(fate.renderPlayer(event.entity, event.renderer, event.partialRenderTick))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void renderWorld(RenderWorldEvent.Pre event) {
		List<IVigridrFate> fates = FateHandler.getAllActiveFates();
		for(IVigridrFate fate : fates) {
			fate.renderWorld(event.renderer, event.renderBlocks, event.chunkCache, event.pass);
		}
		IVigridrFate playerFate = FateHandler.getFate(Minecraft.getMinecraft().thePlayer);
		if(playerFate!=null) {
			playerFate.renderWorldForPlayer(event.renderer, event.renderBlocks, event.chunkCache, event.pass);
		}
	}
	
}
