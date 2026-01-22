package com.lld.vendingmachine;

import java.util.HashMap;
import java.util.Map;

// --- Enums / Entities ---
enum Coin {
    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

    public int value;

    Coin(int v) {
        this.value = v;
    }
}

class Product {
    String name;
    int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

// --- State Interface ---
interface State {
    void insertCoin(VendingMachine vm, Coin coin);

    void selectProduct(VendingMachine vm, String code);

    void dispense(VendingMachine vm);

    void refund(VendingMachine vm);
}

// --- Concrete States ---
class IdleState implements State {
    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        vm.addBalance(coin.value);
        System.out.println("Coin inserted: " + coin + ". Balance: " + vm.getBalance());
        vm.setState(vm.getHasMoneyState());
    }

    @Override
    public void selectProduct(VendingMachine vm, String code) {
        System.out.println("Insert money first.");
    }

    @Override
    public void dispense(VendingMachine vm) {
        System.out.println("Select product first.");
    }

    @Override
    public void refund(VendingMachine vm) {
        System.out.println("No money to refund.");
    }
}

class HasMoneyState implements State {
    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        vm.addBalance(coin.value);
        System.out.println("Coin inserted: " + coin + ". Balance: " + vm.getBalance());
    }

    @Override
    public void selectProduct(VendingMachine vm, String code) {
        Product p = vm.getProduct(code);
        if (p == null) {
            System.out.println("Invalid Product Code.");
            return; // Stay in HasMoney
        }
        if (vm.getBalance() < p.price) {
            System.out.println("Insufficient funds. Price: " + p.price);
            return; // Stay in HasMoney
        }

        System.out.println("Product selected: " + p.name);
        vm.setSelectedProduct(p);
        vm.setState(vm.getDispensingState());
        vm.dispense(); // Auto trigger dispense
    }

    @Override
    public void dispense(VendingMachine vm) {
        System.out.println("Select product first.");
    }

    @Override
    public void refund(VendingMachine vm) {
        System.out.println("Refunding " + vm.getBalance());
        vm.setBalance(0);
        vm.setState(vm.getIdleState());
    }
}

class DispensingState implements State {
    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        System.out.println("Please wait, dispensing.");
    }

    @Override
    public void selectProduct(VendingMachine vm, String code) {
        System.out.println("Please wait, dispensing.");
    }

    @Override
    public void dispense(VendingMachine vm) {
        Product p = vm.getSelectedProduct();
        System.out.println("DISPENSING: " + p.name);
        vm.deductBalance(p.price);

        int change = vm.getBalance();
        if (change > 0) {
            System.out.println("Returning Change: " + change);
            vm.setBalance(0);
        }

        vm.setSelectedProduct(null);
        vm.setState(vm.getIdleState());
    }

    @Override
    public void refund(VendingMachine vm) {
        System.out.println("Cannot refund during dispensing.");
    }
}

// --- Context ---
class VendingMachine {
    private State idleState;
    private State hasMoneyState;
    private State dispensingState;

    private State currentState;
    private int balance;
    private Product selectedProduct;
    private Map<String, Product> inventory;

    public VendingMachine() {
        idleState = new IdleState();
        hasMoneyState = new HasMoneyState();
        dispensingState = new DispensingState();
        currentState = idleState;
        balance = 0;
        inventory = new HashMap<>();

        // Load Inventory
        inventory.put("A1", new Product("Coke", 25));
        inventory.put("B2", new Product("Chips", 10));
    }

    public void setState(State s) {
        this.currentState = s;
    }

    public State getIdleState() {
        return idleState;
    }

    public State getHasMoneyState() {
        return hasMoneyState;
    }

    public State getDispensingState() {
        return dispensingState;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int b) {
        this.balance = b;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }

    public Product getProduct(String code) {
        return inventory.get(code);
    }

    public void setSelectedProduct(Product p) {
        this.selectedProduct = p;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    // Delegates
    public void insertCoin(Coin c) {
        currentState.insertCoin(this, c);
    }

    public void selectProduct(String code) {
        currentState.selectProduct(this, code);
    }

    public void dispense() {
        currentState.dispense(this);
    }

    public void refund() {
        currentState.refund(this);
    }
}

// --- Demo ---
public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Vending Machine State Pattern Demo ---");
        VendingMachine vm = new VendingMachine();

        // Scenario 1: Success Buy
        System.out.println("\n1. Buying Coke (25)");
        vm.insertCoin(Coin.DIME); // 10
        vm.insertCoin(Coin.DIME); // 20
        vm.insertCoin(Coin.NICKEL); // 25
        vm.selectProduct("A1"); // Coke

        // Scenario 2: Refund
        System.out.println("\n2. Refund Test");
        vm.insertCoin(Coin.QUARTER);
        vm.refund();

        // Scenario 3: Insufficient Funds
        System.out.println("\n3. Insufficient Funds");
        vm.insertCoin(Coin.DIME);
        vm.selectProduct("A1"); // Price 25, Balance 10
    }
}
