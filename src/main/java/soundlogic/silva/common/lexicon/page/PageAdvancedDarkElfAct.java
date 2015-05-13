package soundlogic.silva.common.lexicon.page;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.BlockPortalCore;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.DarkElfActSimple;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.block.BlockAlfPortal;
import vazkii.botania.common.lexicon.page.PageRecipe;

public class PageAdvancedDarkElfAct extends PageRecipe{

	PageBackground background;

	private static final ResourceLocation tradeOverlay = new ResourceLocation(LibResources.GUI_PORTAL_TRADE_OVERLAY);
	private static final ResourceLocation darkElfOverlay = new ResourceLocation(LibResources.GUI_DARK_ELF_OVERLAY);

	static boolean mouseDownLastTick = false;

	List<IDarkElfAct> recipes;
	
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PageAdvancedDarkElfAct(String unlocalizedName,List<IDarkElfAct> recipes, PageBackground background) {
		super(unlocalizedName);
		this.recipes=recipes;
		this.background=background;
	}
	public PageAdvancedDarkElfAct(String unlocalizedName,List<ItemStack> outputs, List<ItemStack> inputs, PageBackground background) {
		this(unlocalizedName,(List<IDarkElfAct>)null,background);
		this.recipes=new ArrayList<IDarkElfAct>();
		for(int i = 0 ; i< outputs.size() ; i++) {
			recipes.add(new DarkElfActSimple(outputs.get(i), inputs.get(i)));
		}
	}

	public PageAdvancedDarkElfAct(String unlocalizedName, ItemStack output, ItemStack input, PageBackground background) {
		this(unlocalizedName, Arrays.asList(output), Arrays.asList(input),background);
	}

	public PageAdvancedDarkElfAct(String unlocalizedName, ItemStack[] outputs, ItemStack[] inputs, PageBackground background) {
		this(unlocalizedName, Arrays.asList(outputs), Arrays.asList(inputs),background);
	}
	
	public PageAdvancedDarkElfAct(String unlocalizedName,
			IDarkElfAct act, PageBackground background){
		this(unlocalizedName, Arrays.asList(act), background);
	}
	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(IDarkElfAct recipe : recipes) {
			List<ItemStack> output=recipe.getDisplayOutputs();
			for(ItemStack outputSingle : output)
				LexiconRecipeMappings.map(outputSingle, entry, index);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		boolean mouseDown = Mouse.isButtonDown(0);
		
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(tradeOverlay);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
		IDarkElfAct recipe = recipes.get(recipeAt);
		
		List<ItemStack> output=recipe.getDisplayOutputs();
		for(int i = 0;i<output.size();i++)
			renderItemAtGridPos(gui, 3+i, 1, output.get(i), false);

		List<ItemStack> inputs = recipe.getDisplayInputs();
		int i = 0;
		for(ItemStack stack : inputs) {
			renderItemAtInputPos(gui, i, stack);
			i++;
		}

		IIcon portalTex=((BlockPortalCore)ModBlocks.portalCore).portalTex;
		Color color=Dimension.SVARTALFHEIM.getPortalColor();
		float red=(float)color.getRed()/255F;
		float green=(float)color.getGreen()/255F;
		float blue=(float)color.getBlue()/255F;
		IIcon portalIcon = ((BlockPortalCore)ModBlocks.portalCore).portalTex;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		GL11.glColor4f(red, green, blue, 1F);
		RenderItem.getInstance().renderIcon(gui.getLeft() + 22, gui.getTop() + 36, portalIcon, 48, 48);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		render.bindTexture(darkElfOverlay);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
		
		if(mx >= gui.getLeft() + 22 && mx <= gui.getLeft() + 22 + 48) 
			if(my >= gui.getTop() + 36 && my <= gui.getTop() + 36 + 48) {
				List<String> tooltip=new ArrayList<String>();
				tooltip.add(StatCollector.translateToLocal(Dimension.SVARTALFHEIM.getUnlocalizedName()));
				vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, tooltip);

				EntryData data=DimensionHandler.getSignatureEntryForDimension(Dimension.SVARTALFHEIM);
				if(data!=null) {
					vazkii.botania.client.core.helper.RenderHelper.renderTooltipOrange(mx, my + 19, Arrays.asList(EnumChatFormatting.GRAY + StatCollector.translateToLocal("silva.tooltips.clickToSignature")));
					if(!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown()) {
						GuiLexiconEntry newGui = new GuiLexiconEntry(data.entry, (GuiScreen) gui);
						newGui.page = data.page;
						Minecraft.getMinecraft().displayGuiScreen(newGui);
					}
				}
			}
		mouseDownLastTick = Mouse.isButtonDown(0);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtInputPos(IGuiLexiconEntry gui, int x, ItemStack stack) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 20 + 45;
		int yPos = gui.getTop() + 14;
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, false);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);
		super.renderScreen(gui, mx, my);
	}
	
}
