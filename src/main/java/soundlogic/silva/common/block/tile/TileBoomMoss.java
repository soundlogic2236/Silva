package soundlogic.silva.common.block.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import soundlogic.silva.common.block.BlockBoomMoss;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.BlockDropsHandler;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.core.helper.Vector3;

public class TileBoomMoss extends TileMod implements IManaReceiver{

	private static final String TAG_MANA = "mana";
	private static final String TAG_COUNTDOWN = "countdown";
	private static final String TAG_GENERATION = "generation";
	private static final String TAG_DROP_X = "dropX";
	private static final String TAG_DROP_Y = "dropY";
	private static final String TAG_DROP_Z = "dropZ";
	private static final int MAX_MANA = 800;
	private static final int MANA_PER_MOSS = 10;
	private static final int MANA_THRESHOLD = 5;
	private static final int TIMER = 2;
	private static final int[][] spreadDirections = new int[][]{
		{ 1, 0, 0},
		{-1, 0, 0},
		{ 0, 0, 1},
		{ 0, 0,-1},
		{ 1, 1, 0},
		{-1, 1, 0},
		{ 0, 1, 1},
		{ 0, 1,-1},
		{ 1,-1, 0},
		{-1,-1, 0},
		{ 0,-1, 1},
		{ 0,-1,-1},
		{ 0, 1, 0},
		{ 0,-1, 0},
	};
	
	int mana = 0;
	int countdown = -1;
	int generation = 0;
	double dropX = 0;
	double dropY = 0;
	double dropZ = 0;
	
	Random random;
	
