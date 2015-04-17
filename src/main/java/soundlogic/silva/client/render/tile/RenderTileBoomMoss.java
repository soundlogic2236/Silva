package soundlogic.silva.client.render.tile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.model.ModelBoomMoss;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileManaEater;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelSpreader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileBoomMoss extends TileEntitySpecialRenderer{

	private static final ResourceLocation texture = new ResourceLocation(LibResources.PREFIX_MOD + LibResources.MODEL_BOOM_MOSS.replaceAll(LibResources.PREFIX_MOD, "textures/blocks/")+".png");

	private static final ModelBoomMoss model = new ModelBoomMoss();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks) {
		TileBoomMoss moss = (TileBoomMoss) tileentity;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		GL11.glTranslatef(0F, 1F, 1F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glScalef(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;
		
		int mana=moss.getCurrentMana();
		int max_mana=moss.getMaxMana();
		int mana_cost=moss.getCostMana();
		
		int effective_mana=(int)(Math.sin((time*mana)/(max_mana*2))*mana);
		
		boolean canSpread=effective_mana>=mana_cost;
		int pastSpread=canSpread? effective_mana-mana_cost : 0;
		int preSpread=Math.min(mana_cost, mana_cost);
		float pastSpreadFraction=((float)pastSpread)/(max_mana-mana_cost);
		float preSpreadFraction=((float)preSpread)/(mana_cost);
		
		boolean halfplus=effective_mana>=(max_mana/2);
		int pastHalf=halfplus? effective_mana-(max_mana/2) : 0;
		float pastHalfFraction=((float)pastHalf)/(max_mana/2);

		float red=.6F+preSpreadFraction*.4F;
		float green=.6F-pastSpreadFraction*.6F+pastSpreadFraction;
		float blue=pastHalfFraction;
		GL11.glColor3f(red,green,blue);

		for(int i=0;i<6;i++) {
			if(moss.canAttachToSide(model.getDirectionSide(i))) {
				model.renderSide(i);
			}
		}
		GL11.glPopMatrix();
	}
}
