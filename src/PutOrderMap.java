import java.util.Iterator;

public class PutOrderMap<K extends Comparable<K>, V> implements Iterable<V>{

    private class PutOrderEntry<K, V> {
        K key;
        V value;

        PutOrderEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final MyMap<Integer, PutOrderEntry<K, V>> putOrderMap = new MyMap<>();

    private class KeyEntry<V> {
        int putOrderNumber;
        V value;

        KeyEntry(int putOrderNumber, V value) {
            this.putOrderNumber = putOrderNumber;
            this.value = value;
        }
    }

    private final MyMap<K, KeyEntry<V>> keyMap = new MyMap<>();

    private int counter = 0;

    public V get(K key) { return keyMap.get(key).value; }

    public int size() { return keyMap.size(); }

    public boolean add(K key, V value) {
        if (keyMap.add(key, new KeyEntry<>(counter + 1, value))) {
            putOrderMap.add(counter + 1, new PutOrderEntry<>(key, value));
            counter++;
            return true;
        }
        return false;
    }

    public void remove(K key) {
        putOrderMap.remove(keyMap.remove(key).putOrderNumber);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {
            Iterator<PutOrderEntry<K, V>> iterator = putOrderMap.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().value;
            }
        };
    }

    public Iterator<K> keyIteratorByPutOrderNumber() {
        return new Iterator<>() {
            Iterator<PutOrderEntry<K, V>> iterator = putOrderMap.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().key;
            }
        };
    }

    public Iterator<K> keyIterator() {
        return new Iterator<>() {
            Iterator<K> iterator = keyMap.keyIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next();
            }
        };
    }

    public Iterator<V> iteratorByKeys() {
        return new Iterator<>() {
            Iterator<KeyEntry<V>> iterator = keyMap.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().value;
            }
        };
    }
}
