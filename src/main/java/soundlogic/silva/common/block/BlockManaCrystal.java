package soundlogic.silva.common.block;

import java.util.List;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileManaCrystal;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.Optional;

public class BlockManaCrystal extends BlockContainer implements IWandHUD, IWandable {

	protected BlockManaCrystal() {
		super(Material.iron);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.MANA_CRYSTAL);
		setLightLevel(0.5F);
		setCreativeTab(Silva.creativeTab);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		par3.add(new ItemStack(par1, 1, 0));
		par3.add(new ItemStack(par1, 1, TileManaCrystal.MAX_MANA));
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaCrystal();
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
	public int getRenderType() {
		return LibRenderIDs.idManaCrystal;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 10;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileManaCrystal) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileManaCrystal) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

}
