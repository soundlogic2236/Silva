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
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityManaBurst;

public class EntityEaterManaBurst extends EntityCustomManaBurst{

	TileEntity collidedTile = null;
	
	public EntityEaterManaBurst(ICustomSpreader spreader, boolean fake, int x, int y, int z, float rotx, float roty) {
		super(spreader,fake,x,y,z,rotx,roty);
	}

	public EntityEaterManaBurst(World world) {
		super(world);
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		boolean collided = false;
		boolean dead = false;

		if(movingobjectposition.entityHit == null) {
			TileEntity tile = worldObj.getTileEntity(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			Block block = worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

			if(tile instanceof IManaCollisionGhost && ((IManaCollisionGhost) tile).isGhost() && !(block instanceof IManaTrigger) || block instanceof BlockBush || block instanceof BlockLeaves)
				return;

			if(BotaniaAPI.internalHandler.isBuildcraftPipe(tile))
				return;

			ChunkCoordinates coords = getBurstSourceChunkCoordinates();
			if(tile != null && (tile.xCoord != coords.posX || tile.yCoord != coords.posY || tile.zCoord != coords.posZ))
				collidedTile = tile;

			if(collidedTile instanceof IManaReceiver && !isFake()) {
				ChunkCoordinates sourceCoords = getBurstSourceChunkCoordinates();
				TileEntity sourceTile = worldObj.getTileEntity(sourceCoords.posX, sourceCoords.posY, sourceCoords.posZ);
				if(sourceTile != null && sourceTile instanceof TileManaEater) {
					TileManaEater eater = (TileManaEater) sourceTile;
					int targetMana = eater.getMaxManaTransfer();
					int currentMana = ((IManaReceiver) collidedTile).getCurrentMana();
					int manaToTransfer = currentMana;
					if( currentMana > targetMana)
						manaToTransfer = targetMana;
					((IManaReceiver) collidedTile).recieveMana(-manaToTransfer);
					int newMana = ((IManaReceiver) collidedTile).getCurrentMana();
					int manaRemoved = currentMana - newMana;
					eater.recieveMana(manaRemoved);
					dead=true;
				}
			}

			collided = true;

			if(dead && !isDead) {
				if(!isFake()) {
					Color color = new Color(getColor());
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;

					int mana = getMana();
					int maxMana = getStartingMana();
					float size = (float) mana / (float) maxMana;

					if(!vazkii.botania.common.core.handler.ConfigHandler.subtlePowerSystem)
						for(int i = 0; i < 4; i++)
							Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.15F * size, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
					Botania.proxy.sparkleFX(worldObj, (float) posX, (float) posY, (float) posZ, r, g, b, 4, 2);
				}

				setDead();
			}
		}
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

	public TileEntity getCollidedTile(boolean noParticles) {
		super.getCollidedTile(noParticles);

		return collidedTile;
	}


}
