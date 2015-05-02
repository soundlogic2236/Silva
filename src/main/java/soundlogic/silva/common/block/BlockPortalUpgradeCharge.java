package soundlogic.silva.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import soundlogic.silva.common.block.tile.TilePortalUpgradeRedstone;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalUpgradeCharge extends BlockPortalUpgradeBase implements ILexiconable {

	public IIcon opening;
	public IIcon side;
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePortalUpgradeCharge();
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
    	opening = par1IconRegister.registerIcon(LibResources.PORTAL_UPGRADE_OPENING);
    	side = par1IconRegister.registerIcon(LibResources.BLOCK_BIFROST_SPARKLING);
	}
    
	@Override
	public int getRenderType() {
		return LibRenderIDs.idPortalUpgradeCharge;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    	return side==meta ? this.opening :this.side;
    }
	
    public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        return p_149660_5_;
    }

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity baseTile = par1World.getTileEntity(par2, par3, par4);
		if(!(baseTile instanceof TilePortalUpgradeCharge))
			return false;
		TilePortalUpgradeCharge tile = (TilePortalUpgradeCharge) baseTile;
		ItemStack currentStone = tile.getStackInSlot(0);
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		if(currentStone == null && heldItem != null && tile.isItemValidForSlot(0, heldItem)) {
			par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
			tile.setInventorySlotContents(0, heldItem.copy());
			tile.markDirty();
		}
		else if (currentStone != null) {
			ItemStack add = currentStone.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			tile.setInventorySlotContents(0, null);
			tile.markDirty();
		}
		return true;
    }
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        Random rand = new Random();
    	
        TilePortalUpgradeCharge tile = (TilePortalUpgradeCharge)world.getTileEntity(x, y, z);

        IInventory inventory = (IInventory) tile;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0)
            {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.portalUpgrades;
	}
}
