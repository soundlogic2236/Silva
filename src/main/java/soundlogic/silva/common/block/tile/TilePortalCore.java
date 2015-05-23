package soundlogic.silva.common.block.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.common.block.IDustBlock;
import soundlogic.silva.common.block.IPortalFocus;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.BaseDimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import soundlogic.silva.common.core.handler.portal.DimensionalEnergyHandler;
import soundlogic.silva.common.crafting.PortalDwarfData;
import soundlogic.silva.common.crafting.PortalRecipes;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;
import soundlogic.silva.common.crafting.recipe.IPortalRecipeTransaction;
import soundlogic.silva.common.crafting.recipe.PortalRecipeTransaction;
import soundlogic.silva.common.item.ItemPapers;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ItemLexicon;

public class TilePortalCore extends TileMod{

	private static Block[][] portalShapeBlocks;
	private static int[][] portalShapeMetadata;
	private static int frameWidth;
	private static int frameHeight;
	private static int portalXZOffset;
	private static int portalYOffset;
	private static int pylonDistance;
	private static int pylonOffset;
	private static int pylonHeight;
	private static int signatureWidth = DimensionHandler.SIGNATURE_WIDTH;
	private static int signatureDepth = DimensionHandler.SIGNATURE_HEIGHT;
	private static int[][] frameMetadataTransformations;
	
	private static final String TAG_DIMENSION = "dimension";
	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_TICKS_ALL = "ticksAll";
	private static final String TAG_TICKS_ITEM = "ticksItem";
	private static final String TAG_DIRECTION = "direction";
	private static final String TAG_INVENTORY = "inventory";
	private static final String TAG_INVENTORY_SIZE = "inventorySize";
	private static final String TAG_CAN_OPEN_DARK_ELF = "darkElfPapers";
	private static final String TAG_DARK_ELF_COOLDOWN = "darkElfCooldown";
	private static final String TAG_PORTAL_FLAG = "_superPortal";
	
	private Dimension dimension;
	private int ticksOpen;
	private int ticksSinceLastItem;
	private int ticks=0;
	private ForgeDirection direction;
	private boolean darkElfPapers=false;
	private int darkElfCooldown=0;
	private ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
	
	public PortalDwarfData dwarfData=new PortalDwarfData();
	
	private List<IPortalUpgrade> upgrades = new ArrayList<IPortalUpgrade>();
	
	Random random = new Random();
	
	public static void definePortalShape() {
		Block corner=ModBlocks.bifrostBlockStairs;
		Block plain=ModBlocks.bifrostBlock;
		Block focus=ModBlocks.bifrostBlockSparkling;
		Block core=ModBlocks.portalCore;
		Block inside=Blocks.air;
		
		portalShapeBlocks = new Block[][] {
				{	corner,	plain,	focus,	plain,	corner	},
				{	plain,	inside,	inside,	inside,	plain	},
				{	focus,	inside,	inside,	inside,	focus	},
				{	plain,	inside,	inside,	inside,	plain	},
				{	corner,	plain,	core,	plain,	corner	},
			};
		portalShapeMetadata = new int[][] {
				{	-1,		0,		0,		0,		-2		},
				{	 0,		0,		0,		0,		 0		},
				{	 0,		0,		0,		0,		 0		},
				{	 0,		0,		0,		0,		 0		},
				{	-3,		0,		0,		0,		-4		},
			};
		frameWidth=5;
		frameHeight=5;
		
		portalXZOffset=-2;
		portalYOffset=-4;
		pylonDistance=3;
		pylonOffset=3;
		pylonHeight=1;
		frameMetadataTransformations = new int[][] {
				{0,2},
				{1,3},
				{4,6},
				{5,7},
			};
	}

	@Override
	public void updateEntity() {
		ticks++;
		updateUpgrades();
		tickUpgrades();
		if(darkElfCooldown>0)
			darkElfCooldown--;
		if(dimension!=null) {
			checkStability();
			if(dimension!=null) {
				ticksSinceLastItem++;
				ticksOpen++;
				drainUpkeepMana();
				pylonParticles();
				teleportItems();
				handleDwarvenSigns();
				DimensionalEnergyHandler.applyPortalTick(this);
				if(ticksSinceLastItem>=20)
					resolveRecipes();
			}
		}
	}
	
