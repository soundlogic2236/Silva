package soundlogic.silva.common.lexicon.page;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import soundlogic.silva.common.block.BlockPortalCore;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.block.BlockAlfPortal;
import vazkii.botania.common.lexicon.page.PageRecipe;

public class PageAdvancedPortalTrade extends PageRecipe{

	PageBackground background;

	private static final ResourceLocation tradeOverlay = new ResourceLocation(vazkii.botania.client.lib.LibResources.GUI_ELVEN_TRADE_OVERLAY);

	static boolean mouseDownLastTick = false;

	List<IPortalRecipe> recipes;
	Dimension dim;
	
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PageAdvancedPortalTrade(String unlocalizedName,List<IPortalRecipe> recipes, Dimension dim) {
		super(unlocalizedName);
		this.recipes=recipes;
		this.dim=dim;
	}

	public PageAdvancedPortalTrade(String unlocalizedName, IPortalRecipe recipe, Dimension dim) {
		this(unlocalizedName, Arrays.asList(recipe),dim);
	}
	
	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(IPortalRecipe recipe : recipes) {
			List<ItemStack> output=recipe.getOutput();
			for(ItemStack outputSingle : output)
				LexiconRecipeMappings.map(outputSingle, entry, index);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		boolean mouseDown = Mouse.isButtonDown(0);
		
		IPortalRecipe recipe = recipes.get(recipeAt);
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(tradeOverlay);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);

		List<ItemStack> output=recipe.getOutput();
		for(int i = 0;i<output.size();i++)
			renderItemAtGridPos(gui, 3+i, 1, output.get(i), false);

		List<Object> inputs = recipe.getInputs();
		int i = 0;
		for(Object obj : inputs) {
			Object input = obj;
			if(input instanceof String)
				input = OreDictionary.getOres((String) input).get(0);

			renderItemAtInputPos(gui, i, (ItemStack) input);
			i++;
		}

		IIcon portalTex=((BlockPortalCore)ModBlocks.portalCore).portalTex;
		Color color=dim.getPortalColor();
		if(dim == Dimension.ALFHEIM) {
			portalTex=BlockAlfPortal.portalTex;
			color=new Color(0xFFFFFF);
		}
		float red=(float)color.getRed()/255F;
		float green=(float)color.getGreen()/255F;
		float blue=(float)color.getBlue()/255F;
		IIcon portalIcon = ((BlockPortalCore)ModBlocks.portalCore).portalTex;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		GL11.glColor4f(red, green, blue, 1F);
		RenderItem.getInstance().renderIcon(gui.getLeft() + 22, gui.getTop() + 36, portalIcon, 48, 48);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		if(mx >= gui.getLeft() + 22 && mx <= gui.getLeft() + 22 + 48) 
			if(my >= gui.getTop() + 36 && my <= gui.getTop() + 36 + 48) {
				List<String> tooltip=new ArrayList<String>();
				tooltip.add(StatCollector.translateToLocal(dim.getUnlocalizedName()));
				vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, tooltip);

				EntryData data=DimensionHandler.getSignatureEntryForDimension(dim);
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
}
