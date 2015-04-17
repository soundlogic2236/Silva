package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockDropsHandler {

	public boolean harvesting;
	public List<ItemStack> harvestResults;
	private FakePlayer fakePlayer;
	private static BlockDropsHandler instance;
	
	public BlockDropsHandler() {
		instance=this;
	}
	
	public static List<ItemStack> breakBlockWithDrops(World world, int x, int y, int z, ItemStack tool) {
		if(world.isRemote)
			return new ArrayList();
		if(instance.fakePlayer==null)
			instance.fakePlayer = FakePlayerFactory.get((WorldServer) world, new GameProfile(UUID.fromString("12b67204-bc51-4a62-a1f0-01e7cdc47abe"), "[Silva]"));

		instance.harvesting=true;
		instance.fakePlayer.worldObj=world;
		instance.fakePlayer.setCurrentItemOrArmor(0, tool);
		Block block=world.getBlock(x, y, z);
		int metadata=world.getBlockMetadata(x, y, z);
		block.onBlockHarvested(world, x, y, z, metadata, instance.fakePlayer);
		block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
		block.harvestBlock(world, instance.fakePlayer, x, y, z, metadata);
		world.setBlockToAir(x, y, z);
		instance.harvesting=false;
		instance.fakePlayer.worldObj=null;
		List<ItemStack> output=new ArrayList(instance.harvestResults);
		instance.harvestResults.clear();
		return output;
	}
	
	@SubscribeEvent
	public void HarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(event.world.isRemote)
			return;
		if(!harvesting)
			return;
		this.harvestResults=new ArrayList(event.drops);
		event.drops.clear();
	}
}
