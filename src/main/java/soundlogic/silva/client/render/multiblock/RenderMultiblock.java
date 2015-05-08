package soundlogic.silva.client.render.multiblock;

import java.util.HashMap;

import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;

public abstract class RenderMultiblock {

	static HashMap<String, RenderMultiblock> renderers = new HashMap<String, RenderMultiblock>();

	public static void registerRenderer(RenderMultiblock renderer, String multiblock) {
		renderers.put(multiblock, renderer);
	}
	
	public static void renderTick(TileMultiblockCore tile, double x, double y,
			double z, float ticks) {
		if(tile.getCore()==null || tile.getCore().getData()==null)
			return;
		String name = tile.getCore().getData().getName();
		RenderMultiblock renderer = renderers.get(name);
		if(renderer!=null)
			renderer.renderCoreTick(tile, x, y, z, ticks);
	}

	protected abstract void renderCoreTick(TileMultiblockCore tile, double x, double y,
			double z, float ticks);
	
	
}
