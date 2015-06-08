package soundlogic.silva.common.block.tile;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.EnchantmentMoverHandler;
import soundlogic.silva.common.item.ItemEnchantHolder;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

public class TileEnchantPlate extends TileMod implements ISparkAttachable {

	public static final int MAX_MANA = 1000;
	
	private static final int[][] STONE_BLOCKS = {
		{ 1, 0, }, { -1, 0 }, { 0, 1 }, { 0, -1 }
	};

	private static final int[][] PATTERNED_BLOCKS = {
		{ 0, 0 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
	};

	private static final String TAG_MANA = "mana";
	private static final String TAG_PROGRESS = "progress";

	int mana;
	public int progress;
	
	EntityItem enchanted = null;
	EntityItem target = null;
	EntityItem offering = null;
	
	ItemStack prevEnchanted;
	ItemStack prevTarget;
	ItemStack prevOffering;
	
	boolean acting = false;
	
	public int requiredProgress = -1;
	
	int particleTicks = 0;

	@Override
	public void updateEntity() {

		acting=false;
		
		if(hasValidPlatform()) {
			List<EntityItem> items = getItems();
			if(areItemsValid(items)) {
				updateItemData();
				acting=true;
				ISparkEntity spark = getAttachedSpark();
				if(spark != null) {
					List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
					for(ISparkEntity otherSpark : sparkEntities) {
						if(spark == otherSpark)
							continue;

						if(otherSpark.getAttachedTile() != null && otherSpark.getAttachedTile() instanceof IManaPool)
							otherSpark.registerTransfer(spark);
					}
				}
				
				progress+=mana;
				mana=0;
				
				if(progress > 0)
					doParticles();
				
				if(progress >= requiredProgress && !worldObj.isRemote) {
					enchanted.setDead();
					offering.setDead();
					target.setEntityItemStack(EnchantmentMoverHandler.applyToStack(prevTarget, prevEnchanted));
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
				}
			}
		}
		if(!acting) {
			mana=0;
			progress=0;
			requiredProgress=-1;
		}
	}

	private void updateItemData() {
		if(checkNeedsUpdate()) {
			prevEnchanted = enchanted.getEntityItem();
			prevTarget = target.getEntityItem();
			prevOffering = offering.getEntityItem();
			requiredProgress = getRequiredMana(prevEnchanted, prevTarget);
		}
	}
	
	private int getRequiredMana(ItemStack enchanted, ItemStack holder) {
		return EnchantmentMoverHandler.getManaRequirement(enchanted, holder);
	}

	private boolean checkNeedsUpdate() {
		if(requiredProgress == -1)
			return true;
		if(enchanted.getEntityItem()!=prevEnchanted)
			return true;
		if(target.getEntityItem()!=prevTarget)
			return true;
		if(offering.getEntityItem()!=prevOffering)
			return true;
		return false;
	}

	void doParticles() {
		if(worldObj.isRemote) {
			particleTicks++;
			int ticks = (int) (100.0 * ((double) progress / (double) requiredProgress));

			int totalSpiritCount = 6;
			double tickIncrement = 360D / totalSpiritCount;

			int speed = 5;
			double wticks = ticks * speed - tickIncrement;
			
			double r = Math.sin((ticks - 100) / 10D) * 2;
			double g = Math.sin(wticks * Math.PI / 180 * 0.55);
			
			double v1 = (Math.sin(ticks/10D)+1)/2D;
			double v2 = (Math.cos(ticks/10D)+1)/2D;

			for(int i = 0; i < totalSpiritCount; i++) {
				double x = xCoord + Math.sin(wticks * Math.PI / 180) * r + 0.5;
				double y = yCoord + 0.25 + Math.abs(r) * 0.7;
				double z = zCoord + Math.cos(wticks * Math.PI / 180) * r + 0.5;

				
				wticks += tickIncrement;
				float[] colorsfx;
				double va = i%2==0 ? v1 : v2;
				double vb = i%2==1 ? v1 : v2;
				float [] colorsfx1 = new float[] { 
						(float) va,
						(float) va, 
						(float) va,
				};
				int colorsfx2 = (new Color(
						(float) vb,
						(float) vb, 
						(float) vb
				)).getRGB();

				if(particleTicks%2==0)
					Botania.proxy.wispFX(worldObj, x, y, z, colorsfx1[0], colorsfx1[1], colorsfx1[2], 0.85F, (float)g * 0.05F, 0.25F);

				Vector3 start = new Vector3(x,y,z);
				Vector3 end = new Vector3(
						x+(worldObj.rand.nextDouble()-.5D)/2D,
						y+(worldObj.rand.nextDouble()-.5D)/2D,
						z+(worldObj.rand.nextDouble()-.5D)/2D
						);
				if(particleTicks%4==0)
					Botania.proxy.lightningFX(worldObj, start, end, 1, colorsfx2, colorsfx2);
			}
		}
	}

	List<EntityItem> getItems() {
		return worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
	}

	boolean areItemsValid(List<EntityItem> items) {
		if(items.size() != 3)
			return false;

		ItemStack enchanted = null;
		ItemStack target = null;
		ItemStack offering = null;
		for(EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			if(stack.stackSize != 1)
				return false;
			if(target==null && isTarget(stack)) {
				target = stack;
				this.target=item;
			}
			else if(enchanted == null && isEnchantedItem(stack)) {
				enchanted = stack;
				this.enchanted=item;
			}
			else if(offering==null && isOffering(stack)) {
				offering = stack;
				this.offering=item;
			}
			else
				return false;
		}

		return enchanted != null && target != null && offering != null && doesTargetMatch(enchanted, target);
	}
	
	boolean isEnchantedItem(ItemStack stack) {
		return stack.isItemEnchanted();
	}
	
	boolean isTarget(ItemStack stack) {
		return stack.getItem() instanceof ItemEnchantHolder;
	}
	
	boolean isOffering(ItemStack stack) {
		return stack.getItem()==ModItems.simpleResource && stack.getItemDamage() == 4;
	}
	
	boolean doesTargetMatch(ItemStack enchanted, ItemStack target) {
		return EnchantmentMoverHandler.canApplyToStack(target,enchanted);
	}
	
	boolean hasValidPlatform() {
		return checkAll(STONE_BLOCKS, ModBlocks.paradoxStone, 0) && checkAll(PATTERNED_BLOCKS, ModBlocks.paradoxWood, 4);
	}

	boolean checkAll(int[][] positions, Block block, int metadata) {
		for (int[] position : positions) {
			int[] positions_ = position;
			if(!checkPlatform(positions_[0], positions_[1], block, metadata))
				return false;
		}

		return true;
	}

	boolean checkPlatform(int xOff, int zOff, Block block, int metadata) {
		return worldObj.getBlock(xCoord + xOff, yCoord - 1, zOff + zCoord) == block && worldObj.getBlockMetadata(xCoord + xOff, yCoord - 1, zOff + zCoord) == metadata;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_PROGRESS, progress);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		progress = cmp.getInteger(TAG_PROGRESS);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.max(0, Math.min(MAX_MANA, this.mana + mana));
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return acting;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		// NO-OP
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return !acting;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, MAX_MANA - getCurrentMana());
	}

}