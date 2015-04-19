package soundlogic.silva.client.render.tile;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import scala.util.Random;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.crafting.recipe.DwarfTrade;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class RenderTileDwarvenSign extends TileEntitySpecialRenderer{

	private static final ResourceLocation textureOff = new ResourceLocation(LibResources.MODEL_DWARVEN_SIGN_OFF);
	private static final ResourceLocation textureOn = new ResourceLocation(LibResources.MODEL_DWARVEN_SIGN_ON);
    private final ModelSign model = new ModelSign();

    public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float ticks) {
    	TileDwarvenSign sign=(TileDwarvenSign)tile;

    	Random random=new Random();
    	
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
        GL11.glTranslatef(0, 0, -4209/8192F);
        GL11.glTranslatef(-.5F+1F/32F, 1F/2F+1F/32F+1F/128F, 0);
        GL11.glScalef(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GL11.glDepthMask(false);
        byte b0 = 0;

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, .99f/(.02f));
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(1,1,.02f);
        renderRecipe(sign, d0, d1, d2, ticks);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

    }

	private void renderRecipe(TileDwarvenSign sign, double d0, double d1,
			double d2, float ticks) {
		DwarfTrade trade=sign.getRecipe();
		if(trade==null)
			return;
		List<Object> input=trade.getInputs();
		List<ItemStack> output=trade.getOutput();
		List<ItemStack> inputStacks=new ArrayList<ItemStack>();
		for(Object o : input) {
			if(o instanceof String)
				inputStacks.add(OreDictionary.getOres((String) o).get(0));
			else
				inputStacks.add((ItemStack)o);
		}
		for(int i=0;i<output.size();i++)
			renderStack(output.get(i),false,i);
		for(int i=0;i<inputStacks.size();i++)
			renderStack(inputStacks.get(i),true,i);
		ItemStack tempStack=new ItemStack(ModItems.stoneHorse);
	}
	
	private void renderStack(ItemStack stack, boolean input, int slot) {
		
		if(stack==null || stack.getItem()==null)
			return;
		
    	int x = (slot % 2 ) * 17 + ( input ? 0 : 52 );
		int y = 1 + ( (slot >= 2 ) ? 17 : 00 );
		
		RenderItem render = new RenderItem();
		render.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
		render.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
	}
}
