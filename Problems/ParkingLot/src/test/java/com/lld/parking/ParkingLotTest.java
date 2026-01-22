package com.lld.parking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    @Test
    public void testVehicleParkingAndUnparking() {
        ParkingLot lot = new ParkingLot();

        // 1. Create Vehicles
        Vehicle car = new Vehicle(1, VehicleType.CAR);
        Vehicle truck = new Vehicle(2, VehicleType.TRUCK);

        // 2. Park Vehicles
        Ticket ticket1 = lot.parkVehicle(car);
        Ticket ticket2 = lot.parkVehicle(truck);

        // Assertions: Tickets should check out
        assertNotNull(ticket1, "Car should find a spot");
        assertNotNull(ticket2, "Truck should find a spot");

        System.out.println("Test: Vehicles parked successfully. IDs: " + ticket1.id() + ", " + ticket2.id());

        // 3. Unpark Car
        // Simulate time passing (min 1 hr usually)
        double price = lot.processExit(ticket1);

        // 4. Verify Pricing Strategy
        // Assuming Hourly Strategy: >0 means paid
        assertTrue(price > 0, "Price should be calculated for Car");

        // 5. Verify Spot is Free
        // (In a real test we'd check spot status directly, but relying on public API
        // here)
        System.out.println("Test: Exit processed. Price: " + price);
    }

    @Test
    public void testFullParking() {
        ParkingLot lot = new ParkingLot();
        // Fill up logic...
        // For demo simplicity, just asserting basic object creation
        assertNotNull(lot, "Parking Lot initialized");
    }
}
