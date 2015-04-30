package soundlogic.silva.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.lexicon.KnowledgeType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemChargedStone extends ItemMod{

	private final static String TAG_LEVEL = "Silva.Charge.";
	private final static String TAG_COLOR = "Silva.Charge.Color";
	
	ArrayList<ItemStack> stacks=new ArrayList<ItemStack>();
	IIcon iconOverlay;
	
	public ItemChargedStone(String unLocalizedName) {
		super(unLocalizedName);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer) {
		DimensionalExposureProperties props = DimensionalEnergyHandler.getExposureData(entityPlayer);
		HashMap<Dimension,Integer> levels = getDimensionLevels(stack);
		for(Entry<Dimension,Integer> entry : levels.entrySet()) {
			int level = props.getExposureLevel(entry.getKey());
			level = Math.max(level, entry.getValue());
			props.setExposureLevel(entry.getKey(), level);
		}
		return new ItemStack(this);
	}
	
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	String resourceName=LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "");
		itemIcon = par1IconRegister.registerIcon(resourceName);
    	String resourceName2=LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"_overlay";
		iconOverlay = par1IconRegister.registerIcon(resourceName2);
	}

    @Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
    	HashMap<Dimension,Integer> map = new HashMap<Dimension,Integer>();
    	list.add(new ItemStack(this,1,0));
		for(Dimension dim : Dimension.values()) {
			map.clear();
			map.put(dim, 60*20*10);
			list.add(makeFromLevels(map));
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(par1ItemStack.getItemDamage()==0)
			return;
		if(GuiScreen.isShiftKeyDown()) {
			HashMap<Dimension, Integer> map = getDimensionLevels(par1ItemStack);
			for(Entry<Dimension, Integer> entry : map.entrySet()) {
				addStringToTooltip(StatCollector.translateToLocal(entry.getKey().getUnlocalizedName()) + " (" + StringUtils.ticksToElapsedTime(entry.getValue()) + ")", par3List);
			}
		}
		else
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), par3List);
	}
    
    public static ItemStack makeFromLevels(HashMap<Dimension,Integer> levels) {
    	NBTTagCompound cmp = new NBTTagCompound();
    	int maxLevel=0;
    	for(Entry<Dimension, Integer> entry : levels.entrySet()) {
    		maxLevel=Math.max(maxLevel, entry.getValue());
    		cmp.setInteger(TAG_LEVEL+entry.getKey().ordinal(), entry.getValue());
    	}
    	int color = getColorForDimensions(levels);
    	cmp.setInteger(TAG_COLOR, color);
    	ItemStack stack = new ItemStack(ModItems.chargedStone,1,maxLevel);
    	stack.setTagCompound(cmp);
    	return stack;
    }
    
    public static HashMap<Dimension,Integer> getDimensionLevels(ItemStack stack) {
    	HashMap<Dimension,Integer> map = new HashMap<Dimension,Integer>();
    	NBTTagCompound cmp = stack.getTagCompound();
    	if(cmp==null)
    		return map;
    	for(Dimension dim : Dimension.values()) {
    		if(cmp.hasKey(TAG_LEVEL+dim.ordinal()))
    			map.put(dim, cmp.getInteger(TAG_LEVEL+dim.ordinal()));
    	}
    	return map;
    }
        
	public boolean requiresMultipleRenderPasses() {
    	return true;
    }
	
	public int getRenderPasses(int metadata) {
		return metadata == 0 ? 1 : 2;
	}
	
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return renderPass == 0 ? 0xFFFFFF : getColorFromTag(stack);
	}
    
	private static int getColorFromTag(ItemStack stack) {
		if(!stack.hasTagCompound())
			return 0;
		return stack.getTagCompound().getInteger(TAG_COLOR);
	}
	
    private static int getColorForDimensions(HashMap<Dimension, Integer> dimensionLevels) {
    	int red = 0;
    	int green = 0;
    	int blue = 0;
    	int totalLevel = 0;
    	int maxLevel = 0;
    	for(Entry<Dimension, Integer> entry : dimensionLevels.entrySet()) {
    		int level = entry.getValue();
    		maxLevel=Math.max(maxLevel, level);
    		totalLevel+=level;
    		Color col = entry.getKey().getPortalColor();
    		red+=col.getRed()*level;
    		green+=col.getGreen()*level;
    		blue+=col.getBlue()*level;
    	}
    	red=red/totalLevel;
    	green=green/totalLevel;
    	blue=blue/totalLevel;
    	Color col = new Color(red,green,blue);
		return col.getRGB();
	}

	public IIcon getIcon(ItemStack stack, int renderPass) {
    	return renderPass == 0 ? itemIcon : stack.getItemDamage() != 0 ? iconOverlay : null;
    }

}
