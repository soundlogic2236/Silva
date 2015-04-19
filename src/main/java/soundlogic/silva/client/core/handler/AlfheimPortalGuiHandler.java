package soundlogic.silva.client.core.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;

import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AlfheimPortalGuiHandler {
	static boolean mouseDownLastTick = false;

	@SubscribeEvent
	public void afterDrawn(DrawScreenEvent.Post event) {
		if(event.gui instanceof GuiLexiconEntry) {
			GuiLexiconEntry gui = (GuiLexiconEntry) event.gui;
			ItemStack stack=gui.stackUsed;
			ILexicon lex=(ILexicon) stack.getItem();
			if(lex.isKnowledgeUnlocked(stack,LexiconData.worldTreeKnowledge)) {
				LexiconPage page=gui.getEntry().pages.get(gui.getPageOn());
				if(page instanceof PageElvenRecipe) {
					int mx=event.mouseX;
					int my=event.mouseY;
					if(mx >= gui.getLeft() + 22 && mx <= gui.getLeft() + 22 + 48) {
						if(my >= gui.getTop() + 36 && my <= gui.getTop() + 36 + 48) {
							List<String> tooltip=new ArrayList<String>();
							tooltip.add(StatCollector.translateToLocal(Dimension.ALFHEIM.getUnlocalizedName()));
							vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, tooltip);

							EntryData data=DimensionHandler.getSignatureEntryForDimension(Dimension.ALFHEIM);
							if(data!=null) {
								vazkii.botania.client.core.helper.RenderHelper.renderTooltipOrange(mx, my + 19, Arrays.asList(EnumChatFormatting.GRAY + StatCollector.translateToLocal("silva.tooltips.clickToSignature")));
								boolean mouseDown = Mouse.isButtonDown(0);
								if(!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown()) {
									GuiLexiconEntry newGui = new GuiLexiconEntry(data.entry, (GuiScreen) gui);
									newGui.page = data.page;
									Minecraft.getMinecraft().displayGuiScreen(newGui);
								}
							}
						}
					}
				}
			}
		}
		mouseDownLastTick = Mouse.isButtonDown(0);
	}
}
