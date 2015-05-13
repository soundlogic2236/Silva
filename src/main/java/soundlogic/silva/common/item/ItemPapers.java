package soundlogic.silva.common.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.crafting.recipe.PapersCloneRecipe;
import soundlogic.silva.common.crafting.recipe.PapersIntoLexiconRecipe;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibGUI;
import soundlogic.silva.common.lib.LibItemNames;
import soundlogic.silva.common.lib.LibNBT;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;

public class ItemPapers extends ItemMod{

	public ItemPapers(String unLocalizedName) {
		super(unLocalizedName);
		this.hasSubtypes=true;
	}
	
	public static ItemStack setKnowledgeType(ItemStack stack,KnowledgeType type) {
		NBTTagCompound compound=new NBTTagCompound();
		if(stack.hasTagCompound())
			compound=stack.getTagCompound();
		compound.setString(LibNBT.PAPERS_KNOWLEDGE_TYPE, type.getUnlocalizedName());
		stack.setTagCompound(compound);
		return stack;
	}

	public static ItemStack setOpeningEntry(ItemStack stack,LexiconEntry entry) {
		NBTTagCompound compound=new NBTTagCompound();
		if(stack.hasTagCompound())
			compound=stack.getTagCompound();
		compound.setString(LibNBT.PAPERS_ENTRY, entry.getUnlocalizedName());
		stack.setTagCompound(compound);
		return stack;
	}

	public static ItemStack setData(ItemStack stack,KnowledgeType type,LexiconEntry entry) {
		setKnowledgeType(stack,type);
		setOpeningEntry(stack,entry);
		return stack;
	}
		
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		KnowledgeType type=getKnowledgeType(par1ItemStack);
		if(type==null)
			return LibItemNames.PAPER_ERROR;
		return super.getUnlocalizedName(par1ItemStack) + "." + type.id;
	}

	public static KnowledgeType getKnowledgeType(ItemStack pageStack) {
		NBTTagCompound compound=pageStack.getTagCompound();
		if(compound==null)
			return null;
		String typeName;
		if(compound.hasKey(LibNBT.PAPERS_KNOWLEDGE_TYPE))
			typeName=compound.getString(LibNBT.PAPERS_KNOWLEDGE_TYPE);
		else
			return null;
		for(KnowledgeType type : BotaniaAPI.knowledgeTypes.values())
		{
			if(type.getUnlocalizedName().equals(typeName))
				return type;
		}
		return null;
	}

	public static LexiconEntry getOpeningEntry(ItemStack pageStack) {
		NBTTagCompound compound=pageStack.getTagCompound();
		if(compound==null)
			return null;
		String entryName;
		if(compound.hasKey(LibNBT.PAPERS_ENTRY))
			entryName=compound.getString(LibNBT.PAPERS_ENTRY);
		else
			return null;
		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
		{
			if(entry.getUnlocalizedName().equals(entryName))
				return entry;
		}
		return null;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(GuiScreen.isShiftKeyDown()) {
			KnowledgeType type=getKnowledgeType(par1ItemStack);
			if(type==null)
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.PAPERS_ERROR), par3List);
			else {
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.PAPERS_CRAFT), par3List);
				addStringToTooltip(StatCollector.translateToLocal(LibGUI.PAPERS_KNOWLEDGE_TYPE), par3List);
				addStringToTooltip(StatCollector.translateToLocal(type.getUnlocalizedName()), par3List);
			}
		}
		else
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), par3List);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < LexiconData.papers.size(); i++)
			list.add(LexiconData.papers.get(i));
	}

}
