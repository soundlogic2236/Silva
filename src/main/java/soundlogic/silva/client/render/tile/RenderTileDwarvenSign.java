package soundlogic.silva.client.render.tile;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import scala.util.Random;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockDwarvenSign;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.crafting.ModPortalTradeRecipes;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSigned;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.oredict.OreDictionary;

public class RenderTileDwarvenSign extends TileEntitySpecialRenderer{

//    private final ModelSign model = new ModelSign();

    float currentLookX;
    float currentLookY;
    boolean doTooltip;
    public static List<String> tooltipData;
    public static boolean hasTooltip;
    public static boolean heldItemValid;
    public static boolean tooltipInput;
    
    RenderItem render;
    
    public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float ticks) {
    	if(render==null)
    		render = new RenderItem();

    	TileDwarvenSign sign=(TileDwarvenSign)tile;
    	
    	updateLookLocation(sign);

    	Random random=new Random();
    	
    	GL11.glPushMatrix();
        float angle;

        int j = sign.getBlockMetadata() & 3;

        angle = 0.0F;

        if (j == 1)
        {
            angle = 180.0F;
        }

        if (j == 2)
        {
            angle = -90.0F;
        }

        if (j == 3)
        {
            angle = 90.0F;
        }

        GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.5F);
        GL11.glRotatef(-angle+180, 0.0F, 1.0F, 0.0F);
        FontRenderer fontrenderer = this.func_147498_b();
        GL11.glScalef(1,-1, 1);
        GL11.glTranslatef(-.5F, -.5F, -7F/16F-1F/256F);
        GL11.glScalef(1F/16F, 1F/16F, 1F/16F);
        GL11.glScalef(1F/16F,1F/16F,1F/16F);
        GL11.glNormal3f(0, 0, -1);
        GL11.glDepthMask(false);
        byte b0 = 0;

        GL11.glPushMatrix();
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
		DwarfTradeSigned trade=sign.getRecipe();
		if(trade==null)
			return;
		renderArrows(sign, d0, d1, d2, ticks);
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
			renderStack(output.get(i),false,i, output.size(), trade, sign);
		for(int i=0;i<inputStacks.size();i++)
			renderStack(inputStacks.get(i),true, i, inputStacks.size(), trade, sign);
	}
	
	private void renderArrows(TileDwarvenSign sign, double d0, double d1, double d2, float ticks) {
		GL11.glPushMatrix();
		GL11.glScalef(16, 16, 16);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    	render.zLevel=10;
		render.renderIcon(0, 0, BlockDwarvenSign.arrowOff, 16, 16);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	private void renderStack(ItemStack stack, boolean input, int slot, int slotCount, DwarfTradeSigned trade, TileDwarvenSign sign) {
		
		if(stack==null || stack.getItem()==null)
			return;
		
    	float[] data = renderDataForSlot(stack, input, slot, slotCount, trade, sign);
		renderStackAtCoords(stack, input, slot, trade, sign, data[0], data[1], data[2]);
		
	}
	
	private float[] renderDataForSlot(ItemStack stack, boolean input, int slot, int slotCount, DwarfTradeSigned trade, TileDwarvenSign sign) {
		float x, y, size;

		size = 2F;

		x = 3.25F;
		if(!input)
			x += 7.5F;
		
		switch(slotCount) {
		case 1:
			y = 7F;
			break;
		case 2:
			y = 5.75F + 2.5F * slot;
			break;
		case 3:
			y = 4.5F + 2.5F * slot;
			break;
		case 4:
			y = 3.25F + 2.5F * slot;
			break;
		default:
			y = 0;
			break;
		}
		
		return new float[]{x,y,size};
	}
	
	private void renderStackAtCoords(ItemStack stack, boolean input, int slot, DwarfTradeSigned trade, TileDwarvenSign sign, float x, float y, float size) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x*16, y*16, 0);
		GL11.glScalef(size, size, size);
    	render.zLevel=0F;
		render.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, 0, 0);
		render.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, 0, 0);
		GL11.glPopMatrix();
		
		if(doTooltip)
			renderStackTooltip(stack, input, slot, trade, sign, x, y, size);
		
	}
	
	private void updateLookLocation(TileDwarvenSign sign) {
		MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
		
		doTooltip=false;
		
		if(pos.blockX!=sign.xCoord || pos.blockY!=sign.yCoord || pos.blockZ!=sign.zCoord)
			return;
		if(pos.sideHit!=((sign.getBlockMetadata() & 3)+2))
			return;
		Vec3 hit = pos.hitVec;
		double subX = 0;
		double subY = 1 - (hit.yCoord - sign.yCoord);
		switch(sign.getBlockMetadata() & 3) {
		case 0: subX = 1 - (hit.xCoord - sign.xCoord); break;
		case 1: subX = hit.xCoord - sign.xCoord; break;
		case 2: subX = hit.zCoord - sign.zCoord; break;
		case 3: subX = 1 - (hit.zCoord - sign.zCoord); break;
		}
		currentLookX = (float) (subX * 16);
		currentLookY = (float) (subY * 16);
		doTooltip=true;
		hasTooltip=false;
	}

	private void renderStackTooltip(ItemStack stack, boolean input, int slot,
			DwarfTradeSigned trade, TileDwarvenSign sign, float x, float y, float width) {
		if(currentLookX>=x && currentLookX <= x+width && currentLookY>=y && currentLookY<=y+width) {
			tooltipData =stack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
			hasTooltip=true;
			tooltipInput = input;
			if(input) {
				ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
				if(heldItem==null)
					heldItemValid=false;
				else
					heldItemValid=trade.doesStackMatchSlotForDisplay(heldItem, slot);
			}
		}
	}
}
