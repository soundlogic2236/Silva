package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockBase;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockProxy;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockMultiblockBase extends BlockContainer implements IWandHUD, IWandable, ILexiconable{
	
	Random rand = new Random();
	
	protected BlockMultiblockBase(Material material) {
		super(material);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent) {
		if(world.isRemote)
			return;
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		tile.getCore().getData().onCollision(tile, tile.getCore(), ent);
	}
	
	
	
	public float getBlockHardness(World world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		float hardness = tile.hardness;
		if(hardness==-2)
			return tile.originalHardness;
		return hardness;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		tile.breakMultiblock();
		super.breakBlock(world, x, y, z, block, metadata);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        byte b0 = 4;

        TileEntity baseTile = world.getTileEntity(x, y, z);
        
        if(!(baseTile instanceof TileMultiblockBase))
        	return true;
        
		TileMultiblockBase tile = (TileMultiblockBase) baseTile;

		Block block = tile.getOriginalBlock();
        IIcon override = tile.iconsForSides[world.rand.nextInt(6)];

        if(override==null && block.addDestroyEffects(world, x,y,z,tile.getOriginalMeta(), effectRenderer))
			return true;

        for (int i1 = 0; i1 < b0; ++i1)
        {
            for (int j1 = 0; j1 < b0; ++j1)
            {
                for (int k1 = 0; k1 < b0; ++k1)
                {
                    double d0 = (double)x + ((double)i1 + 0.5D) / (double)b0;
                    double d1 = (double)y + ((double)j1 + 0.5D) / (double)b0;
                    double d2 = (double)z + ((double)k1 + 0.5D) / (double)b0;
                    EntityDiggingFX effect = (new EntityDiggingFX(world, d0, d1, d2, d0 - (double)x - 0.5D, d1 - (double)y - 0.5D, d2 - (double)z - 0.5D, block, tile.getOriginalMeta())).applyColourMultiplier(x, y, z);
                    if(override!=null)
                    	effect.setParticleIcon(override);
                    effectRenderer.addEffect(effect);
                }
            }
        }
		return true;
	}

	@Override
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		
		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;
		
        TileEntity baseTile = world.getTileEntity(x, y, z);

        if(!(baseTile instanceof TileMultiblockBase))
        	return true;
        
        TileMultiblockBase tile = (TileMultiblockBase) baseTile;

		Block block = tile.getOriginalBlock();

		int side = target.sideHit;
        IIcon override = tile.iconsForSides[side];

        if(override == null && block.addHitEffects(world, target, effectRenderer))
			return true;

		
        if (block.getMaterial() != Material.air)
        {
            float f = 0.1F;
            double d0 = (double)x + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
            double d1 = (double)y + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
            double d2 = (double)z + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

            if (side == 0)
            {
                d1 = (double)y + block.getBlockBoundsMinY() - (double)f;
            }

            if (side == 1)
            {
                d1 = (double)y + block.getBlockBoundsMaxY() + (double)f;
            }

            if (side == 2)
            {
                d2 = (double)z + block.getBlockBoundsMinZ() - (double)f;
            }

            if (side == 3)
            {
                d2 = (double)z + block.getBlockBoundsMaxZ() + (double)f;
            }

            if (side == 4)
            {
                d0 = (double)x + block.getBlockBoundsMinX() - (double)f;
            }

            if (side == 5)
            {
                d0 = (double)x + block.getBlockBoundsMaxX() + (double)f;
            }

            EntityFX effect = (new EntityDiggingFX(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, block, tile.getOriginalMeta())).applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
            if(override!=null)
            	effect.setParticleIcon(override);
            effectRenderer.addEffect(effect);
        }
		return true;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		int light = tile.lightValue;
		if(light==-1 && tile.getOriginalBlock()!=null)
			return tile.getOriginalBlock().getLightValue();
		return light;
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		IIcon icon = tile.iconsForSides[side];
		if(icon==null && tile.getOriginalBlock()!=null)
			return tile.getOriginalBlock().getIcon(side, tile.getOriginalMeta());
		return icon;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
	    setBlockBoundsBasedOnState(world,x,y,z);
		return tile.solid ? super.getCollisionBoundingBoxFromPool(world, x, y, z) : null;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		if(tile.getCore().getData()!=null)
			tile.getCore().getData().renderHUD(tile, tile.getCore(), mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		tile.getCore().getData().onWanded(tile, tile.getCore(), player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z,
			EntityPlayer player, ItemStack stack) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		return tile.getCore().getData().getLexiconEntry();
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		this.minX=tile.minBBX;
		this.minY=tile.minBBY;
		this.minZ=tile.minBBZ;
		this.maxX=tile.maxBBX;
		this.maxY=tile.maxBBY;
		this.maxZ=tile.maxBBZ;
    }
}
