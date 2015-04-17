package soundlogic.silva.common.lexicon;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public enum PageBackground {
	DEFAULT (null),
	ELVEN (new ResourceLocation(vazkii.botania.client.lib.LibResources.GUI_PAPER)),
	DWARVEN (null);
	
	private final ResourceLocation background;
	PageBackground(ResourceLocation background) {
		this.background=background;
	}
	public ResourceLocation getLocation() {
		return background;
	}
	
	public void render(IGuiLexiconEntry gui) {
		if(background==null)
			return;
		Minecraft.getMinecraft().renderEngine.bindTexture(background);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
	}
}
