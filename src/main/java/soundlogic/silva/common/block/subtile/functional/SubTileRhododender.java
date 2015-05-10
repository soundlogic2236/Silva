package soundlogic.silva.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.common.core.handler.DustHandler;
import soundlogic.silva.common.entity.EntityEnderPearlRedirected;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;

public class SubTileRhododender extends SubTileFunctional {

	ForgeDirection[] mainDirs = new ForgeDirection[] {
			ForgeDirection.NORTH,
			ForgeDirection.SOUTH,
			ForgeDirection.EAST,
			ForgeDirection.WEST,
	};
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(supertile.getWorldObj().isRemote)
			return;
		List<EntityEnderPearl> pearls = supertile.getWorldObj().getEntitiesWithinAABB(EntityEnderPearl.class, getAABB());
		if(!pearls.isEmpty()) {
			double[] motion = getRedirectVector();
			for(EntityEnderPearl pearl : pearls) {
				EntityEnderPearlRedirected redirected = EntityEnderPearlRedirected.tryRedirectPearl(pearl, supertile.xCoord, supertile.yCoord, supertile.zCoord, motion[0], motion[1], motion[2]);
			}
		}
	}
	
	private double[] getRedirectVector() {
		double totX=0;
		double totZ=0;
		double totAbs=0;
		for(ForgeDirection dir : mainDirs) {
			int x = supertile.xCoord;
			int y = supertile.yCoord;
			int z = supertile.zCoord;
			for(int i = 0 ; i < 5 ; i ++) {
				x+=dir.offsetX;
				z+=dir.offsetZ;
				if(!DustHandler.isDust(supertile.getWorldObj(),x,y,z))
					break;
				totX+=dir.offsetX;
				totZ+=dir.offsetZ;
				totAbs+=1;
			}
		}
		double baseVelocity = .1;
		return new double[]{
				baseVelocity*totX*Math.abs(totX),
				baseVelocity*totAbs*Math.abs(totAbs)/4.0,
				baseVelocity*totZ*Math.abs(totZ),
				};
	}

	AxisAlignedBB getAABB() {
		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;
		
		return AxisAlignedBB.getBoundingBox(x-1, y-1, z-1, x+2, y+2, z+2);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.rhododender;
	}
}
