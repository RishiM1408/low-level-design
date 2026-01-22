package com.lld.atm;

public interface ATMState {
    void insertCard(ATM atm);

    void ejectCard(ATM atm);

    void enterPin(ATM atm, int pin);

    void selectOperation(ATM atm, String operation);

    void withdraw(ATM atm, int amount);
}