	private void tickUpgrades() {
		for(IPortalUpgrade upgrade : upgrades) {
			upgrade.onTick();
		}
	}

	private void handleDwarvenSigns() {
		if(dimension!=Dimension.NIDAVELLIR)
			return;
		TileDwarvenSign[] signs=getDwarvenSigns();
		int locked=-1;
		for(int i=0;i<signs.length;i++) {
			TileDwarvenSign sign = signs[i];
			if(sign!=null) {
				sign.core=this;
				sign.recipeSlot=i;
				if(sign.activated)
					locked=i;
			}
		}
		if(worldObj.isRemote)
			return;
		this.dwarfData.updateTrades(locked,ticks, signs, this);
	}

	private void teleportItems() {
		if(!worldObj.isRemote) {
			if(dimension==null)
				return;
			if(dimension.getState()==State.LOCKED)
				return;
			if(!this.upgradesPermitItemsThroughPortal())
				return;
			AxisAlignedBB aabb = getPortalAABB();
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
			for(EntityItem item : items) {
				if(item.isDead)
					continue;

				ItemStack stack = item.getEntityItem();
				if(stack != null && !item.getEntityData().hasKey(TAG_PORTAL_FLAG)) {
					item.setDead();
					ticksSinceLastItem=0;
					addItem(stack);
				}
			}
		}
	}

	void addItem(ItemStack stack) {
		int size = stack.stackSize;
		stack.stackSize = 1;
		for(int i = 0; i < size; i++) {
			for(IPortalUpgrade upgrade : upgrades) {
				upgrade.onItemThroughPortal(stack.copy());
			}
			inventory.add(stack.copy());
		}
	}

	private void checkStability() {
		if(checkPortalDirection(direction)==null)
			closePortal();
		boolean[][] signature = getDimensionSignature(direction);
		if(!DimensionHandler.dimensionMatchesSignature(dimension, signature))
			closePortal();
	}

	private void pylonParticles() {
		int[][] pylons=getPylonLocations(direction);
		pylonParticles(pylons[0][0],pylons[0][1],pylons[0][2]);
		pylonParticles(pylons[1][0],pylons[1][1],pylons[1][2]);
	}
	
	private void pylonParticles(int x, int y, int z) {
		if(dimension==null)
			return;
		Vector3 centerBlock = new Vector3(xCoord + 0.5, yCoord + 0.75 + (Math.random() - 0.5 * 0.25), zCoord + 0.5);
		double worldTime = ticksOpen;
		worldTime += new Random(xCoord ^ yCoord ^ zCoord).nextInt(1000);
		worldTime /= 5;

		float r = 0.75F + (float) Math.random() * 0.05F;
		double dx = x + 0.5 + Math.cos(worldTime) * r;
		double dz = z + 0.5 + Math.sin(worldTime) * r;

		Vector3 ourCoords = new Vector3(dx, y - 0.75, dz);
		centerBlock.sub(new Vector3(0, 0.5, 0));
		Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);

		Color sparkColor=dimension.getSparkColor();
		
		float red=(float) Math.random() * 0.25F + (float)sparkColor.getRed()/255F;
		float green=(float) Math.random() * 0.25F + (float)sparkColor.getGreen()/255F;
		float blue=(float) Math.random() * 0.25F + (float)sparkColor.getBlue()/255F;
		
