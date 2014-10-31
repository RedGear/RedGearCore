package redgear.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import redgear.core.api.item.ISimpleItem;
import redgear.core.collections.Equivalency;
import redgear.core.collections.EquivalencySet;
import redgear.core.util.ItemRegUtil;
import redgear.core.util.StringHelper;

import java.util.Set;

/**
 * @author Blackhole
 *         Created on 10/24/2014.
 */
public class ShapelessSimpleRecipe implements IRecipe {

    private ItemStack output;
    private Set<ISimpleItem> input = new EquivalencySet<ISimpleItem>(new Equivalency<ISimpleItem>() {
        @Override
        public boolean isEquivalent(ISimpleItem key, Object value) {
            if(value instanceof ISimpleItem)
                return key.isItemEqual((ISimpleItem) value, false);
            else
                return false;
        }
    });

    public ShapelessSimpleRecipe(ItemStack output, Object... input ){
        this.output = output.copy();
        for(Object obj : input){
            ISimpleItem in = ItemRegUtil.parse(obj);

            if(in == null)
                throw new ClassCastException(StringHelper.concat("Invalid shapeless simple recipe: ", this.output, ", Offending object: ", obj));

            this.input.add(in);
        }
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
