// src/main/java/com/example/skiplistapp/SkipListNode.java
package com.example.skiplist_app;

public class SkipListNode<K, V> {
    public final K key; // Immutable key
    public V value;     // Value can be updated
    public int level;   // The level of this specific node instance (for visualization)

    public SkipListNode<K, V> up;
    public SkipListNode<K, V> down;
    public SkipListNode<K, V> forward;
    public SkipListNode<K, V> backward;

    // Constructor for the actual data nodes
    public SkipListNode(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
    }

    // Constructor for sentinel head/tail nodes (key=null, value=null)
    public SkipListNode(int level) {
        this(null, null, level);
    }
}