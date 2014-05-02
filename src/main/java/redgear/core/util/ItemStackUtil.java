package redgear.core.util;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtil
{
    /**
     * Basically a copy of dropBlockAsItem_do in Block, but public and static so anything can use it.
     * @param world A reference to the world
     * @param x The x coord
     * @param y The y coord
     * @param z The z coord
     * @param stack The ItemStack to drop
     */
    public static void dropItemStack(World world, int x, int y, int z, ItemStack stack)
    {
        if (world == null || stack == null || stack.stackSize <= 0)
        {
            return;
        }

        //if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        //{
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        //}
    }
    
    public static boolean areStacksEqualWithSizeAndTags(ItemStack first, ItemStack second){
    	return areStacksEqualWithTags(first, second) && first.stackSize == second.stackSize;
    }

    /**
     * Compares two ItemStacks. Unlike the default ItemStack.equals()
     * this one returns true if any one stack uses OreDictionary.WILDCARD_VALUE
     * as it's meta
     * @param first
     * @param second
     * @return True if the item ID and Metas are the same OR if the ID's are the same and at least one has a wildcard meta
     */
    public static boolean areStacksEqualWithTags(ItemStack first, ItemStack second){
        return areStacksEqual(first, second) && ItemStack.areItemStackTagsEqual(first, second);
    }
    
    public static boolean areStacksEqualWithSize(ItemStack first, ItemStack second){
    	return areStacksEqual(first, second) && first.stackSize == second.stackSize;
    }
    
    public static boolean areStacksEqual(ItemStack first, ItemStack second){
    	return new SimpleItem(first).equals(second);
    }
    
    public static int hashStack(ItemStack stack){
    	return new SimpleItem(stack).hashCode();
    }

    /**
     * Changes the stackSize of the ItemStack passed to newsize.
     * This is useful because calling it requires only a single line instead of three.
     * (One to declare the stack, one to change the size, one to use the stack)
     * @param input
     * @param newSize
     * @return
     */
    public static ItemStack setStackSize(ItemStack input, int newSize)
    {
        if (input != null)
        {
            input.stackSize = newSize;
        }

        return input;
    }

    public static ItemStack getOreWithName(String name)
    {
        return getOreWithName(name, 1);
    }

    /**
     *
     * @param name
     * @param size
     * @return
     */
    public static ItemStack getOreWithName(String name, int size)
    {
        ArrayList<ItemStack> list = OreDictionary.getOres(name);

        if (list.isEmpty())
        {
            return null;
        }

        ItemStack first = list.get(0);
        first.stackSize = size;
        return first;
    }
}