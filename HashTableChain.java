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

    public V get(K key) {
        int index = hashFunction(key);
        if(index < 0){
            index += table.length;
        }
        LinkedList<Entry<K, V>> bucketList = getLinkedListForBucket(index);

        
        for(Entry<K,V> nextItem: bucketList){
            if(nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        return null;
    }

    // methods we need to make still
    public boolean containsKey(K key){
        int index = hashFunction(key);
        LinkedList<Entry<K, V>> bucketList = getLinkedListForBucket(index);

        for(Entry<K,V> nextItem: bucketList){
            if(nextItem.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    public V remove(K key){
        int index = hashFunction(key);
        LinkedList<Entry<K, V>> bucketList = getLinkedListForBucket(index);

        for(Entry<K,V> nextItem: bucketList){
            if(nextItem.getKey().equals(key)){

                bucketList.remove(nextItem);
                numKeys--;
                return nextItem.getValue();
            }
        }

        // if we don't find a key
        return null;

    };

    public void put(K key, V value){
        int index =  hashFunction(key);
        LinkedList<Entry<K, V>> bucketList = getLinkedListForBucket(index);

        // Check if our load Threshold has been exceeded and if so rehash
        if (bucketList.size() > LOAD_THRESHOLD){

            rehashTable();
            // fetch the linked list after resizing it
            bucketList = getLinkedListForBucket(hashFunction(key));

        }
        for(Entry<K,V> nextItem: bucketList){
            if(nextItem.getKey().equals(key)){
               nextItem.setValue(value);
               return;
            }
        }
        // if we don't find a key then add a new entry to our bucket
        bucketList.add(new Entry<>(key,value));
        numKeys++;
    }

    public  int size(){
        return numKeys;
    }

    private void rehashTable(){
        int newCAPACITY = table.length * 2;
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCAPACITY];

        for (LinkedList<Entry<K,V>> list : table){
            if (list != null){
                for(Entry<K,V> nextItem: list){
                    int newIndex = hashFunction(nextItem.getKey());
                    LinkedList<Entry<K, V>> newList = getLinkedListForBucket(newIndex);
                    newList.add(nextItem);
                }
            }
        }
        table = newTable;
    }

    private int hashFunction(K key) {
        return key.hashCode() % table.length;
    }
    

    private LinkedList<Entry<K, V>> getLinkedListForBucket(int bucketIndex) {
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new LinkedList<>();
        }
        return table[bucketIndex];
    }


 public static void main(String[] args) {
        HashTableChain<String, Integer> hashMap = new HashTableChain<>();

        // Example usage
        hashMap.put("one", 1);
        hashMap.put("two", 2);
        hashMap.put("three", 3);

        System.out.println("Size: " + hashMap.size());
        System.out.println("Contains key 'two': " + hashMap.containsKey("two"));
        System.out.println("Value for key 'three': " + hashMap.get("three"));

        hashMap.remove("one");

        System.out.println("Size after removal: " + hashMap.size());
    }
}