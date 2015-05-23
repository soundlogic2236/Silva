package soundlogic.silva.common.core.handler.portal.fate;

import java.util.List;

import soundlogic.silva.common.core.handler.portal.fate.FateHandler.StoredEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

public interface IVigridrFateSpawning extends IVigridrFate {

	public void storeEntityData(StoredEntityData data);
	public List<StoredEntityData> getStoredEntityData();
	public void setStoredEntityData(List<StoredEntityData> data);
}
