package soundlogic.silva.client.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.render.block.RenderBoomMoss;
import soundlogic.silva.client.render.block.RenderDarkenedDust;
import soundlogic.silva.client.render.block.RenderManaEater;
import soundlogic.silva.client.render.block.RenderPixieDust;
import soundlogic.silva.client.render.block.RenderPylon;
import soundlogic.silva.client.render.tile.RenderTileBoomMoss;
import soundlogic.silva.client.render.tile.RenderTileDwarvenSign;
import soundlogic.silva.client.render.tile.RenderTileManaEater;
import soundlogic.silva.client.render.tile.RenderTilePortalCore;
import soundlogic.silva.client.render.tile.RenderTilePylon;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.core.proxy.CommonProxy;

public class ClientProxy extends CommonProxy{

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		FMLCommonHandler.instance().bus().register(new ClientTickHandler());

	}

    @Override
    public void registerRenderers() {
    	
    	LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idManaEater = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idBoomMoss = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idPixieDust = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDarkenedDust = RenderingRegistry.getNextAvailableRenderId();
    	
    	RenderingRegistry.registerBlockHandler(new RenderPylon());
    	RenderingRegistry.registerBlockHandler(new RenderManaEater());
    	RenderingRegistry.registerBlockHandler(new RenderBoomMoss());
    	RenderingRegistry.registerBlockHandler(new RenderPixieDust());
    	RenderingRegistry.registerBlockHandler(new RenderDarkenedDust());
    	
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, new RenderTilePylon());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileManaEater.class, new RenderTileManaEater());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileBoomMoss.class, new RenderTileBoomMoss());
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePortalCore.class, new RenderTilePortalCore());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDwarvenSign.class, new RenderTileDwarvenSign());
    }
        
}
