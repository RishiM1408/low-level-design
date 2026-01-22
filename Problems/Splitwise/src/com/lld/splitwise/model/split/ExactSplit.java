package com.lld.splitwise.model.split;

import com.lld.splitwise.model.User;

public class ExactSplit extends Split {
    public ExactSplit(User user, double amount) {
        super(user);
        this.amount = amount;
    }
}
