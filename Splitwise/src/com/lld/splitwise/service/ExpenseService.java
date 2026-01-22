package com.lld.splitwise.service;

import com.lld.splitwise.model.*;
import com.lld.splitwise.model.split.*;
import java.util.List;

public class ExpenseService {

    public static Expense createExpense(ExpenseType type, double amount, User paidBy, List<Split> splits,
            ExpenseMetadata metadata) {
        validateSplits(type, splits, amount);

        // Factory logic to set split amounts if needed (e.g. for Equal)
        switch (type) {
            case EQUAL:
                double splitAmount = ((double) Math.round(amount * 100 / splits.size())) / 100.0;
                for (Split split : splits) {
                    split.setAmount(splitAmount);
                }
                // Handle remainder
                double total = splitAmount * splits.size();
                double remainder = amount - total;
                if (remainder != 0) {
                    // Give remainder to first person (simple logic)
                    splits.get(0).setAmount(splits.get(0).getAmount() + remainder);
                }
                break;
            case PERCENT:
                for (Split split : splits) {
                    PercentSplit pSplit = (PercentSplit) split;
                    split.setAmount((amount * pSplit.getPercent()) / 100.0);
                }
                break;
            case EXACT:
                // Amounts are already set in ExactSplit, just validation needed
                break;
        }

        return new Expense("EXP" + System.currentTimeMillis(), amount, paidBy, splits, metadata);
    }

    private static void validateSplits(ExpenseType type, List<Split> splits, double totalAmount) {
        switch (type) {
            case EQUAL:
                // Always valid if list is not empty
                if (splits.isEmpty())
                    throw new IllegalArgumentException("Splits cannot be empty");
                break;
            case EXACT:
                double sum = 0;
                for (Split split : splits) {
                    if (!(split instanceof ExactSplit))
                        throw new IllegalArgumentException("Invalid split type");
                    sum += split.getAmount();
                }
                if (Math.abs(totalAmount - sum) > 0.001)
                    throw new IllegalArgumentException("Split amounts don't match total");
                break;
            case PERCENT:
                double totalPercent = 0;
                for (Split split : splits) {
                    if (!(split instanceof PercentSplit))
                        throw new IllegalArgumentException("Invalid split type");
                    totalPercent += ((PercentSplit) split).getPercent();
                }
                if (Math.abs(100 - totalPercent) > 0.001)
                    throw new IllegalArgumentException("Percents must equal 100");
                break;
        }
    }
}
