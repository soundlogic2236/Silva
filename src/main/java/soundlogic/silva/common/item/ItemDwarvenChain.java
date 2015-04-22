package soundlogic.silva.common.item;

import java.util.Iterator;
import java.util.List;

import soundlogic.silva.common.core.handler.DwarvenChainHandler;
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
	}
	
    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
    	if(entity instanceof EntityCreature) {
    		EntityCreature creature = (EntityCreature) entity;
        	DwarvenChainHandler.attachChainToEntity(creature, player);
        	return true;
    	}
    	return false;
    }
	

}
