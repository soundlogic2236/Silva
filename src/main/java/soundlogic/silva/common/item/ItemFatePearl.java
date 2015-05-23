package soundlogic.silva.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler.DimensionalExposureProperties;
import soundlogic.silva.common.core.handler.portal.fate.FateHandler;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibGUI;
import vazkii.botania.api.lexicon.KnowledgeType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemFatePearl extends ItemMod{

	public static int renderColor1 = 0xFFFFFF;
	public static int renderColor2 = 0xFFFFFF;
	public static boolean inert = true;
	
	ArrayList<ItemStack> stacks=new ArrayList<ItemStack>();
	private IIcon iconTint1;
	private IIcon iconTint2;
	private IIcon iconFrame;
	public IIcon iconOverlays;
	private int numOverlays = 6;
	
	public ItemFatePearl(String unLocalizedName) {
		super(unLocalizedName);
		this.setMaxStackSize(1);
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer) {
		if(!entityPlayer.capabilities.isCreativeMode)
			return stack;
		if(entityPlayer.isSneaking()) {
			FateHandler.clearFate(entityPlayer);
			FateHandler.applyRandomFate(entityPlayer, 20*60*10);
		}
		FateHandler.printDebugData(entityPlayer, entityPlayer);
		return stack;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer entityPlayer, EntityLivingBase target) {
		if(!entityPlayer.capabilities.isCreativeMode)
			return false;
		if(entityPlayer.isSneaking()) {
			FateHandler.clearFate(target);
			FateHandler.applyRandomFate(target, 20*60*10);
		}
		FateHandler.printDebugData(target, entityPlayer);
		return true;
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		iconTint1 = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"Tint1");
		iconTint2 = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"Tint2");
		iconFrame = par1IconRegister.registerIcon(LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"Frame");
		PearlOverlay iconOverlays = new PearlOverlay(LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"Overlay");
		for(int i = 0 ; i < numOverlays ; i++) {
			iconOverlays.addIcon(LibResources.PREFIX_MOD+this.getUnlocalizedName().replaceAll("item\\.", "")+"Overlay"+i);
		}
		iconOverlays.register((TextureMap) par1IconRegister);
		this.iconOverlays=iconOverlays;
	}

	public boolean requiresMultipleRenderPasses() {
    	return true;
    }
	
	public int getRenderPasses(int metadata) {
		return 4;
	}
	
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if(Minecraft.getMinecraft().thePlayer.getHeldItem()!=stack)
			return 0xFFFFFF;
		switch(renderPass) {
		case 0: return renderColor1;
		case 1: return inert ? renderColor2 : 0xFFFFFF;
		case 2: return inert ? 0xFFFFFF : renderColor2;
		case 3: return 0xFFFFFF;
		}
		return 0xFFFFFF;
	}
    
	public IIcon getIcon(ItemStack stack, int renderPass) {
		boolean treatAsInert = inert;
		if(Minecraft.getMinecraft().thePlayer.getHeldItem()!=stack)
			treatAsInert=true;
		switch(renderPass) {
		case 0: return iconTint1;
		case 1: return treatAsInert ? iconTint2 : iconOverlays;
		case 2: return treatAsInert ? iconOverlays : iconTint2;
		case 3: return iconFrame;
		}
    	return null;
    }

	public static void setRenderColors(int fateColor1, int fateColor2, boolean inert) {
		renderColor1 = fateColor1 == -1 ? 0xFFFFFF : fateColor1;
		renderColor2 = fateColor2 == -1 ? 0xFFFFFF : fateColor2;
		ItemFatePearl.inert=inert;
	}

	@SideOnly(Side.CLIENT)
	public static class PearlOverlay extends TextureAtlasSprite {

		static Random random = new Random();
		
		public static class PublicFrameIcon extends TextureAtlasSprite {

			protected PublicFrameIcon(String p_i1282_1_) {
				super(p_i1282_1_);
			}
			public int getFrameCounter() {
				return this.frameCounter;
			}
			public void setToStart() {
		        this.frameCounter = 0;
		        this.tickCounter = 0;
			}
		}
		
		ArrayList<PublicFrameIcon> subIcons = new ArrayList<PublicFrameIcon>();
	    int currentIcon = 0;

	    protected PearlOverlay(String iconName, String... otherNames)
	    {
	    	super(iconName);
    		for(String name : otherNames) {
    			addIcon(name);
    		}
	    }
		
		public void addIcon(String name) {
			subIcons.add(new PublicFrameIcon(name));
		}

	    @Override
		public int getIconWidth() {
	    	return getCurrentIcon().getIconWidth();
		}

		@Override
		public int getIconHeight() {
			return getCurrentIcon().getIconHeight();
		}

		@Override
		public float getMinU() {
	    	return getCurrentIcon().getMinU();
		}

		@Override
		public float getMaxU() {
	    	return getCurrentIcon().getMaxU();
		}

		@Override
		public float getInterpolatedU(double p_94214_1_) {
	    	return getCurrentIcon().getInterpolatedU(p_94214_1_);
		}

		@Override
		public float getMinV() {
	    	return getCurrentIcon().getMinV();
		}

		@Override
		public float getMaxV() {
	    	return getCurrentIcon().getMaxV();
		}

		@Override
		public float getInterpolatedV(double p_94207_1_) {
	    	return getCurrentIcon().getInterpolatedV(p_94207_1_);
		}

		protected PublicFrameIcon getCurrentIcon() {
			return subIcons.get(currentIcon);
		}
		
	    public void updateAnimation()
	    {
	        if(getCurrentIcon().getFrameCounter()==0) {
	        	changeIcon();
	        }
	    }
	    
	    private void changeIcon() {
	    	int newIcon = random.nextInt(subIcons.size());
	    	while(newIcon == currentIcon) {
	    		newIcon = random.nextInt(subIcons.size());
	    	}
	    	currentIcon=newIcon;
	    	getCurrentIcon().setToStart();
		}
	    
	    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
	    {
	        return true;
	    }

		public boolean load(IResourceManager manager, ResourceLocation location)
	    {
	        return true;
	    }
		
		public void register(TextureMap register) {
			register.setTextureEntry(this.getIconName(), this);
			for(PublicFrameIcon icon : subIcons) {
				register.setTextureEntry(icon.getIconName(), icon);
			}
		}

	}
	
}
