package soundlogic.silva.common.core.handler.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension.State;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class DimensionExposureHandlerBase implements IDimensionalExposureHandler{

	HashMap<Integer, HashMap<String,List<AttributeModifier>>> modifiers=new HashMap<Integer, HashMap<String,List<AttributeModifier>>>();
	List<PotionEffect> potions = new ArrayList<PotionEffect>();
	
	protected Random random = new Random();
	
	public DimensionExposureHandlerBase() {
	}
	
	public void init(Dimension dim) {
		switch(dim) {
		case GINNUNGAGAP:
			break;
		case VIGRIDR:
			break;
		case FOLKVANGR:
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.02,1,60*20*2);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),0.99,1,60*20*2);
			addAttributeModifier(SharedMonsterAttributes.knockbackResistance,UUID.randomUUID(),1.05,1,60*20*2);
			break;
		case VALHALLA:
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.10,1,60*20*2);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),1.10,1,60*20*2);
			addAttributeModifier(SharedMonsterAttributes.knockbackResistance,UUID.randomUUID(),0.75,1,60*20*2);
			break;
		case HELHEIM:
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),0.95,1,60*20);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),0.95,1,60*20);
			break;
		case ASGARD:
			addAttributeModifier(SharedMonsterAttributes.maxHealth,UUID.randomUUID(),2,0,60*20*3);
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.02,1,60*20*3);
			break;
		case ALFHEIM:
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),1.02,1,60*20);
			break;
		case MIDGARD:
			break;
		case JOTUNHEIMR:
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.05,1,60*20*1);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),.95,1,60*20*1);
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.15,1,60*20*3);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),.85,1,60*20*3);
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),1.25,1,60*20*5);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),.75,1,60*20*5);
			break;
		case SVARTALFHEIM:
			break;
		case MUSPELHEIM:
			break;
		case NIFLHEIM:
			addAttributeModifier(SharedMonsterAttributes.attackDamage,UUID.randomUUID(),0.95,1,60*20);
			addAttributeModifier(SharedMonsterAttributes.movementSpeed,UUID.randomUUID(),0.95,1,60*20);
			break;
		case NIDAVELLIR:
			addPotionEffect(Potion.digSpeed, 0, 60*20*1);
			addPotionEffect(Potion.digSpeed, 1, 60*20*2);
			addPotionEffect(Potion.nightVision, 0, 60*20*2);
			break;
		case VANAHEIMR:
			break;
		}
	}
	
	public void addPotionEffect(Potion potion, int amplifier, int minLevel) {
		potions.add(new PotionEffect(potion.getId(),minLevel,amplifier));
	}
	
	public void addAttributeModifier(IAttribute attribute, UUID uuid, double amount, int operation, int minLevel) {
		HashMap<String,List<AttributeModifier>> metaModifierList;
		if(modifiers.containsKey(minLevel))
			metaModifierList=modifiers.get(minLevel);
		else {
			metaModifierList=new HashMap<String,List<AttributeModifier>>();
			modifiers.put(minLevel, metaModifierList);
		}
		String attributeName = attribute.getAttributeUnlocalizedName();
		List<AttributeModifier> modifierList;
		if(metaModifierList.containsKey(attributeName))
			modifierList=metaModifierList.get(attributeName);
		else {
			modifierList=new ArrayList<AttributeModifier>();
			metaModifierList.put(attributeName, modifierList);
		}
		modifierList.add(new AttributeModifier(uuid,"DimensionalExposure",amount,operation).setSaved(false));
	}
	
	@Override
	public void onEntityUpdate(EntityLivingBase entity, int level) {
		if(level==-1)
			return;
		BaseAttributeMap map = entity.getAttributeMap();
		for(Entry<Integer, HashMap<String,List<AttributeModifier>>> entry : modifiers.entrySet()) {
			boolean shouldApply = entry.getKey() <= level;
			HashMap<String,List<AttributeModifier>> modsForLevel = entry.getValue();
			for(Entry<String,List<AttributeModifier>> entry2 : modsForLevel.entrySet()) {
				IAttributeInstance iattributeinstance = map.getAttributeInstanceByName(entry2.getKey());
				if(iattributeinstance!=null)
					for(AttributeModifier mod : entry2.getValue()) {
						iattributeinstance.removeModifier(mod);
						if(shouldApply)
							iattributeinstance.applyModifier(mod);
					}
			}
		}
		for(PotionEffect effect : potions) {
			int minLevel = effect.getDuration();
			if(minLevel <= level)
				entity.addPotionEffect(new PotionEffect(effect.getPotionID(),level - minLevel, effect.getAmplifier(),true));
		}
	}

}