		Botania.proxy.wispFX(worldObj, dx, yCoord + 0.25, dz, red,green,blue, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
		if(worldObj.rand.nextInt(3) == 0)
			Botania.proxy.wispFX(worldObj, dx, yCoord + 0.25, dz, red,green,blue, 0.25F + (float) Math.random() * 0.1F, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
	}

	private void drainUpkeepMana() {
		drainMana(1);
	}

	public boolean checkForPortal() {
		return getPortalDirection() != null;
	}
	
	public ForgeDirection getPortalDirection() {
		return checkPortalDirection(null);
	}
	
	private ForgeDirection checkPortalDirection(ForgeDirection direction) {
		boolean searchX = direction == null || direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH;
		boolean searchZ = direction == null || direction == ForgeDirection.EAST || direction == ForgeDirection.WEST;
		boolean onX=false;
		boolean onZ=false;
		if(searchX)
			onX = checkForPortalFrame(true);
		if(searchZ && !onX)
			onZ = checkForPortalFrame(false);
		if(!onX && !onZ)
			return null;
		ForgeDirection[] dirs;
		if(direction==null) {
			if(onX)
				dirs=new ForgeDirection[]{ForgeDirection.NORTH,ForgeDirection.SOUTH};
			else
				dirs=new ForgeDirection[]{ForgeDirection.EAST,ForgeDirection.WEST};
		}
		else
			dirs=new ForgeDirection[]{direction};
		for(ForgeDirection dir : dirs) {
			if(checkForPylons(dir))
				return dir;
		}
		return null;
	}
	
	public List<IPortalUpgrade> getUpgrades() {
		return upgrades;
	}
	
	private void updateUpgrades() {
		int[][] locs=getUpgradeLocations(direction);
		upgrades.clear();
		for(int[] loc : locs) {
			TileEntity tile = worldObj.getTileEntity(loc[0], loc[1], loc[2]);
			if(tile instanceof IPortalUpgrade) {
				IPortalUpgrade upgrade = (IPortalUpgrade) tile;
				if(upgrade.isPortalUpgrade()) {
					upgrades.add(upgrade);
					upgrade.setPortalCore(this);
				}
			}
		}
	}
	
	private int[][] getUpgradeLocations(ForgeDirection direction2) {
		boolean onX = direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH;
		
		int [][] output=new int[3][3];
		
		int x=xCoord;
		int y=yCoord;
		int z=zCoord;
		
		y+=2;
		
		if(onX) {
			x-=2;
		}
		else {
			z-=2;
		}

		output[0]=new int[]{x,y,z};
		
		if(onX) {
			x+=2 * 2;
		}
		else {
			z+=2 * 2;
		}
		
		output[1]=new int[]{x,y,z};

		y+=2;
		
		if(onX) {
			x-=2;
		}
		else {
			z-=2;
		}
		
		output[2]=new int[]{x,y,z};
		return output;
	}

	private boolean checkForPortalFrame(boolean onX) {
		for(int rowNum = 0; rowNum < frameHeight; rowNum++) {
			for(int columnNum = 0; columnNum < frameWidth; columnNum++) {
				int x=xCoord + ( onX ? portalXZOffset + columnNum : 0);
				int y=yCoord - portalYOffset-rowNum;
				int z=zCoord + (!onX ? portalXZOffset + columnNum : 0);
				Block block=portalShapeBlocks[rowNum][columnNum];
				int metadata=portalShapeMetadata[rowNum][columnNum];
				if(!checkPortalBlock(x,y,z,block,metadata, onX))
					return false;
			}
		}
		return true;
	}
	private boolean checkPortalBlock(int x,int y,int z, Block block, int metadata, boolean onX) {
		if(block == Blocks.air)
			return worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z);
		if(block == ModBlocks.bifrostBlockSparkling) {
			Block theBlock = worldObj.getBlock(x, y, z);
			return ( theBlock instanceof IPortalFocus && ((IPortalFocus)theBlock).isPortalFocus(this.worldObj,x,y,z) );
		}
		if(worldObj.getBlock(x, y, z)!=block)
			return false;
		int realMetadata;
		if(metadata>=0)
			realMetadata=metadata;
		else
			realMetadata=convertedMetadata(metadata,onX);
		if(worldObj.getBlockMetadata(x, y, z) != realMetadata)
			return false;
		return true;
	}
	
	private boolean checkForPylons(ForgeDirection direction) {
		int[][] locations=getPylonLocations(direction);

		int x1=locations[0][0];
		int y1=locations[0][1];
		int z1=locations[0][2];
		
		int x2=locations[1][0];
		int y2=locations[1][1];
		int z2=locations[1][2];

		return checkForPylon(x1,y1,z1) && checkForPylon(x2,y2,z2);
	}
	private int[][] getPylonLocations(ForgeDirection direction) {
		boolean onX = direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH;
		
		int [][] output=new int[2][3];
		
		int x=xCoord;
		int y=yCoord;
		int z=zCoord;
		
		y+=pylonHeight;
		
		if(onX) {
			x-=pylonOffset;
			z-=pylonDistance*direction.offsetZ;
		}
		else {
			z-=pylonOffset;
			x-=pylonDistance*direction.offsetX;
		}

		output[0]=new int[]{x,y,z};
		
		if(onX) {
			x+=pylonOffset * 2;
		}
		else {
			z+=pylonOffset * 2;
		}
		
		output[1]=new int[]{x,y,z};
		return output;
	}

