package soundlogic.silva.common.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import soundlogic.silva.client.lib.LibResources;
import soundlogic.silva.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityStoneHorse extends EntityHorse{

	public boolean overrideSaddle=false;
	
	public EntityStoneHorse(World p_i1685_1_) {
		super(p_i1685_1_);
		this.isImmuneToFire=true;
		System.out.println(this instanceof EntityHorse);
	}
	
	@Override
	public boolean canMateWith(EntityAnimal animal) {
		return false;
	}

	@Override
    public int getTemper()
    {
        return 0;
    }

	@Override
    public int getTotalArmorValue()
    {
        return super.getTotalArmorValue()+2;
    }

	@Override
    protected Item getDropItem()
    {
        return ModItems.stoneHorse;
    }

	@Override
    public String getHorseTexture()
    {
        return LibResources.MODEL_STONE_HORSE;
    }

	@SideOnly(Side.CLIENT)
	@Override
    public String[] getVariantTexturePaths()
    {
        return new String[]{getHorseTexture()};
    }

	@Override
	public boolean isHorseSaddled()
    {
        return overrideSaddle || super.isHorseSaddled();
    }

    protected boolean isMovementBlocked()
    {
    	overrideSaddle=true;
    	boolean result = super.isMovementBlocked();
    	overrideSaddle=false;
    	return result;
    }

    public void setJumpPower(int p_110206_1_)
    {
    	overrideSaddle=true;
    	super.setJumpPower(p_110206_1_);
    	overrideSaddle=false;
    }
    
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
    	overrideSaddle=true;
    	super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    	overrideSaddle=false;
    }
}
