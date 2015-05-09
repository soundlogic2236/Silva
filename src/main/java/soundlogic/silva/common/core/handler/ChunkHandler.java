package soundlogic.silva.common.core.handler;

import java.util.List;

import soundlogic.silva.common.Silva;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;

import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.ForgeChunkManager;

public class ChunkHandler implements LoadingCallback {

	public ChunkHandler() {
		ForgeChunkManager.setForcedChunkLoadingCallback(Silva.instance, this);
	}
	
    @SubscribeEvent
    public void entityEnteredChunk(EntityEvent.EnteringChunk event) {
        Entity entity = event.entity;
        if (entity instanceof IChunkLoadingEntity) {
            if (!entity.worldObj.isRemote && !entity.isDead) {
            	ChunkHandler.setEntityChunksLoaded((IChunkLoadingEntity)entity);
            }
        }
    }
    
    public static void registerEntity(IChunkLoadingEntity entity) {
    	if(!entity.getWorld().isRemote)
    		setEntityChunksLoaded(entity);
    }
    
    public static void updateChunkForcing(IChunkLoadingEntity entity) {
		setEntityChunksLoaded(entity);
    }
    
    private static void setEntityChunksLoaded(IChunkLoadingEntity entity) {
    	Ticket ticket = entity.getTicket();
    	if(ticket==null)
    		return;
    	boolean needsUpdate = false;
    	ChunkCoordIntPair chunk = entity.getChunk();
    	ChunkCoordIntPair nextChunk = entity.getNextChunk();
    	ImmutableSet<ChunkCoordIntPair> chunks = ticket.getChunkList();
    	if(chunks.size()==1 && !chunk.equals(nextChunk))
    		needsUpdate=true;
    	else if(chunks.size()==2)
    		needsUpdate=true;
    	else if(chunk.equals(entity.getPrevChunk()))
    		needsUpdate=true;
    	if(!needsUpdate)
    		return;
    	for(ChunkCoordIntPair curChunk : chunks) {
    		ForgeChunkManager.unforceChunk(ticket, curChunk);
    	}
    	ForgeChunkManager.forceChunk(ticket, entity.getChunk());
    	if(!chunk.equals(nextChunk))
    		ForgeChunkManager.forceChunk(ticket, entity.getNextChunk());
    }
    
    
    public static interface IChunkLoadingEntity {
    	public Ticket getTicket();
    	public ChunkCoordIntPair getPrevChunk();
    	public ChunkCoordIntPair getChunk();
    	public ChunkCoordIntPair getNextChunk();
    	public World getWorld();
    }

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		// TODO Auto-generated method stub
		
	}
    
}
