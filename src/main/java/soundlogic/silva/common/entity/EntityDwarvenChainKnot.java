package soundlogic.silva.common.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.common.core.handler.DwarvenChainHandler;
import soundlogic.silva.common.core.handler.DwarvenChainHandler.LeashProperties;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDwarvenChainKnot extends EntityHanging{

	final String TAG_CHAIN_COUNT = "chains";
	
	public int chainCount = 0;
	
    public EntityDwarvenChainKnot(World p_i1592_1_)
    {
        super(p_i1592_1_);
    }
    public EntityDwarvenChainKnot(World world, int x, int y, int z)
    {
        super(world, x, y, z, 0);
        this.setPosition((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D);
    }

    public boolean onValidSurface()
    {
        return isValidKnotLocation(this.worldObj, this.field_146063_b, this.field_146064_c, this.field_146062_d);
    }

    public void setDirection(int p_82328_1_) {}
    
	@Override
	public int getWidthPixels() {
		return 9;
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

	@SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double p_70112_1_)
    {
        return p_70112_1_ < 1024.0D;
    }

	@Override
	public void onBroken(Entity p_110128_1_) { }

	public boolean isChainAttached(EntityCreature creature) {
		return !this.isDead;
	}
	public static boolean isValidKnotLocation(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block.getRenderType() == 11;
	}
	public static EntityDwarvenChainKnot createKnotForBlock(World world, int x,
			int y, int z) {
		EntityDwarvenChainKnot knot = new EntityDwarvenChainKnot(world, x, y, z);
        knot.forceSpawn = true;
        world.spawnEntityInWorld(knot);
        return knot;
	}

    public static EntityDwarvenChainKnot getKnotForBlock(World world, int x, int y, int z)
    {
        List<EntityDwarvenChainKnot> list = world.getEntitiesWithinAABB(EntityDwarvenChainKnot.class, AxisAlignedBB.getBoundingBox((double)x - 1.0D, (double)y - 1.0D, (double)z - 1.0D, (double)x + 1.0D, (double)y + 1.0D, (double)z + 1.0D));

        for(EntityDwarvenChainKnot knot : list) {
            if (knot.field_146063_b == x && knot.field_146064_c == y && knot.field_146062_d == z)
            {
                return knot;
            }
        }

        return null;
    }
    public boolean interactFirst(EntityPlayer player)
    {
        ItemStack stack = player.getHeldItem();
    	if(stack!=null && stack.getItem()==ModItems.dwarfChain) {
    		ModItems.dwarfChain.onItemUse(stack, player, this.worldObj, field_146063_b, field_146064_c, field_146062_d, 0, 0, 0, 0);
    		return true;
    	}
    	else if(!worldObj.isRemote && stack==null) {
            double d0 = DwarvenChainHandler.MAX_LENGTH;
            List<EntityCreature> list = DwarvenChainHandler.findChainedCreatures(this);
            EntityCreature creature = list.get(0);
            ItemStack chainStack = DwarvenChainHandler.getChainForEntity(creature).getStack();
    		player.setCurrentItemOrArmor(0, chainStack);
            DwarvenChainHandler.attachChainToEntity(creature, player, chainStack);
            chainCount--;
            if(chainCount==0)
            	this.setDead();
    	}
    	else if(!worldObj.isRemote) {
    		this.setDead();
    	}
    	return true;
    }
    
    public void setDead() {
    	super.setDead();
        List<EntityCreature> list = DwarvenChainHandler.findChainedCreatures(this);
    	for(int i = 0 ; i<chainCount;i++) {
    		ItemStack chainStack = DwarvenChainHandler.getChainForEntity(list.get(0)).getStack();
    		this.entityDropItem(chainStack, 0);
    	}
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound cmp) {
    	super.writeEntityToNBT(cmp);
    	cmp.setInteger(TAG_CHAIN_COUNT, chainCount);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound cmp) {
    	super.readEntityFromNBT(cmp);
    	chainCount = cmp.getInteger(TAG_CHAIN_COUNT);
    }
}
