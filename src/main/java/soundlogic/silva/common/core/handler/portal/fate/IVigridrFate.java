package soundlogic.silva.common.core.handler.portal.fate;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCache;

public interface IVigridrFate {

	public boolean canApplyToEntity(Entity entity);
	public boolean canApplyPeaceful(Entity entity);
	public boolean isFateFinished();
	public void writeToNBT(NBTTagCompound cmp);
	public void readFromNBT(NBTTagCompound cmp);
	public void tickFate();
	public void init();
	public int getWeight(Entity entity, int exposureLevel);
	public void setEntity(Entity entity);
	public void endFate();
	public int getStateCode();
	public void setName(String name);
	public String getName();
	public void setKey(int key);
	public int getKey();
	public IChatComponent getStartMessage();
	public boolean tryForceStart(int tries);
	@SideOnly(Side.CLIENT)
	public void renderWorld(WorldRenderer renderer, RenderBlocks renderBlocks, ChunkCache chunkCache, int pass);
	@SideOnly(Side.CLIENT)
	public void renderWorldForPlayer(WorldRenderer renderer, RenderBlocks renderBlocks, ChunkCache chunkCache, int pass);
	@SideOnly(Side.CLIENT)
	public boolean renderEntity(EntityLivingBase entity, RendererLivingEntity renderer, double x, double y, double z);
	@SideOnly(Side.CLIENT)
	public boolean renderPlayer(Entity entity, RenderPlayer renderer, float partialRenderTick);
}
