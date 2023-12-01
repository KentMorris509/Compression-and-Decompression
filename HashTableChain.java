import java.util.*;


public class HashTableChain<K,V> implements HWHashMap<K,V> {

    // Entry class that includes none of the interface methods
    // Maybe try and test it later just to make sure the subclass works when we need it 
    // for the other methods, otherwise I think this works correctly.

    public static class Entry<K,V> {
        
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey(){
            return key;
        }

        public V getValue(){
            return value;
        }

        public V setValue(V val) {
            V oldValue = value;
            V value = val;
            return oldValue;

        }

        public String toString(){
            return key.toString() + "=" + value.toString();
        }
    }

    // constant variable for Hash Table Chain
    private LinkedList<Entry<K,V>>[] table;
    private int numKeys;
    private static final int CAPACITY = 181;    // We need to figure our what to make the default capacity, Im thinking 181
    private static final double LOAD_THRESHOLD = 15; // I think he recommended 15

    public HashTableChain(){
        table = new LinkedList[CAPACITY];
        numKeys = 0;
    }

    public HashTableChain(int cap) {
        table = new LinkedList[cap];
        numKeys = 0;
    }

    public V get(Object key) {
        int index = key.hashCode() % table.length;
        if(index < 0){
            index += table.length;
        }
        if(table[index] == null){
            return null;
        }
        for(Entry<K,V> nextItem: table[index]){
            if(nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        return null;
    }

    // methods we need to make still
    public boolean containsKey(K key){
        return false;
    }

    public V remove(K key){
        return null;
    }

    public void put(K key, V value){
        
    }

    public  int thresholdSize(){
        return 0;
    }

}