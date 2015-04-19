package soundlogic.silva.common.core.handler;

import java.util.List;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import soundlogic.silva.common.crafting.recipe.RecipeElvenInstantDrop;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
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
			pearls.get(0).setDead();
			double x=pearls.get(0).posX;
			double y=pearls.get(0).posY;
			double z=pearls.get(0).posZ;
			NBTTagCompound hackishCompound=new NBTTagCompound();
			event.portalTile.writeToNBT(hackishCompound);
			hackishCompound.setInteger("ticksOpen", 0);
			event.portalTile.readFromNBT(hackishCompound);
			event.stacksInside.add(taggedPages.copy());
			event.portalTile.getWorldObj().setBlockMetadataWithNotify(event.portalTile.xCoord, event.portalTile.yCoord, event.portalTile.zCoord, 0, 3);
		}
	}
}
