package edu.sdsu.cs.datastructures;


import java.util.Map;

public class MyOwnUnBal<K extends Comparable<K>, V> implements IMap<K, V>{

    private int currentSize;
    private int counter;
    private Node<K,V> root;
    private K keyFound;
    private Node parent;

    public void remove(K key) {
        throw new UnsupportedOperationException();
    }


    private class Node<K,V> {
        K key;
        V value;
        Node<K,V> leftKid;
        Node<K,V> rightKid;

        public Node(K k, V v){
            key = k;
            value = v;
            leftKid = rightKid = null;
        }
    }

    public MyOwnUnBal() {
        currentSize = 0;
        counter = 0;
        root = null;
    }
    //left side < 0 right side > 0 then find corresponding key
    private K find(K key, Node<K,V> node) {
        if (node == null)
            return null;
            //look left
           if(key.compareTo(node.key)< 0)
                return find(key, node.leftKid);

            //look right
            else if (key.compareTo(node.key)> 0)
                return find(key, node.rightKid);

            else
                return node.key;
    }
    private V findVal(K key, Node<K,V> node) {
        if(node == null)
        return null;

        //look left
        if(key.compareTo(node.key)< 0)
            return findVal(key, node.leftKid);


            //look right
        else if (key.compareTo(node.key)> 0)
            return findVal(key, node.rightKid);

        else
            return node.value;
    }
    private void findKey(Node<K,V> n, V value) {
        if(n == null)
            return;
        findKey(n.leftKid, value);// get n left kid key/val

        if(((Comparable<V>)value).compareTo(n.value)==0)
            keyFound = n.key;
        findKey(n.rightKid, value);
    }

    private Node<K,V> gettingSuccesor(Node<K,V> n, Node<K,V> before) {
        if(n.leftKid == null){
            if(n.rightKid == null)
                before.leftKid = null;
            else
                before.leftKid = n.rightKid;
            return n;
        }
        return gettingSuccesor(n.leftKid,n);
    }

    @Override
    public boolean contains(K key) {
        if(find(key,root) == null)
            return false;
        return true;
    }

    @Override
    public boolean add(K key, V value) {
        //dont allow duplicates
        if(contains(key)) //if has "same" key
            return false;
        if(root == null)
            root = new Node<K,V>(key, value);
        else
            insert(key,value,root,null,false);
        currentSize++;
        counter++; // even with duplicates, increment value
        return true;
    }
    private void insert(K key, V v, Node<K,V> n, Node<K,V> parent, boolean prevLeft) {
        if(n == null) {// if empty and previous left was empty
            if(prevLeft)
                parent.leftKid = new Node<K,V>(key,v);
            else
                parent.rightKid = new Node<K, V>(key, v);
        }
        else if(key.compareTo(n.key)< 0)
            insert(key,v,n.leftKid, n,true);
        else
            insert(key,v,n.rightKid, n,false);

    }
    private boolean remove(K k, V v, Node<K, V> n, Node<K,V> parent, boolean prevLeft) {
        if (n == null)
            return false;
        Node<K, V> successor;

        //check left
        if (k.compareTo(n.key) < 0)
            return remove(k, v, n.leftKid, n, true);
            //check right
        else if (k.compareTo(n.key) > 0)
            return remove(k, v, n.rightKid, n, false);
            //node and corresponding key
        else {
            //for node with no kids
            if (n.leftKid == null && n.rightKid == null) {
                if (n == root)
                    root = null;
                else {
                    if (prevLeft)
                        parent.leftKid = null;
                    else
                        parent.rightKid = null;
                }
            }
            //case 2 node with left kid
            else if (n.leftKid != null && n.rightKid == null) {
                if (n == root)
                    root = n.leftKid;
                else {
                    if (prevLeft)
                        parent.leftKid = n.leftKid;
                    else
                        parent.rightKid = n.leftKid;
                }
            }
            //case 3 node with right kid
            else if (n.rightKid != null && n.leftKid == null) {
                if (n == root)
                    root = n.rightKid;
                else {
                    if (prevLeft)
                        parent.leftKid = n.rightKid;
                    else
                        parent.rightKid = n.rightKid;
                }
            }
            //case 4 node with 2 kids
            else {
                if (n.rightKid.leftKid == null) {
                    successor = n.rightKid;
                    successor.leftKid = n.leftKid;
                    if (n == root)
                        root = successor;
                    else {
                        if (prevLeft)
                            parent.leftKid = successor;
                        else
                            parent.rightKid = successor;
                    }
                } else {
                    successor = gettingSuccesor(n.rightKid, null);
                    n.key = successor.key;
                    n.value = successor.value;
                }
            }
        }
            return true;
    }

//    private boolean remove(K k, Node<K,V> leftKid, Node<K,V> n, boolean b) {
//    }

    @Override
    public V delete(K key) {
        return null;
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public K getKey(V value) {
        keyFound = null;
        findKey(root,value);
        return keyFound;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        return null;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize==0;
    }

    @Override
    public void clear() {
        counter = 0;
        currentSize = 0;
        root = null;

    }

    @Override
    public Iterable<K> keyset() {
        return null;
    }

    @Override
    public Iterable<V> values() {
        return null;
    }


    public Iterable<K> keySet() {
        return null;
    }

    public boolean containsKey(K key) {
        return Boolean.parseBoolean(null);
    }



    public <K extends Comparable<K>, V> Map.Entry<K, V>[] entrySet() {
        return entrySet();
    }
}
