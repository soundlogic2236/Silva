package soundlogic.silva.common.entity;

import soundlogic.silva.common.Silva;
import soundlogic.silva.common.lib.LibEntityNames;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void preInit() {
		int id = 0;
		
		EntityRegistry.registerModEntity(EntityManaEaterBurst.class, LibEntityNames.MANA_BURST, id++, Silva.instance, 64, 10, true);
	}

}
