package redgear.core.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import redgear.core.api.item.ISimpleItem;
import cpw.mods.fml.common.registry.GameRegistry;

public class LeveledRecipe {

	final RecipeMap recipe;
	final List<Level> levels;

	public LeveledRecipe(RecipeMap recipe) {
		this(recipe, new ArrayList<Level>(0));
	}

	public LeveledRecipe(RecipeMap recipe, Level... levels) {
		this(recipe, Arrays.asList(levels));
	}

	public LeveledRecipe(RecipeMap recipe, List<Level> levels) {
		this.recipe = recipe;
		this.levels = levels;
	}

	public LeveledRecipe(String... craftMap) {
		this(new RecipeMap(craftMap));
	}

	public LeveledRecipe(String[] craftMap, Level level) {
		this(new RecipeMap(craftMap, level));
	}

	public LeveledRecipe(String[] craftMap, Object... charMap) {
		this(new RecipeMap(craftMap, charMap));
	}

	public LeveledRecipe(String[] craftMap, Object[] charMap, Level... levels) {
		this(new RecipeMap(craftMap, charMap), levels);
	}

	public LeveledRecipe(String[] craftMap, Object[] charMap, List<Level> levels) {
		this(new RecipeMap(craftMap, charMap), levels);
	}

	public void addLevel(Level newLevel) {
		levels.add(newLevel);
	}

	public void addLevel(boolean requirement, Object... data) {
		addLevel(new Level(requirement, data));
	}

	public void addLevel(boolean requirement, char key, Object value) {
		addLevel(new Level(requirement, key, value));
	}

	public void addLevel(boolean requirement, Replacement replace) {
		addLevel(new Level(requirement, replace));
	}

	public Object[] compileShaped() {
		for (Level bit : levels)
			recipe.replace(bit);

		return recipe.outputShaped();
	}

	public Object[] compileShapeless() {
		for (Level bit : levels)
			recipe.replace(bit);

		return recipe.outputShapeless();
	}

	public void registerShaped(ItemStack result) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, compileShaped()));
	}

	public void registerShapeless(ItemStack result) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, compileShapeless()));
	}

	public void registerShaped(ISimpleItem result, int stackSize) {
		registerShaped(result.getStack(stackSize));
	}

	public void registerShapeless(ISimpleItem result, int stackSize) {
		registerShapeless(result.getStack(stackSize));
	}

	public void registerShaped(ISimpleItem result) {
		registerShaped(result, 1);
	}

	public void registerShapeless(ISimpleItem result) {
		registerShapeless(result, 1);
	}

}