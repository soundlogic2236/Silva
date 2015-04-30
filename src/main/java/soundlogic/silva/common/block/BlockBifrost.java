package soundlogic.silva.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBifrost extends Block implements IBifrostBlock, ILexiconable{

	protected BlockBifrost() {
		super(Material.glass);
		setHardness(-1F);
		setResistance(4000F);
		setBlockName(LibBlockNames.BIFROST_BLOCK);
		setCreativeTab(Silva.creativeTab);
		setStepSound(soundTypeGlass);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		onNeighborBlockChange(world,x,y,z,null);
		return meta;
	}
			
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if(world.isRemote)
			return;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int sideX=x+dir.offsetX;
			int sideY=y+dir.offsetY;
			int sideZ=z+dir.offsetZ;
			Block sideBlock=world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
			int sideMeta=world.getBlockMetadata(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
			if(sideBlock == vazkii.botania.common.block.ModBlocks.storage && sideMeta == 4) {
				makeBifrostShards(sideX,sideY,sideZ,world);
			}
		}
	}
	
	private void makeBifrostShards(int x, int y, int z, World world) {
		world.setBlockToAir(x, y, z);
		world.setBlock(x, y, z, this,0,3);
		this.onNeighborBlockChange(world, x, y, z, ModBlocks.bifrostBlock);
		world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(ModItems.bifrostShard,9)));
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		int touchingBifrost = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			Block touchedBlock=world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
			if(touchedBlock instanceof IBifrostBlock && ((IBifrostBlock) touchedBlock).isBifrost(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)) {
				touchingBifrost++;
			}
		}
		boolean emptyHand = player.getCurrentEquippedItem() == null;
		boolean terraPick = emptyHand ? false : player.getCurrentEquippedItem().getItem() == vazkii.botania.common.item.ModItems.terraPick;
		boolean thor = ItemThorRing.getThorRing(player) != null;
		boolean thorPick = terraPick && thor;

		float hardness=.2F;
		switch(touchingBifrost) {
		case 0:hardness = .2F; break;
		case 1:hardness = .2F; break;
		case 2:hardness = thor ? .5F : 2F; break;
		case 3:hardness = thor ? 10F : 40F; break;
		case 4:hardness = thor ? 30F : -1F; break;
		case 5:hardness = -1F; break;
		case 6:hardness = -1F; break;
		}

		if (hardness < 0.0F)
        {
            return 0.0F;
        }

        if (emptyHand || thorPick)
        {
            return player.getBreakSpeed(this, false, 0, x, y, z) / hardness / 30F / (thorPick ? 0.08F : 1F);
        }
        else
        {
            return player.getBreakSpeed(this, true, 0, x, y, z) / hardness / 100F;
        }
	}

	@Override
	public boolean isBifrost(World world, int x, int y, int z) {
		return true;
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(LibResources.BLOCK_BIFROST);
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.bifrost;
	}
    
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
    	return true;
    }
}
