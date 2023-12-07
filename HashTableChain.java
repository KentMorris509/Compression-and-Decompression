import java.util.*;

public class HashTableChain<K, V> implements HWHashMap<K, V> {

    // Entry class that includes none of the interface methods
    // Maybe try and test it later just to make sure the subclass works when we need
    // it
    // for the other methods, otherwise I think this works correctly.

    public static class Entry<K, V> {

        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V val) {
            V oldValue = value;
            value = val;
            return oldValue;

        }

        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }

    // constant variable for Hash Table Chain
    private LinkedList<Entry<K, V>>[] table;
    private int numKeys;
    private int rehashes;
    private static final int CAPACITY = 101; // We need to figure our what to make the default capacity, Im thinking 181
    private static final double LOAD_THRESHOLD = 12; // I think he recommended 15

    @SuppressWarnings("unchecked")
    public HashTableChain() {
        table = new LinkedList[CAPACITY];
        numKeys = 0;
        rehashes = 0;
    }

    @SuppressWarnings("unchecked")
    public HashTableChain(int cap) {
        if (!isPrime(cap))
            cap = nextPrime(cap);
        table = new LinkedList[cap];
        numKeys = 0;
        rehashes = 0;
    }

    public V get(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            return null;
        }

        for (Entry<K, V> nextItem : table[index]) {
            if (nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        return null;
    }

    // methods we need to make still
    public boolean containsKey(K key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            return false;
        }

        for (Entry<K, V> nextItem : table[index]) {
            if (nextItem.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        if (table.length > 0) {
            return false;
        }
        return true;
    }

    public V remove(K key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            return null;
        }

        for (Entry<K, V> nextItem : table[index]) {
            if (nextItem.getKey().equals(key)) {
                table[index].remove(nextItem);
                numKeys--;
                if (isEmpty()) {
                    table[index] = null;
                }
                return nextItem.getValue();
            }
        }

        // if we don't find a key
        return null;

    };

    public V put(K key, V value) {
        int index = key.hashCode() % table.length;
        // LinkedList<Entry<K, V>> bucketList = getLinkedListForBucket(index);
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        // Check if our load Threshold has been exceeded and if so rehash

        for (Entry<K, V> nextItem : table[index]) {

            if (nextItem.getKey().equals(key)) {
                V oldVal = nextItem.getValue();
                nextItem.setValue(value);
                return oldVal;
            }
        }
        // Use an iterator to avoid ConcurrentModificationException
        // if we don't find a key then add a new entry to our bucket
        table[index].addFirst(new Entry<>(key, value));
        numKeys++;

        if (table[index].size() > (LOAD_THRESHOLD)) {
            rehashTable();
        }
        return null;
    }

    public int size() {
        return numKeys;
    }

    public int numRehashes() {
        return rehashes;
    }

    private void rehashTable() {
        int newCAPACITY = table.length * 2;

        if (!isPrime(newCAPACITY)) {
            newCAPACITY = nextPrime(newCAPACITY);
        }
        @SuppressWarnings("unchecked")
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCAPACITY];

        List<Entry<K, V>> allEntries = new ArrayList<>();

        // For each linked list in the array adding
        for (LinkedList<Entry<K, V>> list : table) {
            // For every linked List that is not empty add to new array
            if (list != null) {
                allEntries.addAll(list);
            }
        }

        // rehash all entries to new table
        for (Entry<K, V> nextItem : allEntries) {
            int newIndex = nextItem.getKey().hashCode() % newTable.length;
            if (newIndex < 0) {
                newIndex += newTable.length;
            }
            if (newTable[newIndex] == null) {
                newTable[newIndex] = new LinkedList<>();
            }
            newTable[newIndex].add(nextItem);
        }

        rehashes++;
        table = newTable;
    }

    private int nextPrime(int startNumber) {
        int number = startNumber;

        while (true) {
            number++;
            if (isPrime(number)) {
                return number;
            }
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

}