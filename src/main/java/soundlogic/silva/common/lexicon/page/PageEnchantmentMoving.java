package soundlogic.silva.common.lexicon.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.helper.EquipmentHelper.EquipmentType;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.item.ItemEnchantHolder;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.PageBackground;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class PageEnchantmentMoving extends PageRecipe{

	private static final ResourceLocation terrasteelOverlay = new ResourceLocation(vazkii.botania.client.lib.LibResources.GUI_TERRASTEEL_OVERLAY);

	
	List<ItemStack> enchanteds = new ArrayList<ItemStack>();
	List<ItemStack> holders = new ArrayList<ItemStack>();
	List<ItemStack> outputs = new ArrayList<ItemStack>();
	
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PageEnchantmentMoving(String unlocalizedName) {
		super(unlocalizedName);
		
		enchanteds.add(getStackWithEnchantment(new ItemStack(Items.diamond_helmet),Enchantment.protection));
		holders.add(new ItemStack(ModItems.enchantHolder,1,0));
		outputs.add(getHolderWithEnchantment(new ItemStack(ModItems.enchantHolder,1,0), Enchantment.protection));

		enchanteds.add(getStackWithEnchantment(new ItemStack(Items.diamond_chestplate),Enchantment.blastProtection));
		holders.add(new ItemStack(ModItems.enchantHolder,1,1));
		outputs.add(getHolderWithEnchantment(new ItemStack(ModItems.enchantHolder,1,1), Enchantment.blastProtection));

		enchanteds.add(getStackWithEnchantment(new ItemStack(Items.diamond_sword),Enchantment.sharpness));
		holders.add(new ItemStack(ModItems.enchantHolder,1,7));
		outputs.add(getHolderWithEnchantment(new ItemStack(ModItems.enchantHolder,1,7), Enchantment.sharpness));
	}
	
	private ItemStack getHolderWithEnchantment(ItemStack itemStack,
			Enchantment enchantment) {
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setShort(ItemEnchantHolder.TAG_DUMMY_ENCHANT, (short) enchantment.effectId);
		itemStack.setTagCompound(cmp);
		return itemStack;
	}

	private ItemStack getStackWithEnchantment(ItemStack stack, Enchantment enchantment) {
		stack.addEnchantment(enchantment, 1);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == outputs.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		ItemStack block1 = new ItemStack(ModBlocks.paradoxWood,1,4);
		ItemStack block2 = new ItemStack(ModBlocks.paradoxStone,1,0);
		ItemStack block3 = new ItemStack(ModBlocks.enchantPlate,1,0);
		
		ItemStack offering = new ItemStack(ModItems.simpleResource, 1, 3);
		
		ItemStack output=outputs.get(recipeAt);
		ItemStack enchanted=enchanteds.get(recipeAt);
		ItemStack holder=holders.get(recipeAt);

		GL11.glTranslatef(0F, 0F, -10F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 103, block1, false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 106, block2, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 106, block2, false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 110, block1, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 14, gui.getTop() + 110, block1, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 13, gui.getTop() + 110, block1, false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 114, block2, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 114, block2, false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 1, gui.getTop() + 117, block1, false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 102, block3, false);
		GL11.glTranslatef(0F, 0F, -10F);

		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 30, output, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 80, offering, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 20, gui.getTop() + 86, enchanted, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 19, gui.getTop() + 86, holder, false);

		Minecraft.getMinecraft().renderEngine.bindTexture(terrasteelOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
	}

}
