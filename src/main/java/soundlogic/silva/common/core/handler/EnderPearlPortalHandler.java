package soundlogic.silva.common.core.handler;

import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import soundlogic.silva.common.crafting.recipe.RecipeElvenInstantDrop;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.core.helper.Vector3;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnderPearlPortalHandler {

	private final String TAG_INSTANT_DROP = "instantDropItemStack";
	
	ItemStack taggedPages;
	
	public EnderPearlPortalHandler() {
		taggedPages=LexiconData.dimensionalPapers.copy();
		NBTTagCompound cmp = new NBTTagCompound();
		if(taggedPages.hasTagCompound())
			cmp=taggedPages.getTagCompound();
		cmp.setBoolean(TAG_INSTANT_DROP, true);

		BotaniaAPI.elvenTradeRecipes.add(new RecipeElvenInstantDrop(LexiconData.dimensionalPapers.copy(),taggedPages.copy()));
	}
	
	@SubscribeEvent
	public void onPortalTick(ElvenPortalUpdateEvent event) {
		if(!event.open)
			return;
		if(event.portalTile.getWorldObj().isRemote)
			return;

		List<EntityEnderPearl> pearls=event.portalTile.getWorldObj().getEntitiesWithinAABB(EntityEnderPearl.class, event.aabb);
		if(pearls.size()!=0) {
			TileAlfPortal tile=(TileAlfPortal) event.portalTile;
			pearls.get(0).setDead();
			double x=pearls.get(0).posX;
			double y=pearls.get(0).posY;
			double z=pearls.get(0).posZ;
			if(tile.getBlockMetadata() == 1)
				x=(double)tile.xCoord+.5;
			else
				z=(double)tile.zCoord+.5;
			doOverloadEffect(tile.getWorldObj(), tile,x,y,z, tile.getBlockMetadata() == 1);
			tile.ticksOpen=0;
			event.stacksInside.add(taggedPages.copy());
			tile.getWorldObj().setBlockMetadataWithNotify(tile.xCoord,tile.yCoord,tile.zCoord, 0, 3);
		}
	}

	private void doOverloadEffect(World worldObj, TileAlfPortal tile, double x, double y, double z, boolean onX) {
		Vector3[] edgePoints = new Vector3[4];
		Vector3 pearlPoint=new Vector3(x,y,z);
		edgePoints[0]=new Vector3(tile.xCoord,tile.yCoord,tile.zCoord);
		edgePoints[1]=new Vector3(tile.xCoord,tile.yCoord+4,tile.zCoord);
		edgePoints[2]=new Vector3(tile.xCoord + (onX ? 2 : 0),tile.yCoord+2,tile.zCoord + (onX ? 0 : 2));
		edgePoints[3]=new Vector3(tile.xCoord + (onX ? -2 : 0),tile.yCoord+2,tile.zCoord + (onX ? 0 : -2));

		for(Vector3 point : edgePoints) {
			for(int i=1;i<5;i++) {
				LightningHandler.spawnLightningBolt(worldObj, pearlPoint, point, i*10, worldObj.rand.nextLong(), 0x8f2489, 0xbc60f6);
				LightningHandler.spawnLightningBolt(worldObj, point, pearlPoint, i*10, worldObj.rand.nextLong(), 0x8f2489, 0xbc60f6);
			}
			
		}
        for (int i = 0; i < 64; ++i)
        {
            worldObj.spawnParticle("portal", x, y + worldObj.rand.nextDouble() * 2.0D, z, worldObj.rand.nextGaussian(), 0.0D, worldObj.rand.nextGaussian());
        }
        
        worldObj.addWeatherEffect(new EntityLightningBolt(worldObj,(double)tile.xCoord+.5,(double)tile.yCoord+1,(double)tile.zCoord+.5));
	}
}
