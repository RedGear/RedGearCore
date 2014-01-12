package redgear.core.api.util;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class for creating hashcodes. Is it necessary? Not really. Was it fun
 * to write? Oh yes.
 * 
 * @author BlackHole
 */
@SuppressWarnings("unused")
public final class HashHelper {

	private final static int seed = 7;
	private final static int prime = 31;

	private static int start(int seed) {
		return prime * seed;
	}

	
	private static int hash(int seed, boolean value) {
		return start(seed) + (value ? 1 : 0);
	}

	private static int hash(int seed, char value) {
		return hash((int) value);
	}

	private static int hash(int seed, int value) {
		return start(seed) + value;
	}

	private static int hash(int seed, float value) {
		return hash(seed, Float.floatToIntBits(value));
	}

	private static int hash(int seed, long value) {
		return start(seed) + (int) (value ^ value >>> 32);
	}

	private static int hash(int seed, double value) {
		return hash(seed, Double.doubleToLongBits(value));
	}

	private static int hash(int seed, Object obj) {
		if (obj == null)
			return hash(seed, 0);
		else if (obj.getClass().isArray()) {
			List<Object> values = Arrays.asList(obj);
			int ans = seed;

			for (Object val : values)
				if (val != obj) //prevents infinite recursion of an array inside itself. 
					ans = hash(ans, val);
			return ans;
		} else
			return hash(seed, obj.hashCode());

	}

	public static int hash(Object... objects) {
		int ans = seed;

		for (Object obj : objects)
			ans = hash(ans, obj);

		return ans;
	}
}
