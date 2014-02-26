package redgear.core.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import redgear.core.util.SimpleItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class LeveledRecipe {
	
	final RecipeMap recipe;
	final List<Level> levels;
	
	public LeveledRecipe(RecipeMap recipe){
		this(recipe, new ArrayList<Level>(0));
	}
	
	public LeveledRecipe(RecipeMap recipe, Level... levels){
		this(recipe, Arrays.asList(levels));
	}
	
	public LeveledRecipe(RecipeMap recipe, List<Level> levels){
		this.recipe = recipe;
		this.levels = levels;
	}
	
	public LeveledRecipe(String... craftMap){
		this(new RecipeMap(craftMap));
	}
	
	public LeveledRecipe(String[] craftMap, Level level){
		this(new RecipeMap(craftMap, level));
	}
	
	public LeveledRecipe(String[] craftMap, Object... charMap){
		this(new RecipeMap(craftMap, charMap));
	}
	
	public LeveledRecipe(String[] craftMap, Object[] charMap, Level... levels){
		this(new RecipeMap(craftMap, charMap), levels);
	}
	
	public LeveledRecipe(String[] craftMap, Object[] charMap, List<Level> levels){
		this(new RecipeMap(craftMap, charMap), levels);
	}
	
	
	public void addLevel(Level newLevel){
		levels.add(newLevel);
	}
	
	public void addLevel(boolean requirement, Object... data){
		addLevel(new Level(requirement,  data));
	}
	
	public Object[] compile(){
		for(Level bit : levels)
			recipe.replace(bit);
		
		return recipe.output();
	}
	
	
	public void registerShaped(ItemStack result){
		GameRegistry.addRecipe(new ShapedOreRecipe(result, compile()));
	}
	
	public void registerShapeless(ItemStack result){
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, compile()));
	}
	
	public void registerShaped(SimpleItem result, int stackSize){
		registerShaped(result.getStack(stackSize));
	}
	
	public void registerShapeless(SimpleItem result, int stackSize){
		registerShapeless(result.getStack(stackSize));
	}
	
	public void registerShaped(SimpleItem result){
		registerShaped(result, 0);
	}
	
	public void registerShapeless(SimpleItem result){
		registerShapeless(result, 0);
	}
	
}