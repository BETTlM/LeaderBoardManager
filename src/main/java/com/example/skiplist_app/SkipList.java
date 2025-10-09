// src/main/java/com/example/skiplistapp/SkipList.java
package com.example.skiplist_app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V> {
    private static final int MAX_LEVEL = 16; // Max height for a tower
    private static final double P = 0.5;    // Probability for promoting to next level

    public SkipListNode<K, V> head; // Top-most, left-most sentinel head
    public int currentMaxLevel;     // Actual current max level of the list
    private final Random random;

    public SkipList() {
        // Initialize head sentinel node tower
        head = new SkipListNode<>(MAX_LEVEL - 1); // Head is at max possible level initially
        SkipListNode<K, V> tempHead = head;
        for (int i = MAX_LEVEL - 2; i >= 0; i--) {
            tempHead.down = new SkipListNode<>(i);
            tempHead.down.up = tempHead;
            tempHead = tempHead.down;
        }
        currentMaxLevel = 0; // Starts with only a level 0 node effectively
        random = new Random();
    }

    private int randomLevel() {
        int lvl = 0;
        while (random.nextDouble() < P && lvl < MAX_LEVEL - 1) {
            lvl++;
        }
        return lvl;
    }

    /**
     * Searches for a key and returns the SkipListNode on the bottom level (level 0)
     * corresponding to the key, or null if not found.
     * The search path is also collected.
     */
    public SkipListNode<K, V> search(K key, List<SkipListNode<K, V>> path) {
        SkipListNode<K, V> current = findStartNode(path); // Start from the highest head
        path.add(current); // Add the head node to the path

        while (current != null) {
            // Traverse forward on the current level
            while (current.forward != null && current.forward.key.compareTo(key) < 0) {
                current = current.forward;
                path.add(current);
            }

            // If found at this level (or a level above), we need to check its direct forward
            if (current.forward != null && current.forward.key.compareTo(key) == 0) {
                // If we found the target, add it and drop down to level 0
                path.add(current.forward);
                SkipListNode<K, V> target = current.forward;
                while(target.down != null) {
                    target = target.down;
                    path.add(target);
                }
                return target; // Found at level 0
            }

            // If not found, move down one level
            if (current.down != null) {
                current = current.down;
                path.add(current);
            } else {
                break; // Reached bottom level, key not found
            }
        }
        return null; // Key not found
    }

    // Helper to get the top-most head for searching
    private SkipListNode<K, V> findStartNode(List<SkipListNode<K, V>> path) {
        SkipListNode<K, V> node = head;
        while (node.down != null && node.level > currentMaxLevel) {
            // Find the highest head node that is actually connected (part of current list height)
            node = node.down;
        }
        return node;
    }

    public void insert(K key, V value) {
        List<SkipListNode<K, V>> path = new ArrayList<>();
        // Search to find existing node or insertion point
        SkipListNode<K, V> existingNodeAtBottom = search(key, path);

        if (existingNodeAtBottom != null && existingNodeAtBottom.key.equals(key)) {
            // Key already exists, update value for all tower nodes
            SkipListNode<K,V> current = existingNodeAtBottom;
            while(current != null) {
                current.value = value;
                current = current.up;
            }
            return;
        }

        // Key not found, insert new node tower
        int newLevel = randomLevel();
        if (newLevel > currentMaxLevel) {
            // If new node goes higher than current list height, update currentMaxLevel
            // And potentially "grow" the head sentinel tower to match
            currentMaxLevel = newLevel;
        }

        SkipListNode<K, V> bottomNode = null; // The level 0 node of the new tower

        // Start from the node right before the insertion point on the lowest effective level
        SkipListNode<K, V> currentPosition = path.isEmpty() ? head : path.get(path.size() - 1);
        while(currentPosition.level > newLevel) { // Go down to the relevant level for insertion
            currentPosition = currentPosition.down;
        }

        for (int level = 0; level <= newLevel; level++) {
            // Move currentPosition backward to find the correct insertion point at this level
            while(currentPosition.forward != null && currentPosition.forward.key != null && currentPosition.forward.key.compareTo(key) < 0) {
                currentPosition = currentPosition.forward;
            }

            SkipListNode<K, V> newNode = new SkipListNode<>(key, value, level);
            if (bottomNode != null) { // Link up-pointers from lower levels
                newNode.down = bottomNode;
                bottomNode.up = newNode;
            }

            // Link forward and backward pointers
            newNode.forward = currentPosition.forward;
            if (currentPosition.forward != null) {
                currentPosition.forward.backward = newNode;
            }
            currentPosition.forward = newNode;
            newNode.backward = currentPosition;

            // Move up to insert at the next level
            bottomNode = newNode; // The new node becomes the 'down' for the next level's node
            if (level < newLevel) {
                // Find or create the previous node on the next level to link up
                while (currentPosition.up == null) {
                    currentPosition = currentPosition.backward;
                }
                currentPosition = currentPosition.up;
            }
        }
    }


    public void delete(K key) {
        List<SkipListNode<K, V>> path = new ArrayList<>();
        SkipListNode<K, V> nodeToDelete = search(key, path);

        if (nodeToDelete == null) {
            return; // Key not found
        }

        // Traverse up the tower from level 0 and delete each instance
        while (nodeToDelete != null) {
            SkipListNode<K, V> prev = nodeToDelete.backward;
            SkipListNode<K, V> next = nodeToDelete.forward;

            if (prev != null) {
                prev.forward = next;
            }
            if (next != null) {
                next.backward = prev;
            }

            // Move up the tower
            nodeToDelete = nodeToDelete.up;
        }

        // Adjust currentMaxLevel if top-most level becomes empty
        while (currentMaxLevel > 0 && head.down.forward == null) {
            // Head.down points to the highest level, if it only has null.forward
            // meaning the level is effectively empty (only sentinel nodes)
            currentMaxLevel--;
            head = head.down; // Effectively remove the top level of the head sentinel
            head.up = null; // Decouple from above
        }
    }
}