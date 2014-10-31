package redgear.core.collections;

/**
 * @author Blackhole
 *         Created on 10/24/2014.
 */
public final class Equivalent<T> implements Equivalency<T> {

    @Override
    public boolean isEquivalent(T key, Object value) {
        return key.equals(value);
    }
}
