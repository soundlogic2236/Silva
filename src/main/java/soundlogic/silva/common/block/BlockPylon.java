package soundlogic.silva.common.block;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerBase;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerHelheim;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerMuspelheim;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerSvartalfheim;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerVanaheimr;
import soundlogic.silva.common.core.handler.portal.DimensionExposureHandlerVigridr;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionalBlockHandlerMuspelheim;
import soundlogic.silva.common.core.handler.portal.DimensionalBlockHandlerNiflheim;
import soundlogic.silva.common.core.handler.portal.DimensionalBlockHandlerSvartalfheim;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.BaseDimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.Optional;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockPylon extends BlockContainer implements ILexiconable, IInfusionStabiliser {

	static final int pylonTypes=16;
	
	static final HashMap<Dimension, Integer> dimensionalMetadatas = new HashMap<Dimension,Integer>() {
		{
			put(Dimension.GINNUNGAGAP,2);
			put(Dimension.VIGRIDR,3);
			put(Dimension.FOLKVANGR,4);
			put(Dimension.VALHALLA,5);
			put(Dimension.HELHEIM,6);
			put(Dimension.ASGARD,7);
			put(Dimension.ALFHEIM,8);
			put(Dimension.MIDGARD,9);
			put(Dimension.JOTUNHEIMR,10);
			put(Dimension.SVARTALFHEIM,11);
			put(Dimension.MUSPELHEIM,12);
			put(Dimension.NIFLHEIM,13);
			put(Dimension.NIDAVELLIR,14);
			put(Dimension.VANAHEIMR,15);
		}
	};
	
	protected BlockPylon() {
		super(Material.iron);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.PYLON);
		setLightLevel(0.5F);
		setCreativeTab(Silva.creativeTab);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
		this.setTickRandomly(true);
	}

	@Override
    public void updateTick(World world, int x, int y, int z, Random random) {
		if(isUntunedPylon(world,x,y,z)) {
			Dimension dim = DimensionHandler.getDimensionFromWorld(world);
			if(dim!=null && dim.canTunePylonWithExistance())
				setToDimensionalPylon(world,x,y,z,dim);
		}
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

	public static ItemStack getPylonForDimension(Dimension dim) {
		return new ItemStack(ModBlocks.dimensionalPylon,1,getMetadataForDimension(dim));
	}
	
	public static Dimension getPylonDimension(World world, int x, int y, int z) {
		if(world.getBlock(x, y, z) instanceof BlockPylon) {
			int meta = world.getBlockMetadata(x, y, z);
			for(Entry<Dimension,Integer> entry : dimensionalMetadatas.entrySet()) {
				if(entry.getValue()==meta)
					return entry.getKey();
			}
		}
		return null;
	}
	
	public static Dimension getPylonDimension(ItemStack stack) {
		if(stack.getItem() instanceof ItemBlock)
			if(Block.getBlockFromItem(stack.getItem())==ModBlocks.dimensionalPylon) {
				int meta = stack.getItemDamage();
				for(Entry<Dimension,Integer> entry : dimensionalMetadatas.entrySet()) {
					if(entry.getValue()==meta)
						return entry.getKey();
				}
			}
		return null;
	}

	public static int getMetadataForDimension(Dimension dim) {
		if(dim==null)
			return 1;
		return dimensionalMetadatas.get(dim);
	}
	
	public static void tryTunePylon(World world, int x, int y, int z, Dimension dim) {
		if(isUntunedPylon(world, x,y,z))
			setToDimensionalPylon(world,x,y,z,dim);
	}
	
	public static boolean isUntunedPylon(World world, int x, int y, int z) {
		return world.getBlock(x, y, z)==ModBlocks.dimensionalPylon && world.getBlockMetadata(x, y, z)==1;
	}
	
	public static void setToDimensionalPylon(World world, int x, int y, int z, Dimension dim) {
		world.setBlock(x, y, z, ModBlocks.dimensionalPylon, getMetadataForDimension(dim), 3);
	}
}
