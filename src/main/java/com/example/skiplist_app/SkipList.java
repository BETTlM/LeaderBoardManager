package com.example.skiplist_app;

import java.util.*;
import java.util.List;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V> {
    private static final int MAX_LEVEL = 16; 
    private static final double P = 0.5;

    public SkipListNode<K, V> head; 
    public int currentMaxLevel; 
    private final Random random;

    public SkipList() {
        head = new SkipListNode<>(MAX_LEVEL - 1);
        SkipListNode<K, V> tempHead = head;
        for (int i = MAX_LEVEL - 2; i >= 0; i--) {
            tempHead.down = new SkipListNode<>(i);
            tempHead.down.up = tempHead;
            tempHead = tempHead.down;
        }
        currentMaxLevel = 0; 
        random = new Random();
    }

    private int randomLevel() {
        int lvl = 0;
        while (random.nextDouble() < P && lvl < MAX_LEVEL - 1) {
            lvl++;
        }
        return lvl;
    }


    public SkipListNode<K, V> search(K key, List<SkipListNode<K, V>> path) {
        SkipListNode<K, V> current = findStartNode(path);
        path.add(current);

        while (current != null) {
            while (current.forward != null && current.forward.key.compareTo(key) < 0) {
                current = current.forward;
                path.add(current);
            }

            if (current.forward != null && current.forward.key.compareTo(key) == 0) {
                path.add(current.forward);
                SkipListNode<K, V> target = current.forward;
                while(target.down != null) {
                    target = target.down;
                    path.add(target);
                }
                return target;
            }

            if (current.down != null) {
                current = current.down;
                path.add(current);
            } else {
                break;
            }
        }
        return null;
    }

    private SkipListNode<K, V> findStartNode(List<SkipListNode<K, V>> path) {
        SkipListNode<K, V> node = head;
        while (node.down != null && node.level > currentMaxLevel) {
            node = node.down;
        }
        return node;
    }

    public void insert(K key, V value) {
        List<SkipListNode<K, V>> path = new ArrayList<>();
        SkipListNode<K, V> existingNodeAtBottom = search(key, path);

        if (existingNodeAtBottom != null && existingNodeAtBottom.key.equals(key)) {
            SkipListNode<K,V> current = existingNodeAtBottom;
            while(current != null) {
                current.value = value;
                current = current.up;
            }
            return;
        }

        int newLevel = randomLevel();
        if (newLevel > currentMaxLevel) {
            currentMaxLevel = newLevel;
        }

        SkipListNode<K, V> bottomNode = null; 

        SkipListNode<K, V> currentPosition = path.isEmpty() ? head : path.get(path.size() - 1);
        while(currentPosition.level > newLevel) {
            currentPosition = currentPosition.down;
        }

        for (int level = 0; level <= newLevel; level++) {
            while(currentPosition.forward != null && currentPosition.forward.key != null && currentPosition.forward.key.compareTo(key) < 0) {
                currentPosition = currentPosition.forward;
            }

            SkipListNode<K, V> newNode = new SkipListNode<>(key, value, level);
            if (bottomNode != null) { 
                newNode.down = bottomNode;
                bottomNode.up = newNode;
            }

            newNode.forward = currentPosition.forward;
            if (currentPosition.forward != null) {
                currentPosition.forward.backward = newNode;
            }
            currentPosition.forward = newNode;
            newNode.backward = currentPosition;


            bottomNode = newNode; 
            if (level < newLevel) {
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
            return; 
        }

        while (nodeToDelete != null) {
            SkipListNode<K, V> prev = nodeToDelete.backward;
            SkipListNode<K, V> next = nodeToDelete.forward;

            if (prev != null) {
                prev.forward = next;
            }
            if (next != null) {
                next.backward = prev;
            }

            nodeToDelete = nodeToDelete.up;
        }

        while (currentMaxLevel > 0 && head.down.forward == null) { 
            currentMaxLevel--;
            head = head.down;
            head.up = null;
        }
    }
}