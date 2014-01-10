package redgear.core.api.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import redgear.core.api.reflection.ReflectionHelper;
import cpw.mods.fml.common.Loader;

/**
 * This helper class provides static methods to create Locations without
 * requiring a hard dependency to Core,
 * but it still does require Core for the Location class itself. Any mod using
 * this interface should still depend on Core
 * and should have null checks for all of these because that's the default
 * response to the reflection failing.
 * 
 * @author BlackHole
 */
public class LocationFactory {

	public static ILocation create(int x, int y, int z) {
		return build(x, y, z);
	}

	public static ILocation create(ILocation loc) {
		return build(loc);
	}

	public static ILocation create(TileEntity tile) {
		return build(tile);
	}

	public static ILocation create(MovingObjectPosition pos) {
		return build(pos);
	}

	public static ILocation create(NBTTagCompound tag) {
		return build(tag);
	}

	public static ILocation create(NBTTagCompound tag, String name) {
		return build(tag, name);
	}

	/**
	 * This method uses reflection to find Location in core and instantiate it
	 * with the given object values.
	 * 
	 * @param args Objects to create Location with.
	 * @return A new Location created from given values, or null if something
	 * went wrong.
	 */
	private static ILocation build(Object... args) {
		if (Loader.isModLoaded("RedGear|Core"))
			return (ILocation) ReflectionHelper.constructObjectNullFail("redgear.core.world.Location", args);
		else
			return null;
	}
}
