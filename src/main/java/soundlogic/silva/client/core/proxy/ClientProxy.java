package soundlogic.silva.client.core.proxy;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import soundlogic.silva.client.core.handler.AlfheimPortalGuiHandler;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.core.handler.DwarfChainRenderHandler;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.render.block.RenderBoomMoss;
import soundlogic.silva.client.render.block.RenderDarkenedDust;
import soundlogic.silva.client.render.block.RenderDwarvenPool;
import soundlogic.silva.client.render.block.RenderManaCrystal;
import soundlogic.silva.client.render.block.RenderManaEater;
import soundlogic.silva.client.render.block.RenderPixieDust;
import soundlogic.silva.client.render.block.RenderPylon;
import soundlogic.silva.client.render.entity.RenderDwarvenChainKnot;
import soundlogic.silva.client.render.entity.RenderEntityStoneHorse;
import soundlogic.silva.client.render.tile.RenderTileBoomMoss;
import soundlogic.silva.client.render.tile.RenderTileDwarvenPool;
import soundlogic.silva.client.render.tile.RenderTileDwarvenSign;
import soundlogic.silva.client.render.tile.RenderTileManaCrystal;
import soundlogic.silva.client.render.tile.RenderTileManaEater;
import soundlogic.silva.client.render.tile.RenderTilePortalCore;
import soundlogic.silva.client.render.tile.RenderTilePylon;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.core.proxy.CommonProxy;
import soundlogic.silva.common.entity.EntityDwarvenBarrier;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import soundlogic.silva.common.entity.EntityStoneHorse;
import soundlogic.silva.common.item.ModItems;

public class ClientProxy extends CommonProxy{

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new DwarfChainRenderHandler());
		MinecraftForge.EVENT_BUS.register(new AlfheimPortalGuiHandler());

	}

    @Override
    public void registerRenderers() {
    	
    	LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idManaEater = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idBoomMoss = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idPixieDust = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDarkenedDust = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idManaCrystal = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDwarvenPool = RenderingRegistry.getNextAvailableRenderId();
    	
    	RenderingRegistry.registerBlockHandler(new RenderPylon());
    	RenderingRegistry.registerBlockHandler(new RenderManaEater());
    	RenderingRegistry.registerBlockHandler(new RenderBoomMoss());
    	RenderingRegistry.registerBlockHandler(new RenderPixieDust());
    	RenderingRegistry.registerBlockHandler(new RenderDarkenedDust());
    	RenderingRegistry.registerBlockHandler(new RenderManaCrystal());
    	RenderingRegistry.registerBlockHandler(new RenderDwarvenPool());
    	
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, new RenderTilePylon());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileManaEater.class, new RenderTileManaEater());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileBoomMoss.class, new RenderTileBoomMoss());
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePortalCore.class, new RenderTilePortalCore());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDwarvenSign.class, new RenderTileDwarvenSign());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileManaCrystal.class, new RenderTileManaCrystal());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDwarvenPool.class, new RenderTileDwarvenPool());
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityStoneHorse.class, new RenderEntityStoneHorse());
    	RenderingRegistry.registerEntityRenderingHandler(EntityDwarvenBarrier.class, new RenderSnowball(ModItems.dwarfBarrier));
    	RenderingRegistry.registerEntityRenderingHandler(EntityDwarvenChainKnot.class, new RenderDwarvenChainKnot());
    }
        
    @Override
    public int getTicks() {
    	return ClientTickHandler.ticks;
    }
}
