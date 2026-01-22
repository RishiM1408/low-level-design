# Parking Lot System

## 1. Problem Statement & Company Tags

**Problem:** Design a Parking Lot system that can handle multiple floors, different spot types (Compact, Large, Handicapped), and vehicle types. The system must assign spots using a strategy (e.g., closest to entrance) and calculate parking fees.

**Company Tags:** `Amazon` `Microsoft` `Goldman Sachs` `Salesforce`

---

## 2. Requirement Clarification

### Functional Requirements

1.  **Multiple Floors:** The parking lot has many levels.
2.  **Spot Types:** Compact, Large, Handicapped, Motorcycle.
3.  **Vehicle Types:** Car (fits Compact/Large), Truck (fits Large), Motorcycle.
4.  **Entry/Exit:** Issue Ticket at entry, Process Payment at exit.
5.  **Strategies:**
    - _Parking Strategy:_ Find the first available spot (or closest to elevator).
    - _Pricing Strategy:_ Hourly rates, different for weekends/vehicle types.

### Non-Functional Requirements

1.  **Concurrency:** Critical. If two cars enter simultaneously, they shouldn't be assigned the same spot.
2.  **Scalability:** Supports adding more floors/spots dynamically.
3.  **Extensibility:** Easy to add new pricing models.

---

## 3. The Seniority Perspective

### SDE-1 Focus: Class Hierarchy & SOLID

- **Focus:** Can you correctly model `Vehicle`, `ParkingSpot`, `ParkingLot`, `Ticket`?
- **Expectation:** Use Enums for `VehicleType` and `SpotType`. Ensure `Spot` has a `park(Vehicle)` method.
- **Anti-Pattern:** Putting all logic in `main()`.

### SDE-2 Focus: Design Patterns (Strategy & Factory)

- **Focus:** Finding a spot and Calculating Fee.
- **Pattern:** **Strategy Pattern** for `ParkingAssignmentStrategy` (Random, NearEntrance) and `PricingStrategy`.
- **Pattern:** **Factory Pattern** to create Vehicles or Spots.

### SDE-3 Focus: Concurrency & Trade-offs

- **Focus:** **Race Conditions**.
- **Scenario:** 1000 entry gates, 1 empty spot.
- **Solution:**
  - **Fine-grained Locking:** Lock individual `Floor` or `Spot`.
  - **Atomic Definitions:** Use `AtomicBoolean` for spot availability or a `ConcurrentQueue` for free spots.
  - **Semaphore:** Limit access to the "Find Spot" critical section.

---

## 4. Trade-offs (SDE-3 Deep Dive)

| Decision         | Option A                                 | Option B                                | Why we chose B?                                                                                                                                                                                                                                                                                            |
| :--------------- | :--------------------------------------- | :-------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Spot Storage** | Loop through `List<Spot>` to find valid. | Maintain `PriorityQueue` of free spots. | **A (or Optimized A)**. Maintaining a synchronizing PQ is complex. We chose a simple `List` with `synchronized` search for simplicity in LLD code, but a PQ is cleaner for "Nearest Spot". _Correction:_ Let's use a **Concurrent List** or `CopyOnWriteArrayList` but strictly lock the assignment block. |
| **Concurrency**  | `synchronized` entire `park()` method.   | Fine-grained lock per floor.            | **B**. Locking the whole lot creates a bottleneck. Better to lock per floor or use concurrent collections.                                                                                                                                                                                                 |
| **Pricing**      | Hardcode `calculate()` in Ticket.        | Strategy Interface.                     | **Strategy Interface**. Allows easy updates for "Weekend Pricing" or "Dynamic Pricing" without code changes.                                                                                                                                                                                               |
