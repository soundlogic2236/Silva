package soundlogic.silva.common.core.helper;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class WorldHelper {

	static final List<String> terraformBlocks = Arrays.asList(new String[] {
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
	
	public static boolean isBlockTerraformable(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		int[] ids = OreDictionary.getOreIDs(new ItemStack(block, 1, meta));
		for(int id : ids)
			if(terraformBlocks.contains(OreDictionary.getOreName(id))) {
				return true;
			}
		return false;
	}
	
	public static boolean isBlockSkyExposed(World world, int x, int y, int z) {
		return world.canBlockSeeTheSky(x, y, z);
	}
	
	public static boolean isBlockRainExposed(World world, int x, int y, int z) {
		return y>=world.getPrecipitationHeight(x,z);
	}
	
	public static boolean isBlockFallExposed(World world, int x, int y, int z) {
		int curY = getMaxBlockHeightInChunk(world,x,z);
		while(curY>y) {
			if(world.getBlock(x, curY, z).getCollisionBoundingBoxFromPool(world, x, y, z)!=null)
				return false;
		}
		return true;
	}
	
	public static boolean isBlockWaterFallExposed(World world, int x, int y, int z) {
		int curY = getMaxBlockHeightInChunk(world,x,z);
		while(curY>y) {
			if(!canWaterDisplaceBlock(world,x,y,z))
				return false;
		}
		return true;
	}
	
	public static boolean isOnlyAirAbove(World world, int x, int y, int z) {
		int curY = getMaxBlockHeightInChunk(world,x,z);
		while(curY>y) {
			if(world.isAirBlock(x, y, z))
				return false;
		}
		return true;
	}
	
	public static boolean canWaterDisplaceBlock(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        return block != Blocks.wooden_door && block != Blocks.iron_door && block != Blocks.standing_sign && block != Blocks.ladder && block != Blocks.reeds ? (block.getMaterial() == Material.portal ? true : block.getMaterial().blocksMovement()) : true;
	}
	
	public static int getMaxBlockHeightInChunk(World world, int x, int z) {
		return world.getChunkFromBlockCoords(x, z).getTopFilledSegment()+16-1;
	}
	
}
