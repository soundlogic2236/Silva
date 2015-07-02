package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.entity.EntityPixieProxy;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieData;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieGroupHandler;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.nbt.NBTTagCompound;

public class PixieFarmTileData implements IMultiblockTileData {

	private static final String TAG_PIXIES = "pixies";
	private static final String TAG_COOKIE = "cookieLevel";
	
	private static final String TAG_POWER = "pixiePower";

	public static final int maxPixiePower = 1000000;
	
	int cookieLevel;

	int pixiePowerLevel = 0;
	
	int flowercount = -1;
	
	public PixieGroupHandler<FarmPixieData> pixieGroup = new PixieGroupHandler() {

		@Override
		protected FarmPixieData create() {
			return new FarmPixieData(this);
		}
		
	};
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		pixieGroup.writeToNBT(cmp);
		cmp.setInteger(TAG_COOKIE, cookieLevel);

		cmp.setInteger(TAG_POWER, pixiePowerLevel);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		pixieGroup.readFromNBT(cmp);
		cookieLevel = cmp.getInteger(TAG_COOKIE);

		pixiePowerLevel = cmp.getInteger(TAG_POWER);
	}
	
	public int getPowerLevel() {
		return this.pixiePowerLevel;
	}
	public int getMaxPowerLevel() {
		return this.maxPixiePower;
	}
	
	public boolean drawPower(int power) {
		boolean result = this.pixiePowerLevel>=power;
		this.pixiePowerLevel=Math.max(0, this.pixiePowerLevel-power);
		return result;
	}
}
