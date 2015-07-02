package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PixiePowerSimpleTileData extends PixiePowerTileData {

	boolean requirementsDirty=false;
	boolean nextStackDirty=true;
	Object nextRequestedObject;
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		this.requirementsDirty=true;
		this.nextStackDirty=true;
	}
}
