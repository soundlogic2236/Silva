package soundlogic.silva.client.core.handler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.common.core.handler.DwarvenChainHandler;
import soundlogic.silva.common.core.handler.DwarvenChainHandler.LeashProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DwarfChainRenderHandler {

	private static final int[] COLOR1 = new int[]{154,154,154};
	private static final int[] COLOR2 = new int[]{ 86, 86, 86};
	private static final double WIDTH = 0.025D;
	private static final int SEGMENTS = 24;
	
	@SubscribeEvent
	public void renderLiving(RenderLivingEvent.Pre event) {
		if ( event.entity instanceof EntityCreature) {
			LeashProperties props = DwarvenChainHandler.getChainForEntity((EntityCreature) event.entity);
			props.searchTick(event.entity, true);
			if(props==null || props.getEntity() == null || !props.getActive())
				return;
			renderChain((EntityLiving) event.entity, props.getEntity(), event.x, event.y, event.z, ClientTickHandler.partialTicks, WIDTH, SEGMENTS, COLOR1, COLOR2);
		}
	}

    public void renderChain(EntityLiving entity, Entity attachedTo, double x, double y, double z, float partialTicks, double width, int segments, int[] color1, int[] color2)
    {
        if (attachedTo != null)
        {
            double[] end = getChainEnd(attachedTo, partialTicks);
            renderChain(entity, x, y, z, partialTicks, end[0], end[1], end[2], width, segments, color1, color2);
        }
    }

    public void renderChain(EntityLiving entity, double x, double y, double z, float partialTicks, double endX, double endY, double endZ, double width, int segments, int[] color1, int[] color2)
    {
        double[] start = getChainStart(entity, x, y, z, partialTicks);
        renderChain(entity, x, y, z, partialTicks, start[0], start[1], start[2], endX, endY, endZ, width, segments, color1, color2);
    }
    public void renderChain(EntityLiving entity, double x, double y, double z, float partialTicks, double startX, double startY, double startZ, double endX, double endY, double endZ, double width, int segments, int[] color1, int[] color2)
    {
        double[] offset = getChainOffset(entity, x, y, z, partialTicks);

        renderChain(
        		startX+offset[0], 
        		startY+offset[1], 
        		startZ+offset[2], 
        		endX+offset[0], 
        		endY+offset[1], 
        		endZ+offset[2],
        		width,
        		segments,
        		color1,
        		color2);
    }
    
    public void renderChain(double x1, double y1, double z1, double x2, double y2, double z2, double width, int segments, int[] color1, int[] color2) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        tessellator.startDrawing(5);
    	
        double dx = x2-x1;
        double dy = y2-y1;
        double dz = z2-z1;
        
        int i;
        float portion;

        for (i = 0; i <= segments; ++i)
        {
            if (i % 2 == 0)
            {
                tessellator.setColorOpaque(color1[0], color1[1], color1[2]);
            }
            else
            {
                tessellator.setColorOpaque(color2[0], color2[1], color2[2]);
            }

            double[] coords;

            coords = getVertexCoords(x1,y1,z1,dx,dy,dz,i,segments);

            tessellator.addVertex(
            		coords[0],
            		coords[1],
            		coords[2]);

            coords = getVertexCoords(x1,y1,z1,dx,dy,dz,i,segments);

            tessellator.addVertex(
            		coords[0]+width,
            		coords[1]+width,
            		coords[2]);
        }

        tessellator.draw();
        tessellator.startDrawing(5);

        for (i = 0; i <= segments; ++i)
        {
            if (i % 2 == 0)
            {
                tessellator.setColorOpaque(color1[0], color1[1], color1[2]);
            }
            else
            {
                tessellator.setColorOpaque(color2[0], color2[1], color2[2]);
            }

            double[] coords;
            
            coords = getVertexCoords(x1,y1,z1,dx,dy,dz,i,24);
            
            tessellator.addVertex(
            		coords[0],
            		coords[1]+width,
            		coords[2]);
            
            coords = getVertexCoords(x1,y1,z1,dx,dy,dz,i,24);

            tessellator.addVertex(
            		coords[0]+width,
            		coords[1],
            		coords[2]+width);
        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    protected double[] getVertexCoords(double x, double y, double z, double dx, double dy, double dz, int segment, int segments) {
    	double nx;
    	double ny;
    	double nz;
    	
    	float portion = (float)segment/ (float)segments;
    	
		nx = x + dx * (double)portion;

		ny = y + dy * (double)(portion * portion + portion) * 0.5D + (double)(((float)segments - (float)segment) / 18.0F + 0.125F);

		nz = z + dz * (double)portion;

    	return new double[]{nx,ny,nz};
    }

    protected double[] getChainStart(EntityLiving entity, double x, double y, double z, float partialTicks)
    {
        double entityX = this.interpolate(entity.prevPosX, entity.posX, (double)partialTicks);
        double entityY = this.interpolate(entity.prevPosY, entity.posY, (double)partialTicks);
        double entityZ = this.interpolate(entity.prevPosZ, entity.posZ, (double)partialTicks);

        entityY -= (1.6D - (double)entity.height) * 0.5D;
        double entityYawOffset = this.interpolate((double)entity.prevRenderYawOffset, (double)entity.renderYawOffset, (double)partialTicks) * 0.01745329238474369D + (Math.PI / 2D);
        double d5 = Math.cos(entityYawOffset) * (double)entity.width * 0.4D;
        double d6 = Math.sin(entityYawOffset) * (double)entity.width * 0.4D;
        entityX += d5;
        entityZ += d6;
        return new double[]{
        		entityX,
        		entityY,
        		entityZ};
    }
    
    protected double[] getChainEnd(Entity attachedTo, float partialTicks)
    {
        double attachedToYaw = this.interpolate((double)attachedTo.prevRotationYaw, (double)attachedTo.rotationYaw, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
        double attachedToPitch = this.interpolate((double)attachedTo.prevRotationPitch, (double)attachedTo.rotationPitch, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
        double d5 = Math.cos(attachedToYaw);
        double d6 = Math.sin(attachedToYaw);
        double d7 = Math.sin(attachedToPitch);

        if (attachedTo instanceof EntityHanging)
        {
            d5 = 0.0D;
            d6 = 0.0D;
            d7 = -1.0D;
        }

        double d8 = Math.cos(attachedToPitch);
        double attachedToX = this.interpolate(attachedTo.prevPosX, attachedTo.posX, (double)partialTicks) - d5 * 0.7D - d6 * 0.5D * d8;
        double attachedToY = this.interpolate(attachedTo.prevPosY + (double)attachedTo.getEyeHeight() * 0.7D, attachedTo.posY + (double)attachedTo.getEyeHeight() * 0.7D, (double)partialTicks) - d7 * 0.5D - 0.25D;
        double attachedToZ = this.interpolate(attachedTo.prevPosZ, attachedTo.posZ, (double)partialTicks) - d6 * 0.7D + d5 * 0.5D * d8;
        
        return new double[]{
        		attachedToX,
        		attachedToY,
        		attachedToZ};
    }
    
    protected double[] getChainOffset(EntityLiving entity, double x, double y, double z, float partialTicks) {

    	y -= (1.6D - (double)entity.height) * 0.5D;
        double entityX = this.interpolate(entity.prevPosX, entity.posX, (double)partialTicks);
        double entityY = this.interpolate(entity.prevPosY, entity.posY, (double)partialTicks);
        double entityZ = this.interpolate(entity.prevPosZ, entity.posZ, (double)partialTicks);
        
        return new double[]{
        		x - entityX,
        		y - entityY,
        		z - entityZ};
    }
    
    private double interpolate(double d1, double d2, double partialTicks)
    {
        return d1 + (d2 - d1) * partialTicks;
    }
}
