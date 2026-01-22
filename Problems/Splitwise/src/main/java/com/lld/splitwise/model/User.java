package com.lld.splitwise.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
    private String id;
    private String name;
    // userBalances stores how much *this* user OWES others (positive) or is OWED
    // (negative) ?
    // Usually: Map<OtherUserID, Amount>.
    // If Amount > 0: This user owes separate user.
    // If Amount < 0: This user is owed by separate user.
    // Let's stick to: Map<User, Double> balances.
    // But for a simple design, we might just store "totalOwed" or a strictly
    // separate BalanceSheet service.
    // For SDE-3 concurrency, we'll keep it simple but thread-safe.

    // Attempting a simpler model: We only store user details here. Balance is
    // managed by ExpenseService/BalanceSheet.
    // However, for the demo, let's keep a balance map here for quick lookup.
    private Map<String, Double> balances;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.balances = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getBalances() {
        return balances;
    }

    // Thread-safe balance update
    public void updateBalance(String otherUserId, double amount) {
        lock.writeLock().lock();
        try {
            balances.put(otherUserId, balances.getOrDefault(otherUserId, 0.0) + amount);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
