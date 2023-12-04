
public interface HWHashMap<K,V>
{
    // get method to key value of key
    V get(K key);

    // return True if key-value mapping
    boolean isEmpty();
    // put method to enter key-value pair into  HashMapChain
    V put(K key, V value);

    // remove method to remove a key for our HashMapChain
    V remove(K key);

    // method to get the size of the HashMap to check for rehashing
    int size();
}