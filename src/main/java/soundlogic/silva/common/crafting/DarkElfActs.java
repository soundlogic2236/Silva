package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.List;

import soundlogic.silva.common.crafting.recipe.IDarkElfAct;
import soundlogic.silva.common.crafting.recipe.IPortalRecipe;

public class DarkElfActs {

	public static List<IDarkElfAct> acts = new ArrayList<IDarkElfAct>();
	
	public static void addAct(IDarkElfAct act) {
		acts.add(act);
	}
	public static void addActs(List<IDarkElfAct> acts) {
		for(IDarkElfAct act : acts)
			addAct(act);
	}
}
