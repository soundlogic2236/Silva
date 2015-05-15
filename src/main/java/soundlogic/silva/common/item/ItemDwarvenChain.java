package soundlogic.silva.common.item;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.common.core.handler.DwarvenChainHandler;
import soundlogic.silva.common.core.handler.DwarvenChainHandler.LeashProperties;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemDwarvenChain extends ItemMod{

	public ItemDwarvenChain(String unLocalizedName) {
		super(unLocalizedName);
		this.setMaxStackSize(1);
	}

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_)
    {
        return true;
    }
	
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xs, float ys, float zs)
    {
        if (EntityDwarvenChainKnot.isValidKnotLocation(world,x,y,z))
        {
            if (world.isRemote)
            {
                return true;
            }
            else
            {
                DwarvenChainHandler.attachChainToBlock(player, world, x, y, z);
                stack.stackSize--;
                if(stack.stackSize==0)
                	player.setCurrentItemOrArmor(0, null);
                return true;
            }
        }
        else
        {
            return false;
        }
    }
    
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
    	if(player.worldObj.isRemote)
    		return false;
    	if(entity instanceof EntityCreature) {
    		EntityCreature creature = (EntityCreature) entity;
    		LeashProperties props = DwarvenChainHandler.getChainForEntity(creature);
    		if(props.getActive() && props.getEntity()==player) {
    			props.setActive(false);
    			return true;
    		}
    		else if(!props.getActive() && DwarvenChainHandler.findChainedCreatures(player).size()==0) {
    			DwarvenChainHandler.attachChainToEntity(creature, player);
    			return true;
    		}
        	return false;
    	}
    	return false;
    }
}
