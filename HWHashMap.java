
public interface HWHashMap<K,V>
{

    // get method to key value of key
    V get(K key);

    // put method to enter key-value pair into  HashMapChain
    void put(K key, V value);

    // remove method to remove a key for our HashMapChain
    V remove(K key);

    // method to check if we have a key value in our HashMapChain
    boolean containsKey(K key);

    // method to get the size of the HashMap to check for rehashing
    int thresholdSize();
}