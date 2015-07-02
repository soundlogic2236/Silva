package soundlogic.silva.common.item;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemPriceProxy extends ItemMod{

	ArrayList<ItemStack> stacks=new ArrayList<ItemStack>();
	
	public ItemPriceProxy(String unLocalizedName) {
		super(unLocalizedName);
		stacks.add(new ItemStack(Items.diamond));
		stacks.add(new ItemStack(Items.emerald));
		stacks.add(new ItemStack(Items.gold_ingot));
		stacks.add(new ItemStack(Items.quartz));
		stacks.add(new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,4));
		stacks.add(new ItemStack(BotaniaAccessHandler.findItem("manaResource"),1,5));
	}
	
	public ItemStack getCurrentPriceStack() {
		int ticks=Silva.proxy.getTicks();
		int slot = (ticks / 20) % stacks.size();
    	return stacks.get(slot);
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		//NO OP
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        //NO OP
    }
    
}
