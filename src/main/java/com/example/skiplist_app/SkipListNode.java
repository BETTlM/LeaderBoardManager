package com.example.skiplist_app;

public class SkipListNode<K, V> {
    public final K key;
    public V value;
    public int level;

    public SkipListNode<K, V> up;
    public SkipListNode<K, V> down;
    public SkipListNode<K, V> forward;
    public SkipListNode<K, V> backward;

    public SkipListNode(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
    }

    public SkipListNode(int level) {
        this(null, null, level);
    }
}