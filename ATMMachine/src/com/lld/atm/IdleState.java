package com.lld.atm;

public class IdleState implements ATMState {
    @Override
    public void insertCard(ATM atm) {
        System.out.println("Card inserted.");
        atm.setATMState(new HasCardState());
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("No card to eject.");
    }

    @Override
    public void enterPin(ATM atm, int pin) {
        System.out.println("Insert card first.");
    }

    @Override
    public void selectOperation(ATM atm, String op) {
        System.out.println("Insert card first.");
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        System.out.println("Insert card first.");
    }
}

class HasCardState implements ATMState {
    @Override
    public void insertCard(ATM atm) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Card ejected.");
        atm.setATMState(new IdleState());
    }

    @Override
    public void enterPin(ATM atm, int pin) {
        if (pin == 1234) {
            System.out.println("PIN Correct.");
            atm.setATMState(new SelectOptionState());
        } else {
            System.out.println("Invalid PIN.");
            ejectCard(atm); // Or return card
        }
    }

    @Override
    public void selectOperation(ATM atm, String op) {
        System.out.println("Enter PIN first.");
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        System.out.println("Enter PIN first.");
    }
}

class SelectOptionState implements ATMState {
    @Override
    public void insertCard(ATM atm) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Card ejected.");
        atm.setATMState(new IdleState());
    }

    @Override
    public void enterPin(ATM atm, int pin) {
        System.out.println("PIN already entered.");
    }

    @Override
    public void selectOperation(ATM atm, String op) {
        if (op.equalsIgnoreCase("WITHDRAW")) {
            System.out.println("Select amount to withdraw.");
            atm.setATMState(new WithdrawState());
        } else {
            System.out.println("Operation not supported yet.");
        }
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        System.out.println("Select operation first.");
    }
}

class WithdrawState implements ATMState {
    @Override
    public void insertCard(ATM atm) {
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Card ejected.");
        atm.setATMState(new IdleState());
    }

    @Override
    public void enterPin(ATM atm, int pin) {
    }

    @Override
    public void selectOperation(ATM atm, String op) {
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        if (atm.getATMTotalBalance() < amount) {
            System.out.println("Insufficient funds in ATM.");
            ejectCard(atm);
            return;
        }

        atm.dispenseCash(amount);
        // After withdraw, return card
        ejectCard(atm);
    }
}