	private int[][] getDwarfSignLocations(ForgeDirection dir) {
		boolean onX = direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH;
		
		int [][] output=new int[9][3];
		
		for(int yOffset=0;yOffset<3;yOffset++) {
			int x=xCoord;
			int y=yCoord;
			int z=zCoord;
			
			if(onX) {
				x-=2;
				z-=direction.offsetZ;
			}
			else {
				z-=2;
				x-=direction.offsetX;
			}
	
			output[yOffset*2]=new int[]{x,y+yOffset+1,z};

			if(onX) {
				x+=2 * 2;
			}
			else {
				z+=2 * 2;
			}
			
			output[yOffset*2+1]=new int[]{x,y+yOffset+1,z};
		}
		int x=xCoord;
		int y=yCoord;
		int z=zCoord;

		if(onX) {
			x-=1;
			z-=direction.offsetZ;
		}
		else {
			z-=1;
			x-=direction.offsetX;
		}

		output[6]=new int[]{x,y+4,z};

		if(onX) {
			x+=1;
		}
		else {
			z+=1;
		}

		output[7]=new int[]{x,y+4,z};

		if(onX) {
			x+=1;
		}
		else {
			z+=1;
		}

		output[8]=new int[]{x,y+4,z};

		return output;
	}
	
	public TileDwarvenSign[] getDwarvenSigns() {
		int[][] locs=getDwarfSignLocations(direction);
		TileDwarvenSign[] output=new TileDwarvenSign[9];
		for(int i=0;i<locs.length;i++) {
			int[] pos=locs[i];
			Block block=worldObj.getBlock(pos[0], pos[1], pos[2]);
			if(block==ModBlocks.dwarvenSign)
				output[i]=(TileDwarvenSign) worldObj.getTileEntity(pos[0], pos[1], pos[2]);
		}
		return output;
	}
	
	private boolean checkForPylon(int x,int y,int z) {
		return worldObj.getBlock(x, y, z) == ModBlocks.dimensionalPylon && worldObj.getBlockMetadata(x, y, z) == 0;
	}
	
	private int convertedMetadata(int fakeMetadata,boolean onX) {
		return frameMetadataTransformations[-1-fakeMetadata][onX ? 0 : 1];
	}

	public boolean onWanded() {
		NBTTagCompound cmp=new NBTTagCompound();
		this.dwarfData.writeNBT(cmp);
		if(this.dimension!=null)
			return true;
		tryOpenPortal();
		return true;
	}
	
	public void tryOpenPortal() {
		ForgeDirection direction=getPortalDirection();
		if(direction!=null) {
			boolean[][] signature = getDimensionSignature(direction);
			Dimension dim=DimensionHandler.getDimensionFromSignature(signature);
			if(dim == Dimension.SVARTALFHEIM && (!this.darkElfPapers || this.darkElfCooldown > 0)) {
				if(!darkElfPapers && drainMana(150000, direction, false)) {
					Vector3 origin=new Vector3(
							xCoord + .5,
							yCoord + 3.5,
							zCoord + .5);
					if(!this.worldObj.isRemote) {
						ItemStack stack = LexiconData.darkElfPapers.copy();
						EntityItem ent = new EntityItem(this.worldObj);
						ent.setEntityItemStack(stack);
						ent.setPosition(origin.x,origin.y,origin.z);
						this.worldObj.spawnEntityInWorld(ent);
					}
					int col = Dimension.ALFHEIM.getSparkColor().getRGB();
					for(int i = 0; i < 10; i++) {
						Vector3 point=new Vector3(
								origin.x + random.nextDouble()*6 - 3,
								origin.y + random.nextDouble()*6 - 3 ,
								origin.z + random.nextDouble()*6 - 3);
						LightningHandler.spawnLightningBolt(getWorldObj(), origin, point, 2, random.nextLong(), col, col);
					}
					drainMana(Integer.MAX_VALUE,direction, true);
				}
				this.darkElfCooldown=20*5;
				this.darkElfPapers=true;
				return;
			}
			if(dim!=null)
				openPortal(direction,dim);
		}
	}
	
