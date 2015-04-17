package soundlogic.silva.client.render.tile;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class RenderTileDwarvenSign extends TileEntitySpecialRenderer{

	private static final ResourceLocation textureOff = new ResourceLocation(LibResources.MODEL_DWARVEN_SIGN_OFF);
	private static final ResourceLocation textureOn = new ResourceLocation(LibResources.MODEL_DWARVEN_SIGN_ON);
    private final ModelSign model = new ModelSign();

    public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float ticks) {
    	TileDwarvenSign sign=(TileDwarvenSign)tile;

    	GL11.glPushMatrix();
        float f1 = 0.6666667F;
        float f3;

        int j = sign.getBlockMetadata();
        f3 = 0.0F;

        if (j == 2)
        {
            f3 = 180.0F;
        }

        if (j == 4)
        {
            f3 = 90.0F;
        }

        if (j == 5)
        {
            f3 = -90.0F;
        }

        GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 0.75F * f1, (float)d2 + 0.5F);
        GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
        this.model.signStick.showModel = false;

        this.bindTexture(sign.isActivated() ? textureOn : textureOff);
        GL11.glPushMatrix();
        GL11.glScalef(f1, -f1, -f1);
        this.model.renderSign();
        GL11.glPopMatrix();
        FontRenderer fontrenderer = this.func_147498_b();
        f3 = 0.016666668F * f1;
        GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
        GL11.glScalef(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GL11.glDepthMask(false);
        byte b0 = 0;

        renderRecipe(sign, d0, d1, d2, ticks);

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

    }

	private void renderRecipe(TileDwarvenSign sign, double d0, double d1,
			double d2, float ticks) {
		// TODO Auto-generated method stub
		
	}
}
