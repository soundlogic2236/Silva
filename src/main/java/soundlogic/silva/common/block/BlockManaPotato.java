package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.TileManaPotato;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.Optional;

public class BlockManaPotato extends BlockContainer implements IWandHUD, IWandable, ILexiconable {

	protected BlockManaPotato() {
		super(Material.cloth);
		setHardness(0.25F);
		this.setBlockName(LibBlockNames.MANA_POTATO);
		setCreativeTab(Silva.creativeTab);
		
		setBlockBounds(6F / 16F, 0F / 16F, 6F / 16F, 10F / 16F, 6F / 16F, 10F / 16F);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList list=new ArrayList();
		list.add(new ItemStack(Items.baked_potato));
		return list;
	}

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity baseTile = par1World.getTileEntity(par2, par3, par4);
		if(!(baseTile instanceof TileManaPotato))
			return false;
		TileManaPotato tile = (TileManaPotato) baseTile;
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		if(heldItem==null)
			return false;
		if(heldItem.isItemEqual(new ItemStack(this))) {
			tile.fillPotato();
			ItemStack add = new ItemStack(Items.baked_potato);
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			tile.markDirty();
			heldItem.stackSize--;
			return true;
		}
		return false;
    }
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idManaPotato;
	}

	
	@Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

	@Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaPotato();
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
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileManaPotato) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileManaPotato) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.manaPotato;
	}

}
