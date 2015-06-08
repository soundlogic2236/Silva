package soundlogic.silva.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

public class BlockDarkElfTrap extends BlockContainer implements ILexiconable {

    public BlockDarkElfTrap() {
		super(Material.iron);
		setHardness(1.0F);
		setResistance(10.0F);
		setStepSound(soundTypeAnvil);
		setBlockName(LibBlockNames.DARK_ELF_TRAP);
		setBlockBounds(0F, 0F, 0F, 1F, 3F/16F, 1F);
		setCreativeTab(Silva.creativeTab);
	}

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity baseTile = par1World.getTileEntity(par2, par3, par4);
		if(!(baseTile instanceof TileDarkElfTrap))
			return false;
		TileDarkElfTrap tile = (TileDarkElfTrap) baseTile;
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		if(heldItem!=null) {
			if(tile.putStackIn(heldItem)) {
				heldItem.stackSize--;
				return true;
			}
		}
		if(tile.hasLoot()) {
			ItemStack add = tile.getLoot().copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			tile.setLoot(null);
			tile.emptyTrap();
			tile.markDirty();
			return true;
		}
		return false;
    }
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO OP
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDarkElfTrap();
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		
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
		return LibRenderIDs.idDarkElfTrap;
	}

    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        Random rand = new Random();
    	
        TileDarkElfTrap tile = (TileDarkElfTrap)world.getTileEntity(x, y, z);

        IInventory inventory = (IInventory) tile;

        for (int i = 0; i < inventory.getSizeInventory()+1; i++)
        {
        	
            ItemStack item;
            if(i<inventory.getSizeInventory())
            	item = inventory.getStackInSlot(i);
            else
            	item = tile.getLoot();

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
        tile.emptyTrap();
        super.breakBlock(world, x, y, z, block, meta);
    }

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TileDarkElfTrap trap = (TileDarkElfTrap) par1World.getTileEntity(par2, par3, par4);
		return trap.hasLoot() ? 15 : 0;
	}

	@Override
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3,
			EntityPlayer arg4, ItemStack arg5) {
		return LexiconData.dwarvenManaPool;
	}
	
}
