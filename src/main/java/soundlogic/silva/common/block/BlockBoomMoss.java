package soundlogic.silva.common.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockBoomMoss extends BlockContainer implements ILexiconable{

	public BlockBoomMoss() {
		super(Material.vine);
		setHardness(0.2F);
		setBlockName(LibBlockNames.BOOM_MOSS);
		setCreativeTab(Silva.creativeTab);
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileBoomMoss();
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int newX=x+dir.offsetX;
			int newY=y+dir.offsetY;
			int newZ=z+dir.offsetZ;
			Block block=world.getBlock(newX,newY,newZ);
			if(block.isNormalCube(world, newX, newY, newZ))
				return true;
		}
		return false;
	}
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		ItemStack stack=player.getHeldItem();
		if(stack!=null && stack.getItem()==vazkii.botania.common.item.ModItems.manaGun)
			return false;
		TileBoomMoss tile=(TileBoomMoss) world.getTileEntity(x, y, z);
		tile.detonate(player.posX,player.posY,player.posZ);
		return true;
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idBoomMoss;
	}

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
    	if(meta == 1) {
    		int i = (int) ((Math.sin(ClientTickHandler.ticksInGame)+1)/2*256);
    		int j = 256-i;
    		int col1 = 0x999900;
    		int col2 = 0x004199;
    		int mask1 = 0xFF00FF;
    		int mask2 = 0x00FF00;
    		return ((((( col1 & mask1 ) * i ) + ( ( col2 & mask1 ) * j )) >> 8 ) & mask1 ) 
    		         			| ((((( col1 & mask2 ) * i ) + ( ( col2 & mask2 ) * j )) >> 8 ) & mask2 );
    	}
    	if(meta == 2)
    		return 0x5f5f35;
    	
    	
    	return 0x999900;
    }
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return getRenderColor(0);
    }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
    	String resourceName=LibResources.MODEL_BOOM_MOSS;
		blockIcon = par1IconRegister.registerIcon(resourceName);
	}
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metdata, int fortune) {
    	return new ArrayList();
    }

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.boomMoss;
	}

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
    }
}
