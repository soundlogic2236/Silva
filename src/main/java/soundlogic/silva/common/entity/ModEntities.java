package soundlogic.silva.common.entity;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibEntityNames;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void preInit() {
		int id = 0;
		
		EntityRegistry.registerModEntity(EntityEaterManaBurst.class, LibEntityNames.MANA_BURST, id++, Silva.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityStoneHorse.class, LibEntityNames.STONE_HORSE, id++, Silva.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityDwarvenBarrier.class, LibEntityNames.DWARVEN_BARRIER, id++, Silva.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityDwarvenChainKnot.class, LibEntityNames.DWARVEN_CHAIN_KNOT, id++, Silva.instance, 64, 10, true);
	}

}
