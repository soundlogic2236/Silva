package soundlogic.silva.common.item;

import java.util.Iterator;
import java.util.List;

import soundlogic.silva.common.potion.ModPotions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDwarvenMead extends ItemMod{

	public ItemDwarvenMead(String unLocalizedName) {
		super(unLocalizedName);
	}

	@Override
	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
        if (!p_77654_3_.capabilities.isCreativeMode)
        {
            --p_77654_1_.stackSize;
        }

        if (!p_77654_2_.isRemote)
        {
            p_77654_3_.addPotionEffect(new PotionEffect(ModPotions.potionMead.getId(),20*60*5));
        }

        if (!p_77654_3_.capabilities.isCreativeMode)
        {
            if (p_77654_1_.stackSize <= 0)
            {
                return new ItemStack(Items.glass_bottle);
            }

            p_77654_3_.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return p_77654_1_;
    }

    /**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
	@Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.drink;
    }

	@Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }

	@Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }
    
}
