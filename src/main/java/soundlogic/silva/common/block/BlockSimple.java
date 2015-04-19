package soundlogic.silva.common.block;

import soundlogic.silva.common.Silva;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockSimple extends Block{

	protected BlockSimple(Material material) {
		super(material);
		setCreativeTab(Silva.creativeTab);
	}

}
