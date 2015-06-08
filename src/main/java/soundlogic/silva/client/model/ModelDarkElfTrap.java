package soundlogic.silva.client.model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelDarkElfTrap extends ModelBase {
	
	ModelRenderer base;
	ModelRenderer side1;
	ModelRenderer side2;
	ModelRenderer side3;
	ModelRenderer side4;
	ModelRenderer floor0;
	ModelRenderer floor1;
	ModelRenderer floor2;
	ModelRenderer floor3;
	ModelRenderer floor4;
	ModelRenderer spike1;
	ModelRenderer spike2;
	ModelRenderer spike3;
	ModelRenderer spike4;
	ModelRenderer seperator;
	ModelRenderer crystalHolder;
	ModelPylon crystal;
	ModelRenderer bowl1;
	ModelRenderer bowl2;

	public ModelDarkElfTrap() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.spike2 = new ModelRenderer(this, 30, 20);
        this.spike2.setRotationPoint(-7.0F, 15.0F, -7.0F);
        this.spike2.addBox(11.0F, 2.0F, 2.0F, 1, 5, 1, 0.0F);
        this.floor3 = new ModelRenderer(this, 0, 32);
        this.floor3.setRotationPoint(-8.0F, 21.0F, -14.0F);
        this.floor3.addBox(2.0F, 1.0F, 7.0F, 12, 1, 6, 0.0F);
        this.bowl2 = new ModelRenderer(this, 0, 3);
        this.bowl2.setRotationPoint(-8.0F, 21.0F, -8.0F);
        this.bowl2.addBox(12.0F, 1.0F, 7.0F, 2, 1, 2, 0.0F);
        this.spike4 = new ModelRenderer(this, 30, 20);
        this.spike4.setRotationPoint(-7.0F, 15.0F, -7.0F);
        this.spike4.addBox(11.0F, 2.0F, 11.0F, 1, 5, 1, 0.0F);
        this.bowl1 = new ModelRenderer(this, 0, 0);
        this.bowl1.setRotationPoint(-9.0F, 21.0F, -8.0F);
        this.bowl1.addBox(3.0F, 1.0F, 7.0F, 2, 1, 2, 0.0F);
        this.floor2 = new ModelRenderer(this, 0, 39);
        this.floor2.setRotationPoint(-8.0F, 21.0F, -8.0F);
        this.floor2.addBox(14.0F, 1.0F, 1.0F, 1, 1, 14, 0.0F);
        this.side3 = new ModelRenderer(this, 34, 27);
        this.side3.setRotationPoint(-8.0F, 21.0F, -7.0F);
        this.side3.addBox(0.0F, 0.0F, 0.0F, 1, 2, 14, 0.0F);
        this.floor1 = new ModelRenderer(this, 0, 39);
        this.floor1.setRotationPoint(-8.0F, 21.0F, -8.0F);
        this.floor1.addBox(1.0F, 1.0F, 1.0F, 1, 1, 14, 0.0F);
        this.side4 = new ModelRenderer(this, 34, 27);
        this.side4.setRotationPoint(7.0F, 21.0F, -7.0F);
        this.side4.addBox(0.0F, 0.0F, 0.0F, 1, 2, 14, 0.0F);
        this.floor4 = new ModelRenderer(this, 0, 54);
        this.floor4.setRotationPoint(-8.0F, 22.0F, 0.0F);
        this.floor4.addBox(4.0F, 0.0F, -1.0F, 8, 1, 2, 0.0F);
        this.spike1 = new ModelRenderer(this, 30, 20);
        this.spike1.setRotationPoint(-7.0F, 15.0F, -7.0F);
        this.spike1.addBox(2.0F, 2.0F, 2.0F, 1, 5, 1, 0.0F);
        this.side1 = new ModelRenderer(this, 30, 45);
        this.side1.setRotationPoint(-8.0F, 21.0F, -8.0F);
        this.side1.addBox(0.0F, 0.0F, 0.0F, 16, 2, 1, 0.0F);
        this.crystalHolder = new ModelRenderer(this, 0, 8);
        this.crystalHolder.setRotationPoint(-8.0F, 17.0F, -8.0F);
        this.crystalHolder.addBox(7.0F, 3.0F, 7.0F, 2, 1, 2, 0.0F);
        this.seperator = new ModelRenderer(this, 0, 17);
        this.seperator.setRotationPoint(-8.0F, 19.0F, -8.0F);
        this.seperator.addBox(6.0F, 2.0F, 1.0F, 4, 1, 14, 0.0F);
        this.side2 = new ModelRenderer(this, 30, 45);
        this.side2.setRotationPoint(-8.0F, 21.0F, 7.0F);
        this.side2.addBox(0.0F, 0.0F, 0.0F, 16, 2, 1, 0.0F);
        this.floor0 = new ModelRenderer(this, 0, 32);
        this.floor0.setRotationPoint(-8.0F, 21.0F, 0.0F);
        this.floor0.addBox(2.0F, 1.0F, 1.0F, 12, 1, 6, 0.0F);
        this.base = new ModelRenderer(this, 0, 0);
        this.base.setRotationPoint(-8.0F, 23.0F, -8.0F);
        this.base.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
        this.spike3 = new ModelRenderer(this, 30, 20);
        this.spike3.setRotationPoint(-7.0F, 15.0F, -7.0F);
        this.spike3.addBox(2.0F, 2.0F, 11.0F, 1, 5, 1, 0.0F);
		crystal = new ModelPylon();
	}
	
	public void renderBase() {
		float f = 1F / 16F;
		base.render(f);
		side1.render(f);
		side2.render(f);
		side3.render(f);
		side4.render(f);
		floor0.render(f);
		floor1.render(f);
		floor2.render(f);
		floor3.render(f);
		floor4.render(f);
		seperator.render(f);
		crystalHolder.render(f);
	}
	public void renderSpikes() {
		float f = 1F / 16F;
		spike1.render(f);
		spike2.render(f);
		spike3.render(f);
		spike4.render(f);
	}
	public void renderCrystal() {
		crystal.renderCrystal();
	}
	public void renderBowls(int bowlnum) {
		float f = 1F / 16F;
		if(bowlnum==0)
			bowl1.render(f);
		if(bowlnum==1)
			bowl2.render(f);
	}
	
}
