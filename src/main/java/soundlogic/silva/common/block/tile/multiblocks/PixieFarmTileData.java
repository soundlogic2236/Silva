package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.Pixie;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.nbt.NBTTagCompound;

public class PixieFarmTileData implements IMultiblockTileData{

	private static final String TAG_PIXIES = "pixies";
	private static final String TAG_PIXIES_POS = "pixiesPos";
	private static final String TAG_PIXIES_MOT = "pixiesMot";
	private static final String TAG_PIXIES_TIME = "pixiesTime";
	private static final String TAG_PIXIES_TYPE = "pixiesType";
	private static final String TAG_PIXIE_ROTATION = "pixiesRotation";
	
	private static final String TAG_COOKIE = "cookieLevel";
	
	public List<Pixie> pixies = new ArrayList<Pixie>();
	boolean pixieDied = false;
	int cookieLevel;
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_PIXIES, pixies.size());
		for(int i = 0 ; i < pixies.size() ; i++) {
			Pixie pixie = pixies.get(i);
			cmp.setDouble(TAG_PIXIES_POS+"x"+i, pixie.posX);
			cmp.setDouble(TAG_PIXIES_POS+"y"+i, pixie.posY);
			cmp.setDouble(TAG_PIXIES_POS+"z"+i, pixie.posZ);
			cmp.setDouble(TAG_PIXIES_MOT+"x"+i, pixie.motionX);
			cmp.setDouble(TAG_PIXIES_MOT+"y"+i, pixie.motionY);
			cmp.setDouble(TAG_PIXIES_MOT+"z"+i, pixie.motionZ);
			cmp.setInteger(TAG_PIXIES_TIME+i, pixie.ticks);
			cmp.setInteger(TAG_PIXIES_TYPE+i, pixie.type);
			cmp.setFloat(TAG_PIXIE_ROTATION+i, pixie.rotation);

			cmp.setDouble(TAG_PIXIES_POS+"px"+i, pixie.prevPosX);
			cmp.setDouble(TAG_PIXIES_POS+"py"+i, pixie.prevPosY);
			cmp.setDouble(TAG_PIXIES_POS+"pz"+i, pixie.prevPosZ);
			cmp.setFloat(TAG_PIXIE_ROTATION+"p"+i, pixie.prevRotation);
		}
		cmp.setInteger(TAG_COOKIE, cookieLevel);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		pixies.clear();
		int size = cmp.getInteger(TAG_PIXIES);
		for(int i = 0 ; i< size; i++) {
			Pixie pixie = new Pixie();
			pixie.posX=cmp.getDouble(TAG_PIXIES_POS+"x"+i);
			pixie.posY=cmp.getDouble(TAG_PIXIES_POS+"y"+i);
			pixie.posZ=cmp.getDouble(TAG_PIXIES_POS+"z"+i);
			pixie.motionX=cmp.getDouble(TAG_PIXIES_MOT+"x"+i);
			pixie.motionY=cmp.getDouble(TAG_PIXIES_MOT+"y"+i);
			pixie.motionZ=cmp.getDouble(TAG_PIXIES_MOT+"z"+i);
			pixie.ticks=cmp.getInteger(TAG_PIXIES_TIME+i);
			pixie.type=cmp.getInteger(TAG_PIXIES_TYPE+i);
			pixie.rotation=cmp.getFloat(TAG_PIXIE_ROTATION+i);

			pixie.prevPosX=cmp.getDouble(TAG_PIXIES_POS+"px"+i);
			pixie.prevPosY=cmp.getDouble(TAG_PIXIES_POS+"py"+i);
			pixie.prevPosZ=cmp.getDouble(TAG_PIXIES_POS+"pz"+i);
			pixie.prevRotation=cmp.getFloat(TAG_PIXIE_ROTATION+"p"+i);
			
			pixies.add(pixie);
		}
		cookieLevel = cmp.getInteger(TAG_COOKIE);
	}
	
}
