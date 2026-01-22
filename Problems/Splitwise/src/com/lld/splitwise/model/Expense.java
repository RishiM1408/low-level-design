package com.lld.splitwise.model;

import com.lld.splitwise.model.split.Split;
import java.util.List;

public class Expense {
    private String id;
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private ExpenseMetadata metadata;

    public Expense(String id, double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        this.id = id;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public ExpenseMetadata getMetadata() {
        return metadata;
    }
}
