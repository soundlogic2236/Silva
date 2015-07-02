package soundlogic.silva.common.block.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.core.handler.EnchantmentMoverHandler;
import soundlogic.silva.common.crafting.DarkElfLoot;
import soundlogic.silva.common.crafting.recipe.IDarkElfAct;
import soundlogic.silva.common.entity.EntitySpook;
import soundlogic.silva.common.item.ItemEnchantHolder;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

public class TileDarkElfTrap extends TileMod implements IInventory, IManaReceiver {

	public static ArrayList<TileDarkElfTrap> activeTraps = new ArrayList<TileDarkElfTrap>();
	
	private static final String TAG_MANA = "mana";
	private static final String TAG_INVENTORY = "inventory";
	private static final String TAG_LOOT = "loot";
	private static final String TAG_SPOOK = "spook";
	
	private static final int MAX_MANA = 3000;
	
    private ItemStack[] inventory = new ItemStack[2];
    private ItemStack loot = null;
    private int spookLevel = 0;
    private int mana = 0;
    
	private int particleTicks;
    
	public void trigger(TilePortalCore core, IDarkElfAct act) {
		this.discharge();
		DarkElfLoot.LootResult result = DarkElfLoot.getLoot(core, act);
		this.setLoot(result.getStack());
		if(this.worldObj.rand.nextFloat()<=result.getSpookChance()) {
			this.spookLevel=result.getSpookLevel();
		}
		this.markDirty();
	}

	@Override
	public void updateEntity() {
		doParticles();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if(activeTraps.contains(this))
			activeTraps.remove(this);
	}
    
	@Override
	public void validate() {
		super.validate();
		if(isCharged())
			activeTraps.add(this);
	}
    
