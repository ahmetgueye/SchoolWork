package edu.sdsu.cs.datastructures;
//Ahmet Gueye
//821319753

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {

    private TreeMap<K, V> treeMap;

    @SuppressWarnings("unchecked")
    public BalancedMap(IMap<K, V> original) {
        treeMap = new TreeMap<K, V>();
        for (K key : original.keyset()) {
            V actValue = original.getValue(key);
            treeMap.put(key, actValue);
        }

    }

    public BalancedMap() {
        treeMap = new TreeMap<K, V>();
    }

    @Override
    public boolean contains(K key) {
        if (treeMap.containsKey(key))
            return true;
        else
            return false;
    }

    @Override
    public boolean add(K key, V value) {
        treeMap.put(key, value);
        return true;
    }

    @Override
    public V delete(K key) {
        V value = treeMap.get(key);
        treeMap.remove(key);
        return value;
    }

    @Override
    public V getValue(K key) {
        if (treeMap.containsKey(key))
            return treeMap.get(key);
        else
            return null;
    }

    @Override
    public K getKey(V value) {
        for (Map.Entry<K, V> entry : treeMap.entrySet()) {
            V actValue = entry.getValue();
            if (actValue.equals(value)) // TODO: V what if it is a UserDefined
                return entry.getKey();
        }
        return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        List<K> keys = new ArrayList<K>();

        for (Map.Entry<K, V> entry : treeMap.entrySet()) {
            V actValue = entry.getValue();
            if (actValue.equals(value))
                keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public int size() {
        return treeMap.size();
    }

    @Override
    public boolean isEmpty() {
        return treeMap.isEmpty();
    }

    @Override
    public void clear() {
        treeMap.clear();

    }

    @Override
    public Iterable<K> keyset() {
        return treeMap.keySet();
    }

    @Override
    public Iterable<V> values() {
        return treeMap.values();
    }

}
