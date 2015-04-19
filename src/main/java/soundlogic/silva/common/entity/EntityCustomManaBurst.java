package soundlogic.silva.common.entity;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import soundlogic.silva.common.block.tile.ICustomSpreader;
import soundlogic.silva.common.block.tile.TileFakeSpreaderWrapper;
import soundlogic.silva.common.block.tile.TileManaEater;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IClientManaHandler;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityManaBurst;

public class EntityCustomManaBurst extends EntityManaBurst{

	public EntityCustomManaBurst(ICustomSpreader spreader, boolean fake, int x, int y, int z, float rotx, float roty) {
		super(new TileFakeSpreaderWrapper(spreader,x,y,z,rotx,roty), fake);
	}
	
	public EntityCustomManaBurst(World world) {
		super(world);
	}

	@Override
	public void setDead() {
		super.setDead();

		if(!isFake()) {
			ChunkCoordinates coords = getBurstSourceChunkCoordinates();
			TileEntity tile = worldObj.getTileEntity(coords.posX, coords.posY, coords.posZ);
			if(tile != null && tile instanceof ICustomSpreader) {
				((ICustomSpreader) tile).prepBurst();
			}
		}
	}

	private void incrementFakeParticleTick() {
		ChunkCoordinates coords = getBurstSourceChunkCoordinates();
		TileEntity tile = worldObj.getTileEntity(coords.posX, coords.posY, coords.posZ);
		if(tile != null && tile instanceof ICustomSpreader) {
			ICustomSpreader spreader = (ICustomSpreader) tile;
			spreader.setBurstParticleTick(spreader.getBurstParticleTick() + 2);
			if(spreader.getLastBurstDeathTick() != -1 && spreader.getBurstParticleTick() > spreader.getLastBurstDeathTick())
				spreader.setBurstParticleTick(0);
		}
	}
	

}
