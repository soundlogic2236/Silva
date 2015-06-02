package soundlogic.silva.client.render.multiblock;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.CarnilotusTileData;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataCarnilotus;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class RenderMultiblockCarnilotus extends RenderMultiblock{

	IIcon toothIcon;
	
	@Override
	protected void renderCoreTick(TileMultiblockCore tile, double d0, double d1,
			double d2, float ticks) {
		MultiblockDataCarnilotus multiblock = (MultiblockDataCarnilotus) tile.getData();
		CarnilotusTileData data = (CarnilotusTileData) tile.getTileData();
		Tessellator tessellator = Tessellator.instance;
		renderTeeth(tile,d0,d1,d2,ClientTickHandler.ticksInGame + ticks);
		if(data.activated) {
			for(int i = 0; i < 3 ; i++) {
				for( int j = 0 ; j < 3 ; j++) {
					int[] coords = multiblock.getTransformedCoords(tile, 2, i+1, j+1);
					GL11.glPushMatrix();
					GL11.glTranslated(d0, d1, d2);
					GL11.glTranslatef(-tile.xCoord,-tile.yCoord,-tile.zCoord);
					GL11.glTranslatef(coords[0],coords[1],coords[2]);
					GL11.glTranslatef(0,15F/16F,0);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1F, 1F, 1F, 1F);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glEnable(GL11.GL_CULL_FACE);
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
					GL11.glRotatef(90F, 1F, 0F, 0F);
					renderIcon(0, 0, multiblock.iconAcid, 1, 1, 240);
					GL11.glDisable(GL11.GL_CULL_FACE);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1F, 1F, 1F, 1F);
					GL11.glPopMatrix();
				}
			}
		}
	}

	private void renderTeeth(TileMultiblockCore tile, double d0, double d1,
			double d2, float time) {
		MultiblockDataCarnilotus multiblock = (MultiblockDataCarnilotus) tile.getData();
		toothIcon = multiblock.iconTeethLip;
		float fraction = (time / 40F) % 1F;
		CarnilotusTileData data = (CarnilotusTileData) tile.getTileData();
		if(!data.activated) {
			return;
		}
		float baseAngle = tile.rotation*90;
		for(int i = 0; i < 5 ; i++) {
			for( int j = 0 ; j < 5 ; j++) {
				if(i != 1 || j != 1) {
					int[] coords = multiblock.getTransformedCoords(tile, 4, i, j);
					float angle = 0;
					int end = 0;
					switch(i*5+j) {
					case 1:angle=90-baseAngle;end=-1;break;
					case 2:angle=90-baseAngle;break;
					case 3:angle=90-baseAngle;end=1;break;
					case 5:angle=180-baseAngle;end=-1;break;
					case 10:angle=180-baseAngle;break;
					case 15:angle=180-baseAngle;end=1;break;
					case 21:angle=270-baseAngle;end=-1;break;
					case 22:angle=270-baseAngle;break;
					case 23:angle=270-baseAngle;end=1;break;
					default: continue;
					}
					angle = (angle+360) % 360;
					float x = coords[0]+.5F;
					float y = coords[1];
					float z = coords[2]+.5F;
					int dx = angle == 90 ? 1 : angle == 270 ? -1 : 0;
					int dz = angle == 0 ? -1 : angle == 180 ? 1 : 0;

					if(tile.rotation==0)
						end = end * (+dx + +dz);
					if(tile.rotation==1)
						end = end * (-dx + +dz);
					if(tile.rotation==2)
						end = end * (-dx + -dz);
					if(tile.rotation==3)
						end = end * (+dx + -dz);
					
					end = end == 0 ? -1 : end == 1 ? 0 : 3;
					
					x+=dx*.85F;
					y-=.2F;
					z+=dz*.85F;
					
					for(int k = 0 ; k < 4 ; k++) { 
						if(k==end)
							continue;
						double desync_val = desync(coords[0],coords[1],coords[2],k);
						float pitch = angle + 90 + 180 * dz * dz + (float)Math.sin(desync_val*desync_val)*5F;
						float roll = (float) (Math.sin(fraction*Math.PI*2+desync_val)*10);
						float rx = (float) (x + dz*.25 - dz * .25 * (-.5+k));
						float ry = y;
						float rz = (float) (z + dx*.25 - dx * .25 * (-.5+k));
						renderTooth(tile,d0,d1,d2,rx,ry,rz,0,pitch,roll,.75F);
					}
				}
			}
		}
		
	}
	
	private double desync(int x, int y, int z, int k) {
		return (""+x+y+z+k).hashCode();
	}
	
	private void renderTooth(TileMultiblockCore tile, double d0, double d1, double d2, float x, float y, float z, float pitch, float yaw, float roll, float scale) {
		float[] rotationOrigin = new float[]{15F/16F, 1F/16F};
		renderTooth(tile,d0,d1,d2,x,y,z,pitch,yaw,roll,scale,rotationOrigin);
	}
	private void renderTooth(TileMultiblockCore tile, double d0, double d1, double d2, float x, float y, float z, float pitch, float yaw, float roll, float scale, float[] rotationOrigin) {
		GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslatef(-tile.xCoord,-tile.yCoord,-tile.zCoord);
		GL11.glTranslatef(x,y,z);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glScalef(scale,scale,scale);
		GL11.glRotatef(yaw, 0F, 1F, 0F);
        GL11.glTranslatef(rotationOrigin[0], rotationOrigin[1], 0F);
		GL11.glRotatef(pitch, 1F, 0F, 0F);
		GL11.glRotatef(roll, 0F, 0F, 1F);
        GL11.glTranslatef(-rotationOrigin[0], -rotationOrigin[1], 0F);
		renderIconWithDepth(toothIcon);
		GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
	
	private void renderIconWithDepth(IIcon iicon) {
		ItemRenderer.renderItemIn2D(Tessellator.instance, iicon.getMaxU(), iicon.getMinV(), iicon.getMinU(), iicon.getMaxV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
	}
	
	public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(brightness);
		tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
		tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
		tessellator.draw();
	}
}