	private void openPortal(ForgeDirection direction, Dimension dim) {
		if(dim.getBaseDimension().worldMatches(worldObj))
			return;
		this.direction=direction;
		if(!drainMana(150000))
			return;
		this.dimension=dim;
		this.inventory.clear();
		this.ticksOpen=0;
		this.ticksSinceLastItem=0;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void closePortal() {
		if(dimension==null)
			return;
		dimension=null;
		this.inventory.clear();
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	public ForgeDirection getDirection() {
		return direction;
	}
	public int getTicksOpen() {
		return ticksOpen;
	}
		
	private boolean drainMana(int costPerPool) {
		return drainMana(costPerPool, this.direction, true);
	}
	
	private boolean drainMana(int costPerPool, boolean doDrain) {
		return drainMana(costPerPool, this.direction, doDrain);
	}
	private boolean drainMana(int costPerPool, ForgeDirection direction, boolean doDrain) {
		int[][] pylonLocations=getPylonLocations(direction);
		IManaPool pool1=(IManaPool) worldObj.getTileEntity(pylonLocations[0][0], pylonLocations[0][1]-1, pylonLocations[0][2]);
		IManaPool pool2=(IManaPool) worldObj.getTileEntity(pylonLocations[1][0], pylonLocations[1][1]-1, pylonLocations[1][2]);
		boolean result=true;
		if(pool1==null || pool1.getCurrentMana() < costPerPool) {
			if(doDrain)
				closePortal();
			result=false;
		}
		if(pool2==null || pool2.getCurrentMana() < costPerPool) {
			if(doDrain)
				closePortal();
			result=false;
		}
		if(doDrain) {
			if(pool1!=null)
				pool1.recieveMana(-costPerPool);
			if(pool2!=null)
				pool2.recieveMana(-costPerPool);
		}
		return result;
	}

	private AxisAlignedBB getPortalAABB() {
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord + 1, zCoord, xCoord + 2, yCoord + 4, zCoord + 1);
		if(direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
			aabb = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord - 1, xCoord + 1, yCoord + 4, zCoord + 2);
		return aabb;
	}
	
	private void spawnItem(ItemStack stack) {
		EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, stack);
		item.getEntityData().setBoolean(TAG_PORTAL_FLAG, true);
		float velocity=.5F;
		item.setVelocity(-direction.offsetX * velocity, -direction.offsetY * velocity, -direction.offsetZ * velocity);
		worldObj.spawnEntityInWorld(item);
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);

		cmp.setInteger(TAG_INVENTORY_SIZE, inventory.size());
		int i = 0;
		for(ItemStack stack : inventory) {
			NBTTagCompound stackcmp = new NBTTagCompound();
			stack.writeToNBT(stackcmp);
			cmp.setTag(TAG_INVENTORY + i, stackcmp);
			i++;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);

		int count = cmp.getInteger(TAG_INVENTORY_SIZE);
		inventory.clear();
		for(int i = 0; i < count; i++) {
			NBTTagCompound stackcmp = cmp.getCompoundTag(TAG_INVENTORY + i);
			ItemStack stack = ItemStack.loadItemStackFromNBT(stackcmp);
			inventory.add(stack);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS_ALL, ticks);
		cmp.setInteger(TAG_TICKS_OPEN, ticksOpen);
		cmp.setInteger(TAG_TICKS_ITEM, ticksSinceLastItem);
		if(dimension==null)
			cmp.setInteger(TAG_DIMENSION, -1);
		else
			cmp.setInteger(TAG_DIMENSION, dimension.ordinal());
		if(direction==null)
			cmp.setInteger(TAG_DIRECTION, -1);
		else
			cmp.setInteger(TAG_DIRECTION, direction.ordinal());
		cmp.setBoolean(TAG_CAN_OPEN_DARK_ELF, darkElfPapers);
		cmp.setInteger(TAG_DARK_ELF_COOLDOWN, darkElfCooldown);
		dwarfData.writeNBT(cmp);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		ticks = cmp.getInteger(TAG_TICKS_ALL);
		ticksOpen = cmp.getInteger(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInteger(TAG_TICKS_ITEM);
		int dimNum=cmp.getInteger(TAG_DIMENSION);
		if(dimNum==-1)
			dimension = null;
		else
			dimension = Dimension.values()[dimNum];
		int dirNum=cmp.getInteger(TAG_DIRECTION);
		if(dirNum==-1)
			direction = null;
		else
			direction = ForgeDirection.values()[dirNum];
		darkElfPapers = cmp.getBoolean(TAG_CAN_OPEN_DARK_ELF);
		darkElfCooldown = cmp.getInteger(TAG_DARK_ELF_COOLDOWN);
		dwarfData.readNBT(cmp);
	}

