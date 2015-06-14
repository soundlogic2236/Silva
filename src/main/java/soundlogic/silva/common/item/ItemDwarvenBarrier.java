package soundlogic.silva.common.item;

import soundlogic.silva.common.entity.EntityDwarvenBarrier;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDwarvenBarrier extends ItemMod{

	public ItemDwarvenBarrier(String unLocalizedName) {
		super(unLocalizedName);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new BehaviorProjectileDispense()
        {
            private static final String __OBFID = "CL_00001404";
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World world, IPosition position)
            {
                return new EntityDwarvenBarrier(world, position.getX(), position.getY(), position.getZ());
            }
        });

	}

    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        if (!p_77659_3_.capabilities.isCreativeMode)
        	--p_77659_1_.stackSize;
        p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!p_77659_2_.isRemote)
        {
            p_77659_2_.spawnEntityInWorld(new EntityDwarvenBarrier(p_77659_2_, p_77659_3_));
        }

        return p_77659_1_;
    }
}
