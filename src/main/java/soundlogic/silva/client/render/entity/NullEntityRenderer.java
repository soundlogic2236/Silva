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

public class NullEntityRenderer extends Render{

	@Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        // NO OP
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
}