    void doParticles() {
		if(worldObj.isRemote) {
			particleTicks++;
			Vector3 item = getItemPosition();
			if(spookLevel>0)
				EntitySpook.doParticles(spookLevel-1, this.worldObj, item.x, item.y, item.z, particleTicks);
			if(loot!=null) {
				if(particleTicks%20==0) {
					int source = (particleTicks/20)%4;
					Vector3 spike = getSpikePosition(source);
					BotaniaAccessHandler.lightningFX(worldObj, spike, item, 1, 0xFFFFFF, 0xFFFFFF);
				}
			}
			else {
				if(hasDusts() && mana>0) {
					boolean shouldSpark = false;
					float manaFraction;
					boolean transparent = false;
					if(isFull()) {
						shouldSpark = particleTicks%60==0;
						manaFraction = (particleTicks/60)%3==0 ? .2F : .1F;
						transparent=true;
					}
					else {
						manaFraction = (float)mana / (float)MAX_MANA;
						int manaInverseFraction = MAX_MANA/mana;
						shouldSpark = particleTicks%(manaInverseFraction+1)==0;
					}
					if(shouldSpark) {
						int source = worldObj.rand.nextInt(5);
						Vector3 start;
						if(source==4)
							start = getDiamondTipPosition();
						else
							start = getSpikePosition(source);
						Vector3 end = new Vector3(
								start.x+(Math.random()-.5)*(manaFraction+.1),
								start.y+(Math.random()-.5)*(manaFraction+.1),
								start.z+(Math.random()-.5)*(manaFraction+.1));
						BotaniaAccessHandler.lightningFX(worldObj, start, end, transparent ? 2 : 1, 0xFFFFFF, 0xFFFFFF);
					}
				}
			}
		}
	}
    private Vector3 getItemPosition() {
    	return new Vector3(this.xCoord+.5, this.yCoord+.6, this.zCoord+.5);
    }
    private Vector3 getDiamondTipPosition() {
    	return new Vector3(this.xCoord+.5, this.yCoord+6D/16D, this.zCoord+.5);
    }
    private Vector3 getSpikePosition(int spike) {
		double spike_x = this.xCoord + .25;
		double spike_y = this.yCoord + 7D/16D;
		double spike_z = this.zCoord + .25;
		if(spike==1 || spike == 2)
			spike_x += .5;
		if(spike==2 || spike == 3)
			spike_z += .5;
		return new Vector3(spike_x,spike_y,spike_z);
    }

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_SPOOK, spookLevel);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        cmp.setTag(TAG_INVENTORY, nbttaglist);
        
        if(this.loot!=null) {
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        this.loot.writeToNBT(nbttagcompound1);
	        cmp.setTag(TAG_LOOT, nbttagcompound1);
        }
        
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		spookLevel = cmp.getInteger(TAG_SPOOK);
        NBTTagList nbttaglist = cmp.getTagList(TAG_INVENTORY, 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        this.loot=null;
        if(cmp.hasKey(TAG_LOOT)) {
        	NBTTagCompound nbttagcompound1 = cmp.getCompoundTag(TAG_LOOT);
        	this.loot=ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
	}
    
	public boolean putStackIn(ItemStack stack) {
    	for(int slot=0 ; slot < inventory.length;slot++) {
	    	if(isItemValidForSlot(slot,stack)) {
	    		if(inventory[slot]==null) {
	    			inventory[slot]=getStackForSlot(slot);
	    			this.markDirty();
	    			return true;
	    		}
	    		return false;
	    	}
    	}
    	return false;
    }
    public int getColorForBowl(int bowlnum) {
    	ItemStack stack = inventory[bowlnum];
    	if(stack==null)
    		return -1;
    	return getColorForStack(stack);
    }
    public int getColorForStack(ItemStack stack) {
    	if(stack.isItemEqual(new ItemStack(Items.redstone)))
    		return 0x550000;
    	if(stack.isItemEqual(new ItemStack(ModBlocks.darkenedDust)))
    		return 0x000000;
    	return -1;
    }
    public boolean hasLoot() {
    	return loot!=null;
    }
    public ItemStack getLoot() {
    	return loot;
    }
    public int getSpook() {
    	return spookLevel;
    }
    public void setSpook(int spookLevel) {
    	this.spookLevel=spookLevel;
    }
    public void setLoot(ItemStack loot) {
    	this.loot=loot;
    }
    public void emptyTrap() {
    	if(worldObj.isRemote)
    		return;
    	EntitySpook spook = new EntitySpook(this.worldObj, this.spookLevel-1);
    	Vector3 item = this.getItemPosition();
    	spook.setPosition(item.x, item.y, item.z);
    	this.worldObj.spawnEntityInWorld(spook);
    	this.spookLevel=0;
    }
    public void discharge() {
    	this.inventory[0]=null;
    	this.inventory[1]=null;
    	this.mana=0;
    	activeTraps.remove(this);
    }
    public boolean isCharged() {
    	return hasDusts() && isFull();
    }
    public boolean hasDusts() {
    	return inventory[0]!=null && inventory[1]!=null;
    }

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null)
        {
            ItemStack itemstack;

            if (this.inventory[slot].stackSize <= amount)
            {
                itemstack = this.inventory[slot];
                this.inventory[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack amount) {
        this.inventory[slot] = amount;

        if (amount != null && amount.stackSize > this.getInventoryStackLimit())
        {
            amount.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "darkElfTrap";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory() {
		// NO OP
	}

	@Override
	public void closeInventory() {
		// NO OP
	}
	
	public ItemStack getStackForSlot(int slot) {
		if(slot==0)
			return new ItemStack(Items.redstone);
		if(slot==1)
			return new ItemStack(ModBlocks.darkenedDust);
		return null;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.isItemEqual(getStackForSlot(slot));
	}
	@Override
	public int getCurrentMana() {
		return mana;
	}
	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull() && hasDusts() && !hasLoot();
	}
	@Override
	public boolean isFull() {
		return mana>=MAX_MANA;
	}
	@Override
	public void recieveMana(int arg0) {
		mana=Math.min(mana+arg0, MAX_MANA);
		if(isCharged())
			activeTraps.add(this);
		this.markDirty();
	}

    public void markDirty()
    {
    	super.markDirty();
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}