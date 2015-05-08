package soundlogic.silva.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.client.render.multiblock.RenderMultiblock;
import soundlogic.silva.common.block.BlockPortalUpgradeCharge;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelPool;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class RenderTileMultiblockCore extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float ticks) {
		TileMultiblockCore tile = (TileMultiblockCore) p_147500_1_;
		RenderMultiblock.renderTick(tile,x,y,z,ticks);
    }
}
