package soundlogic.silva.common.block.tile.multiblocks;

public class ModMultiblocks {

	public static MultiblockDataBase lavashroom;
	public static MultiblockDataBase carnilotus;
	public static MultiblockDataBase mysticalgrinder;
	public static MultiblockDataBase endercatcher;
	public static MultiblockDataBase pixiefarm;
	public static MultiblockDataBase pixieroom;
	
	public static void init() {
		lavashroom=new MultiblockDataLavashroom();
		carnilotus=new MultiblockDataCarnilotus();
		mysticalgrinder=new MultiblockDataMysticalGrinder();
		endercatcher=new MutliblockDataEnderCatcher();
		pixiefarm=new MultiblockDataPixieFarm();
		pixieroom=new MultiblockDataPixieRoomEmpty();
		new MultiblockDataPixieRoomDarkenedTheater();
	}
	
}
