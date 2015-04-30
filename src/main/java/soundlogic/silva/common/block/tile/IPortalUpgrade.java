package soundlogic.silva.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPortalUpgrade {

	public boolean isPortalUpgrade();
	public boolean permitBlockExposureTicks();
	public boolean permitEntityExposureTicks();
	public boolean permitTrades();
	public boolean permitItemsThroughPortal();
	public void onTick();
	public void onItemThroughPortal(ItemStack stack);
	public void setPortalCore(TilePortalCore core);
}
