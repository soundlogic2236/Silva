package soundlogic.silva.core;

import java.io.File;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class FakeWorld extends World{

	public static FakeWorld INSTANCE = new FakeWorld();
	
	private static class FakeSaveHandler implements ISaveHandler {

		@Override
		public WorldInfo loadWorldInfo() {
			return info;
		}

		@Override
		public void checkSessionLock() throws MinecraftException {
			// NO OP
		}

		@Override
		public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_,
				NBTTagCompound p_75755_2_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void saveWorldInfo(WorldInfo p_75761_1_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IPlayerFileData getSaveHandler() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void flush() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public File getWorldDirectory() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public File getMapFileFromName(String p_75758_1_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getWorldDirectoryName() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	private static class FakeWorldInfo extends WorldInfo {
		
	}
	private static class FakeWorldProvider extends WorldProvider {

		@Override
		public String getDimensionName() {
			return "";
		}
		
	}
	private static class FakeProfiler extends Profiler {
		
	}
	
	static FakeWorldInfo info;
	
	public FakeWorld() {
		super(new FakeSaveHandler(), "", new WorldSettings(new FakeWorldInfo()), new FakeWorldProvider(), new FakeProfiler());
		this.rand=new Random();
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int func_152379_p() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Entity getEntityByID(int p_73045_1_) {
		// TODO Auto-generated method stub
		return null;
	}

}
