package soundlogic.silva.client.model;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojang.authlib.GameProfile;

import soundlogic.silva.common.block.tile.multiblocks.MultiblockDataPixieFarm.FarmPixieData;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.entity.EntityPixieProxy.IEquipmentRenderData;
import soundlogic.silva.common.entity.EntityPixieProxy.PixieData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;

public class ModelPixie extends ModelBase {

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");

	ModelRenderer Body;
	ModelRenderer LeftWing;
	ModelRenderer RightWing;

	ModelRenderer bipedHead;
	ModelRenderer bipedHeadwear;

	public ModelPixie() {
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(0F, 0F, 0F, 4, 4, 4);
		Body.setRotationPoint(-2F, 16F, -2F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		LeftWing = new ModelRenderer(this, 32, 0);
		LeftWing.addBox(0F, 0F, -1F, 0, 4, 7);
		LeftWing.setRotationPoint(2F, 15F, 2F);
		LeftWing.setTextureSize(64, 32);
		LeftWing.mirror = true;
		setRotation(LeftWing, 0F, 0F, 0F);
		RightWing = new ModelRenderer(this, 50, 0);
		RightWing.addBox(0F, 0F, -1F, 0, 4, 7);
		RightWing.setRotationPoint(-2F, 15F, 2F);
		RightWing.setTextureSize(64, 32);
		RightWing.mirror = true;
		setRotation(RightWing, 0F, 0F, 0F);

        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void render(PixieData pixie, float pticks) {
		RenderManager.instance.renderEngine.bindTexture(getPixieTexture());
		GL11.glPushMatrix();
		GL11.glTranslated(pixie.prevPosX + (pixie.posX - pixie.prevPosX)
				* pticks, pixie.prevPosY + (pixie.posY - pixie.prevPosY)
				* pticks + 1.3D, pixie.prevPosZ + (pixie.posZ - pixie.prevPosZ)
				* pticks);
		GL11.glRotatef(pixie.prevRotation
				+ (pixie.rotation - pixie.prevRotation) * pticks + 180, 0, 1, 0);
		GL11.glScalef(1F, -1F, 1F);
		setRotationAngles(pixie.ticks, pticks);
		GL11.glDisable(GL11.GL_CULL_FACE);
		if (pixie.useDopplegangerRendering())
			BotaniaAccessHandler.startDopplegangerRendering();
		Body.render(0.0625F);
		LeftWing.render(0.0625F);
		RightWing.render(0.0625F);
		if (pixie.useDopplegangerRendering())
			BotaniaAccessHandler.endDopplegangerRendering();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glTranslated(0, 1.3D, 0);
		renderEquipmentItem(pixie, pticks);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		renderName(pixie);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(int ticks, float pticks) {
		RightWing.rotateAngleY = -((MathHelper
				.cos((ticks + pticks) * 1.7F * .5F) + 1F)
				/ 2F
				* (float) Math.PI * 0.5F);
		LeftWing.rotateAngleY = (MathHelper.cos((ticks + pticks) * 1.7F * .5F) + 1F)
				/ 2F * (float) Math.PI * 0.5F;
	}

	private ResourceLocation getPixieTexture() {
		return new ResourceLocation(
				BotaniaAccessHandler.LibResources.MODEL_PIXIE);
	}

    public static float NAME_TAG_RANGE = 64.0f;
	
    private String formatName(PixieData pixie) {
    	return new ChatComponentText(pixie.getDisplayName()).getFormattedText();
    }

	private void renderName(PixieData pixie) {
		if(pixie.shouldDispayName())
			renderName(pixie, 64, pixie.posX, pixie.posY, pixie.posZ);
	}
    
	private void renderName(PixieData pixie, float name_tag_range, double x, double y, double z) {
		renderName(pixie,formatName(pixie), name_tag_range, x, y, z);
	}
    
	private void renderName(PixieData pixie, String formattedName, float name_tag_range, double x, double y, double z) {
		double dx = RenderManager.instance.livingPlayer.posX-pixie.posX;
		double dy = RenderManager.instance.livingPlayer.posY-pixie.posY;
		double dz = RenderManager.instance.livingPlayer.posZ-pixie.posZ;
        double distanceSq = dx *dx + dy * dy + dz * dz;
        
        if (distanceSq < (double)(name_tag_range * name_tag_range))
        {
            FontRenderer fontrenderer = RenderManager.instance.getFontRenderer();
            float f1 = 0.016666668F * 1.6F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.0F, (float)y + .2f + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int j = fontrenderer.getStringWidth(formattedName) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
            tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(formattedName, -fontrenderer.getStringWidth(formattedName) / 2, b0, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(formattedName, -fontrenderer.getStringWidth(formattedName) / 2, b0, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
	}

	private CopyPlayer copyPlayer = new CopyPlayer();

	private static class CopyPlayer implements IEquipmentRenderData {

		public EntityPlayer player;

		@Override
		public ItemStack getEquipment() {
			return player.getCurrentEquippedItem();
		}

		@Override
		public int getItemInUseCount() {
			return player.getItemInUseCount();
		}

		@Override
		public float getPrevSwingProgress() {
			return player.prevSwingProgress;
		}

		@Override
		public float getSwingProgress() {
			return player.swingProgress;
		}

		@Override
		public float getPrevAimYawOffset() {
			return player.prevRotationYawHead-player.prevRenderYawOffset;
		}

		@Override
		public float getAimYawOffset() {
			return player.rotationYawHead-player.renderYawOffset;
		}

		@Override
		public float getPrevAimPitchOffset() {
			return player.prevRotationPitch;
		}

		@Override
		public float getAimPitchOffset() {
			return player.rotationPitch;
		}

		@Override
		public boolean isSneaking() {
			return player.isSneaking();
		}

		@Override
		public float getTicksExisted() {
			return player.ticksExisted;
		}

		@Override
		public boolean shouldRenderHeld() {
			return player.getCurrentEquippedItem()!=null;
		}

		@Override
		public ItemStack getHelmet() {
			return player.getCurrentArmor(3);
		}

		@Override
		public boolean shouldRenderHelmet() {
			return player.getCurrentArmor(3)!=null;
		}
	}

	private void renderEquipmentItem(PixieData pixie, float pticks) {
//		copyPlayer.player=Minecraft.getMinecraft().thePlayer;
//		renderEquipmentItem(pixie, copyPlayer, pticks);
		renderEquipmentItem(pixie, pixie.getEquipment(), pticks);
	}

	
	public void renderEquipmentItem(PixieData pixiebase, IEquipmentRenderData equipmentrender, float pticks) {
		if(equipmentrender==null)
			return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0F, -.05F, 0F);
		GL11.glScaled(0.51, 0.51, 0.51);
		renderHelmet(equipmentrender, pticks);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0F, -.3F, -.2F);
		GL11.glScaled(0.3, 0.3, 0.3);
		renderEquipment(equipmentrender, 1.5F, pticks);
		GL11.glPopMatrix();
	}
	    

	private static final Logger logger = LogManager.getLogger();

	private RenderBlocks renderBlocksIr = new RenderBlocks();

	private void renderHelmet(IEquipmentRenderData renderData, float pticks) {
		if(!renderData.shouldRenderHelmet())
			return;
		ItemStack helmet = renderData.getHelmet();

        if (helmet.getItem() instanceof ItemArmor)
        {
            float f1 = 1.0F;
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
        	try {
	        	RenderManager.instance.renderEngine.bindTexture(RenderBiped.getArmorResource(null, helmet, 4, null));
	            ModelBiped modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(null, helmet, 0, null);
	            if(modelbiped==null) {
		            bipedHead.render(0.0625F);
		            bipedHeadwear.render(0.0625F);
	            }
	            else {
	            	modelbiped.bipedHead.render(0.0625F);
	            	modelbiped.bipedHeadwear.render(0.0625F);
	            }
        	} catch (Exception exception) {
    			logger.error("Couldn\'t render Pixie Helmet", exception);
    		}
        	GL11.glPopMatrix();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_CULL_FACE);
        }
	}

	public void renderEquipment(IEquipmentRenderData renderData, float scaleFactorSimpleRenders, float pticks) {
		if(!renderData.shouldRenderHeld())
			return;
	    int heldItemStatus = 0;
	    boolean aimedBow = false;
	    float interpolatedSwingProgress;
	    float bipedBodyrotateAngleYCalc = 0;
	    float interpolatedAimPichRads = 0;
	    float interpolatedAimYawRads = 0;
	    float rotX = 0;
	    float rotY = 0;
	    float rotZ = 0;
	    float rotationPointX=-5F;
	    float rotationPointY=2F;
	    float rotationPointZ=0F;
	
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		ItemStack itemstack = renderData.getEquipment();
		heldItemStatus = 1;
		
		EnumAction enumaction = null;
	
		if (renderData.getItemInUseCount() > 0) {
			enumaction = itemstack.getItemUseAction();
		}

		if (enumaction == EnumAction.block) {
			heldItemStatus = 3;
		} else if (enumaction == EnumAction.bow) {
			aimedBow = true;
		}
		
		GL11.glPushMatrix();
		GL11.glScaled(-1, 1, 1);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float swingProgressDiff = renderData.getSwingProgress() - renderData.getPrevSwingProgress();
	
		if (swingProgressDiff < 0.0F) {
			++swingProgressDiff;
		}
	
		interpolatedSwingProgress = renderData.getPrevSwingProgress() + swingProgressDiff * pticks;
	
		try {
			float interpolatedAimYaw = this.interpolateRotation(renderData.getPrevAimYawOffset(),renderData.getAimYawOffset(), pticks);
			float interpolatedAimPitch = renderData.getPrevAimPitchOffset()+ (renderData.getAimPitchOffset() - renderData.getPrevAimPitchOffset()) * pticks;
			float totalAge = (float) renderData.getTicksExisted() + pticks;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			interpolatedAimYawRads = interpolatedAimYaw / (180F / (float) Math.PI);
			interpolatedAimPichRads = interpolatedAimPitch / (180F / (float) Math.PI);
			rotX = 0;
			rotZ = 0F;
	
			if (heldItemStatus != 0) {
				rotX = rotX
						* 0.5F
						- ((float) Math.PI / 10F)
						* (float) heldItemStatus;
			}
	
			rotY = 0.0F;
	
			if (interpolatedSwingProgress > -9990.0F) {
				float f1 = interpolatedSwingProgress;
				bipedBodyrotateAngleYCalc = MathHelper
						.sin(MathHelper.sqrt_float(f1) * (float) Math.PI * 2.0F) * 0.2F;
				rotationPointZ = MathHelper
						.sin(bipedBodyrotateAngleYCalc) * 5.0F;
				rotationPointX = -MathHelper
						.cos(bipedBodyrotateAngleYCalc) * 5.0F;
				rotY += bipedBodyrotateAngleYCalc;
				f1 = 1.0F - interpolatedSwingProgress;
				f1 *= f1;
				f1 *= f1;
				f1 = 1.0F - f1;
				float f2 = MathHelper.sin(f1 * (float) Math.PI);
				float f3 = MathHelper.sin(interpolatedSwingProgress
						* (float) Math.PI)
						* -(interpolatedAimPichRads - 0.7F)
						* 0.75F;
				rotX = (float) ((double) rotX - ((double) f2 * 1.2D + (double) f3));
				rotY += bipedBodyrotateAngleYCalc * 2.0F;
				rotZ = MathHelper
						.sin(interpolatedSwingProgress * (float) Math.PI) * -0.4F;
			}
	
			if (renderData.isSneaking()) {
				rotX += 0.4F;
			}
	
			rotZ += MathHelper
					.cos(totalAge * 0.09F) * 0.05F + 0.05F;
			rotX += MathHelper
					.sin(totalAge * 0.067F) * 0.05F;
	
			if (aimedBow) {
				float f1 = 0.0F;
				float f2 = 0.0F;
				rotZ = 0.0F;
				rotY = -(0.1F - f1 * 0.6F)
						+ interpolatedAimYawRads;
				rotX = -((float) Math.PI / 2F)
						+ interpolatedAimPichRads;
				rotX -= f1 * 1.2F - f2
						* 0.4F;
				rotZ += MathHelper
						.cos(totalAge * 0.09F) * 0.05F + 0.05F;
				rotX += MathHelper
						.sin(totalAge * 0.067F) * 0.05F;
			}
			
			doExtraEnumCompatibility(itemstack, enumaction, renderData, pticks);
	
			GL11.glDepthMask(true);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
	
			GL11.glPushMatrix();
            if (rotX == 0.0F && rotY == 0.0F && rotZ == 0.0F)
            {
                if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F)
                {
                    GL11.glTranslatef(rotationPointX * 0.0625F, rotationPointY * 0.0625F, rotationPointZ * 0.0625F);
                }
            }
            else
            {
                GL11.glTranslatef(rotationPointX * 0.0625F, rotationPointY * 0.0625F, rotationPointZ * 0.0625F);

                if (rotZ != 0.0F)
                {
                    GL11.glRotatef(rotZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }

                if (rotY != 0.0F)
                {
                    GL11.glRotatef(rotY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if (rotX != 0.0F)
                {
                    GL11.glRotatef(rotX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }
            }
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack,net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
			boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,itemstack,net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

			if (is3D || itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
				float scale = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				scale *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-scale, -scale, scale);
			} else if (itemstack.getItem() == Items.bow) {
				float scale = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(scale, -scale, scale);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (itemstack.getItem().isFull3D()) {
				float scale = 0.625F;

				if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (renderData.getItemInUseCount() > 0
						&& enumaction == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(scale, -scale, scale);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				float scale = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(scale, scale, scale);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				for (int pass = 0; pass < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++pass) {
					int color = itemstack.getItem().getColorFromItemStack(itemstack, pass);
					float r = (float) (color >> 16 & 255) / 255.0F;
					float g = (float) (color >> 8 & 255) / 255.0F;
					float b = (float) (color & 255) / 255.0F;
					GL11.glColor4f(r, g, b, 1.0F);
					renderItem(itemstack, pass, scaleFactorSimpleRenders, renderData);
				}
			} 
			else {
				int color = itemstack.getItem().getColorFromItemStack(itemstack,0);
				float r = (float) (color >> 16 & 255) / 255.0F;
				float g = (float) (color >> 8 & 255) / 255.0F;
				float b = (float) (color & 255) / 255.0F;
				GL11.glColor4f(r, g, b, 1.0F);
				renderItem(itemstack, 0, scaleFactorSimpleRenders, renderData);
			}

			GL11.glPopMatrix();
		} catch (Exception exception) {
			logger.error("Couldn\'t render Pixie Held Item", exception);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	private void doExtraEnumCompatibility(ItemStack stack, EnumAction enumaction, IEquipmentRenderData renderData, float pticks) {
        if (enumaction == EnumAction.eat || enumaction == EnumAction.drink)
        {
            float f6 = (float)renderData.getItemInUseCount() - pticks + 1.0F;
            float f7 = 1.0F - f6 / (float)stack.getMaxItemUseDuration();
            float f8 = 1.0F - f7;
            f8 = f8 * f8 * f8;
            f8 = f8 * f8 * f8;
            f8 = f8 * f8 * f8;
            float f9 = 1.0F - f8;
            GL11.glTranslatef(
            		0.0F, 
            		MathHelper.abs(MathHelper.cos(f6 / 4.0F * (float)Math.PI) * 0.1F) * (float)((double)f7 > 0.2D ? 1 : 0), 
            		0.0F);
            GL11.glTranslatef(
            		f9 * 0.6F *.7F, 
            		-f9 * 0.5F*.5F, 
            		f9 * 0.4F);
        }
	}

	private float interpolateRotation(float ang1, float ang2, float pticks) {
		float f;
	
		for (f = ang2 - ang1; f < -180.0F; f += 360.0F) {
			;
		}
	
		while (f >= 180.0F) {
			f -= 360.0F;
		}
	
		return ang1 + pticks * f;
	}
	
	public void renderItem(ItemStack stack, int pass, float scaleFactorSimpleRenders, IEquipmentRenderData renderData) {
		GL11.glPushMatrix();
		TextureManager texturemanager = Minecraft.getMinecraft()
				.getTextureManager();
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(item);
	
		if (stack != null && block != null
				&& block.getRenderBlockPass() != 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}
		IItemRenderer customRenderer = MinecraftForgeClient
				.getItemRenderer(stack, EQUIPPED);
		if (customRenderer != null) {
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			if (customRenderer.shouldUseRenderHelper(EQUIPPED, stack,
					EQUIPPED_BLOCK)) {
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				customRenderer.renderItem(EQUIPPED, stack, renderBlocksIr,
						(EntityLivingBase) null);
				GL11.glPopMatrix();
			} else {
				GL11.glPushMatrix();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glTranslatef(0.0F, -0.3F, 0.0F);
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
				customRenderer.renderItem(EQUIPPED, stack, renderBlocksIr,
						(EntityLivingBase) null);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glPopMatrix();
			}
		} else if (stack.getItemSpriteNumber() == 0
				&& item instanceof ItemBlock
				&& RenderBlocks.renderItemIn3d(block.getRenderType())) {
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(0));
	
			if (stack != null && block != null
					&& block.getRenderBlockPass() != 0) {
				GL11.glDepthMask(false);
				this.renderBlocksIr.renderBlockAsItem(block,
						stack.getItemDamage(), 1.0F);
				GL11.glDepthMask(true);
			} else {
				this.renderBlocksIr.renderBlockAsItem(block,
						stack.getItemDamage(), 1.0F);
			}
		} else {
			IIcon iicon = getIcon(stack, pass, renderData);
	
			if (iicon == null) {
				GL11.glPopMatrix();
				return;
			}
	
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			TextureUtil.func_152777_a(false, false, 1.0F);
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();
			float f4 = 0.0F;
			float f5 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-f4, -f5, 0.0F);
			float f6 = 1.5F;
			GL11.glScalef(f6, f6, f6);
			if(!stack.getItem().isFull3D())
				GL11.glScalef(scaleFactorSimpleRenders, scaleFactorSimpleRenders, scaleFactorSimpleRenders);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3,
					iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
	
			if (stack.hasEffect(pass)) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				texturemanager.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				float f7 = 0.76F;
				GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float f8 = 0.125F;
				GL11.glScalef(f8, f8, f8);
				float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(f9, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256,
						256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(f8, f8, f8);
				f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-f9, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256,
						256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
	
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			TextureUtil.func_147945_b();
		}
	
		if (stack != null && block != null
				&& block.getRenderBlockPass() != 0) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	
		GL11.glPopMatrix();
	}
	
	private ArrayList<Item> erroredItems = new ArrayList<Item>();
	
	private IIcon getIcon(ItemStack stack, int pass, IEquipmentRenderData renderData) {
        if (stack.getItem() == Items.bow && renderData.getItemInUseCount()>0)
        {
            int j = stack.getMaxItemUseDuration() - renderData.getItemInUseCount();

            if (j >= 18)
            {
                return Items.bow.getItemIconForUseDuration(2);
            }

            if (j > 13)
            {
                return Items.bow.getItemIconForUseDuration(1);
            }

            if (j > 0)
            {
                return Items.bow.getItemIconForUseDuration(0);
            }
        }
        try {
        	if(!erroredItems.contains(stack.getItem()))
        		return stack.getItem().getIcon(stack, pass, null, stack, renderData.getItemInUseCount());
        } catch (Exception exception) {
			erroredItems.add(stack.getItem());
		}
		return stack.getItem().getIcon(stack, pass);
	}
	
/*	public void renderEquipment(IEquipmentRenderData renderData, float scaleFactorSimpleRenders, float pticks) {
		if(!renderData.shouldRenderHeld())
			return;
	    int heldItemRightCalc = 0;
	    boolean isSneak = false;
	    boolean aimedBow = false;
	    float onGroundCalc;
	    float bipedBodyrotateAngleYCalc = 0;
	    float bipedHeadrotateAngleXCalc = 0;
	    float bipedHeadrotateAngleYCalc = 0;
	    float bipedRightArmrotateAngleXpost = 0;
	    float bipedRightArmrotateAngleYpost = 0;
	    float bipedRightArmrotateAngleZpost = 0;
	    float bipedRightArmrotationPointXpost=-5F;
	    float bipedRightArmrotationPointYpost=2F;
	    float bipedRightArmrotationPointZpost=0F;

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		ItemStack itemstack = renderData.getEquipment();
		heldItemRightCalc = 1;

		if (renderData.getItemInUseCount() > 0) {
			EnumAction enumaction = itemstack.getItemUseAction();

			if (enumaction == EnumAction.block) {
				heldItemRightCalc = 3;
			} else if (enumaction == EnumAction.bow) {
				aimedBow = true;
			}
		}

		isSneak = renderData.isSneaking();

		GL11.glPushMatrix();
		GL11.glScaled(-1, 1, 1);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f1 = renderData.getSwingProgress()
				- renderData.getPrevSwingProgress();

		if (f1 < 0.0F) {
			++f1;
		}

		onGroundCalc = renderData.getPrevSwingProgress() + f1 * pticks;

		try {
			float f2 = this.interpolateRotation(
					renderData.getPrevAimYawOffset(),
					renderData.getAimYawOffset(), pticks);
			float f13 = renderData.getPrevAimPitchOffset()
					+ (renderData.getAimPitchOffset() - renderData
							.getPrevAimPitchOffset()) * pticks;
			float f4 = (float) renderData.getTicksExisted() + pticks;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			float rm3=f4;
			float rm4=f2;
			float rm5=f13;
			bipedHeadrotateAngleYCalc = rm4
					/ (180F / (float) Math.PI);
			bipedHeadrotateAngleXCalc = rm5
					/ (180F / (float) Math.PI);
			bipedRightArmrotateAngleXpost = 0;
			bipedRightArmrotateAngleZpost = 0.0F;

			if (heldItemRightCalc != 0) {
				bipedRightArmrotateAngleXpost = bipedRightArmrotateAngleXpost
						* 0.5F
						- ((float) Math.PI / 10F)
						* (float) heldItemRightCalc;
			}

			bipedRightArmrotateAngleYpost = 0.0F;
			float _39setModelRotationAngles_f6;
			float _39setModelRotationAngles_f7;

			if (onGroundCalc > -9990.0F) {
				_39setModelRotationAngles_f6 = onGroundCalc;
				bipedBodyrotateAngleYCalc = MathHelper
						.sin(MathHelper.sqrt_float(_39setModelRotationAngles_f6) * (float) Math.PI * 2.0F) * 0.2F;
				bipedRightArmrotationPointZpost = MathHelper
						.sin(bipedBodyrotateAngleYCalc) * 5.0F;
				bipedRightArmrotationPointXpost = -MathHelper
						.cos(bipedBodyrotateAngleYCalc) * 5.0F;
				bipedRightArmrotateAngleYpost += bipedBodyrotateAngleYCalc;
				_39setModelRotationAngles_f6 = 1.0F - onGroundCalc;
				_39setModelRotationAngles_f6 *= _39setModelRotationAngles_f6;
				_39setModelRotationAngles_f6 *= _39setModelRotationAngles_f6;
				_39setModelRotationAngles_f6 = 1.0F - _39setModelRotationAngles_f6;
				_39setModelRotationAngles_f7 = MathHelper.sin(_39setModelRotationAngles_f6 * (float) Math.PI);
				float f8 = MathHelper.sin(onGroundCalc
						* (float) Math.PI)
						* -(bipedHeadrotateAngleXCalc - 0.7F)
						* 0.75F;
				bipedRightArmrotateAngleXpost = (float) ((double) bipedRightArmrotateAngleXpost - ((double) _39setModelRotationAngles_f7 * 1.2D + (double) f8));
				bipedRightArmrotateAngleYpost += bipedBodyrotateAngleYCalc * 2.0F;
				bipedRightArmrotateAngleZpost = MathHelper
						.sin(onGroundCalc * (float) Math.PI) * -0.4F;
			}

			if (isSneak) {
				bipedRightArmrotateAngleXpost += 0.4F;
			}

			bipedRightArmrotateAngleZpost += MathHelper
					.cos(rm3 * 0.09F) * 0.05F + 0.05F;
			bipedRightArmrotateAngleXpost += MathHelper
					.sin(rm3 * 0.067F) * 0.05F;

			if (aimedBow) {
				_39setModelRotationAngles_f6 = 0.0F;
				_39setModelRotationAngles_f7 = 0.0F;
				bipedRightArmrotateAngleZpost = 0.0F;
				bipedRightArmrotateAngleYpost = -(0.1F - _39setModelRotationAngles_f6 * 0.6F)
						+ bipedHeadrotateAngleYCalc;
				bipedRightArmrotateAngleXpost = -((float) Math.PI / 2F)
						+ bipedHeadrotateAngleXCalc;
				bipedRightArmrotateAngleXpost -= _39setModelRotationAngles_f6 * 1.2F - _39setModelRotationAngles_f7
						* 0.4F;
				bipedRightArmrotateAngleZpost += MathHelper
						.cos(rm3 * 0.09F) * 0.05F + 0.05F;
				bipedRightArmrotateAngleXpost += MathHelper
						.sin(rm3 * 0.067F) * 0.05F;
			}

			GL11.glDepthMask(true);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);

			float _15renderEquippedItems_f2;

			if (itemstack != null) {
				GL11.glPushMatrix();
	            if (bipedRightArmrotateAngleXpost == 0.0F && bipedRightArmrotateAngleYpost == 0.0F && bipedRightArmrotateAngleZpost == 0.0F)
	            {
	                if (bipedRightArmrotationPointXpost != 0.0F || bipedRightArmrotationPointYpost != 0.0F || bipedRightArmrotationPointZpost != 0.0F)
	                {
	                    GL11.glTranslatef(bipedRightArmrotationPointXpost * 0.0625F, bipedRightArmrotationPointYpost * 0.0625F, bipedRightArmrotationPointZpost * 0.0625F);
	                }
	            }
	            else
	            {
	                GL11.glTranslatef(bipedRightArmrotationPointXpost * 0.0625F, bipedRightArmrotationPointYpost * 0.0625F, bipedRightArmrotationPointZpost * 0.0625F);

	                if (bipedRightArmrotateAngleZpost != 0.0F)
	                {
	                    GL11.glRotatef(bipedRightArmrotateAngleZpost * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
	                }

	                if (bipedRightArmrotateAngleYpost != 0.0F)
	                {
	                    GL11.glRotatef(bipedRightArmrotateAngleYpost * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
	                }

	                if (bipedRightArmrotateAngleXpost != 0.0F)
	                {
	                    GL11.glRotatef(bipedRightArmrotateAngleXpost * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
	                }
	            }
				GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

				EnumAction enumaction = null;

				if (renderData.getItemInUseCount() > 0) {
					enumaction = itemstack.getItemUseAction();
				}

				net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient
						.getItemRenderer(
								itemstack,
								net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
				boolean is3D = (customRenderer != null && customRenderer
						.shouldUseRenderHelper(
								net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,
								itemstack,
								net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

				if (is3D
						|| itemstack.getItem() instanceof ItemBlock
						&& RenderBlocks.renderItemIn3d(Block.getBlockFromItem(
								itemstack.getItem()).getRenderType())) {
					_15renderEquippedItems_f2 = 0.5F;
					GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
					_15renderEquippedItems_f2 *= 0.75F;
					GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(-_15renderEquippedItems_f2, -_15renderEquippedItems_f2, _15renderEquippedItems_f2);
				} else if (itemstack.getItem() == Items.bow) {
					_15renderEquippedItems_f2 = 0.625F;
					GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
					GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(_15renderEquippedItems_f2, -_15renderEquippedItems_f2, _15renderEquippedItems_f2);
					GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				} else if (itemstack.getItem().isFull3D()) {
					_15renderEquippedItems_f2 = 0.625F;

					if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
						GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(0.0F, -0.125F, 0.0F);
					}

					if (renderData.getItemInUseCount() > 0
							&& enumaction == EnumAction.block) {
						GL11.glTranslatef(0.05F, 0.0F, -0.1F);
						GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
					}

					GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
					GL11.glScalef(_15renderEquippedItems_f2, -_15renderEquippedItems_f2, _15renderEquippedItems_f2);
					GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				} else {
					_15renderEquippedItems_f2 = 0.375F;
					GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
					GL11.glScalef(_15renderEquippedItems_f2, _15renderEquippedItems_f2, _15renderEquippedItems_f2);
					GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
				}

				float _15renderEquippedItems_f3;
				int _15renderEquippedItems_k;
				float _15renderEquippedItems_f12;

				if (itemstack.getItem().requiresMultipleRenderPasses()) {
					for (_15renderEquippedItems_k = 0; _15renderEquippedItems_k < itemstack.getItem().getRenderPasses(
							itemstack.getItemDamage()); ++_15renderEquippedItems_k) {
						int i = itemstack.getItem().getColorFromItemStack(
								itemstack, _15renderEquippedItems_k);
						_15renderEquippedItems_f12 = (float) (i >> 16 & 255) / 255.0F;
						_15renderEquippedItems_f3 = (float) (i >> 8 & 255) / 255.0F;
						float _15renderEquippedItems_f4 = (float) (i & 255) / 255.0F;
						GL11.glColor4f(_15renderEquippedItems_f12, _15renderEquippedItems_f3, _15renderEquippedItems_f4, 1.0F);
						renderItem(itemstack, _15renderEquippedItems_k, scaleFactorSimpleRenders);
					}
				} else {
					_15renderEquippedItems_k = itemstack.getItem().getColorFromItemStack(itemstack,
							0);
					float f11 = (float) (_15renderEquippedItems_k >> 16 & 255) / 255.0F;
					_15renderEquippedItems_f12 = (float) (_15renderEquippedItems_k >> 8 & 255) / 255.0F;
					_15renderEquippedItems_f3 = (float) (_15renderEquippedItems_k & 255) / 255.0F;
					GL11.glColor4f(f11, _15renderEquippedItems_f12, _15renderEquippedItems_f3, 1.0F);
					renderItem(itemstack, 0, scaleFactorSimpleRenders);
				}

				GL11.glPopMatrix();
			}
		} catch (Exception exception) {
			logger.error("Couldn\'t render Pixie Held Item", exception);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		aimedBow = false;
		isSneak = false;
		heldItemRightCalc = 0;
	}

	private float interpolateRotation(float ang1, float ang2, float pticks) {
		float f;

		for (f = ang2 - ang1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return ang1 + pticks * f;
	}

	public void renderItem(ItemStack stack, int pass, float scaleFactorSimpleRenders) {
		GL11.glPushMatrix();
		TextureManager texturemanager = Minecraft.getMinecraft()
				.getTextureManager();
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(item);

		if (stack != null && block != null
				&& block.getRenderBlockPass() != 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}
		IItemRenderer customRenderer = MinecraftForgeClient
				.getItemRenderer(stack, EQUIPPED);
		if (customRenderer != null) {
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			if (customRenderer.shouldUseRenderHelper(EQUIPPED, stack,
					EQUIPPED_BLOCK)) {
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				customRenderer.renderItem(EQUIPPED, stack, renderBlocksIr,
						(EntityLivingBase) null);
				GL11.glPopMatrix();
			} else {
				GL11.glPushMatrix();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glTranslatef(0.0F, -0.3F, 0.0F);
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
				customRenderer.renderItem(EQUIPPED, stack, renderBlocksIr,
						(EntityLivingBase) null);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glPopMatrix();
			}
		} else if (stack.getItemSpriteNumber() == 0
				&& item instanceof ItemBlock
				&& RenderBlocks.renderItemIn3d(block.getRenderType())) {
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(0));

			if (stack != null && block != null
					&& block.getRenderBlockPass() != 0) {
				GL11.glDepthMask(false);
				this.renderBlocksIr.renderBlockAsItem(block,
						stack.getItemDamage(), 1.0F);
				GL11.glDepthMask(true);
			} else {
				this.renderBlocksIr.renderBlockAsItem(block,
						stack.getItemDamage(), 1.0F);
			}
		} else {
			IIcon iicon = stack.getItem().requiresMultipleRenderPasses() ? stack
					.getItem().getIconFromDamageForRenderPass(
							stack.getItemDamage(), pass)
					: stack.getIconIndex();

			if (iicon == null) {
				GL11.glPopMatrix();
				return;
			}

			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			TextureUtil.func_152777_a(false, false, 1.0F);
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();
			float f4 = 0.0F;
			float f5 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-f4, -f5, 0.0F);
			float f6 = 1.5F;
			GL11.glScalef(f6, f6, f6);
			if(!stack.getItem().isFull3D())
				GL11.glScalef(scaleFactorSimpleRenders, scaleFactorSimpleRenders, scaleFactorSimpleRenders);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3,
					iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);

			if (stack.hasEffect(pass)) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				texturemanager.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				float f7 = 0.76F;
				GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float f8 = 0.125F;
				GL11.glScalef(f8, f8, f8);
				float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(f9, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256,
						256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(f8, f8, f8);
				f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-f9, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256,
						256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			texturemanager.bindTexture(texturemanager
					.getResourceLocation(stack.getItemSpriteNumber()));
			TextureUtil.func_147945_b();
		}

		if (stack != null && block != null
				&& block.getRenderBlockPass() != 0) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glPopMatrix();
	}
	*/
}