package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.crafting.recipe.AlfheimPortalRecipeWrapper;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;

public class PortalRecipes {

	public static Map<Dimension,ArrayList<IPortalRecipe>> recipes = new HashMap<Dimension,ArrayList<IPortalRecipe>>();
	
	public static ArrayList<IPortalRecipe> getRecipesForDimension(Dimension dimension) {
		if(recipes.containsKey(dimension))
			return new ArrayList<IPortalRecipe>(recipes.get(dimension));
		return new ArrayList<IPortalRecipe>();
	}
	
	public static void addRecipe(Dimension dimension,IPortalRecipe recipe) {
		if(recipe==null)
			return;
		if(recipes.containsKey(dimension))
			recipes.get(dimension).add(recipe);
		else {
			ArrayList<IPortalRecipe> array=new ArrayList<IPortalRecipe>();
			array.add(recipe);
			recipes.put(dimension, array);
		}
	}
	
	public static void preInit() {
		PortalRecipes.addRecipe(Dimension.ALFHEIM, new AlfheimPortalRecipeWrapper());
	}
}
