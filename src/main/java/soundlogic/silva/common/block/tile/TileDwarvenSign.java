package soundlogic.silva.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.ModPortalTradeRecipes;
import soundlogic.silva.common.crafting.recipe.DwarfTrade;

public class TileDwarvenSign extends TileMod{
	
	private static final String TAG_ACTIVATED = "activated";
	private static final String TAG_CORE_X = "coreX";
	private static final String TAG_CORE_Y = "coreY";
	private static final String TAG_CORE_Z = "coreZ";
	private static final String TAG_HAS_CORE = "hasCore";

	public TilePortalCore core;
	public int recipeSlot=-1;
	int ticks=0;
	boolean activated;
	
	boolean findCore=false;
	int findX;
	int findY;
	int findZ;
	
	@Override
	public void updateEntity() {
		ticks++;
		findCore();
		checkValid();
	}
	
	private void findCore() {
		if(!findCore)
			return;
		core=(TilePortalCore) worldObj.getTileEntity(findX, findY, findZ);
		findCore=false;
	}

	private void checkValid() {
		if(core!=null)
			if(core.isInvalid())
				core=null;
		if(core!=null)
			if(core.getDimension()!=Dimension.NIDAVELLIR)
				core=null;
		if(core==null)
			activated=false;
	}

	public void activate() {
		if(activated) {
			activated=false;
		}
		else {
			if(core==null)
				return;
			TileDwarvenSign[] signs=core.getDwarvenSigns();
			for(TileDwarvenSign sign : signs)
				if(sign!=null) {
					sign.activated=false;
					sign.getWorldObj().markBlockForUpdate(sign.xCoord, sign.yCoord, sign.zCoord);
				}
			this.activated=true;
		}
		worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setBoolean(TAG_ACTIVATED, activated);

		cmp.setBoolean(TAG_HAS_CORE, core!=null);

		if(core!=null) {
			cmp.setInteger(TAG_CORE_X, core.xCoord);
			cmp.setInteger(TAG_CORE_Y, core.yCoord);
			cmp.setInteger(TAG_CORE_Z, core.zCoord);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		activated = cmp.getBoolean(TAG_ACTIVATED);
		findX = cmp.getInteger(TAG_CORE_X);
		findY = cmp.getInteger(TAG_CORE_Y);
		findZ = cmp.getInteger(TAG_CORE_Z);
		findCore = cmp.getBoolean(TAG_HAS_CORE);
	}
	
	public boolean isActivated() {
		return activated;
	}

	public boolean matchesRecipe(DwarfTrade dwarfTrade) {
		return dwarfTrade==getRecipe();
	}
	
	public DwarfTrade getRecipe() {
		if(recipeSlot==-1 || core == null)
			return null;
		return core.dwarfData.getTrade(recipeSlot);
	}
	
	public void setRecipe(DwarfTrade dwarfTrade) {
		core.dwarfData.setTrade(recipeSlot, dwarfTrade);
	}
}
