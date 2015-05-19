package soundlogic.silva.common.block.subtile.functional;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.DustHandler;
import soundlogic.silva.common.entity.EntityEnderPearlRedirected;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.decor.IFloatingFlower;

public class SubTileBlazeBloom extends SubTileFunctional {

	ForgeDirection[] mainDirs = new ForgeDirection[] {
			ForgeDirection.NORTH,
			ForgeDirection.SOUTH,
			ForgeDirection.EAST,
			ForgeDirection.WEST,
	};

	static final List<String> validBlocks = Arrays.asList(new String[] {
			"stone",
			"dirt",
			"grass",
			"sand",
			"gravel",
			"hardenedClay",
			"snowLayer",
			"mycelium",
			"podzol",
			"sandstone"
	});
	
	private final int maxHeight = 20;
	private final int maxLength = 20;
	
	private final String TAG_HEIGHT = "height";
	private final String TAG_OTHER_X = "otherX";
	private final String TAG_OTHER_Z = "otherZ";

	int height;
	int otherX;
	int otherZ;
	boolean foundShape=false;
	
	int requiredMana = -1;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(supertile.getWorldObj().isRemote)
			return;
		if(!foundShape)
			findShape();
		if(foundShape)
			validateShape();
		if(foundShape) {
			boolean acted = false;
			if(requiredMana == -1)
				updateRequiredMana();
			if(requiredMana != -1 && mana>=requiredMana)
				acted = doRemoveblocks();
			if(acted)
				this.mana=0;
		}
		else {
			requiredMana = -1;
			mana=0;
		}
	}
	
	private boolean doRemoveblocks() {

		int bx = supertile.xCoord;
		int by = supertile.yCoord;
		int bz = supertile.zCoord;
		
		int dx = otherX>supertile.xCoord ? 1 : -1;
		int dy = -1;
		int dz = otherZ>supertile.zCoord ? 1 : -1;
		
		int rx = Math.abs(otherX-supertile.xCoord)+1;
		int ry = height+1;
		int rz = Math.abs(otherZ-supertile.zCoord)+1;
		
		boolean acted = false;
		
		for(int i = 0; i < rx ; i++) {
			for(int j = 0; j < ry ; j++) {
				for(int k = 0; k < rz ; k++) {
					int x = bx + dx * i;
					int y = by + dy * j;
					int z = bz + dz * k;
					if(tryRemoveBlock(x,y,z))
						acted=true;
				}
			}
		}
		if(!acted)
			return false;
		for(int i = 0; i < rx ; i++) {
			for(int j = 0; j < ry ; j++) {
				for(int k = 0; k < rz ; k++) {
					int x = bx + dx * i;
					int y = by + dy * j;
					int z = bz + dz * k;
					tryIgnite(x,y,z);
				}
			}
		}
		return true;
	}

	protected void tryIgnite(int x, int y, int z) {
		if(ModBlocks.blazeFire.canPlaceBlockAt(supertile.getWorldObj(), x, y, z)) {
			supertile.getWorldObj().setBlock(x, y, z, ModBlocks.blazeFire, 0, 3);
		}
	}

	protected boolean tryRemoveBlock(int x, int y, int z) {
		if(canRemoveBlock(x,y,z)) {
			removeBlock(x,y,z);
			return true;
		}
		return false;
	}

	protected void removeBlock(int x, int y, int z) {
        Block block = supertile.getWorldObj().getBlock(x, y, z);
        int l = supertile.getWorldObj().getBlockMetadata(x, y, z);
        block.onBlockDestroyedByPlayer(supertile.getWorldObj(), x, y, z, l);
        supertile.getWorldObj().setBlockToAir(x, y, z);
	}

	protected boolean canRemoveBlock(int x, int y, int z) {
		if(x==supertile.xCoord && y==supertile.yCoord && z==supertile.zCoord)
			return false;
		if(x==supertile.xCoord && y==supertile.yCoord-1 && z==supertile.zCoord)
			if(!(supertile instanceof IFloatingFlower))
				return false;
		Block block = supertile.getWorldObj().getBlock(x, y, z);
		int meta = supertile.getWorldObj().getBlockMetadata(x, y, z);
		if(block.isAir(supertile.getWorldObj(), x, y, z))
			return false;
		if(block == ModBlocks.blazeDust)
			return false;
		if(block.getBlockHardness(supertile.getWorldObj(), x, y, z)<0)
			return false;
		if(block.getMaterial()==Material.fire)
			return false;
		int[] ids = OreDictionary.getOreIDs(new ItemStack(block, 1, meta));
		for(int id : ids)
			if(validBlocks.contains(OreDictionary.getOreName(id))) {
				return true;
			}
		return false;
	}

	@Override
	public int getMaxMana() {
		return Math.max(0, requiredMana);
	}


	protected void updateRequiredMana() {
		requiredMana = (height + Math.abs(otherX - supertile.xCoord) + Math.abs(otherZ - supertile.zCoord)+3)*100;
	}

	protected void validateShape() {
		
		foundShape=false;
		
		int x,y,z;
		
		int dx = otherX>supertile.xCoord ? 1 : -1;
		int dz = otherZ>supertile.zCoord ? 1 : -1;
		
		int rx = Math.abs(otherX-supertile.xCoord);
		int rz = Math.abs(otherZ-supertile.zCoord);
		
		y = supertile.yCoord - height;

		for(int i = 0 ; i < rx ; i++) {
			x = supertile.xCoord+i*dx;
			z = supertile.zCoord;
			if(supertile.getWorldObj().getBlock(x, y, z)!=ModBlocks.blazeDust)
				return;
		}

		for(int i = 0 ; i < rx ; i++) {
			x = supertile.xCoord+i*dx;
			z = otherZ;
			if(supertile.getWorldObj().getBlock(x, y, z)!=ModBlocks.blazeDust)
				return;
		}

		for(int i = 0 ; i < rx ; i++) {
			x = supertile.xCoord;
			z = supertile.zCoord+i*dz;
			if(supertile.getWorldObj().getBlock(x, y, z)!=ModBlocks.blazeDust)
				return;
		}
		
		for(int i = 0 ; i < rx ; i++) {
			x = otherX;
			z = supertile.zCoord+i*dz;
			if(supertile.getWorldObj().getBlock(x, y, z)!=ModBlocks.blazeDust)
				return;
		}
		
		foundShape=true;
	}

	protected void findShape() {
		height = -1;
		
		for(int i = 1 ; i < maxHeight ; i++) {
			int x = supertile.xCoord;
			int y = supertile.yCoord-i;
			int z = supertile.zCoord;
			if(y<1)
				return;
			if(supertile.getWorldObj().getBlock(x, y, z) == ModBlocks.blazeDust) {
				height = i;
				break;
			}
		}
		if(height == -1)
			return;

		ForgeDirection firstDir = null;
		int x = supertile.xCoord;
		int y = supertile.yCoord-height;
		int z = supertile.zCoord;
		for(ForgeDirection dir : mainDirs) {
			if(supertile.getWorldObj().getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ) == ModBlocks.blazeDust) {
				firstDir = dir;
				break;
			}
		}
		if(firstDir == null)
			return;
		
		ForgeDirection secondDir = null;
		
		for(ForgeDirection dir : mainDirs) {
			if(dir.equals(firstDir) || dir.equals(firstDir.getOpposite()))
				continue;
			if(supertile.getWorldObj().getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ) == ModBlocks.blazeDust) {
				secondDir = dir;
				break;
			}
		}
		if(secondDir == null)
			return;
		
		int length1 = 0;
		
		for(int i = 1 ; i < maxLength ; i++) {
			x = supertile.xCoord + firstDir.offsetX * i;
			y = supertile.yCoord-height;
			z = supertile.zCoord + firstDir.offsetZ * i;
			if(supertile.getWorldObj().getBlock(x, y, z) != ModBlocks.blazeDust)
				break;
			if(supertile.getWorldObj().getBlock(x+secondDir.offsetX, y, z+secondDir.offsetZ) == ModBlocks.blazeDust) {
				length1 = i;
				break;
			}
		}
		if(length1==0)
			return;
		
		int length2 = 0;
		
		for(int i = 1 ; i < maxLength ; i++) {
			x = supertile.xCoord + secondDir.offsetX * i;
			y = supertile.yCoord-height;
			z = supertile.zCoord + secondDir.offsetZ * i;
			if(supertile.getWorldObj().getBlock(x, y, z) != ModBlocks.blazeDust)
				break;
			if(supertile.getWorldObj().getBlock(x+firstDir.offsetX, y, z+firstDir.offsetZ) == ModBlocks.blazeDust) {
				length2 = i;
				break;
			}
		}
		
		if(length2==0)
			return;
		
		otherX = length1 * firstDir.offsetX + length2 * secondDir.offsetX + supertile.xCoord;
		otherZ = length1 * firstDir.offsetZ + length2 * secondDir.offsetZ + supertile.zCoord;
		
		foundShape = true;
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		if(foundShape) {
			cmp.setInteger(TAG_HEIGHT, height);
			cmp.setInteger(TAG_OTHER_X, otherX);
			cmp.setInteger(TAG_OTHER_Z, otherZ);
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		foundShape = cmp.hasKey(TAG_HEIGHT);
		if(foundShape) {
			height = cmp.getInteger(TAG_HEIGHT);
			otherX = cmp.getInteger(TAG_OTHER_X);
			otherZ = cmp.getInteger(TAG_OTHER_Z);
		}
	}	
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.blazeBloom;
	}
}
