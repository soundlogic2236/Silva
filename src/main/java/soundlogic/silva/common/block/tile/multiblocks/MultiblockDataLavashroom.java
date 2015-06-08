package soundlogic.silva.common.block.tile.multiblocks;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.GameRegistry;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.HUDHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiblockDataLavashroom extends MultiblockDataBase {
	
	private ArrayList<EntityItem> trackedItems = new ArrayList<EntityItem>();
	private int maxMana = 10000;
	private int maxLava = 10000;
	
	IIcon iconLava;
	IIcon iconDullLava;
	IIcon iconMushroom_skin;
	IIcon iconMushroom_dullSkin;
	IIcon iconMushroom_skinEdge;
	IIcon iconMushroom_dullSkinEdge;
	IIcon iconMushroom_stem;
	IIcon iconMushroom_inside;
	IIcon iconMushroom_inside_edge;
	IIcon iconMushroom_inside_edge_dull;
	
	public MultiblockDataLavashroom() {
		super(new BlockData(GameRegistry.findBlock("Botania", "mushroom"),14));
		BlockData core = new BlockData(GameRegistry.findBlock("Botania", "mushroom"),14);
		BlockData mushroom0 = new BlockData(Blocks.red_mushroom_block,0);
		BlockData mushroom1 = new BlockData(Blocks.red_mushroom_block,1);
		BlockData mushroom2 = new BlockData(Blocks.red_mushroom_block,2);
		BlockData mushroom3 = new BlockData(Blocks.red_mushroom_block,3);
		BlockData mushroom4 = new BlockData(Blocks.red_mushroom_block,4);
		BlockData mushroom5 = new BlockData(Blocks.red_mushroom_block,5);
		BlockData mushroom6 = new BlockData(Blocks.red_mushroom_block,6);
		BlockData mushroom7 = new BlockData(Blocks.red_mushroom_block,7);
		BlockData mushroom8 = new BlockData(Blocks.red_mushroom_block,8);
		BlockData mushroom9 = new BlockData(Blocks.red_mushroom_block,9);
		BlockData mushroom10 = new BlockData(Blocks.red_mushroom_block,10);
		BlockData deepRock = new BlockData(ModBlocks.dwarfRock,0);

		creationRequirementsTemplate = new BlockData[][][] {
					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, deepRock, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},
					
					{{BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD},
					 {BlockData.AIR, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.AIR},
					 {BlockData.AIR, BlockData.WILDCARD, core, BlockData.WILDCARD, BlockData.AIR},
					 {BlockData.AIR, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.AIR},
					 {BlockData.WILDCARD, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.WILDCARD}},
						
					{{BlockData.WILDCARD, mushroom1, mushroom4, mushroom7, BlockData.WILDCARD},
					 {mushroom1, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom7},
					 {mushroom2, BlockData.WILDCARD, mushroom10, BlockData.WILDCARD, mushroom8},
					 {mushroom3, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom9},
					 {BlockData.WILDCARD, mushroom3, mushroom6, mushroom9, BlockData.WILDCARD}},

					{{BlockData.WILDCARD, mushroom1, mushroom4, mushroom7, BlockData.WILDCARD},
					 {mushroom1, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom7},
					 {mushroom2, BlockData.WILDCARD, mushroom10, BlockData.WILDCARD, mushroom8},
					 {mushroom3, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom9},
					 {BlockData.WILDCARD, mushroom3, mushroom6, mushroom9, BlockData.WILDCARD}},

					{{BlockData.WILDCARD, mushroom1, mushroom4, mushroom7, BlockData.WILDCARD},
					 {mushroom1, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom7},
					 {mushroom2, BlockData.WILDCARD, mushroom10, BlockData.WILDCARD, mushroom8},
					 {mushroom3, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, mushroom9},
					 {BlockData.WILDCARD, mushroom3, mushroom6, mushroom9, BlockData.WILDCARD}},

					{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
					 {BlockData.WILDCARD, mushroom1, mushroom4, mushroom7, BlockData.WILDCARD},
					 {BlockData.WILDCARD, mushroom2, BlockData.WILDCARD, mushroom8, BlockData.WILDCARD},
					 {BlockData.WILDCARD, mushroom3, mushroom6, mushroom9, BlockData.WILDCARD},
					 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},
			};
		templateOrigin = new int[] {2,1,2};
		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, deepRock, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},
				
				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK_CORE, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},
					
				{{BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.MULTIBLOCK_LAVA, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.MULTIBLOCK},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD}},

				{{BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.WILDCARD},
				 {BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD, BlockData.WILDCARD}},
		};
	}
	
	

	@Override
	public String getName() {
		return "lavaShroom";
	}



	@Override
	public IMultiblockTileData createTileData() {
		// TODO Auto-generated method stub
		return new LavashroomTileData();
	}



	@Override
	public void onTick(TileMultiblockCore core) {
		int lavaX = core.xCoord;
		int lavaY = core.yCoord+4;
		int lavaZ = core.zCoord;
		LavashroomTileData data = (LavashroomTileData) core.tileData;
		Block block = core.getWorldObj().getBlock(lavaX, lavaY, lavaZ);
		if(data.activated && block == Blocks.lava) {
			if(data.lavaAmount<=maxLava-1000) {
				data.lavaAmount += 1000;
				if(data.lavaAmount==1000)
					core.markForVisualUpdate();
				core.getWorldObj().setBlockToAir(lavaX, lavaY, lavaZ);
			}
		}
		else if(!data.activated && block == Blocks.red_mushroom_block) {
			core.getWorldObj().setBlockToAir(lavaX, lavaY, lavaZ);
		}
		else if(!data.activated && block == Blocks.lava) {
			AxisAlignedBB aabb= AxisAlignedBB.getBoundingBox(lavaX,lavaY,lavaZ,lavaX+1,lavaY+1,lavaZ+1);
			List<EntityItem> items = core.getWorldObj().getEntitiesWithinAABB(EntityItem.class, aabb);
			for(EntityItem ent : items) {
				ItemStack stack = ent.getEntityItem();
				if(stack.getItem() != GameRegistry.findItem("Botania", "rune"))
					continue;
				int count = stack.stackSize;
				int meta = stack.getItemDamage();
				int toRemove = 0;
				if(meta == 1) {
					toRemove = Math.min(count, data.fireRunesNeeded);
					data.fireRunesNeeded-=toRemove;
				}
				else if (meta == 2) {
					toRemove = Math.min(count, data.earthRunesNeeded);
					data.earthRunesNeeded-=toRemove;
				}
				else if (meta == 8) {
					toRemove = Math.min(count, data.manaRunesNeeded);
					data.manaRunesNeeded-=toRemove;
				}
				else if (meta == 13) {
					toRemove = Math.min(count, data.wrathRunesNeeded);
					data.wrathRunesNeeded-=toRemove;
				}
				stack.stackSize-=toRemove;
			}
			if(data.fireRunesNeeded == 0 && data.earthRunesNeeded == 0 && data.manaRunesNeeded == 0 && data.wrathRunesNeeded == 0) {
				data.activated=true;
			}
		}
		if(data.activated) {
			if(data.lavaAmount>0) {
				data.lavaAmount = Math.max(0, data.lavaAmount-2);
				if(data.currentMana <= maxMana) {
					if(data.fuels.size()>0 && data.lavaAmount>0) {
						data.lavaAmount = Math.max(0, data.lavaAmount-10);
						data.fuelTicks+=100;
						data.currentMana= Math.min(maxMana, data.currentMana + data.fuels.get(0)/20);
						if(data.fuelTicks>=data.fuels.get(0))
							data.fuels.remove(0);
					}
				}
				if(data.lavaAmount==0) {
					core.markForVisualUpdate();
				}
			}
			else {
				data.fuels.clear();
				data.fuelTicks=0;
			}
		}
		
		if(data.currentMana>0) {
			ForgeDirection[] directions = new ForgeDirection[] {
					ForgeDirection.NORTH,
					ForgeDirection.EAST,
					ForgeDirection.SOUTH,
					ForgeDirection.WEST,
			};
			int tempMana = data.currentMana;
			for(ForgeDirection dir : directions) {
				int x = core.xCoord + dir.offsetX;
				int y = core.yCoord + dir.offsetY;
				int z = core.zCoord + dir.offsetZ;
				
				TileEntity tile = core.getWorldObj().getTileEntity(x, y, z);
				if(tile instanceof IManaPool) {
					IManaPool pool = (IManaPool) tile;
					int mana = Math.min(data.currentMana, Math.max(100, tempMana / 10));
					pool.recieveMana(mana);
					data.currentMana-=mana;
					core.getWorldObj().markBlockForUpdate(x,y,z);
				}
			}
		}
	}



	@Override
	public void onCollision(TileMultiblockBase tile,
			TileMultiblockCore core, Entity entity) {
		if(entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			int burnTime = TileEntityFurnace.getItemBurnTime(item.getEntityItem());
			if(burnTime>0) {
				for(int i = 0; i < item.getEntityItem().stackSize ; i++)
					((LavashroomTileData)core.tileData).fuels.add(burnTime);
				item.setDead();
			}
		}
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		LavashroomTileData data = (LavashroomTileData) core.tileData;
		IIcon icon = data.lavaAmount > 0 ? iconLava : iconDullLava;
		IIcon[] lavaIcons = new IIcon[] {icon,icon,icon,icon,icon,icon};
		IIcon skinNonEdgeIcon = data.lavaAmount > 0 ? iconMushroom_skin : iconMushroom_dullSkin;
		IIcon skinEdgeIcon = data.lavaAmount > 0 ? iconMushroom_skinEdge : iconMushroom_dullSkinEdge;
		int lavaLight = data.lavaAmount > 0 ? 15 : 10;
		if(y<=1 && (x!=0 || z!=0)) {
			tile.iconsForSides=lavaIcons;
			tile.lightValue = lavaLight;
		}
		else if (x != 0 || y != 0 || z != 0){
			for(int side = 0 ; side < 6 ; side++) {
				int meta = tile.getOriginalMeta();
				IIcon skinIcon = skinNonEdgeIcon;
				IIcon insideIcon = this.iconMushroom_inside;
				if(y==2) {
					skinIcon=skinEdgeIcon;
					insideIcon=data.lavaAmount> 0 ? this.iconMushroom_inside_edge : this.iconMushroom_inside_edge_dull;
				}
				IIcon current = ( meta == 10 && side > 1 ? iconMushroom_stem : (meta >= 1 && meta <= 9 && side == 1 ? skinIcon : (meta >= 1 && meta <= 3 && side == 2 ? skinIcon : (meta >= 7 && meta <= 9 && side == 3 ? skinIcon : ((meta == 1 || meta == 4 || meta == 7) && side == 4 ? skinIcon : ((meta == 3 || meta == 6 || meta == 9) && side == 5 ? skinIcon : (meta == 14 ? skinIcon : (meta == 15 ? iconMushroom_stem : insideIcon))))))));
				tile.iconsForSides[side]=current;
			}
		}
		else {
			for(int side = 0 ; side < 6 ; side++) {
				int meta = 10;
				IIcon skinIcon = skinNonEdgeIcon;
				IIcon current = ( meta == 10 && side > 1 ? iconMushroom_stem : (meta >= 1 && meta <= 9 && side == 1 ? skinIcon : (meta >= 1 && meta <= 3 && side == 2 ? skinIcon : (meta >= 7 && meta <= 9 && side == 3 ? skinIcon : ((meta == 1 || meta == 4 || meta == 7) && side == 4 ? skinIcon : ((meta == 3 || meta == 6 || meta == 9) && side == 5 ? skinIcon : (meta == 14 ? skinIcon : (meta == 15 ? iconMushroom_stem : iconMushroom_inside))))))));
				tile.iconsForSides[side]=current;
			}
		}
	}

    @Override
	public void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		LavashroomTileData data = (LavashroomTileData) core.tileData;
		if(y<=2 && (x!=0 || z!=0)) {
			tile.solid=false;
		}
	}



	@Override
	public void init(TileMultiblockCore core) {
		// NO OP
	}


	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core,
			EntityPlayer player, ItemStack stack) {
		if(player == null)
			return;

		if(!core.getWorldObj().isRemote) {
			LavashroomTileData data = (LavashroomTileData) core.tileData;
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			core.writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(data.TAG_KNOWN_MANA, data.currentMana);
			nbttagcompound.setInteger(data.TAG_KNOWN_LAVA, data.lavaAmount);
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(core.xCoord, core.yCoord, core.zCoord, -999, nbttagcompound));
		}

		core.getWorldObj().playSoundAtEntity(player, "botania:ding", 0.11F, 1F);
	}


	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core,
			Minecraft mc, ScaledResolution res) {
		LavashroomTileData data = (LavashroomTileData) core.tileData;
		String name = StatCollector.translateToLocal(LibResources.PREFIX_MOD + this.getUnlocalizedName() + ".name");
		int lavaBarOffset = 15;
		int color = 0x007eff;
		HUDHandler.drawSimpleManaHUD(color, data.knownMana, maxMana, name, res);
		int x = res.getScaledWidth() / 2 - 51;
		int y = res.getScaledHeight() / 2 + 15 + lavaBarOffset;
		color=0xFF0000;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(data.activated) {
			HUDHandler.renderManaBar(x, y, color, data.knownLava < 0 ? .5F : 1F, data.knownLava, this.maxLava);

			if(data.knownLava < 0) {
				String text = StatCollector.translateToLocal("botaniamisc.statusUnknown");
				x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
				y -= 1;
				mc.fontRenderer.drawString(text, x, y, color);
			}
		}
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}



	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.LAVASHROOM;
	}

	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconLava = par1IconRegister.registerIcon(LibResources.MAGIC_LAVA);
		iconDullLava = par1IconRegister.registerIcon(LibResources.DULL_LAVA);
		iconMushroom_skin = par1IconRegister.registerIcon(LibResources.LAVASHROOM_SKIN);
		iconMushroom_dullSkin = par1IconRegister.registerIcon(LibResources.LAVASHROOM_DULL_SKIN);
		iconMushroom_skinEdge = par1IconRegister.registerIcon(LibResources.LAVASHROOM_SKIN_EDGE);
		iconMushroom_dullSkinEdge = par1IconRegister.registerIcon(LibResources.LAVASHROOM_DULL_SKIN_EDGE);
		iconMushroom_stem = par1IconRegister.registerIcon(LibResources.LAVASHROOM_STEM);
		iconMushroom_inside = par1IconRegister.registerIcon(LibResources.LAVASHROOM_INSIDE);
		iconMushroom_inside_edge = par1IconRegister.registerIcon(LibResources.LAVASHROOM_INSIDE_EDGE);
		iconMushroom_inside_edge_dull = par1IconRegister.registerIcon(LibResources.LAVASHROOM_INSIDE_EDGE_DULL);
	}



	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) { return trial==0; }



	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.lavaShroom;
	}
}
