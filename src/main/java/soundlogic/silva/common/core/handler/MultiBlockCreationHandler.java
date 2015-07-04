package soundlogic.silva.common.core.handler;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataBase;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataCarnilotus;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataLavashroom;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataMysticalGrinder;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomEmpty;
import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieRoomDarkenedTheater;
import soundlogic.silva.common.block.tile.multiblocks.MutliblockDataEnderCatcher;
import soundlogic.silva.common.core.handler.MultiBlockCreationHandler.RenderData.RenderFace;
import soundlogic.silva.common.item.ModItems;
import vazkii.botania.api.mana.ManaItemHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class MultiBlockCreationHandler {

	public static RenderData renderData = new RenderData();
	
	public static void clearRenderData() {
		renderData = new RenderData();
	}
	
	public static class RenderData {
		
		public int lastX = 0;
		public int lastY = 0;
		public int lastZ = 0;

		public boolean normalCube = false;
		public IIcon[] lastIcons = new IIcon[6];
		
		public ArrayList<RenderFace> faces = new ArrayList<RenderFace>();
		
		public static class RenderFace {
			public static enum Face {
				XNeg,XPos,YNeg,YPos,ZNeg,ZPos;
			}
			
			private final Face face;
			
			private final IIcon icon;
			
			private final double x;
			private final double y;
			private final double z;
			
			private final boolean field_152631_f;
			private final boolean partialRenderBounds;
			private final double renderMinX;
			private final double renderMaxX;
			private final double renderMinY;
			private final double renderMaxY;
			private final double renderMinZ;
			private final double renderMaxZ;
			private final boolean flipTexture;
			private final boolean renderFromInside;
		    private final int uvRotateEast;
		    private final int uvRotateWest;
		    private final int uvRotateSouth;
		    private final int uvRotateNorth;
		    private final int uvRotateTop;
		    private final int uvRotateBottom;
		    private final boolean enableAO;
		    private final float aoLightValueScratchXYZNNN;
		    private final float aoLightValueScratchXYNN;
		    private final float aoLightValueScratchXYZNNP;
		    private final float aoLightValueScratchYZNN;
		    private final float aoLightValueScratchYZNP;
		    private final float aoLightValueScratchXYZPNN;
		    private final float aoLightValueScratchXYPN;
		    private final float aoLightValueScratchXYZPNP;
		    private final float aoLightValueScratchXYZNPN;
		    private final float aoLightValueScratchXYNP;
		    private final float aoLightValueScratchXYZNPP;
		    private final float aoLightValueScratchYZPN;
		    private final float aoLightValueScratchXYZPPN;
		    private final float aoLightValueScratchXYPP;
		    private final float aoLightValueScratchYZPP;
		    private final float aoLightValueScratchXYZPPP;
		    private final float aoLightValueScratchXZNN;
		    private final float aoLightValueScratchXZPN;
		    private final float aoLightValueScratchXZNP;
		    private final float aoLightValueScratchXZPP;
		    private final int aoBrightnessXYZNNN;
		    private final int aoBrightnessXYNN;
		    private final int aoBrightnessXYZNNP;
		    private final int aoBrightnessYZNN;
		    private final int aoBrightnessYZNP;
		    private final int aoBrightnessXYZPNN;
		    private final int aoBrightnessXYPN;
		    private final int aoBrightnessXYZPNP;
		    private final int aoBrightnessXYZNPN;
		    private final int aoBrightnessXYNP;
		    private final int aoBrightnessXYZNPP;
		    private final int aoBrightnessYZPN;
		    private final int aoBrightnessXYZPPN;
		    private final int aoBrightnessXYPP;
		    private final int aoBrightnessYZPP;
		    private final int aoBrightnessXYZPPP;
		    private final int aoBrightnessXZNN;
		    private final int aoBrightnessXZPN;
		    private final int aoBrightnessXZNP;
		    private final int aoBrightnessXZPP;
		    private final int brightnessTopLeft;
		    private final int brightnessBottomLeft;
		    private final int brightnessBottomRight;
		    private final int brightnessTopRight;
		    private final float colorRedTopLeft;
		    private final float colorRedBottomLeft;
		    private final float colorRedBottomRight;
		    private final float colorRedTopRight;
		    private final float colorGreenTopLeft;
		    private final float colorGreenBottomLeft;
		    private final float colorGreenBottomRight;
		    private final float colorGreenTopRight;
		    private final float colorBlueTopLeft;
		    private final float colorBlueBottomLeft;
		    private final float colorBlueBottomRight;
		    private final float colorBlueTopRight;
			
		    public RenderFace(RenderBlocks render, IIcon icon, Face face, double x, double y, double z) {
		    	this.face=face;
		    	if(render.hasOverrideBlockTexture())
		    		this.icon=render.overrideBlockTexture;
		    	else
		    		this.icon=icon;
		    	
		    	this.x=x;
		    	this.y=y;
		    	this.z=z;
		    	
		    	this.field_152631_f=render.field_152631_f;
		    	this.partialRenderBounds=render.partialRenderBounds;
		    	this.renderMinX=render.renderMinX;
		    	this.renderMaxX=render.renderMaxX;
		    	this.renderMinY=render.renderMinY;
		    	this.renderMaxY=render.renderMaxY;
		    	this.renderMinZ=render.renderMinZ;
		    	this.renderMaxZ=render.renderMaxZ;
		    	this.flipTexture=render.flipTexture;
		    	this.renderFromInside=render.renderFromInside;
		    	this.uvRotateEast=render.uvRotateEast;
		    	this.uvRotateWest=render.uvRotateWest;
		    	this.uvRotateSouth=render.uvRotateSouth;
		    	this.uvRotateNorth=render.uvRotateNorth;
		    	this.uvRotateTop=render.uvRotateTop;
		    	this.uvRotateBottom=render.uvRotateBottom;
		    	this.enableAO=render.enableAO;
		    	this.aoLightValueScratchXYZNNN=render.aoLightValueScratchXYZNNN;
		    	this.aoLightValueScratchXYNN=render.aoLightValueScratchXYNN;
		    	this.aoLightValueScratchXYZNNP=render.aoLightValueScratchXYZNNP;
		    	this.aoLightValueScratchYZNN=render.aoLightValueScratchYZNN;
		    	this.aoLightValueScratchYZNP=render.aoLightValueScratchYZNP;
		    	this.aoLightValueScratchXYZPNN=render.aoLightValueScratchXYZPNN;
		    	this.aoLightValueScratchXYPN=render.aoLightValueScratchXYPN;
		    	this.aoLightValueScratchXYZPNP=render.aoLightValueScratchXYZPNP;
		    	this.aoLightValueScratchXYZNPN=render.aoLightValueScratchXYZNPN;
		    	this.aoLightValueScratchXYNP=render.aoLightValueScratchXYNP;
		    	this.aoLightValueScratchXYZNPP=render.aoLightValueScratchXYZNPP;
		    	this.aoLightValueScratchYZPN=render.aoLightValueScratchYZPN;
		    	this.aoLightValueScratchXYZPPN=render.aoLightValueScratchXYZPPN;
		    	this.aoLightValueScratchXYPP=render.aoLightValueScratchXYPP;
		    	this.aoLightValueScratchYZPP=render.aoLightValueScratchYZPP;
		    	this.aoLightValueScratchXYZPPP=render.aoLightValueScratchXYZPPP;
		    	this.aoLightValueScratchXZNN=render.aoLightValueScratchXZNN;
		    	this.aoLightValueScratchXZPN=render.aoLightValueScratchXZPN;
		    	this.aoLightValueScratchXZNP=render.aoLightValueScratchXZNP;
		    	this.aoLightValueScratchXZPP=render.aoLightValueScratchXZPP;
		    	this.aoBrightnessXYZNNN=render.aoBrightnessXYZNNN;
		    	this.aoBrightnessXYNN=render.aoBrightnessXYNN;
		    	this.aoBrightnessXYZNNP=render.aoBrightnessXYZNNP;
		    	this.aoBrightnessYZNN=render.aoBrightnessYZNN;
		    	this.aoBrightnessYZNP=render.aoBrightnessYZNP;
		    	this.aoBrightnessXYZPNN=render.aoBrightnessXYZPNN;
		    	this.aoBrightnessXYPN=render.aoBrightnessXYPN;
		    	this.aoBrightnessXYZPNP=render.aoBrightnessXYZPNP;
		    	this.aoBrightnessXYZNPN=render.aoBrightnessXYZNPN;
		    	this.aoBrightnessXYNP=render.aoBrightnessXYNP;
		    	this.aoBrightnessXYZNPP=render.aoBrightnessXYZNPP;
		    	this.aoBrightnessYZPN=render.aoBrightnessYZPN;
		    	this.aoBrightnessXYZPPN=render.aoBrightnessXYZPPN;
		    	this.aoBrightnessXYPP=render.aoBrightnessXYPP;
		    	this.aoBrightnessYZPP=render.aoBrightnessYZPP;
		    	this.aoBrightnessXYZPPP=render.aoBrightnessXYZPPP;
		    	this.aoBrightnessXZNN=render.aoBrightnessXZNN;
		    	this.aoBrightnessXZPN=render.aoBrightnessXZPN;
		    	this.aoBrightnessXZNP=render.aoBrightnessXZNP;
		    	this.aoBrightnessXZPP=render.aoBrightnessXZPP;
		    	this.brightnessTopLeft=render.brightnessTopLeft;
		    	this.brightnessBottomLeft=render.brightnessBottomLeft;
		    	this.brightnessBottomRight=render.brightnessBottomRight;
		    	this.brightnessTopRight=render.brightnessTopRight;
		    	this.colorRedTopLeft=render.colorRedTopLeft;
		    	this.colorRedBottomLeft=render.colorRedBottomLeft;
		    	this.colorRedBottomRight=render.colorRedBottomRight;
		    	this.colorRedTopRight=render.colorRedTopRight;
		    	this.colorGreenTopLeft=render.colorGreenTopLeft;
		    	this.colorGreenBottomLeft=render.colorGreenBottomLeft;
		    	this.colorGreenBottomRight=render.colorGreenBottomRight;
		    	this.colorGreenTopRight=render.colorGreenTopRight;
		    	this.colorBlueTopLeft=render.colorBlueTopLeft;
		    	this.colorBlueBottomLeft=render.colorBlueBottomLeft;
		    	this.colorBlueBottomRight=render.colorBlueBottomRight;
		    	this.colorBlueTopRight=render.colorBlueTopRight;
		    }
		    
		    public void renderFace(RenderBlocks render) {
		    	setRender(render);
		    	switch(face) {
				case XNeg:
					render.renderFaceXNeg(null, x, y, z, icon);
					break;
				case XPos:
					render.renderFaceXPos(null, x, y, z, icon);
					break;
				case YNeg:
					render.renderFaceYNeg(null, x, y, z, icon);
					break;
				case YPos:
					render.renderFaceYPos(null, x, y, z, icon);
					break;
				case ZNeg:
					render.renderFaceZNeg(null, x, y, z, icon);
					break;
				case ZPos:
					render.renderFaceZPos(null, x, y, z, icon);
					break;
		    	}
		    }
		    
		    private void setRender(RenderBlocks render) {
		    	render.field_152631_f=this.field_152631_f;
		    	render.partialRenderBounds=this.partialRenderBounds;
		    	render.renderMinX=this.renderMinX;
		    	render.renderMaxX=this.renderMaxX;
		    	render.renderMinY=this.renderMinY;
		    	render.renderMaxY=this.renderMaxY;
		    	render.renderMinZ=this.renderMinZ;
		    	render.renderMaxZ=this.renderMaxZ;
		    	render.flipTexture=this.flipTexture;
		    	render.renderFromInside=this.renderFromInside;
		    	render.uvRotateEast=this.uvRotateEast;
		    	render.uvRotateWest=this.uvRotateWest;
		    	render.uvRotateSouth=this.uvRotateSouth;
		    	render.uvRotateNorth=this.uvRotateNorth;
		    	render.uvRotateTop=this.uvRotateTop;
		    	render.uvRotateBottom=this.uvRotateBottom;
		    	render.enableAO=this.enableAO;
		    	render.aoLightValueScratchXYZNNN=this.aoLightValueScratchXYZNNN;
		    	render.aoLightValueScratchXYNN=this.aoLightValueScratchXYNN;
		    	render.aoLightValueScratchXYZNNP=this.aoLightValueScratchXYZNNP;
		    	render.aoLightValueScratchYZNN=this.aoLightValueScratchYZNN;
		    	render.aoLightValueScratchYZNP=this.aoLightValueScratchYZNP;
		    	render.aoLightValueScratchXYZPNN=this.aoLightValueScratchXYZPNN;
		    	render.aoLightValueScratchXYPN=this.aoLightValueScratchXYPN;
		    	render.aoLightValueScratchXYZPNP=this.aoLightValueScratchXYZPNP;
		    	render.aoLightValueScratchXYZNPN=this.aoLightValueScratchXYZNPN;
		    	render.aoLightValueScratchXYNP=this.aoLightValueScratchXYNP;
		    	render.aoLightValueScratchXYZNPP=this.aoLightValueScratchXYZNPP;
		    	render.aoLightValueScratchYZPN=this.aoLightValueScratchYZPN;
		    	render.aoLightValueScratchXYZPPN=this.aoLightValueScratchXYZPPN;
		    	render.aoLightValueScratchXYPP=this.aoLightValueScratchXYPP;
		    	render.aoLightValueScratchYZPP=this.aoLightValueScratchYZPP;
		    	render.aoLightValueScratchXYZPPP=this.aoLightValueScratchXYZPPP;
		    	render.aoLightValueScratchXZNN=this.aoLightValueScratchXZNN;
		    	render.aoLightValueScratchXZPN=this.aoLightValueScratchXZPN;
		    	render.aoLightValueScratchXZNP=this.aoLightValueScratchXZNP;
		    	render.aoLightValueScratchXZPP=this.aoLightValueScratchXZPP;
		    	render.aoBrightnessXYZNNN=this.aoBrightnessXYZNNN;
		    	render.aoBrightnessXYNN=this.aoBrightnessXYNN;
		    	render.aoBrightnessXYZNNP=this.aoBrightnessXYZNNP;
		    	render.aoBrightnessYZNN=this.aoBrightnessYZNN;
		    	render.aoBrightnessYZNP=this.aoBrightnessYZNP;
		    	render.aoBrightnessXYZPNN=this.aoBrightnessXYZPNN;
		    	render.aoBrightnessXYPN=this.aoBrightnessXYPN;
		    	render.aoBrightnessXYZPNP=this.aoBrightnessXYZPNP;
		    	render.aoBrightnessXYZNPN=this.aoBrightnessXYZNPN;
		    	render.aoBrightnessXYNP=this.aoBrightnessXYNP;
		    	render.aoBrightnessXYZNPP=this.aoBrightnessXYZNPP;
		    	render.aoBrightnessYZPN=this.aoBrightnessYZPN;
		    	render.aoBrightnessXYZPPN=this.aoBrightnessXYZPPN;
		    	render.aoBrightnessXYPP=this.aoBrightnessXYPP;
		    	render.aoBrightnessYZPP=this.aoBrightnessYZPP;
		    	render.aoBrightnessXYZPPP=this.aoBrightnessXYZPPP;
		    	render.aoBrightnessXZNN=this.aoBrightnessXZNN;
		    	render.aoBrightnessXZPN=this.aoBrightnessXZPN;
		    	render.aoBrightnessXZNP=this.aoBrightnessXZNP;
		    	render.aoBrightnessXZPP=this.aoBrightnessXZPP;
		    	render.brightnessTopLeft=this.brightnessTopLeft;
		    	render.brightnessBottomLeft=this.brightnessBottomLeft;
		    	render.brightnessBottomRight=this.brightnessBottomRight;
		    	render.brightnessTopRight=this.brightnessTopRight;
		    	render.colorRedTopLeft=this.colorRedTopLeft;
		    	render.colorRedBottomLeft=this.colorRedBottomLeft;
		    	render.colorRedBottomRight=this.colorRedBottomRight;
		    	render.colorRedTopRight=this.colorRedTopRight;
		    	render.colorGreenTopLeft=this.colorGreenTopLeft;
		    	render.colorGreenBottomLeft=this.colorGreenBottomLeft;
		    	render.colorGreenBottomRight=this.colorGreenBottomRight;
		    	render.colorGreenTopRight=this.colorGreenTopRight;
		    	render.colorBlueTopLeft=this.colorBlueTopLeft;
		    	render.colorBlueBottomLeft=this.colorBlueBottomLeft;
		    	render.colorBlueBottomRight=this.colorBlueBottomRight;
		    	render.colorBlueTopRight=this.colorBlueTopRight;
		    }
		}
		
		private RenderBlocksDataCatcher renderBlocks = new RenderBlocksDataCatcher();
		
		private static class RenderBlocksDataCatcher extends RenderBlocks {

			RenderData data;
		    
			public void renderFaceXNeg(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.XNeg, x, y, z);
		    	data.faces.add(renderFace);
		    }
		    public void renderFaceXPos(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.XPos, x, y, z);
		    	data.faces.add(renderFace);
		    }
		    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.YNeg, x, y, z);
		    	data.faces.add(renderFace);
		    }
		    public void renderFaceYPos(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.YPos, x, y, z);
		    	data.faces.add(renderFace);
		    }
		    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.ZNeg, x, y, z);
		    	data.faces.add(renderFace);
		    }
		    public void renderFaceZPos(Block block, double x, double y, double z, IIcon iicon) {
		    	RenderFace renderFace = new RenderFace(this, iicon, RenderFace.Face.ZPos, x, y, z);
		    	data.faces.add(renderFace);
		    }
		}

		public void buildFaces(Block block, IBlockAccess access, int x, int y, int z) {
			renderBlocks.blockAccess=access;
			renderBlocks.data=this;
			this.faces.clear();
			renderBlocks.renderBlockByRenderType(block, x, y, z);
		}
		
		public void renderFaces(RenderBlocks renderer) {
			for(RenderFace face : faces) {
				face.renderFace(renderer);
			}
		}
		
	}

	@SubscribeEvent
	public void onUse(PlayerInteractEvent event) {
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if(stack!=null && stack.getItem() == BotaniaAccessHandler.findItem("twigWand") && event.action == Action.RIGHT_CLICK_BLOCK) {
			if(event.world.isRemote) {
				Block block = event.world.getBlock(event.x, event.y, event.z);
				renderData.lastX=event.x;
				renderData.lastY=event.y;
				renderData.lastZ=event.z;
				for(int i = 0 ; i < 6 ; i++)
					renderData.lastIcons[i]=block.getIcon(event.world, event.x, event.y, event.z, i);
				if(block.isNormalCube(event.world, event.x, event.y, event.z)) {
					renderData.normalCube=true;
				}
				else {
					renderData.normalCube=false;
					renderData.buildFaces(block, event.world, event.x, event.y, event.z);
				}
			}
			else {
				Block block = event.world.getBlock(event.x, event.y, event.z);
				if(!MultiblockDataBase.multiBlocks.containsKey(block))
					return;
				List<MultiblockDataBase> list = MultiblockDataBase.multiBlocks.get(block);
				for(MultiblockDataBase multiblock : list)
					if(multiblock.tryCreate(event.world, event.x, event.y, event.z)) {
						event.setCanceled(true);
						return;
					}
			}
		}
	}
}
