package soundlogic.silva.common.block.tile.multiblocks.darkenedtheater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import soundlogic.silva.common.block.tile.multiblocks.DarkenedTheaterTileData;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater.ActorPixieData;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;

public class DarkenedTheaterPlays {
	public static void registerDarkenedTheaterPlay(IDarkenedTheaterPlay play) {
		MultiblockDataPixieRoomDarkenedTheater.playList.add(play);
		MultiblockDataPixieRoomDarkenedTheater.stringToPlay.put(play.getKey(), play);
	}
	
	public static void init() {
		for(int i = 0 ; i < 8 ; i++) {
			new dummyPlay("test"+i);
		}
	}
	
	static Random random = new Random();
	
	private static class dummyPlay implements IDarkenedTheaterPlay {

		private final String key;
		private final int level;
		private final boolean select;
		
		public dummyPlay(String key) {
			this.key=key;
			this.level=random.nextInt(3);
			this.select=random.nextBoolean();
			registerDarkenedTheaterPlay(this);
		}
		
		@Override
		public ItemStack getEquipmentForPosition(String position) {
			return null;
		}

		@Override
		public ItemStack getHelmetForPosition(String position) {
			return null;
		}

		@Override
		public List<String> getRequiredPositionList() {
			return new ArrayList<String>();
		}

		@Override
		public String getDisplayName() {
			// TODO Auto-generated method stub
			return key;
		}

		@Override
		public String getKey() {
			// TODO Auto-generated method stub
			return key;
		}

		@Override
		public int getPlaywrightLevel() {
			// TODO Auto-generated method stub
			return level;
		}

		@Override
		public boolean canSelect(TileMultiblockCore core,
				DarkenedTheaterTileData data) {
			return select;
		}

		@Override
		public void setProgress(TileMultiblockCore core,
				DarkenedTheaterTileData data, int progress) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void tickPixie(TileMultiblockCore core,
				DarkenedTheaterTileData data, ActorPixieData pixie,
				String position) {
			// TODO Auto-generated method stub
			
		}
		
	};
}
