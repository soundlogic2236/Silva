package soundlogic.silva.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.potion.Potion;

public class PotionMod extends Potion{

	protected PotionMod(int id, boolean isBad, int color) {
		super(id, isBad, color);
	}

	@Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	//NO OP
    }

	@Override
	public void affectEntity(EntityLivingBase thrower, EntityLivingBase target, int amplifer, double distance)
    {
		//NO OP
    }

	@Override
	public boolean isInstant()
    {
        return false;
    }

	@Override
    public boolean isReady(int duration, int amplifier)
    {
		return false;
    }
 
	@Override
    public Potion setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }
	
}
