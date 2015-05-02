package soundlogic.silva.common.block;

import java.util.List;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.Optional;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockPylon extends BlockContainer implements ILexiconable, IInfusionStabiliser {

	static final int pylonTypes=15;
	
	protected BlockPylon() {
		super(Material.iron);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.PYLON);
		setLightLevel(0.5F);
		setCreativeTab(Silva.creativeTab);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < pylonTypes; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TilePylon();
	}

	@Override
	public boolean canStabaliseInfusion(World arg0, int arg1, int arg2, int arg3) {
		return true;
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.advancedPortals;
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
		return LibRenderIDs.idPylon;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 15;
	}


}
