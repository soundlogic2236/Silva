package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import soundlogic.silva.common.block.tile.TileMod;

public class TileMultiblockCore extends TileMultiblockBase{

	private static final String TAG_MULTIBLOCK_NAME = "multiblockName";
	private static final String TAG_MULTIBLOCK_MIRROR_X = "multiblockRotationMirrorX";
	private static final String TAG_MULTIBLOCK_MIRROR_Z = "multiblockRotationMirrorZ";
	private static final String TAG_MULTIBLOCK_ROTATION = "multiblockRotationAngle";

	MultiblockDataBase data;
	IMultiblockTileData tileData;

	public List<TileMultiblockProxy> proxies = new ArrayList<TileMultiblockProxy>();
	
	public boolean mirrorX = false;
	public boolean mirrorZ = false;
	public int rotation = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.isRemote)
			return;
		data.onTick(this);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setString(TAG_MULTIBLOCK_NAME, data.getName());
		tileData.writeCustomNBT(cmp);
		cmp.setBoolean(TAG_MULTIBLOCK_MIRROR_X, mirrorX);
		cmp.setBoolean(TAG_MULTIBLOCK_MIRROR_Z, mirrorZ);
		cmp.setInteger(TAG_MULTIBLOCK_ROTATION, rotation);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		this.data=MultiblockDataBase.multiBlocksByName.get(cmp.getString(TAG_MULTIBLOCK_NAME));
		this.mirrorX=cmp.getBoolean(TAG_MULTIBLOCK_MIRROR_X);
		this.mirrorZ=cmp.getBoolean(TAG_MULTIBLOCK_MIRROR_Z);
		this.rotation=cmp.getInteger(TAG_MULTIBLOCK_ROTATION);
		if(tileData==null)
			tileData=data.createTileData();
		tileData.readCustomNBT(cmp);
	}
	
	public void setData(MultiblockDataBase data) {
		this.data=data;
		this.tileData=data.createTileData();
	}
	
	public MultiblockDataBase getData() {
		return data;
	}
	
	public IMultiblockTileData getTileData() {
		return tileData;
	}

	@Override
	public TileMultiblockCore getCore() {
		return this;
	}

	@Override
	public boolean isValid() {
		if(data!=null)
			return data.matchesTemplateForPersistance(this, worldObj, xCoord, yCoord, zCoord, mirrorX, mirrorZ, rotation);
		return false;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public int[] getRelativePos() {
		return new int[]{0,0,0};
	}
	
	public void markForVisualUpdate() {
		this.markForRefresh();
		
		for(TileMultiblockBase tile : proxies) {
			tile.markForRefresh();
		}
	}
}
