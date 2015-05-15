package soundlogic.silva.client.render.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.ResourceLocation;

public class RenderDwarvenChainKnot extends Render{

    private ModelLeashKnot leashKnotModel = new ModelLeashKnot();
    private static final ResourceLocation chainKnotTextures = new ResourceLocation(LibResources.CHAIN_KNOT);

	public void doRender(EntityDwarvenChainKnot p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        float f2 = 0.0625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.bindEntityTexture(p_76986_1_);
        this.leashKnotModel.render(p_76986_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
        GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(EntityDwarvenChainKnot p_110775_1_) {
		return chainKnotTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityDwarvenChainKnot)p_110775_1_);
    }
	@Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityDwarvenChainKnot)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
