package soundlogic.silva.common.core.handler.portal.fate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import soundlogic.silva.common.core.helper.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class VigridrFateFireBlade extends VigridrFateGenericRepeating {
	
	private static final String TAG_MIN_LENGTH="minLength";
	private static final String TAG_MAX_LENGTH="maxLength";
	private static final String TAG_MIN_WIDTH="minWidth";
	private static final String TAG_MAX_WIDTH="maxWidth";
	private static final String TAG_MIN_DISTANCE="minDistance";
	private static final String TAG_MAX_DISTANCE="maxDistance";

	private static final int PENETRATION_DEPTH = 8;
	
	float minLength;
	float maxLength;
	float minWidth;
	float maxWidth;
	float minDistance;
	float maxDistance;
	
	float[][] floatHeightMap;
	int[][] heightMap;
	int offsetX;
	int offsetZ;
	float averageHeight;
	ArrayList<Entity> hitBySword = new ArrayList<Entity>();
	
	public VigridrFateFireBlade() {
		
	}
	
	public VigridrFateFireBlade(float minLength, float maxLength, float minWidth, float maxWidth, float minDistance, float maxDistance, int minTicksBeforeStart, float startChance, float doChance, int minTicksBetweenDoing, int weight) {
		super(1, minTicksBeforeStart, startChance, doChance, minTicksBetweenDoing, weight);
		this.minLength=minLength;
		this.maxLength=maxLength;
		this.minWidth=minWidth;
		this.maxWidth=maxWidth;
		this.minDistance=minDistance;
		this.maxDistance=maxDistance;
	}
	
	@Override
	public boolean canApplyToEntity(Entity entity) {
		return entity instanceof EntityLivingBase;
	}
	
	@Override
	public boolean canApplyPeaceful(Entity entity) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
		cmp.setFloat(TAG_MIN_LENGTH, minLength);
		cmp.setFloat(TAG_MAX_LENGTH, maxLength);
		cmp.setFloat(TAG_MIN_WIDTH, minWidth);
		cmp.setFloat(TAG_MAX_WIDTH, maxWidth);
		cmp.setFloat(TAG_MIN_DISTANCE, minDistance);
		cmp.setFloat(TAG_MAX_DISTANCE, maxDistance);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
		minLength=cmp.getFloat(TAG_MIN_LENGTH);
		maxLength=cmp.getFloat(TAG_MAX_LENGTH);
		minWidth=cmp.getFloat(TAG_MIN_WIDTH);
		maxWidth=cmp.getFloat(TAG_MAX_WIDTH);
		minDistance=cmp.getFloat(TAG_MIN_DISTANCE);
		maxDistance=cmp.getFloat(TAG_MAX_DISTANCE);
	}

	@Override
	public boolean doFate() {
		World world = entity.worldObj;
		if(world.isRemote)
			return false;
		int x = (int) entity.posX;
		int y = (int) entity.boundingBox.minY;
		int z = (int) entity.posZ;
		if(world.isAirBlock(x, y, z) && world.isAirBlock(x, y-1, z) && world.isAirBlock(x, y-2, z))
			return false;
		if(!isBlockIsExposed(world,x,y+PENETRATION_DEPTH,z) || !mightApplyToBlock(world,x,y,z) || !mightApplyToBlock(world,x,y-1,z))
			return false;
		float angle = (float) (Math.random()*2F*Math.PI);
		float length = (float) (minLength + (maxLength-minLength)*Math.random());
		float width = (float) (minWidth + (maxWidth-minWidth)*Math.random());
		float offsetAng = (float) (Math.random()*2F*Math.PI);
		float distance = (float) (minDistance + (maxDistance-minDistance)*Math.random());
		
		x+=Math.sin(offsetAng)*distance;
		z+=Math.cos(offsetAng)*distance;
		
		createInitialHeightMap(world, x, y, z, angle, length, width);
		modifyHeightMap(world);
		finalizeHeightMap();
		applyHeightMap(world);
		damageEntities(angle);
		
		return true;
	}
	
	private void damageEntities(float angle) {
		Set<Entity> ents =  ImmutableSet.copyOf(hitBySword);
		for(Entity entity : ents) {
			entity.setFire(20*30);
			entity.attackEntityFrom(DamageSource.magic, 7);
			entity.attackEntityFrom(DamageSource.generic, 20);
			entity.attackEntityFrom(DamageSource.inFire, 5);
			entity.attackEntityFrom(DamageSource.onFire, 5);
			double knockbackRes = 0;
			if(entity instanceof EntityLivingBase) {
				knockbackRes = ((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
			}
			entity.moveEntity(0, 3, 0);
			entity.addVelocity(
					( 2F * 9.2F / (2F+knockbackRes) ) * -Math.cos(angle), 
					( 2F * 1.8F / (2F+knockbackRes) ),
					( 2F * 9.2F / (2F+knockbackRes) ) * Math.sin(angle));
			entity.velocityChanged=true;
		}
	}

	private void finalizeHeightMap() {
		heightMap = new int[floatHeightMap.length][floatHeightMap.length];
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				heightMap[i][j] = (int) floatHeightMap[i][j];
			}
		}
	}

	public void createInitialHeightMap(World world, int x, int y, int z, float angle, float length, float width) {
		int mid = ((int) Math.ceil(maxLength+1))/2;
		floatHeightMap = new float[mid*2+1][mid*2+1];
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				int dx = mid-i;
				int dz = mid-j;
				double rx = dx*cos-dz*sin;
				double rz = dx*sin+dz*cos;
				double vx = Math.abs(rx)*2;
				double vz = Math.abs(rz)*2;
				if(vx<=length && vz<=width)
					floatHeightMap[i][j]=y-1;
				else
					floatHeightMap[i][j]=-1;
			}
		}
		offsetX = x - mid;
		offsetZ = z - mid;
	}
	
	public void modifyHeightMap(World world) {
		fuzzHeightMap(1.5F);
		fuzzHeightMapDelta(.1F, .8F);
		updateAverageHeight();
		adjustToAverageByWorld(world);
		fuzzHeightMapDelta(0F, -.3F);
	}
	
	private void adjustToAverageByWorld(World world) {
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				float height = getFloatHeightMapValue(i,j);
				if(height==-1)
					continue;
				int[] coords = convertCoordsFloatMap(i,j);
				int x = coords[0];
				int y = coords[1];
				int z = coords[2];
				int curY = y;
				int blocks = (int) (curY - averageHeight);
				int resistance = 0;
				while(curY > averageHeight) {
					if(world.isAirBlock(x, curY, z))
						continue;
					float hardness = world.getBlock(x, curY, z).getBlockHardness(world, x, curY, z);
					if(hardness==-1)
						resistance+=50;
					if(mightApplyToBlock(world, x,curY,z))
						resistance+=hardness/3F;
					else
						resistance+=hardness;
					if(resistance>60)
						break;
				}
				setFloatHeightMapValue(i,j,(height+curY)/2F);
			}
		}
	}

	private void updateAverageHeight() {
		averageHeight = 0;
		int totalBlocks = 0;
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				float height = getFloatHeightMapValue(i,j);
				if(height!=-1) {
					averageHeight+=height;
					totalBlocks++;
				}
			}
		}
		averageHeight=averageHeight/totalBlocks;
	}

	public void fuzzHeightMap(float amount) {
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				float height = getFloatHeightMapValue(i,j);
				if(height!=-1)
					setFloatHeightMapValue(i,j,(float) (height+amount*random.nextGaussian()));
			}
		}
	}
	
	public void fuzzHeightMapDelta(float addBlocksChance, float deltaMultiplier) {
		float[][] heightMapDiff0=getHeightMapDelta(0,1);
		float[][] heightMapDiff1=getHeightMapDelta(0,-1);
		float[][] heightMapDiff2=getHeightMapDelta(1,0);
		float[][] heightMapDiff3=getHeightMapDelta(-1,0);
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				float height = getFloatHeightMapValue(i,j);
				if(height==-1 && random.nextFloat()<addBlocksChance)
					height = getAdjacentHeight(i,j);
				if(height==-1)
					continue;
				float diff0 = heightMapDiff0[i][j];
				float diff1 = heightMapDiff1[i][j];
				float diff2 = heightMapDiff2[i][j];
				float diff3 = heightMapDiff3[i][j];
				if(diff0==Integer.MAX_VALUE && diff1==Integer.MAX_VALUE && diff2==Integer.MAX_VALUE && diff3==Integer.MAX_VALUE)
					continue;
				float diff = getRandomFuzzedDiff(0,diff0,diff1,diff2,diff3);
				setFloatHeightMapValue(i,j,height+diff*deltaMultiplier);
			}
		}
	}
	
	private float getAdjacentHeight(int i, int j) {
		float[] adjacent = new float[8];
		boolean valid = false;
		adjacent[0]=getFloatHeightMapValue(i,j+1);
		adjacent[1]=getFloatHeightMapValue(i,j-1);
		adjacent[2]=getFloatHeightMapValue(i+1,j);
		adjacent[3]=getFloatHeightMapValue(i-1,j);
		adjacent[4]=getFloatHeightMapValue(i+1,j+1);
		adjacent[5]=getFloatHeightMapValue(i-1,j+1);
		adjacent[6]=getFloatHeightMapValue(i+1,j-1);
		adjacent[7]=getFloatHeightMapValue(i-1,j-1);
		valid = valid || adjacent[0]!=-1;
		valid = valid || adjacent[1]!=-1;
		valid = valid || adjacent[2]!=-1;
		valid = valid || adjacent[3]!=-1;
		valid = valid || adjacent[4]!=-1;
		valid = valid || adjacent[5]!=-1;
		valid = valid || adjacent[6]!=-1;
		valid = valid || adjacent[7]!=-1;
		if(!valid)
			return -1;
		return chooseRandomAdjacentHeight(adjacent, false);
	}
	
	private float chooseRandomAdjacentHeight(float[] adjacent, boolean searchOuter) {
		if(!searchOuter && random.nextDouble()<.3)
			return chooseRandomAdjacentHeight(adjacent, true);
		if(searchOuter && random.nextDouble()<.3)
			return chooseRandomAdjacentHeight(adjacent, false);
		float curVal = adjacent[random.nextInt(4) + (searchOuter ? 4 : 0)];
		if(curVal==-1)
			return chooseRandomAdjacentHeight(adjacent,searchOuter);
		return curVal;
	}

	private float getRandomFuzzedDiff(float... diffs) {
		float[] potentialDiffs = new float[diffs.length];
		int j = 0;
		for(int i = 0 ; i < diffs.length ; i++) {
			if(random.nextBoolean()) {
				if(diffs[i]==Integer.MAX_VALUE)
					return getRandomFuzzedDiff(diffs);
				potentialDiffs[j]=diffs[i];
				j++;
			}
		}
		float[] useDiffs = new float[j];
		for(int i = 0 ; i < useDiffs.length ; i ++) {
			useDiffs[i]=potentialDiffs[i];
		}
		return randomInterpolate(useDiffs);
	}
	
	private float randomInterpolate(float... f) {
		float r = 0;
		float t = 0;
		for(int i = 0 ; i < f.length ; i++) {
			float v=random.nextFloat();
			r+=f[i]*v;
			t+=v;
		}
		return r/t;
	}
	
	private float[][] getHeightMapDelta(int dx, int dz) {
		float[][] curMap = new float[floatHeightMap.length][floatHeightMap.length];
		for(int i = 0 ; i < floatHeightMap.length ; i++) {
			for(int j = 0 ; j < floatHeightMap.length ; j++) {
				float height = getFloatHeightMapValue(i,j);
				float otherHeight = getFloatHeightMapValue(i+dx,j+dz);
				if(height==-1 || otherHeight == -1)
					curMap[i][j]=Integer.MAX_VALUE;
				else
					curMap[i][j]=otherHeight-height;
			}
		}
		return curMap;
	}
	
	private float getFloatHeightMapValue(int x, int z) {
		if(x>0 && z>0 && x<floatHeightMap.length && z<floatHeightMap.length)
			return floatHeightMap[x][z];
		return -1;
	}
	
	private void setFloatHeightMapValue(int x, int z, float height) {
		if(x>0 && z>0 && x<floatHeightMap.length && z<floatHeightMap.length)
			floatHeightMap[x][z]=Math.max(0F, height);
	}
	
	public void applyHeightMap(World world) {
		hitBySword.clear();
		for(int i = 0 ; i < heightMap.length ; i++) {
			for(int j = 0 ; j < heightMap.length ; j++) {
				int[] coords = convertCoords(i,j);
				int x = coords[0];
				int y = coords[1];
				int z = coords[2];
				applyChange(world, x, y, z);
			}
		}
	}
	
	public int[] convertCoords(int i, int j) {
		return new int[] {
			i + offsetX,
			heightMap[i][j],
			j + offsetZ
		};
	}
	public int[] convertCoordsFloatMap(int i, int j) {
		return new int[] {
			i + offsetX,
			(int) floatHeightMap[i][j],
			j + offsetZ
		};
	}
	
	protected void applyChange(World world, int x, int finalY, int z) {
		
		if(finalY==-1)
			return;
		
		boolean removedPreviousBlock = false;
		int y;
		int lowestRemovedFullBlock = -1;
		for(y = finalY + PENETRATION_DEPTH; y >= finalY ; y--) {
			if(!removedPreviousBlock && !isBlockIsExposed(world,x,y,z))
				break;
			if(mightApplyToBlock(world,x,y,z)) {
				removedPreviousBlock=true;
				Block block = world.getBlock(x, y, z);
				if(block.isNormalCube(world, x, y, z))
					lowestRemovedFullBlock=y;
				world.setBlockToAir(x, y, z);
			}
		}
		
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x-1, y-1, z-1, x+2, world.provider.getHeight()+4, z+2);
		List<Entity> inBounds = world.getEntitiesWithinAABB(Entity.class, bounds);
		hitBySword.addAll(inBounds);
		
		y = lowestRemovedFullBlock;
		if(y==-1)
			return;
		if((y-finalY)<4) {
			while(!world.isAirBlock(x, y, z) && y <= finalY + PENETRATION_DEPTH) {
				y++;
			}
			if(y>finalY + PENETRATION_DEPTH)
				return;
			world.setBlock(x, y, z, Blocks.netherrack, 0, 3);
			if(world.isAirBlock(x, y+1, z) && random.nextFloat()<.3F)
				world.setBlock(x, y+1, z, Blocks.fire, 0, 3);
		}
	}

	public boolean mightApplyToBlock(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block.getBlockHardness(world, x, y, z)<0)
			return false;
		return WorldHelper.isBlockTerraformable(world, x, y, z) || block.isReplaceable(world, x, y, z) || WorldHelper.canWaterDisplaceBlock(world, x, y, z) || block.isFlammable(world, x, y, z, ForgeDirection.UP);
	}

	private boolean isBlockIsExposed(World world, int x, int y, int z) {
		if((WorldHelper.isBlockRainExposed(world, x, y, z) || WorldHelper.isBlockWaterFallExposed(world, x, y, z)))
			return true;
		return false;
	}
	
}
