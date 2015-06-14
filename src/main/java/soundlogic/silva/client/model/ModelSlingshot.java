package soundlogic.silva.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSlingshot extends ModelBase {

    public ModelRenderer handle;
    public ModelRenderer bar;
    public ModelRenderer stick1;
    public ModelRenderer stick2;

    public ModelSlingshot() {
        this.textureWidth = 22;
        this.textureHeight = 9;
        this.handle = new ModelRenderer(this, 0, 2);
        this.handle.setRotationPoint(-0.5F, 18.0F, -0.5F);
        this.handle.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.bar = new ModelRenderer(this, 0, 0);
        this.bar.setRotationPoint(-5.0F, 17.0F, -0.5F);
        this.bar.addBox(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
        this.stick1 = new ModelRenderer(this, 4, 2);
        this.stick1.setRotationPoint(4.0F, 11.0F, -0.5F);
        this.stick1.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.stick2 = new ModelRenderer(this, 8, 2);
        this.stick2.setRotationPoint(-5.0F, 11.0F, -0.5F);
        this.stick2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
    }

    public void render() { 
		float f = 1F / 16F;
        this.bar.render(f);
        this.handle.render(f);
        this.stick1.render(f);
        this.stick2.render(f);
    }
    
}
