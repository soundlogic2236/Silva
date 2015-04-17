package soundlogic.silva.common.lexicon.page;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.common.lexicon.page.PageText;

public class PageDimensionSignature extends LexiconPage{

	private static final ResourceLocation pageOverlay = new ResourceLocation(LibResources.GUI_SIGNATURE_OVERLAY);

	private static final ResourceLocation texNode = new ResourceLocation(LibResources.GUI_SIGNATURE_NODE);
	private static final ResourceLocation texPathUp = new ResourceLocation(LibResources.GUI_SIGNATURE_PATH_UP);
	private static final ResourceLocation texPathDown = new ResourceLocation(LibResources.GUI_SIGNATURE_PATH_DOWN);
	private static final ResourceLocation texPathLeft = new ResourceLocation(LibResources.GUI_SIGNATURE_PATH_LEFT);
	private static final ResourceLocation texPathRight = new ResourceLocation(LibResources.GUI_SIGNATURE_PATH_RIGHT);
	private static int nodeWidth=16;
	private static int nodeHeight=16;
	private static Random random = new Random();
	
	ArrayList<boolean[][]> signatures;
	Dimension dimension;
	PageBackground background;
	int ticks=0;
	boolean[][] currentSignature;
	boolean[][] currentSignatureConnectionUp;
	boolean[][] currentSignatureConnectionRight;
	boolean[][] currentSignatureConnectionDown;
	boolean[][] currentSignatureConnectionLeft;
	
	int signatureAt=0;
	
	
	public PageDimensionSignature(String unlocalizedName, Dimension dimension, ArrayList<boolean[][]> signatures, PageBackground background) {
		super(unlocalizedName);
		this.dimension=dimension;
		this.signatures=new ArrayList(signatures);
		this.background=background;
		loadSignature(0);
	}
	
	public void onPageAdded(LexiconEntry entry, int index) {
		super.onPageAdded(entry, index);
		DimensionHandler.setSignatureEntry(dimension,new EntryData(entry,index));
	}
	
	public PageDimensionSignature(String unlocalizedName, Dimension dimension, PageBackground background) {
		this(unlocalizedName, dimension, DimensionHandler.getSignaturesFromDimension(dimension), background);
	}

	@Override
	public void updateScreen() {
		ticks++;
		updateSignature();
	}
	
	private void updateSignature() {
		int prevSignatureAt=signatureAt;
		if(ticks % 80 == 0) {
			signatureAt++;
			
			if(signatureAt==signatures.size())
				signatureAt=0;
		}
		if(prevSignatureAt!=signatureAt)
			loadSignature(signatureAt);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(pageOverlay);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		
		int totalSignatureWidth=nodeWidth*DimensionHandler.SIGNATURE_WIDTH;
		int totalSignatureHeight=nodeHeight*DimensionHandler.SIGNATURE_HEIGHT;
		
		int center_x=gui.getLeft() + gui.getWidth() / 2;
		int center_y=gui.getTop() + gui.getHeight() / 2;

		int text_x = gui.getLeft() + 16;
		int text_y = gui.getTop() + 90;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0F, 0F, 0F, 1F);

		for(int row = 0;row<DimensionHandler.SIGNATURE_HEIGHT;row++) {
			for(int column = 0;column<DimensionHandler.SIGNATURE_WIDTH;column++) {
				int x=center_x - totalSignatureWidth/2 + nodeWidth*column;
				int y=center_y - totalSignatureHeight/2 + nodeHeight*row - 38;
				boolean node=currentSignature[row][column];
				boolean pathUp=currentSignatureConnectionUp[row][column];
				boolean pathDown=currentSignatureConnectionDown[row][column];
				boolean pathLeft=currentSignatureConnectionLeft[row][column];
				boolean pathRight=currentSignatureConnectionRight[row][column];
				if(node) {
					render.bindTexture(texNode);
					drawTexturedModalRect(x, y, 16, 16, nodeWidth, nodeHeight);
				}
				if(pathUp) {
					render.bindTexture(texPathUp);
					drawTexturedModalRect(x, y, 0, 0, nodeWidth, nodeHeight);
				}
				if(pathDown) {
					render.bindTexture(texPathDown);
					drawTexturedModalRect(x, y, 0, 0, nodeWidth, nodeHeight);
				}
				if(pathLeft) {
					render.bindTexture(texPathLeft);
					drawTexturedModalRect(x, y, 0, 0, nodeWidth, nodeHeight);
				}
				if(pathRight) {
					render.bindTexture(texPathRight);
					drawTexturedModalRect(x, y, 0, 0, nodeWidth, nodeHeight);
				}
			}
		}
		GL11.glDisable(GL11.GL_BLEND);

		PageText.renderText(text_x, text_y, gui.getWidth() - 30, gui.getHeight(), dimension.getUnlocalizedName());
		
	}

	private void loadSignature(int signature) {
		loadSignature(signatures.get(signature));
	}
	
	private void loadSignature(boolean[][] signature) {
		currentSignature=signature;
		int[][] connectedSignature=DimensionHandler.signatureToConnections(signature);
		currentSignatureConnectionUp=DimensionHandler.connectedSignatureToUpSide(connectedSignature);
		currentSignatureConnectionDown=DimensionHandler.connectedSignatureToDownSide(connectedSignature);
		currentSignatureConnectionLeft=DimensionHandler.connectedSignatureToLeftSide(connectedSignature);
		currentSignatureConnectionRight=DimensionHandler.connectedSignatureToRightSide(connectedSignature);
	}

    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        float f = 1F/16F;
        float f1 = 1F/16F;
        float zLevel=0;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + p_73729_6_), (double)zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + p_73729_6_), (double)zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + 0), (double)zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.draw();
    }

}
