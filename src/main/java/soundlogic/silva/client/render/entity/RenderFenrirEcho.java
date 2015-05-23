package soundlogic.silva.client.render.entity;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.common.entity.EntityFenrirEcho;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderFenrirEcho extends RenderLiving {

    private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public RenderFenrirEcho(ModelBase p_i1269_1_, ModelBase p_i1269_2_, float p_i1269_3_)
    {
        super(p_i1269_1_, p_i1269_3_);
        this.setRenderPassModel(p_i1269_2_);
    }

    public void doRender(EntityFenrirEcho p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        BossStatus.setBossStatus(p_76986_1_, false);
        super.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        GL11.glScalef(EntityFenrirEcho.SCALE_FACTOR, EntityFenrirEcho.SCALE_FACTOR, EntityFenrirEcho.SCALE_FACTOR);
    }

    protected float handleRotationFloat(EntityFenrirEcho p_77044_1_, float p_77044_2_)
    {
        return 0;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return anrgyWolfTextures;
    }
}
