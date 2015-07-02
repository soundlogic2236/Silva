package soundlogic.silva.common.block.tile.multiblocks.darkenedtheater;

import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.DarkenedTheaterTileData;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater.ActorPixieData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import net.minecraft.item.ItemStack;

public interface IDarkenedTheaterPlay {

	ItemStack getEquipmentForPosition(String position);

	ItemStack getHelmetForPosition(String position);
	
	List<String> getRequiredPositionList();

	String getDisplayName();

	String getKey();

	int getPlaywrightLevel();

	boolean canSelect(TileMultiblockCore core, DarkenedTheaterTileData data);

	void setProgress(TileMultiblockCore core, DarkenedTheaterTileData data, int progress);

	void tickPixie(TileMultiblockCore core, DarkenedTheaterTileData data, ActorPixieData pixie, String position);

}
