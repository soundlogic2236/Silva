package soundlogic.silva.common.block;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.relic.ItemThorRing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDwarfWeed extends BlockBush implements IShearable {

	protected BlockDwarfWeed() {
		super(Material.plants);
		setBlockName(LibBlockNames.DWARF_WEED);
		setCreativeTab(Silva.creativeTab);
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x,
			int y, int z) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world,
			int x, int y, int z, int fortune) {
		return (ArrayList<ItemStack>) Arrays.asList(new Object[]{new ItemStack(this)});
	}
    

}
