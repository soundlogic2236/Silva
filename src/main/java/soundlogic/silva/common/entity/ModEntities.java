package soundlogic.silva.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibEntityNames;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void preInit() {
		int id = 0;
		
		register(EntityEaterManaBurst.class, LibEntityNames.MANA_BURST, id++, 64, 10, true);
		register(EntityStoneHorse.class, LibEntityNames.STONE_HORSE, id++, 80, 3, true);
		register(EntityDwarvenBarrier.class, LibEntityNames.DWARVEN_BARRIER, id++, 64, 10, true);
		register(EntityDwarvenChainKnot.class, LibEntityNames.DWARVEN_CHAIN_KNOT, id++, 160, Integer.MAX_VALUE, false);
		register(EntityEnderPearlRedirected.class, LibEntityNames.CUSTOM_ENDER_PEARL, id++, 64, 10, true);
		register(EntityFenrirEcho.class, LibEntityNames.FENRIR_ECHO, id++, 80, 3, true);
		register(EntityPhantomEndermanEcho.class, LibEntityNames.ENDERMAN_ECHO, id++, 80, 3, true);
		register(EntityNidhoggEcho.class, LibEntityNames.NIDHOGG, id++, 160, 3, true);
	}

	private static void register(Class<? extends Entity> entityClass, String entityName, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, id, Silva.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}
