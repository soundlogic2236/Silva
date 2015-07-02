package soundlogic.silva.common.entity;

import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.item.block.ItemBlockPixieFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityItemPixieFlower extends EntityItem {

	public EntityItemPixieFlower(World world) {
		super(world);
	}
	
	public EntityItemPixieFlower(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
	}
	
	public EntityItemPixieFlower(EntityItem original) {
		this(original.worldObj, original.posX, original.posY, original.posZ, original.getEntityItem());
		this.delayBeforeCanPickup=original.delayBeforeCanPickup;
		NBTTagCompound cmp = new NBTTagCompound();
		original.writeToNBT(cmp);
		this.readFromNBT(cmp);
	}
	
	@Override
	public boolean handleWaterMovement() {
		if(!super.handleWaterMovement())
			return false;
		if(!worldObj.isRemote) {
			ItemStack stack = this.getEntityItem();
			ItemStack flowers = new ItemStack(BotaniaAccessHandler.findItem("flower"),stack.stackSize,stack.getItemDamage());
			ItemStack dust = new ItemStack(BotaniaAccessHandler.findItem("manaResource"),0,8);
			for(int i = 0 ; i < stack.stackSize; i++)
				if(worldObj.rand.nextFloat()<ItemBlockPixieFlower.dustChance)
					dust.stackSize++;
			EntityItem flowersEntity = new EntityItem(worldObj, posX, posY, posZ, flowers);
			flowersEntity.motionX=this.motionX;
			flowersEntity.motionY=this.motionY;
			flowersEntity.motionZ=this.motionZ;
			flowersEntity.delayBeforeCanPickup=this.delayBeforeCanPickup;
			worldObj.spawnEntityInWorld(flowersEntity);
			if(dust.stackSize>0) {
				EntityItem dustEntity = new EntityItem(worldObj, posX, posY, posZ, dust);
				dustEntity.motionX=this.motionX;
				dustEntity.motionY=this.motionY;
				dustEntity.motionZ=this.motionZ;
				dustEntity.delayBeforeCanPickup=this.delayBeforeCanPickup;
				worldObj.spawnEntityInWorld(dustEntity);
			}
			this.setDead();
		}
		return true;
	}

}
 