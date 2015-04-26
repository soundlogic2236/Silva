package soundlogic.silva.common.lexicon.page;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class PageAdvancedFurnaceRecipe extends PageRecipe{

	private static final ResourceLocation furnaceOverlay = new ResourceLocation(LibResources.GUI_FURNACE_OVERLAY);
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");

	
	PageBackground background;
	List<ItemStack> inputs;
	List<ItemStack> outputs;
	
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PageAdvancedFurnaceRecipe(String unlocalizedName, List<ItemStack> outputs, List<ItemStack> inputs, PageBackground background) {
		super(unlocalizedName);
		this.inputs=inputs;
		this.outputs=outputs;
		this.background=background;
	}

	public PageAdvancedFurnaceRecipe(String unlocalizedName,
			ItemStack output, ItemStack input, PageBackground background) {
		this(unlocalizedName, Arrays.asList(input), Arrays.asList(output), background);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		background.render(gui);
		super.renderScreen(gui, mx, my);
	}
	
	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(ItemStack output : outputs) {
			LexiconRecipeMappings.map(output, entry, index);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == inputs.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {

		ItemStack input = inputs.get(recipeAt);
		ItemStack output = outputs.get(recipeAt);
		renderItemAtGridPos(gui, 1, 1, input, false);
		renderItemAtGridPos(gui, 2, 1, new ItemStack(Blocks.lit_furnace, 1, 3), false);
		renderItemAtGridPos(gui, 3, 1, output, false);

		Minecraft.getMinecraft().renderEngine.bindTexture(furnaceOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
		
	}
}
