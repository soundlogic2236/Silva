package soundlogic.silva.client.render.entity;

import soundlogic.silva.common.entity.EntityStoneHorse;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderEntityStoneHorse extends RenderHorse{

	private ResourceLocation texture;
	
    public RenderEntityStoneHorse() {
		super(new ModelHorse(), 0.75F);
	}

    protected void renderModel(EntityStoneHorse p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
    	super.renderModel(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
    }

    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
        this.renderModel((EntityStoneHorse)p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
    }

	protected ResourceLocation getEntityTexture(EntityHorse horse)
    {
		if(texture==null)
			texture=new ResourceLocation(((EntityStoneHorse)horse).getHorseTexture());
    	return texture;
    }
}
