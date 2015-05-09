package soundlogic.silva.common.entity;

import java.util.ArrayList;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.subtile.functional.SubTileRhododender;
import soundlogic.silva.common.core.handler.ChunkHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.ISubTileContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityEnderPearlRedirected extends EntityEnderPearl implements ChunkHandler.IChunkLoadingEntity{

	final String TAG_REDIRECTS = "redirects";
	final String TAG_REDIRECTS_SIZE = "redirectsSize";
	Ticket ticket;
	ArrayList<int[]> redirects = new ArrayList<int[]>();
	
	public static EntityEnderPearlRedirected tryRedirectPearl(EntityEnderPearl pearl, int x, int y, int z, double motionX, double motionY, double motionZ) {
		if(pearl instanceof EntityEnderPearlRedirected)
			if(((EntityEnderPearlRedirected) pearl).containsRedirect(x, y, z))
				return null;
		return getRedirectedPearl(pearl,x,y,z,motionX,motionY,motionZ);
	}
	
	static EntityEnderPearlRedirected getRedirectedPearl(EntityEnderPearl pearl, int x, int y, int z, double motionX, double motionY, double motionZ) {
		EntityEnderPearlRedirected result;
		boolean newPearl;
		if(pearl instanceof EntityEnderPearlRedirected) {
			result = (EntityEnderPearlRedirected) pearl;
			newPearl = false;
		}
		else {
			result = new EntityEnderPearlRedirected(pearl);
			newPearl = true;
		}
		result.setPosition(x+.5, y+.5, z+.5);
		result.setVelocity(motionX, motionY, motionZ);
		result.addRedirect(x, y, z);
		if(newPearl)
			pearl.worldObj.spawnEntityInWorld(result);
		return result;
	}
	
	
	
	public EntityEnderPearlRedirected(World p_i1782_1_) {
		super(p_i1782_1_);
    	ChunkHandler.registerEntity(this);
	}

    public EntityEnderPearlRedirected(EntityEnderPearl pearl) {
    	super(pearl.worldObj);
    	NBTTagCompound cmp = new NBTTagCompound();
    	pearl.writeToNBT(cmp);
    	this.readFromNBT(cmp);
    	pearl.setDead();
    	ChunkHandler.registerEntity(this);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound cmp) {
    	super.writeEntityToNBT(cmp);
    	cmp.setInteger(TAG_REDIRECTS_SIZE, redirects.size());
    	for(int i = 0 ; i < redirects.size() ; i ++) {
    		cmp.setInteger(TAG_REDIRECTS+i+"x", redirects.get(i)[0]);
    		cmp.setInteger(TAG_REDIRECTS+i+"y", redirects.get(i)[1]);
    		cmp.setInteger(TAG_REDIRECTS+i+"z", redirects.get(i)[2]);
    	}
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound cmp) {
    	super.readEntityFromNBT(cmp);
    	if(cmp.hasKey(TAG_REDIRECTS_SIZE)) {
    		redirects.clear();
    		int size = cmp.getInteger(TAG_REDIRECTS_SIZE);
    		for(int i = 0; i < size ; i++) {
    			int x = cmp.getInteger(TAG_REDIRECTS+i+"x");
    			int y = cmp.getInteger(TAG_REDIRECTS+i+"y");
    			int z = cmp.getInteger(TAG_REDIRECTS+i+"z");
    			redirects.add(new int[]{x,y,z});
    		}
    	}
    }
    
    public void addRedirect(int x, int y, int z) {
    	redirects.add(new int[]{x,y,z});
    }
    
    public boolean containsRedirect(int x, int y, int z) {
    	for(int[] coords : redirects) {
    		if(coords[0]==x && coords[1] == y && coords[2] == z)
    			return true;
    	}
    	return false;
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	if(!this.isDead)
    		ChunkHandler.updateChunkForcing(this);
    }
    
    @Override
    protected void onImpact(MovingObjectPosition pos)
    {
    	if(pos.typeOfHit==pos.typeOfHit.BLOCK) {
    		TileEntity tile = worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
    		if(tile!=null && tile instanceof ISubTileContainer) {
    			ISubTileContainer container = (ISubTileContainer) tile;
    			if(container.getSubTile() instanceof SubTileRhododender)
    				return;
    		}
    	}
    	
        if (pos.entityHit != null)
        {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote)
        {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.getThrower();

                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == this.worldObj)
                {
                	float damage = 5.0F+1F*redirects.size();
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, damage);
                    if (!MinecraftForge.EVENT_BUS.post(event))
                    { // Don't indent to lower patch size
                    if (this.getThrower().isRiding())
                    {
                        this.getThrower().mountEntity((Entity)null);
                    }

                    this.getThrower().setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    this.getThrower().fallDistance = 0.0F;
                    this.getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }
                }
            }

            this.setDead();
        }
    }

	@Override
	public Ticket getTicket() {
		if(worldObj.isRemote)
			return null;
		if(ticket==null) {
	        if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP)
	        {
	        	ticket = ForgeChunkManager.requestPlayerTicket(
	        			Silva.instance,
	        			((EntityPlayerMP)this.getThrower()).getCommandSenderName(),
	        			worldObj,
	        			Type.ENTITY);
	        	ticket.bindEntity(this);
	        }
		}
		return ticket;
	}
	
	

	@Override
	public ChunkCoordIntPair getChunk() {
		return new ChunkCoordIntPair(this.chunkCoordX, this.chunkCoordZ);
	}

    @Override
    public void setDead() {
    	if(ticket!=null)
    		ForgeChunkManager.releaseTicket(ticket);
        super.setDead();
    }

	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public ChunkCoordIntPair getNextChunk() {
		int ticks=2;
		return worldObj.getChunkFromBlockCoords((int)(this.posX+this.motionX*ticks), (int)(this.posZ+this.motionZ*ticks)).getChunkCoordIntPair();
	}

	@Override
	public ChunkCoordIntPair getPrevChunk() {
		return worldObj.getChunkFromBlockCoords((int)(this.prevPosX), (int)(this.prevPosZ)).getChunkCoordIntPair();
	}
}
