package redgear.core.collections;

import java.util.*;

/**
 * @author Blackhole
 *         Created on 10/24/2014.
 */
public class EquivalencySet<E> implements Set<E> {

    private final Equivalency<E> comparer;
    private final List<E> values = new LinkedList<E>();

    public EquivalencySet(Equivalency<E> comparer){
        this.comparer = comparer;
    }

    public EquivalencySet(Collection<E> collect, Equivalency<E> comparer){
        this(comparer);
        this.addAll(collect);
    }

    public EquivalencySet(){
        this(new Equivalent<E>());
    }


    public EquivalencySet(Collection<E> collect){
        this(collect, new Equivalent<E>());
    }


    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    private int findKey(Object search){
        for(int i = 0; i < values.size(); i++)
            if(comparer.isEquivalent(values.get(i), search))
                return i;
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return findKey(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return values.toArray(a);
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException("Null values are not allowed in EquivalencySet!");

        if(findKey(e) >= 0)
            return false;
        else
            return values.add(e);
    }
    
    public E get(Object key){
    	int index = findKey(key);
    	if(index >= 0)
    		return values.get(index);
    	else
    		return null;
    }

    @Override
    public boolean remove(Object o) {
        int index = findKey(o);

        if(index >= 0) {
            values.remove(index);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean check = true;

        for(Object obj : c)
            check &= this.contains(obj);

        return check;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean check = false;

        for(E obj : c)
            check |= this.add(obj);

        return check;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("EquivalencySet does not support the retainAll method.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean check = false;

        for(Object obj : c)
            check |= this.remove(obj);

        return check;
    }

    @Override
    public void clear() {
        values.clear();
    }

}
