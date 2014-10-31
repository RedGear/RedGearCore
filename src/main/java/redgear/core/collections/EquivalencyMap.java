package redgear.core.collections;

import java.util.*;

/**
 * @author Blackhole
 *         Created on 10/24/2014.
 */
public class EquivalencyMap<K, V> implements Map<K, V> {

    private Equivalency<K> comparer;
    private List<EquivalencyEntry<K, V>> values = new LinkedList<EquivalencyEntry<K, V>>();

    public EquivalencyMap(Equivalency<K> comparer){
        this.comparer = comparer;
    }

    public EquivalencyMap(){
        this(new Equivalent<K>());
    }

    private EquivalencyEntry<K, V> findKey(Object search){
        for(EquivalencyEntry<K, V> entry : values)
            if(entry.isEquivalent(search))
                return entry;

        return null;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return findKey(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for(EquivalencyEntry<K, V> entry : values)
            if(entry.getValue() != null && entry.getValue() == value)
                return true;

        return false;
    }

    @Override
    public V get(Object key) {
        EquivalencyEntry<K, V> entry = findKey(key);

        if(entry != null)
            return entry.getValue();
        else
            return null;
    }

    @Override
    public V put(K key, V value) {
        if(key == null)
            throw new NullPointerException("Null keys are not allowed in EquivalencyMap!");

        EquivalencyEntry<K, V> entry = findKey(key);

        if(entry == null){
            values.add(new EquivalencyEntry<K, V>(key, value, comparer));
            return null;
        }
        else {
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        }
    }

    @Override
    public V remove(Object key) {
        Iterator<EquivalencyEntry<K, V>> it = values.iterator();

        while(it.hasNext()){
            EquivalencyEntry<K, V> entry = it.next();

            if(comparer.isEquivalent(entry.getKey(), key)) {
                V oldValue = entry.getValue();
                it.remove();
                return oldValue;
            }
        }

        return null;
    }

    public void removeAll(Object key) {
        Iterator<EquivalencyEntry<K, V>> it = values.iterator();

        while(it.hasNext())
            if(comparer.isEquivalent(it.next().getKey(), key))
                it.remove();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<? extends K, ? extends V> set : m.entrySet())
            put(set.getKey(), set.getValue());
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Set<K> keySet() {
        Set ans = new EquivalencySet<K>(comparer);

        for(EquivalencyEntry<K, V> entry : values)
            ans.add(entry.getKey());

        return ans;
    }

    @Override
    public Collection<V> values() {
        List ans = new LinkedList<V>();

        for(EquivalencyEntry<K, V> entry : values)
            ans.add(entry.getValue());

        return ans;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new HashSet<Entry<K, V>>(values);
    }

    private class EquivalencyEntry<K, V> implements Entry<K, V>{

        private final Equivalency<K> comparer;
        private final K key;
        private V value;

        private EquivalencyEntry(K key, V value, Equivalency<K> comparer){
            if(key == null)
                throw new NullPointerException("Null keys are not allowed in an EquivalencyEntry!");

            this.comparer = comparer;
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean isEquivalent(Object o){
            return comparer.isEquivalent(getKey(), o);
        }

        @Override
        public boolean equals(Object other){
           if(other != null && other instanceof Entry){
               Entry entry = (Entry) other;

               return this.getKey().equals(entry.getKey()) && this.getValue() != null && this.getValue().equals(entry.getValue());
           }
            else
               return false;

        }

        @Override
        public int hashCode(){
            return getKey().hashCode() ^ (getValue() == null ? 0 : getValue().hashCode());
        }



    }
}
