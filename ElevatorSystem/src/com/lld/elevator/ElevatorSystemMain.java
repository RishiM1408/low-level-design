package com.lld.elevator;

public class ElevatorSystemMain {
    public static void main(String[] args) {
        System.out.println("--- Elevator System Demo ---");

        ElevatorController elevator = new ElevatorController(0); // Ground floor

        // Simulate requests
        // 1. Internal request to go to floor 5
        elevator.addRequest(5);

        // 2. Someone at floor 3 wants to go UP (assuming external request just adds
        // target floor for simplification logic)
        elevator.addRequest(3);

        // 3. Someone at floor 8 wants to go DOWN
        elevator.addRequest(8);

        // 4. Someone at floor 1 wants to go UP
        elevator.addRequest(1);

        // Expected Order for LOOK algorithm starting at 0 UP:
        // UP QUEUE: 1, 3, 5, 8 (Depending on insertion order relative to current floor)
        // Wait, if 1 > 0, 3 > 0, 5 > 0, 8 > 0. All in UP queue.
        // It should visit 1 -> 3 -> 5 -> 8.

        System.out.println("\nStarting Processing:");
        elevator.run();

        // New Scenario: From Top to Bottom
        System.out.println("\n--- Scenario 2: Downward requests ---");
        // Elevator is at 8.
        elevator.addRequest(2); // Down
        elevator.addRequest(4); // Down

        elevator.run();
    }
}
