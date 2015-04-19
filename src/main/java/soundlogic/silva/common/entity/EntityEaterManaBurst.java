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
import soundlogic.silva.common.block.tile.TileManaEater;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IClientManaHandler;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityManaBurst;

public class EntityEaterManaBurst extends EntityManaBurst{

	public EntityEaterManaBurst(IManaSpreader spreader, boolean fake) {
		super(spreader,fake);
	}

	public EntityEaterManaBurst(World world) {
		super(world);
	}

	@Override
	public void onRecieverImpact(IManaReceiver tile, int x, int y, int z) {

		ChunkCoordinates sourceCoords = getBurstSourceChunkCoordinates();
		TileEntity sourceTile = worldObj.getTileEntity(sourceCoords.posX, sourceCoords.posY, sourceCoords.posZ);
		if(sourceTile != null && sourceTile instanceof TileManaEater) {
			TileManaEater eater = (TileManaEater) sourceTile;
			int targetMana = eater.getMaxManaTransfer();
			int currentMana = tile.getCurrentMana();
			int manaToTransfer = currentMana;
			if( currentMana > targetMana)
				manaToTransfer = targetMana;
			tile.recieveMana(-manaToTransfer);
			int newMana = tile.getCurrentMana();
			int manaRemoved = currentMana - newMana;
			eater.recieveMana(manaRemoved);
			this.setDead();
			worldObj.markBlockForUpdate(x,y,z);
		}
	}

}
