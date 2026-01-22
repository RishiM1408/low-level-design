package com.lld.splitwise.service;

import com.lld.splitwise.model.*;
import com.lld.splitwise.model.split.Split;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    private List<Expense> expenses;
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> balanceSheet; // Global view for simplification?
    // OR we just use User's internal balance map. Let's use User's internal map for
    // this LLD to keep it modular.

    public ExpenseManager() {
        expenses = new ArrayList<>();
        userMap = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public void addExpense(ExpenseType type, double amount, String paidByUserId, List<Split> splits,
            ExpenseMetadata metadata) {
        User paidBy = userMap.get(paidByUserId);
        Expense expense = ExpenseService.createExpense(type, amount, paidBy, splits, metadata);
        expenses.add(expense);

        // Update Balances
        for (Split split : expense.getSplits()) {
            User paidTo = split.getUser();
            double splitAmount = split.getAmount();

            // validation: paidBy shouldn't owe themselves (net effect 0)
            // But usually we just skip updating if paidBy == paidTo
            if (!paidBy.getId().equals(paidTo.getId())) {
                // PaidBy is OWED simpleAmount
                paidBy.updateBalance(paidTo.getId(), splitAmount);

                // PaidTo OWES simpleAmount
                paidTo.updateBalance(paidBy.getId(), -splitAmount);
            }
        }
    }

    public void showBalance(String userId) {
        User user = userMap.get(userId);
        boolean isEmpty = true;
        for (Map.Entry<String, Double> entry : user.getBalances().entrySet()) {
            if (entry.getValue() != 0) {
                isEmpty = false;
                printBalance(userId, entry.getKey(), entry.getValue());
            }
        }
        if (isEmpty) {
            System.out.println("No balances");
        }
    }

    public void showBalances() {
        boolean isEmpty = true;
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            User user = entry.getValue();
            for (Map.Entry<String, Double> userBalance : user.getBalances().entrySet()) {
                if (userBalance.getValue() > 0) {
                    isEmpty = false;
                    printBalance(entry.getKey(), userBalance.getKey(), userBalance.getValue());
                }
            }
        }
        if (isEmpty) {
            System.out.println("No balances");
        }
    }

    private void printBalance(String user1, String user2, double amount) {
        String user1Name = userMap.get(user1).getName();
        String user2Name = userMap.get(user2).getName();
        if (amount < 0) {
            System.out.println(user1Name + " owes " + user2Name + ": " + Math.abs(amount));
        } else if (amount > 0) {
            System.out.println(user2Name + " owes " + user1Name + ": " + Math.abs(amount));
        }
    }
}
