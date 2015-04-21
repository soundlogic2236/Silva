package soundlogic.silva.common.block;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDwarvenPool extends BlockContainer implements IWandHUD, IWandable {

	public BlockDwarvenPool() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.DWARVEN_POOL);
		setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
		setCreativeTab(Silva.creativeTab);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO OP
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDwarvenPool();
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TileDwarvenPool tile = (TileDwarvenPool) par1World.getTileEntity(par2, par3, par4);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(par1World, par2, par3, par4);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return ModBlocks.dwarfRock.getIcon(par1, 0);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idDwarvenPool;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TileDwarvenPool pool = (TileDwarvenPool) par1World.getTileEntity(par2, par3, par4);
		int val = (int) ((double) pool.getCurrentMana() / (double) pool.manaCap * 15.0);
		if(pool.getCurrentMana() > 0)
			val = Math.max(val, 1);

		return val;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileDwarvenPool) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileDwarvenPool) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}
}
