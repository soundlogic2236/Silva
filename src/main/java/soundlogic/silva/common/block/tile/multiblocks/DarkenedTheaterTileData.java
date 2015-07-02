package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater.ActorPixieData;
import soundlogic.silva.common.block.tile.multiblocks.darkenedtheater.IDarkenedTheaterPlay;
import soundlogic.silva.common.entity.EntityPixieProxy;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieGroupHandler;
import net.minecraft.nbt.NBTTagCompound;

public class DarkenedTheaterTileData extends PixiePowerSimpleTileData {
	
	private static final String TAG_FLOOR_BRIGHT_TICKS="floorBrightTicks";
	private static final String TAG_CURRENT_PLAY="currentPlay";
	private static final String TAG_CURRENT_PLAY_PROGRESS="progress";
	private static final String TAG_SELECTING_PLAY="selectingPlay";
	private static final String TAG_PLAY_SELECTION_PAGE="selectionPage";
	

	int floorBrightTicks=0;
	String currentPlayString = "";
	int currentPlayProgress = 0;
	
	boolean selectingPlay = false;
	int selectionPage = 0;
	
	IDarkenedTheaterPlay currentPlay;
	
	boolean allRequiredPositionsFilled = false;
	List<String> requiredPositions = new ArrayList<String>();
	boolean requiredPositionsDirty = true;
	
	int ticksWithoutPlayer = 0;
	
	List<String> missingPositions = new ArrayList<String>();
	boolean missingPositionsDirty = true;
	
	public PixieGroupHandler<ActorPixieData> pixieGroup = new PixieGroupHandler() {

		@Override
		protected ActorPixieData create() {
			return new ActorPixieData(this);
		}
		
	};
	public boolean decreasePageTriggered = false;
	public boolean increasePageTriggered = false;
	public int preSelectedPlay = -1;
	public int selectionCount = -1;
	

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		pixieGroup.writeToNBT(cmp);
		cmp.setInteger(TAG_FLOOR_BRIGHT_TICKS, floorBrightTicks);
		cmp.setString(TAG_CURRENT_PLAY, currentPlayString);
		cmp.setInteger(TAG_CURRENT_PLAY_PROGRESS, currentPlayProgress);
		cmp.setBoolean(TAG_SELECTING_PLAY, selectingPlay);
		cmp.setInteger(TAG_PLAY_SELECTION_PAGE, selectionPage);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		pixieGroup.readFromNBT(cmp);
		floorBrightTicks = cmp.getInteger(TAG_FLOOR_BRIGHT_TICKS);
		currentPlayString = cmp.getString(TAG_CURRENT_PLAY);
		currentPlayProgress = cmp.getInteger(TAG_CURRENT_PLAY_PROGRESS);
		if(currentPlayString.equals(""))
			currentPlay=null;
		else
			currentPlay=MultiblockDataPixieRoomDarkenedTheater.stringToPlay.get(currentPlayString);
		selectingPlay=cmp.getBoolean(TAG_SELECTING_PLAY);
		selectionPage=cmp.getInteger(TAG_PLAY_SELECTION_PAGE);
	}
	
	public String getPlay() {
		return currentPlayString;
	}
	public int getProgress() {
		return currentPlayProgress;
	}
	
	public void setPlay(String play) {
		this.currentPlayString=play;
		if(currentPlayString.equals(""))
			currentPlay=null;
		else
			currentPlay=MultiblockDataPixieRoomDarkenedTheater.stringToPlay.get(currentPlayString);
		currentPlayProgress=0;
		this.allRequiredPositionsFilled=false;
		this.missingPositionsDirty=true;
		this.requiredPositionsDirty=true;
	}

}
