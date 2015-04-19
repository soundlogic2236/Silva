package soundlogic.silva.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import soundlogic.silva.common.entity.EntityStoneHorse;
import soundlogic.silva.common.lib.LibItemNames;

public class ItemStoneHorse extends ItemMod{

	public ItemStoneHorse() {
		super(LibItemNames.STONE_HORSE);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xs, float ys, float zs) {	
		if(world.isRemote)
			return true;
		EntityStoneHorse horse=new EntityStoneHorse(world);
		horse.setLocationAndAngles(x, y+1, z, player.cameraYaw, player.cameraPitch);
		horse.setTamedBy(player);
		world.spawnEntityInWorld(horse);
		stack.stackSize--;
		return true;
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.entityLiving;
		if(entity.ridingEntity != null && entity.ridingEntity instanceof EntityLivingBase)
			entity = (EntityLivingBase) entity.ridingEntity;

		if(entity instanceof EntityStoneHorse && event.source == DamageSource.fall) {
			EntityStoneHorse horse = (EntityStoneHorse) entity;
			event.setCanceled(true);
		}
	}
}
