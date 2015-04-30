package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockBase;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockProxy;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockMultiblockBase extends BlockContainer implements IWandHUD, IWandable{
	
	protected BlockMultiblockBase(Material material) {
		super(material);
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent) {
		if(world.isRemote)
			return;
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		tile.getCore().getData().onCollision(tile, tile.getCore(), ent);
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		int light = tile.lightValue;
		if(light==-1 && tile.getOriginalBlock()!=null)
			return tile.getOriginalBlock().getLightValue();
		return light;
	}

	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
		IIcon icon = tile.iconsForSides[side];
		if(icon==null && tile.getOriginalBlock()!=null)
			return tile.getOriginalBlock().getIcon(side, tile.getOriginalMeta());
		return icon;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileMultiblockBase tile = (TileMultiblockBase) world.getTileEntity(x, y, z);
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
}
