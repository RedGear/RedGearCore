package redgear.core.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import redgear.core.api.util.ReflectionHelper;
import redgear.core.world.Location;
import cpw.mods.fml.common.Loader;

/**
 * This helper class provides static methods to create SimpleItems without
 * requiring a hard dependency to Core,
 * but it still does require Core for the SimpleItem class itself. Any mod using
 * this interface should still depend on Core
 * and should have null checks for all of these because that's the default
 * response to the reflection failing.
 * 
 * @author BlackHole
 */
public class SimpleItemFactory {

	/**
	 * @param stack ItemStack
	 * @return SimpleItem with the id and meta from the given ItemStack, or null
	 * if Core was not found.
	 */
	public static ISimpleItem create(ItemStack stack) {
		return build(stack);
	}

	/**
	 * @param item Item
	 * @return SimpleItem with the id of the given Item and a meta of 0, or null
	 * if Core was not found.
	 */
	public static ISimpleItem create(Item item) {
		return build(item);
	}

	/**
	 * @param block Block
	 * @return SimpleItem with the id of the given Block and a meta of 0, or
	 * null if Core was not found.
	 */
	public static ISimpleItem create(Block block) {
		return build(block);
	}

	/**
	 * @param world World to look in
	 * @param x X Coord of block
	 * @param y Y Coord of block
	 * @param z Z Coord of block
	 * @return SimpleItem with the id and meta of the block at x, y, z, in
	 * world, or null if Core was not found.
	 */
	public static ISimpleItem create(IBlockAccess world, int x, int y, int z) {
		return build(world, x, y, z);
	}

	/**
	 * @param world World to look in
	 * @param loc ILocation in world
	 * @return SimpleItem with the id and meta of the block at the location
	 * given, in world, or null if Core was not found.
	 */
	public static ISimpleItem create(IBlockAccess world, Location loc) {
		return build(world, loc);
	}

	/**
	 * @param tag NBTTagCompound to load item from
	 * @return SimpleItem with id and meta values found in tag, or 0, 0 if
	 * values not found, or null if Core was not found.
	 */
	public static ISimpleItem create(NBTTagCompound tag) {
		return build(tag);
	}

	/**
	 * @param tag NBTTagCompound to load item from
	 * @param name Name of the subtag inside tag.
	 * @return SimpleItem with id and meta values found in
	 * tag.getCompoundTag(name), or 0, 0 if values not found, or null if Core
	 * was not found.
	 */
	public static ISimpleItem create(NBTTagCompound tag, String name) {
		return build(tag, name);
	}

	/**
	 * This method uses reflection to find SimpleItem in core and instantiate it
	 * with the given object values.
	 * 
	 * @param args Objects to create SimpleItem with.
	 * @return A new SimpleItem created from given values, or null if something
	 * went wrong.
	 */
	private static ISimpleItem build(Object... args) {
		if (Loader.isModLoaded("redgear_core"))
			return (ISimpleItem) ReflectionHelper.constructObjectNullFail("redgear.core.util.SimpleItem", args);
		else
			return null;
	}
}
