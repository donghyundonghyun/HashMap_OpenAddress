/**
 * Created by DongHyun on 2015-11-11.
 */
public class HashEntry<K,V> {
    private K key;
    private V value;

    public HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }
}