	public TileBoomMoss() {
		super();
		random = new Random();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	@Override
	public boolean isFull() {
		return mana>=MAX_MANA;
	}

	@Override
	public void recieveMana(int arg0) {
		mana=Math.min(mana+arg0, MAX_MANA);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_COUNTDOWN, countdown);
		cmp.setInteger(TAG_GENERATION, generation);
		cmp.setDouble(TAG_DROP_X, dropX);
		cmp.setDouble(TAG_DROP_Y, dropY);
		cmp.setDouble(TAG_DROP_Z, dropZ);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		mana = cmp.getInteger(TAG_MANA);
		countdown = cmp.getInteger(TAG_COUNTDOWN);
		generation = cmp.getInteger(TAG_GENERATION);
		dropX = cmp.getDouble(TAG_DROP_X);
		dropY = cmp.getDouble(TAG_DROP_Y);
		dropZ = cmp.getDouble(TAG_DROP_Z);
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote)
			return;
		if(countdown == -1) {
			if(mana < MANA_THRESHOLD)
				return;
			
			int[] offset=spreadDirections[random.nextInt(spreadDirections.length)];
			int x=this.xCoord+offset[0];
			int y=this.yCoord+offset[1];
			int z=this.zCoord+offset[2];
			applyMana(x,y,z);
		}
		else if(countdown > 0) {
			countdown--;
		}
		else {
			detonate();
		}
		
	}
	
	private void applyMana(int x,int y, int z) {
		TileEntity tile=worldObj.getTileEntity(x,y,z);
		Block block=worldObj.getBlock(x,y,z);
		if(tile instanceof TileBoomMoss) {
			shareMana((TileBoomMoss)tile);
			return;
		}
		if(canPutMossAt(x,y,z) && mana >= MANA_PER_MOSS) {
			worldObj.setBlockToAir(x, y, z);
			worldObj.setBlock(x, y, z, ModBlocks.boomMoss,worldObj.getBlockMetadata(xCoord, yCoord, zCoord),3);
			mana-=MANA_PER_MOSS;
		}
	}
	
	private void shareMana(TileBoomMoss target) {
		int otherMana=target.mana;
		if(otherMana>mana)
			return;
		int totalMana=otherMana+mana;
		if(totalMana<60) {
			mana=0;
			target.mana=totalMana;
			worldObj.markBlockForUpdate(target.xCoord, target.yCoord, target.zCoord);
			return;
		}
		mana=totalMana/3-20;
		target.mana=totalMana*2/3+20;
		worldObj.markBlockForUpdate(target.xCoord, target.yCoord, target.zCoord);
	}
	
	private boolean canPutMossAt(int x,int y, int z) {
		Block block=worldObj.getBlock(x,y,z);
		if(block==null || block.canBeReplacedByLeaves(worldObj, x, y, z) || block.isReplaceable(worldObj, x, y, z) || block.isAir(worldObj, x, y, z)) {
			return ModBlocks.boomMoss.canPlaceBlockAt(worldObj, x, y, z);
		}
		return false;
	}
	
	public void detonate(double x,double y, double z) {
		this.dropX=x;
		this.dropY=y;
		this.dropZ=z;
		detonate();
	}
	
	public void detonate() {
		if(worldObj.isRemote)
			return;
		boolean upgraded = worldObj.getBlockMetadata(xCoord,yCoord,zCoord)==1;
		boolean dry = worldObj.getBlockMetadata(xCoord,yCoord,zCoord)==2;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileAt = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(!(tileAt instanceof TileBoomMoss)) {
				int targetX=xCoord + dir.offsetX;
				int targetY=yCoord + dir.offsetY;
				int targetZ=zCoord + dir.offsetZ;
				Block block = worldObj.getBlock(targetX,targetY,targetZ);
				int metadata = worldObj.getBlockMetadata(targetX,targetY,targetZ);
				float hardness = block.getBlockHardness(worldObj, targetX,targetY,targetZ);
				
				if(block!=null && hardness != -1 && hardness < 70F) {
					List<ItemStack> items = new ArrayList();
					items.addAll(breakBlockForStacks(targetX,targetY,targetZ,upgraded));
					float dropX=targetX+.5F;
					float dropY=targetY+.5F;
					float dropZ=targetZ+.5F;
					if(upgraded) {
						dropX=(float) (this.dropX+.5F);
						dropY=(float) (this.dropY+.5F);
						dropZ=(float) (this.dropZ+.5F);
					}
					for(ItemStack stack_ : items) {
						if(!dry)
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, dropX, dropY, dropZ , stack_));
					}
					items.clear();
				}
			}
		}
		for(int[] offset : spreadDirections) {
			TileEntity tileAt = worldObj.getTileEntity(xCoord + offset[0], yCoord + offset[1], zCoord + offset[2]);
			if(tileAt instanceof TileBoomMoss) {
				if(((TileBoomMoss) tileAt).countdown==-1) {
					((TileBoomMoss) tileAt).countdown=(int) (TIMER + generation/5 + Math.random() * 3);
					((TileBoomMoss) tileAt).generation=generation+1;
					((TileBoomMoss) tileAt).dropX=this.dropX;
					((TileBoomMoss) tileAt).dropY=this.dropY;
					((TileBoomMoss) tileAt).dropZ=this.dropZ;
				}
			}
		}
		worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		
	}
	
	private List<ItemStack> breakBlockForStacks(int x, int y, int z, boolean silk) {
		ItemStack tool=new ItemStack(Items.diamond_pickaxe);
		if(silk)
			tool.addEnchantment(Enchantment.silkTouch, 1);
		return BlockDropsHandler.breakBlockWithDrops(worldObj, x, y, z, tool);
	}

	
	public boolean canAttachToSide(ForgeDirection direction) {
		int newX=xCoord+direction.offsetX;
		int newY=yCoord+direction.offsetY;
		int newZ=zCoord+direction.offsetZ;
		Block block=worldObj.getBlock(newX,newY,newZ);
		return block.isNormalCube(worldObj, newX, newY, newZ);
	}
	
	public int getMaxMana() {
		return MAX_MANA;
	}
	public int getCostMana() {
		return MANA_PER_MOSS;
	}

}
