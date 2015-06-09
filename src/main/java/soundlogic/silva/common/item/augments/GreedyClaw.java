package soundlogic.silva.common.item.augments;

import java.util.HashMap;
import java.util.UUID;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

public class GreedyClaw implements ITickingDarkElfAugment {

	private final static int SEARCH_RANGE = 24;
	private final static int SEARCHES_PER_TICK = 1000;
	private final static int WEIGHT_SCALE = 10000000;
	private final static int WEIGHT_OFFSET = 100;
	
	private static HashMap<UUID, Integer> prevValues = new HashMap<UUID, Integer>();
	
	private boolean shouldAttack;
	private int attackX;
	private int attackY;
	private int attackZ;
	
	@Override
	public void onTick(EntityLivingBase entity, ItemStack augmentedStack, int slot) {
		if(entity.worldObj.isRemote)
			return;
		if(!(entity instanceof EntityPlayer))
			return;
		shouldAttack = false;
		EntityPlayer player = (EntityPlayer) entity;
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		int range = SEARCH_RANGE/2;
		for(int i = 0 ; i < SEARCHES_PER_TICK ; i ++) {
			int xp = x - range + entity.worldObj.rand.nextInt(range*2);
			int yp = y - range + entity.worldObj.rand.nextInt(range*2);
			int zp = z - range + entity.worldObj.rand.nextInt(range*2);
			scanBlock(player, xp, yp, zp);
		}
		UUID uuid = player.getUniqueID();
		if(prevValues.containsKey(uuid)) {
			int prevValue = prevValues.get(uuid);
			if(prevValue>0)
				prevValues.put(uuid, prevValue-1);
		}
		if(shouldAttack) {
			System.out.println("attacking");
			Vector3 vec_player = new Vector3(
					player.posX,
					player.posY+player.eyeHeight,
					player.posZ);
			Vector3 vec_target = new Vector3(
					attackX+.5,
					attackY+.5,
					attackZ+.5);
			Vector3 diff = vec_target.sub(vec_player);
	        double d3 = (double)MathHelper.sqrt_double(diff.x * diff.x + diff.z * diff.z);

	        player.rotationYaw = (float)(Math.atan2(diff.z, diff.x) * 180.0D / Math.PI) - 90.0F;
	        player.rotationPitch = (float)(-(Math.atan2(diff.y, d3) * 180.0D / Math.PI));
	        
			player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
			player.attackEntityFrom(DamageSource.generic, 2);
			player.hurtTime=player.maxHurtTime=10;
		}
	}

	private void scanBlock(EntityPlayer player, int x, int y, int z) {
		Block block = player.worldObj.getBlock(x, y, z);
		if(block.equals(Blocks.air))
			return;
		ItemStack stack = new ItemStack(block, 1, block.getDamageValue(player.worldObj, x, y, z));
		int[] ores = OreDictionary.getOreIDs(stack);
		if(ores.length==0)
			return;
		int weight = -1;
		for(int ore : ores) {
			String name = OreDictionary.getOreName(ore);
			if(!BotaniaAPI.oreWeights.containsKey(name))
				continue;
			int q = BotaniaAPI.oreWeights.get(name);
			if(q>0) {
				weight = q;
				break;
			}
		}
		if(weight>0) {
			int value = WEIGHT_SCALE / weight;
			UUID uuid = player.getUniqueID();
			int prevValue = 0;
			if(prevValues.containsKey(uuid))
				prevValue = prevValues.get(uuid);
			if(value>prevValue) {
				shouldAttack=true;
				attackX = x;
				attackY = y;
				attackZ = z;
			}
			if(value+WEIGHT_OFFSET>prevValue) {
				prevValues.put(uuid, value+WEIGHT_OFFSET);
			}
		}
	}

}
