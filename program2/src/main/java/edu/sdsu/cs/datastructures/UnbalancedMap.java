package edu.sdsu.cs.datastructures;
//Ahmet Gueye
//821319753

import java.util.Comparator;
import java.util.LinkedList;

public class UnbalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {


    private Node root;
    private Comparator<? super K> comparator;
    int size;

    private class Node {
        private K key;
        private V value;
        private Node leftNode, rightNode;

        public Node(K k, V v) {
            key = k;
            value = v;
            leftNode = rightNode = null;

        }

        public Node() {
            key = null;
            value = null;
            leftNode = rightNode = null;

        }

        public String toString() {
            return key.toString() + " " + value.toString();
        }

    }

    public UnbalancedMap(IMap<K, V> tree) {
        if (tree == null) {
            root = new Node();
            size = 0;
            comparator = null;
        } else { //convert to unBal from Imap, initializing
            root = ((UnbalancedMap) tree).getRoot();
            comparator = ((UnbalancedMap) tree).getComparator();
            size = ((UnbalancedMap) tree).size();
        }
    }
    public UnbalancedMap() {
        root = null;
        comparator = null;
        size = 0;
    }

    private Comparator<? super K> getComparator() {
        return comparator;
    }

    private Node getRoot() {
        return root;
    }

    private Node insert(Node parent, K key, V value) {
        if (parent == null) {
            ++size;
            return new Node(key, value);
        }

        Node curr = parent;
        while (true) {
                //Goes Left
            if (compare(key, curr.key) < 0) {
                if (curr.leftNode == null) {
                    ++size;
                    curr.leftNode = new Node(key, value);
                    break;
                } else
                    curr = curr.leftNode;
                //Goes Right
            } else if (compare(key, curr.key) > 0) {
                if (curr.rightNode == null) {
                    ++size;
                    curr.rightNode = new Node(key, value);
                    break;
                } else
                    curr = curr.rightNode;
            } else {
                curr.key = key;
                curr.value = value;
                break;
            }
        }
        return root;
    }

    private int compare(K key, K key2) {
        return ((Comparable<K>) key).compareTo(key2);
    }

    @Override
    public boolean add(K key, V value) {
        root = insert(root, key, value);
        return true;
    }
    @Override
    public V delete(K key) {
        V v = getValue(key);
        if (v != null) {
            root = delete(root, key);
            size--;
        }
        return v;
    }
    private Node delete(Node parent, K toDelete) {

        if (parent == null)
            return parent;
        int compare = toDelete.compareTo(parent.key);
        if (compare < 0)
            parent.leftNode = delete(parent.leftNode, toDelete);
        else if (compare > 0)
            parent.rightNode = delete(parent.rightNode, toDelete);
        else {
            if (parent.leftNode == null)
                return parent.rightNode;
            else if (parent.rightNode == null)
                return parent.leftNode;

            parent.key = minValue(parent.rightNode);
            parent.rightNode = delete(parent.rightNode, parent.key);
        }
        return parent;
    }

    K minValue(Node parent) {
        K minv = parent.key;
        while (parent.leftNode != null) {
            minv = parent.leftNode.key;
            parent = parent.leftNode;
        }
        return minv;
    }

    private K retrieveData(Node parent) {
        while (parent.rightNode != null)
            parent = parent.rightNode;

        return parent.key;
    }

    @Override
    public V getValue(K key) {
        return getValueByKey(root, key);
    }

    private V getValueByKey(Node parent, K toSearch) {
        if (parent == null)
            return null;
        int compare = toSearch.compareTo(parent.key);
        if (compare > 0)
            return getValueByKey(parent.rightNode, toSearch);
        else if (compare < 0)
            return getValueByKey(parent.leftNode, toSearch);
        else
            return parent.value;
    }
    @Override
    public K getKey(V value) {
        LinkedList<K> list = new LinkedList<K>();
        inorder(root, list, value);
        if (list.size() > 0)
            return list.getFirst();
        return null;
    }

    private void inorder(Node x, LinkedList<K> list, V value) {
        if (x == null)
            return;
        inorder(x.leftNode, list, value);
        if (x.value.equals(value))
            list.add(x.key);
        inorder(x.rightNode, list, value);
    }

    @Override
    public Iterable<K> getKeys(V value) {
        LinkedList<K> linkedList = new LinkedList<K>();
        inorder(root, linkedList, value);
        return linkedList;
    }
    private void inorder(Node x, LinkedList<K> list) {
        if (x == null)
            return;
        inorder(x.leftNode, list);
        list.add(x.key);
        inorder(x.rightNode, list);
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null ? true : false;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        comparator = null;
    }
    @Override
    public Iterable<K> keyset() {
        LinkedList<K> linkedList = new LinkedList<K>();
        inorder(root, linkedList);
        return linkedList;
    }
    // TODO
    @Override
    public Iterable<V> values() {
        LinkedList<V> list = new LinkedList<V>();
        inorderForValue(root, list);
        return list;
    }
    private void inorderForValue(Node x, LinkedList<V> list) {
        if (x == null)
            return;
        inorderForValue(x.leftNode, list);
        list.add(x.value);
        inorderForValue(x.rightNode, list);
    }
    @Override
    public boolean contains(K key) {
        return getValue(key) != null;
    }
}
