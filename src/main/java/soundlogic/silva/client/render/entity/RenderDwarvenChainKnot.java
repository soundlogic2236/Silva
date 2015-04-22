package soundlogic.silva.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.ResourceLocation;

public class RenderDwarvenChainKnot extends Render{

    private static final ResourceLocation chainKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		// TODO Auto-generated method stub
		return chainKnotTextures;
	}
}
