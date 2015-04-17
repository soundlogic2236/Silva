package soundlogic.silva.client.model;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelPylon {

	private IModelCustom model;
	
	public ModelPylon() {
		model = AdvancedModelLoader.loadModel(new ResourceLocation(vazkii.botania.client.lib.LibResources.OBJ_MODEL_PYLON));
	}

	public void renderCrystal() {
		model.renderPart("Crystal");
	}

	public void renderRing() {
		model.renderAllExcept("Crystal", "Ring_Gem01", "Ring_Gem02", "Ring_Gem03", "Ring_Gem04");
	}

	public void renderGems() {
		for(int i = 1; i < 5; i++)
			model.renderPart("Ring_Gem0" + i);
	}
}
