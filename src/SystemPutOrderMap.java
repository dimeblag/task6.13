import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SystemPutOrderMap<K extends Comparable<K>, V> implements Iterable<V>{

    private class PutOrderEntry<K, V> {
        K key;
        V value;

        PutOrderEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final TreeMap<Integer, PutOrderEntry<K, V>> putOrderMap = new TreeMap<>();

    private class KeyEntry<V> {
        int putOrderNumber;
        V value;

        KeyEntry(int putOrderNumber, V value) {
            this.putOrderNumber = putOrderNumber;
            this.value = value;
        }
    }

    private final TreeMap<K, KeyEntry<V>> keyMap = new TreeMap<>();

    private int counter = 0;

    public V get(K key) { return keyMap.get(key).value; }

    public int size() { return keyMap.size(); }

    public void add(K key, V value) {
        keyMap.put(key, new KeyEntry<>(counter + 1, value));
        putOrderMap.put(counter + 1, new PutOrderEntry<>(key, value));
        counter++;
    }

    public void remove(K key) {
        putOrderMap.remove(keyMap.remove(key).putOrderNumber);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {
            Set<Map.Entry<Integer, PutOrderEntry<K, V>>> set = putOrderMap.entrySet();

            Iterator<Map.Entry<Integer, PutOrderEntry<K, V>>> iterator = set.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().getValue().value;
            }
        };
    }

    public Iterator<K> keyIteratorByPutOrderNumber() {
        return new Iterator<>() {
            Set<Map.Entry<Integer, PutOrderEntry<K, V>>> set = putOrderMap.entrySet();
            Iterator<Map.Entry<Integer, PutOrderEntry<K, V>>> iterator = set.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().getValue().key;
            }
        };
    }

    public Iterator<K> keyIterator() {
        return new Iterator<>() {
            Set<Map.Entry<K, KeyEntry<V>>> set = keyMap.entrySet();
            Iterator<Map.Entry<K, KeyEntry<V>>> iterator = set.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().getKey();
            }
        };
    }

    public Iterator<V> iteratorByKeys() {
        return new Iterator<>() {
            Set<Map.Entry<K, KeyEntry<V>>> set = keyMap.entrySet();
            Iterator<Map.Entry<K, KeyEntry<V>>> iterator = set.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().getValue().value;
            }
        };
    }
}
