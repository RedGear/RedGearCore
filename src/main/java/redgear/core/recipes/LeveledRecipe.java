package redgear.core.recipes;

import java.util.ArrayList;

public class LeveledRecipe {
	
	RecipeMap recipe;
	ArrayList<Level> levels;
	
	public LeveledRecipe(RecipeMap recipe){
		this.recipe = recipe;	
		this.levels = new ArrayList<Level>(0);
	}
	
	public LeveledRecipe(RecipeMap recipe, Level[] levels){
		this.recipe = recipe;
		this.levels = new ArrayList<Level>(levels.length - 1);
		
		for(Level add : levels)
			this.levels.add(add);
	}
	
	public void addLevel(Level newLevel){
		levels.add(newLevel);
	}
	
	public void addLevel(boolean requirement, Object[] data){
		addLevel(new Level(requirement,  data));
	}
	
	public Object[] register(){
		for(Level bit : levels)
			recipe.replace(bit);
		
		return recipe.output();
	}

}