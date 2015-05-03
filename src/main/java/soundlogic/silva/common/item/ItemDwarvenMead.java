package soundlogic.silva.common.item;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import soundlogic.silva.common.potion.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemDwarvenMead extends ItemMod{

	static Random random = new Random();
	
	public static DamageSource ALCOHOL_DAMAGE = new DamageSource("alcohol") {
		public IChatComponent func_151519_b(EntityLivingBase p_151519_1_)
	    {
	        EntityLivingBase entitylivingbase1 = p_151519_1_.func_94060_bK();
	        String s = "death.attack." + this.damageType +"." + random.nextInt(4);
	        String s1 = s + ".player";
	        return entitylivingbase1 != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {p_151519_1_.func_145748_c_(), entitylivingbase1.func_145748_c_()}): new ChatComponentTranslation(s, new Object[] {p_151519_1_.func_145748_c_()});
	    }
	}.setDamageBypassesArmor().setDamageIsAbsolute();
	public static ResourceLocation shader_blur = new ResourceLocation("minecraft:shaders/post/blur.json");
	public static ResourceLocation shader_phosphor = new ResourceLocation("minecraft:shaders/post/phosphor.json");
	
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
        	PotionEffect prev = p_77654_3_.getActivePotionEffect(ModPotions.potionMead);
        	int level = 0;
        	if(prev!=null)
        		level = prev.getAmplifier();
            p_77654_3_.addPotionEffect(new PotionEffect(ModPotions.potionMead.getId(),20*60*5,level+1));
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
