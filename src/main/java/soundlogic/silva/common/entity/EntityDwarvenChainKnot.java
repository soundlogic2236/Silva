package soundlogic.silva.common.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import soundlogic.silva.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDwarvenChainKnot extends EntityHanging{

    public EntityDwarvenChainKnot(World p_i1592_1_)
    {
        super(p_i1592_1_);
    }

    public boolean onValidSurface()
    {
        return this.worldObj.getBlock(this.field_146063_b, this.field_146064_c, this.field_146062_d).getRenderType() == 11;
    }

	@Override
	public int getWidthPixels() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeightPixels() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBroken(Entity p_110128_1_) {
		// TODO Auto-generated method stub
		
	}

	public void setAttachedEntity(EntityLiving toLeash) {
		// TODO Auto-generated method stub
		
	}

}
