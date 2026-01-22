package com.lld.atm;

public abstract class CashDispenser {
    protected CashDispenser nextDispenser;

    public void setNextDispenser(CashDispenser nextDispenser) {
        this.nextDispenser = nextDispenser;
    }

    public void dispense(int amount) {
        if (amount >= getDenomination()) {
            int count = amount / getDenomination();
            int remainder = amount % getDenomination();
            System.out.println("Dispensing " + count + " x " + getDenomination() + " note(s)");

            if (remainder != 0 && nextDispenser != null) {
                nextDispenser.dispense(remainder);
            } else if (remainder != 0) {
                System.out.println("Cannot dispense remaining " + remainder);
            }
        } else {
            if (nextDispenser != null) {
                nextDispenser.dispense(amount);
            } else {
                System.out.println("Cannot dispense " + amount);
            }
        }
    }

    protected abstract int getDenomination();
}