	private void resolveRecipes() {
		if(!this.upgradesPermitTrades())
			return;
		if(inventory.size()==0)
			return;
		if(dimension==Dimension.ALFHEIM) {
			int i = 0;
			for(ItemStack stack : inventory) {
				if(stack.getItem() instanceof ILexicon) {
					((ILexicon) stack.getItem()).unlockKnowledge(stack, BotaniaAPI.elvenKnowledge);
					ItemLexicon.setForcedPage(stack, vazkii.botania.common.lexicon.LexiconData.elvenMessage.unlocalizedName);
					spawnItem(stack);
					inventory.remove(i);
					return;
				}
				i++;
			}
		}
		for(IPortalRecipe recipe : PortalRecipes.getRecipesForDimension(dimension)) {
			IPortalRecipeTransaction transaction=recipe.getTransaction(inventory,this);
			while(transaction!=null) {
				transaction.removeItems(inventory);
				transaction.doTransaction(this);
				List<ItemStack> output=transaction.getOutput();
				for(ItemStack stack : output) {
					spawnItem(stack.copy());
				}
				transaction=recipe.getTransaction(inventory,this);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	public boolean[][] getDimensionSignature(ForgeDirection direction) {
		boolean[][] output=new boolean[signatureDepth][signatureWidth];
		Block[][] signature=getDimensionSignatureBlocks(direction);
		for(int row = 0; row < signatureDepth; row++) {
			for(int column = 0; column < signatureWidth; column++) {
				output[row][column]=isSignatureBlock(signature[row][column]);
			}
		}
		return output;
	}
	
	private Block[][] getDimensionSignatureBlocks(ForgeDirection direction) {
		Block[][] output=new Block[signatureDepth][signatureWidth];
		
		for(int row = 0; row < signatureDepth; row++) {
			for(int column = 0; column < signatureWidth; column++) {
				int[] coords=getDimensionalSignatureBlockCoords(direction,row,column);
				output[row][column]=worldObj.getBlock(coords[0], coords[1], coords[2]);
			}
		}
		return output;
	}
	
	public int[] getDimensionalSignatureBlockCoords(int row, int column) {
		return getDimensionalSignatureBlockCoords(direction,row,column);
	}
	
	private int[] getDimensionalSignatureBlockCoords(ForgeDirection direction, int row, int column) {
		int x=xCoord;
		int y=yCoord;
		int z=zCoord;
		
		boolean onX = direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH;
		int otherSign = direction == ForgeDirection.SOUTH || direction == ForgeDirection.WEST ? -1 : 1;
		
		if(onX) {
			x+=otherSign * (column - (signatureWidth - 1)/2);
			z-=(row+1)*direction.offsetZ;
		}
		else {
			z+=otherSign * (column - (signatureWidth - 1)/2);
			x-=(row+1)*direction.offsetX;
		}
		
		return new int[]{x,y,z};
	}
	
	private boolean isSignatureBlock(Block block) {
		return block==Blocks.redstone_wire || ( block instanceof IDustBlock && ((IDustBlock) block).isDustMagic());
	}
	
	public boolean upgradesPermitBlockExposureTicks() {
		for(IPortalUpgrade upgrade : upgrades)
			if(!upgrade.permitBlockExposureTicks())
				return false;
		return true;
	}
	public boolean upgradesPermitEntityExposureTicks() {
		for(IPortalUpgrade upgrade : upgrades)
			if(!upgrade.permitEntityExposureTicks())
				return false;
		return true;
	}
	public boolean upgradesPermitTrades() {
		for(IPortalUpgrade upgrade : upgrades)
			if(!upgrade.permitTrades())
				return false;
		return true;
	}
	public boolean upgradesPermitItemsThroughPortal() {
		for(IPortalUpgrade upgrade : upgrades)
			if(!upgrade.permitItemsThroughPortal())
				return false;
		return true;
	}
	
	public boolean shouldRenderInPass(int pass) {
		return pass==1;
	}
}
