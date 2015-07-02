package soundlogic.silva.common.block.tile.multiblocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase.BlockData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibMultiblockNames;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.helper.Vector3;

public class MultiblockDataPixieRoomEmpty extends MultiblockDataBase {

	public static IIcon iconBlack;
	
	public MultiblockDataPixieRoomEmpty() {
		super(new BlockData(ModBlocks.pixieStone, 2));
		BlockData core = new BlockData(ModBlocks.pixieStone, 2);
		BlockData brick = new BlockData(ModBlocks.pixieStone, 1);
		BlockData chiseled = new BlockData(ModBlocks.pixieStone, 2);
		BlockData door = new BlockData(ModBlocks.pixieDoor, -1);
		creationRequirementsTemplate = new BlockData[][][] {
				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, core, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
				 
				{{brick, brick, brick, brick, door, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
				 
				{{brick, brick, brick, brick, door, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
				 
				{{brick, brick, brick, brick, chiseled, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
				
				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
				
				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},
			
				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},

				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}},

				{{brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick},
				 {brick, brick, brick, brick, brick, brick, brick, brick, brick}}};

		templateOrigin = new int[] {4,0,4};

		persistanceAndCreationBlocks = new BlockData[][][] {
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK_CORE, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, door, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, door, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				 
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
				
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},
			
				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},

				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.AIR, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}},

