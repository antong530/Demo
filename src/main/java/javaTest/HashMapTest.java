package javaTest;

import java.util.Objects;

/**
 * Created by antong on 16/7/26.
 */

/**
 * 首先new HashMap初始化负载因子和数量
 * 调用put方法时,如果map数组为空则按照number >= MAXIMUM_CAPACITY
 ? MAXIMUM_CAPACITY
 : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1初始化数组
 * 如果put的key为空，获取数组的第一个位置，遍历链表，（如果链表的key为空，将值替换，如果没有为空的，则在当前数组位置调用addEntry（mod++），
 * 如果数组数量>负载因子乘以容量的数量 重新构建hashmap 遍历老的hashMap 计算hash（key）& 总得数量*2 分布到个个新的entry中,然后addEntry，从链表的头部插入）
 * 如果put的key不为空  查找hash（key）值相同并且key相同的；剩下的一样
 */
public class HashMapTest {
    private float loadFactor;
    private Entry[] entries;
    //entry总个数
    private int size;
    //负载entry个数
    private int loadSize;
    //entry缓存个数
    private int mod;

    class Entry {
        Object key;
        Object value;
        Entry next;
        int hash;

        public Entry(Object key, Object value, Entry next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }

        public final Object getKey() {
            return key;
        }

        public final Object getValue() {
            return value;
        }

        public final Object setValue(Object newValue) {
            Object oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            return true;
        }

        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    public HashMapTest() {
        this.loadFactor = 0.75f;
        this.size = 16;
    }

    public void put(Object key, Object value) {
        if (entries == null) {
            initTable();
        }
        if (key == null) {
            putForNull(value);
            return;
        }
        int hash = hash(key);
        int i = indexFor(hash, size);
        Entry entry = entries[i];
        while(entry != null){
            if (entry.hash == hash && entry.key == key) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        mod++;
        addEntry(hash, key, value, i);
    }

    private int indexFor(int hash, int size) {
        return hash & size;
    }

    final int hash(Object k) {
        int h = 0;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private void putForNull(Object value) {
        Entry entry = entries[0];
        while (entry != null) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        mod++;
        addEntry(0, null, value, 0);
    }

    private void initTable() {
        this.entries = new Entry[16];
    }

    void addEntry(int hash, Object key, Object value, int bucketIndex) {
        if (size >= loadSize && entries[bucketIndex] != null) {
            resize(entries.length * 2);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    void resize(int newCapacity) {
        Entry[] oldTable = entries;
        int oldCapacity = oldTable.length;
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        entries = newTable;
        loadSize = (int) (newCapacity * loadFactor);
    }

    void transfer(Entry[] newTable) {
        int newCapacity = newTable.length;
        for (Entry e : entries) {
            while (null != e) {
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    void createEntry(int hash, Object key, Object value, int bucketIndex) {
        Entry e = entries[bucketIndex];
        entries[bucketIndex] = new Entry(key, value, e, hash);
        size++;
    }


}
