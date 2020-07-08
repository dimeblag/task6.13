import java.util.Iterator;

public class MyMap<K extends Comparable<K>, V> implements Iterable<V> {

    private class MapEntry<K extends Comparable<K>, v> implements Comparable<MapEntry<K, v>> {
        K key;
        V value;

        MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(MapEntry<K, v> kvMapEntry) {
            return this.key.compareTo(kvMapEntry.key);
        }

        K getKey() { return key; }

        V getValue() { return value; }

        void setValue(V value) { this.value = value; }
    }

    private final AVLTree<MapEntry<K, V>> tree = new AVLTree<>();

    public int size() { return tree.size(); }

    public boolean isEmpty() { return tree.size() == 0; }

    public V get(K key) {
        MapEntry<K, V> entry = tree.getValue(new MapEntry<>(key, null));
        return entry != null ? entry.value : null;
    }

    public boolean add(K key, V value) { return tree.add(new MapEntry<>(key, value)); }

    public V remove(K key) { return tree.remove(new MapEntry<>(key, null)).getValue().value; }

    public void setKey(K oldKey, K newKey) throws Exception {
        MapEntry<K, V> entry = tree.remove(new MapEntry<>(oldKey, null)).getValue();
        if (entry == null)
            throw new Exception("Entry with this old key is not exist");
        entry.key = newKey;
        tree.add(entry);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {

            Iterator<MapEntry<K, V>> iterator = tree.iterator();

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

    public Iterator<K> keyIterator() {
        return new Iterator<>() {

            Iterator<MapEntry<K, V>> iterator = tree.iterator();

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
}
