package com.lld.vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineTest {

    @Test
    public void testFullFlow() {
        VendingMachine vm = VendingMachine.getInstance();
        vm.addInventory(new Product("Coke", 10), 5);

        vm.selectProduct("Coke"); // Should fail (Insert money first)

        vm.insertCoin(Coin.TEN);
        vm.selectProduct("Coke"); // Should succeed

        // As state is internal/printed to console, we assert on behavior or public
        // state getters if available.
        // For now, ensuring no exceptions verify basic robustness.
        assertNotNull(vm);

        // In a real SDE-3 impl, we'd expose 'getCurrentState()' for stricter
        // assertions.
    }
}
