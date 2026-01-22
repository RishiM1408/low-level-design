package com.lld.atm;

public class ATM {
    private ATMState currentState;
    private int balance;
    private CashDispenser dispenserChain;

    public ATM() {
        this.currentState = new IdleState();
        this.balance = 10000; // Initial funds
        initializeDispenserChain();
    }

    private void initializeDispenserChain() {
        dispenserChain = new TwoThousandDispenser();
        CashDispenser d2 = new FiveHundredDispenser();
        CashDispenser d3 = new OneHundredDispenser();

        dispenserChain.setNextDispenser(d2);
        d2.setNextDispenser(d3);
    }

    public void setATMState(ATMState state) {
        this.currentState = state;
    }

    public int getATMTotalBalance() {
        return balance;
    }

    public void insertCard() {
        currentState.insertCard(this);
    }

    public void ejectCard() {
        currentState.ejectCard(this);
    }

    public void enterPin(int pin) {
        currentState.enterPin(this, pin);
    }

    public void selectOperation(String op) {
        currentState.selectOperation(this, op);
    }

    public void withdraw(int amount) {
        currentState.withdraw(this, amount);
    }

    public void dispenseCash(int amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return;
        }
        balance -= amount;
        dispenserChain.dispense(amount);
    }
}
