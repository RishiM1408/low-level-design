package com.lld.splitwise;

import com.lld.splitwise.model.*;
import com.lld.splitwise.model.split.*;
import com.lld.splitwise.service.ExpenseManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SplitwiseDemo {
    public static void main(String[] args) {
        ExpenseManager expenseManager = new ExpenseManager();

        User user1 = new User("u1", "User1");
        User user2 = new User("u2", "User2");
        User user3 = new User("u3", "User3");
        User user4 = new User("u4", "User4");

        expenseManager.addUser(user1);
        expenseManager.addUser(user2);
        expenseManager.addUser(user3);
        expenseManager.addUser(user4);

        // 1. User1 pays 1000 for User1, User2, User3, User4 (EQUAL)
        List<Split> splits1 = new ArrayList<>();
        splits1.add(new EqualSplit(user1));
        splits1.add(new EqualSplit(user2));
        splits1.add(new EqualSplit(user3));
        splits1.add(new EqualSplit(user4));

        expenseManager.addExpense(ExpenseType.EQUAL, 1000, "u1", splits1, null);
        System.out.println("--- After Expense 1 (Equal 1000 by u1) ---");
        expenseManager.showBalances();

        // 2. User2 pays 1250 for User2 and User3 (EXACT) -> u2 pays 370, u3 pays 880
        List<Split> splits2 = new ArrayList<>();
        splits2.add(new ExactSplit(user2, 370));
        splits2.add(new ExactSplit(user3, 880));

        expenseManager.addExpense(ExpenseType.EXACT, 1250, "u2", splits2, null);
        System.out.println("\n--- After Expense 2 (Exact 1250 by u2) ---");
        expenseManager.showBalances();

        // 3. User4 pays 1200 for User1, User2, User3, User4 (PERCENT) -> 40%, 20%, 20%,
        // 20%
        List<Split> splits3 = new ArrayList<>();
        splits3.add(new PercentSplit(user1, 40));
        splits3.add(new PercentSplit(user2, 20));
        splits3.add(new PercentSplit(user3, 20));
        splits3.add(new PercentSplit(user4, 20));

        expenseManager.addExpense(ExpenseType.PERCENT, 1200, "u4", splits3, null);
        System.out.println("\n--- After Expense 3 (Percent 1200 by u4) ---");
        expenseManager.showBalances();
    }
}
