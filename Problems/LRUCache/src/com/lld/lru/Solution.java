package com.lld.lru;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

// --- Node for DLL ---
class Node {
    int key;
    int value;
    Node prev;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

// --- Doubly Linked List ---
// Helper class to manage O(1) removals and additions
class DoublyLinkedList {
    private Node head;
    private Node tail;

    public DoublyLinkedList() {
        head = new Node(0, 0); // Sentinel
        tail = new Node(0, 0); // Sentinel
        head.next = tail;
        tail.prev = head;
    }

    public void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public Node removeTail() {
        if (tail.prev == head)
            return null; // Empty
        Node res = tail.prev;
        removeNode(res);
        return res;
    }
}

// --- LRU Cache ---
class LRUCache {
    private final int capacity;
    private final Map<Integer, Node> map;
    private final DoublyLinkedList dll;

    // SDE-3 Focus: Concurrency
    private final ReentrantReadWriteLock rwLock;
    private final Lock readLock;
    private final Lock writeLock;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.dll = new DoublyLinkedList();
        this.rwLock = new ReentrantReadWriteLock();
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
    }

    public int get(int key) {
        writeLock.lock(); // We need write lock because GET promotes to head (modifies list)
        try {
            if (!map.containsKey(key))
                return -1;

            Node node = map.get(key);
            dll.removeNode(node);
            dll.addToHead(node);
            return node.value;
        } finally {
            writeLock.unlock();
        }
    }

    public void put(int key, int value) {
        writeLock.lock();
        try {
            if (map.containsKey(key)) {
                // Update
                Node node = map.get(key);
                node.value = value;
                dll.removeNode(node);
                dll.addToHead(node); // Move to front
            } else {
                // Insert
                if (map.size() == capacity) {
                    Node lru = dll.removeTail();
                    map.remove(lru.key);
                    System.out.println("Evicting Key: " + lru.key);
                }
                Node newNode = new Node(key, value);
                dll.addToHead(newNode);
                map.put(key, newNode);
            }
        } finally {
            writeLock.unlock();
        }
    }

    // For verification, not part of strict API
    public void printCache() {
        System.out.println("Cache Size: " + map.size());
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("--- LRU Cache Demo ---");
        LRUCache cache = new LRUCache(2);

        System.out.println("Put (1, 10)");
        cache.put(1, 10);

        System.out.println("Put (2, 20)");
        cache.put(2, 20);

        System.out.println("Get 1: " + cache.get(1)); // 1 is now most recent

        System.out.println("Put (3, 30)"); // Should evict 2
        cache.put(3, 30);

        System.out.println("Get 2: " + cache.get(2)); // Should be -1
    }
}