				{{BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK},
				 {BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK, BlockData.MULTIBLOCK}}};
	}

	@Override
	public IMultiblockTileData createTileData() {
		return new PixieRoomTileData();
	}
	
	public void onTick(TileMultiblockCore core) {
		PixieRoomTileData data = (PixieRoomTileData) core.getTileData();
		World world = core.getWorldObj();
		if(data.hasFarm)
			return;
		if(data.ticksTillNextSearch!=0) {
			data.ticksTillNextSearch--;
			return;
		}
		data.ticksTillNextSearch=20;
		boolean pointingX = core.rotation==0 || core.rotation==2;
		boolean pointingSign = core.rotation==0 || core.rotation==1;
		boolean foundFarm = false;
		for(TileMultiblockCore farm : MultiblockDataPixieFarm.activeFarms) {
			if(pointingX && farm.zCoord!=core.zCoord)
				continue;
			if(!pointingX && farm.xCoord!=core.xCoord)
				continue;
			if(farm.yCoord>core.yCoord+4 || farm.yCoord<core.yCoord-4)
				continue;
			if(pointingX && pointingSign && farm.xCoord<core.xCoord)
				continue;
			if(pointingX && !pointingSign && farm.xCoord>core.xCoord)
				continue;
			if(!pointingX && pointingSign && farm.zCoord<core.zCoord)
				continue;
			if(!pointingX && !pointingSign && farm.zCoord>core.zCoord)
				continue;
			foundFarm=true;
			break;
		}
		if(!foundFarm)
			return;
		for(int offset = 0 ; offset < 60 ; offset++) {
			for(int yOffset = -4 ; yOffset <= 4 ; yOffset ++) {
				int x = core.xCoord;
				int y = core.yCoord;
				int z = core.zCoord;
				y += yOffset;
				if(pointingX && pointingSign)
					x+=offset;
				if(pointingX && !pointingSign)
					x-=offset;
				if(!pointingX && pointingSign)
					z+=offset;
				if(!pointingX && !pointingSign)
					z-=offset;
				if(world.getBlock(x, y, z)==ModBlocks.multiblockCore) {
					TileMultiblockCore tile = (TileMultiblockCore) world.getTileEntity(x, y, z);
					if(tile.getData() instanceof MultiblockDataPixieFarm) {
						data.hasFarm=true;
						data.farmX=x;
						data.farmY=y;
						data.farmZ=z;
						return;
					}
				}
			}
		}
	}

	@Override
	public void onWanded(TileMultiblockBase tile, TileMultiblockCore core, EntityPlayer player, ItemStack stack) {
		if(tile.getOriginalBlock()==ModBlocks.pixieStone && tile.getOriginalMeta()==2 && tile!=core) {
			if(((PixieRoomTileData)core.getTileData()).hasFarm && roomIsEmpty(core))
				startupInterior(core);
		}
	}
	
	private boolean roomIsEmpty(TileMultiblockCore core) {
		AxisAlignedBB interior =  AxisAlignedBB.getBoundingBox(
				core.xCoord+-3, 
				core.yCoord+1, 
				core.zCoord+-3, 
				core.xCoord+3, 
				core.yCoord+7, 
				core.zCoord+3);
		return core.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, interior).isEmpty();
	}

	private void startupInterior(TileMultiblockCore core) {
		PixieRoomTileData data = (PixieRoomTileData) core.getTileData();
		World world = core.getWorldObj();
		boolean pointingX = core.rotation==0 || core.rotation==2;
		boolean pointingSign = core.rotation==0 || core.rotation==1;
		int patternX = data.farmX;
		int patternY = data.farmY-1;
		int patternZ = data.farmZ;
		if(pointingX && pointingSign)
			patternX-=3;
		if(pointingX && !pointingSign)
			patternX+=3;
		if(!pointingX && pointingSign)
			patternZ-=3;
		if(!pointingX && !pointingSign)
			patternZ+=3;
		setInteriorBasedOnBlock(core, data, world, patternX, patternY, patternZ);
	}

	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBlack = par1IconRegister.registerIcon(LibResources.ICON_BLACK);
	}
	
	private void setInteriorBasedOnBlock(TileMultiblockCore core, PixieRoomTileData data, World world, int x, int y, int z) {
		for(MultiblockDataPixieRoomBase room : MultiblockDataPixieRoomBase.pixieRooms) {
			if(room.isTemplateBlockValid(null, world, x, y, z)) {
				core.setData(room);
				PixiePowerTileData newData = (PixiePowerTileData) core.getTileData();
				newData.farmX=data.farmX;
				newData.farmY=data.farmY;
				newData.farmZ=data.farmZ;
				newData.hasFarm=true;
				newData.patternX=x;
				newData.patternY=y;
				newData.patternZ=z;
				room.onStartRoomUnactivated(core);
				core.markForVisualUpdate();
			}
		}
	}

	@Override
	public String getName() {
		return "emptyPixieRoom";
	}

	@Override
	public void onClientTick(TileMultiblockCore core) {
		PixieRoomTileData data = (PixieRoomTileData) core.getTileData();
		MultiblockDataPixieRoomBase.doPowerBeam(core, data.farmX, data.farmY, data.farmZ, 0, ClientTickHandler.forestWandRendering);
	}

	protected static ForgeDirection getDirection(TileMultiblockCore core) {
		if(core.rotation==0)
			return ForgeDirection.WEST;
		if(core.rotation==1)
			return ForgeDirection.SOUTH;
		if(core.rotation==2)
			return ForgeDirection.EAST;
		if(core.rotation==3)
			return ForgeDirection.NORTH;
		return ForgeDirection.UNKNOWN;
	}

	@Override
	public void onBreak(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void onCollision(TileMultiblockBase tile, TileMultiblockCore core, Entity ent) {
		// NO OP
	}

	@Override
	public void init(TileMultiblockCore core) {
		// NO OP
	}

	@Override
	public void setVisualData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		// NO OP
	}

	@Override
	public void setPhysicalData(TileMultiblockCore core, TileMultiblockBase tile, int x, int y, int z) {
		tile.lightValue=1;
	}

	@Override
	public void renderHUD(TileMultiblockBase tile, TileMultiblockCore core, Minecraft mc, ScaledResolution res) {
		// NO OP
	}

	@Override
	public String getUnlocalizedName() {
		return LibMultiblockNames.EMPTY_PIXIE_ROOM;
	}

	@Override
	public boolean shouldTryTransform(int trial, boolean mirrorX, boolean mirrorZ, int rot) {
		return !(mirrorX || mirrorZ);
	}

	@Override
	public LexiconEntry getLexiconEntry() {
		return LexiconData.pixie_farm_upgrades;
	}

	@Override
	public void onInvalidate(TileMultiblockCore core) {
		// NO OP
	}
}
