package soundlogic.silva.client.model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelPortalUpgradeCharge extends ModelBase {
	
	int size=64;
	int offset=4;
	
	ModelRenderer[] cubeSides=new ModelRenderer[6];
	ForgeDirection[] directionSides=new ForgeDirection[] {
			ForgeDirection.UP,
			ForgeDirection.DOWN,
			ForgeDirection.WEST,
			ForgeDirection.EAST,
			ForgeDirection.SOUTH,
			ForgeDirection.NORTH,
	};
	Random random=new Random();

	public ModelPortalUpgradeCharge() {
		cubeSides[0] = new ModelRenderer(this, 0, 0);
		cubeSides[0].addBox(0F, offset, 0F, size, 0, size);

		cubeSides[1] = new ModelRenderer(this, 0, 0);
		cubeSides[1].addBox(0F, size-offset, 0F, size, 0, size);

		cubeSides[2] = new ModelRenderer(this, 0, 0);
		cubeSides[2].addBox(offset, 0F, 0F, 0, size, size);

		cubeSides[3] = new ModelRenderer(this, 0, 0);
		cubeSides[3].addBox(size-offset, 0F, 0F, 0, size, size);

		cubeSides[4] = new ModelRenderer(this, 0, 0);
		cubeSides[4].addBox(0F, 0F, offset, size, size, 0);

		cubeSides[5] = new ModelRenderer(this, 0, 0);
		cubeSides[5].addBox(0F, 0F, size-offset, size, size, 0);
	}
	
	public void renderSide(int side) {
		float f = 1F / size;
		cubeSides[side].render(f);
	}
	public ForgeDirection getDirectionSide(int side) {
		return directionSides[side];
	}
}
