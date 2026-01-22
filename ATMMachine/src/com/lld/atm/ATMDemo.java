package com.lld.atm;

public class ATMDemo {
    public static void main(String[] args) {
        System.out.println("--- ATM Machine Demo ---");
        ATM atm = new ATM();

        // 1. Success Flow
        System.out.println("\nScenario 1: Withdraw 2600");
        atm.insertCard();
        atm.enterPin(1234);
        atm.selectOperation("WITHDRAW");
        atm.withdraw(2600); // Should be 1x2000, 1x500, 1x100

        // 2. Incorrect PIN
        System.out.println("\nScenario 2: Incorrect PIN");
        atm.insertCard();
        atm.enterPin(9999); // Card should eject

        // 3. State check (Try withdrawing without card)
        System.out.println("\nScenario 3: Withdraw without auth");
        atm.withdraw(500); // Should say "Insert card first"
    }
}
