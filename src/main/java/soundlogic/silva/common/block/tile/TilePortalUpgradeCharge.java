package soundlogic.silva.common.block.tile;

import java.util.HashMap;
import java.util.Random;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import soundlogic.silva.common.item.ItemChargedStone;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.item.ItemStack;

public class TilePortalUpgradeCharge extends TileSimpleInventory implements IPortalUpgrade{

	public TilePortalCore core;
	public Random random = new Random();
	
	@Override
	public boolean isPortalUpgrade() {
		return true;
	}

	@Override
	public boolean permitBlockExposureTicks() {
		return true;
	}

	@Override
	public boolean permitEntityExposureTicks() {
		return true;
	}

	@Override
	public boolean permitTrades() {
		return true;
	}

	@Override
	public boolean permitItemsThroughPortal() {
		return true;
	}

	@Override
	public void onTick() {
		if(core.getDimension()==null)
			return;
		ItemStack stack = this.getStackInSlot(0);
		if(stack==null)
			return;
		if(core.getDimension().getState() == State.LOCKED)
			return;
		HashMap<Dimension,Integer> map = ItemChargedStone.getDimensionLevels(stack);
		int level = 0;
		if(map.containsKey(core.getDimension()))
				level = map.get(core.getDimension());
		level = Math.min(level + 4, 60*20*10);
		map.put(core.getDimension(), level);
		this.setInventorySlotContents(0, ItemChargedStone.makeFromLevels(map));
		this.markDirty();

		int meta=this.getBlockMetadata();
		float offsetX=0;
		float offsetY=0;
		float offsetZ=0;
		
		switch(meta) {
		case 0:
			offsetY=-.25F;
			break;
		case 1:
			offsetY=.25F;
			break;
		case 2:
			offsetZ=-.25F;
			break;
		case 3:
			offsetZ=.25F;
			break;
		case 4:
			offsetX=-.25F;
			break;
		case 5:
			offsetX=.25F;
			break;
		}

		if(level/4 % 10 == 0) {
			int col = core.getDimension().getPortalColor().getRGB();
			Vector3 entityPoint=new Vector3(xCoord+.5f+offsetX,yCoord+.5f+offsetY,zCoord+.5f+offsetZ);
			Vector3 point=new Vector3(
					entityPoint.x + random.nextDouble()/2 - .25,
					entityPoint.y + random.nextDouble()/2 - .25,
					entityPoint.z + random.nextDouble()/2 - .25);
			LightningHandler.spawnLightningBolt(getWorldObj(), entityPoint, point, 2, random.nextLong(), col, col);
		}
	}

	@Override
	public void onItemThroughPortal(ItemStack stack) {
		// NO OP
	}

	@Override
	public void setPortalCore(TilePortalCore core) {
		this.core=core;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.PORTAL_UPGRADE_CHARGE;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemChargedStone;
	}
}
