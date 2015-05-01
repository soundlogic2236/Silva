package soundlogic.silva.client.model;

import soundlogic.silva.client.lib.LibResources;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelManaCrystal {

	private IModelCustom model;
	
	public ModelManaCrystal() {
		model = AdvancedModelLoader.loadModel(new ResourceLocation(LibResources.OBJ_MODEL_MANA_CRYSTAL));
	}

	public void render() {
		model.renderPart("Crystal");
	}
}
