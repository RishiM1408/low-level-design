# Design Patterns Reference

This directory serves as a quick reference guide for the Design Patterns used in our LLD solutions.

## 1. Behavioral Patterns

### Strategy Pattern

- **Concept:** Define a family of algorithms, encapsulate each one, and make them interchangeable.
- **Used In:**
  - **[Parking Lot](../Problems/ParkingLot):** `PricingStrategy` (Hourly, Minute).
  - **[Splitwise](../Problems/Splitwise):** `Split` Strategy (Equal, Exact, Percent).
  - **[Unix Find](../Problems/UnixFind):** `Filter` Strategy.

### State Pattern

- **Concept:** Allow an object to alter its behavior when its internal state changes.
- **Used In:**
  - **[Vending Machine](../Problems/VendingMachine):** `Idle`, `HasMoney`, `Dispensing`.
  - **[ATM Machine](../Problems/ATM):** `Idle`, `HasCard`, `SelectOption`.
  - **[Elevator System](../Problems/ElevatorSystem):** `MovingUp`, `MovingDown`, `Idle`.

### Observer Pattern

- **Concept:** One-to-many dependency so that when one object changes state, all its dependents are notified.
- **Used In:**
  - **[CricInfo](../Problems/CricInfo):** `MatchSubject` notifies `ScoreObserver`s.
  - **[Notification System](../Problems/NotificationSystem):** `NotificationService` acts as a publisher.

### Chain of Responsibility

- **Concept:** Pass requests along a chain of handlers.
- **Used In:**
  - **[Log Framework](../Problems/LogFramework):** `InfoLogger` -> `DebugLogger` -> `ErrorLogger`.
  - **[ATM Machine](../Problems/ATM):** `2000 dispenser` -> `500 dispenser` -> `100 dispenser`.

### Command Pattern

- **Concept:** Encapsulate a request as an object.
- **Used In:**
  - **[Chess](../Problems/Chess):** `Move` object encapsulates the action.

---

## 2. Structural Patterns

### Adapter Pattern

- **Concept:** Convert the interface of a class into another interface clients expect.
- **Used In:**
  - **[Notification System](../Problems/NotificationSystem):** `EmailAdapter`, `SMSAdapter`.

### Composite Pattern

- **Concept:** Compose objects into tree structures to represent part-whole hierarchies.
- **Used In:**
  - **[Unix Find](../Problems/UnixFind):** `AndFilter`, `OrFilter` combine other filters.

---

## 3. Creational Patterns

### Factory Pattern

- **Concept:** Define an interface for creating an object, but let subclasses decide which class to instantiate.
- **Used In:**
  - **[Notification System](../Problems/NotificationSystem):** `NotificationFactory`.
  - **[Parking Lot](../Problems/ParkingLot):** `VehicleFactory` (implicit).

### Singleton Pattern

- **Concept:** Ensure a class has only one instance.
- **Used In:**
  - **[Snake & Ladders](../Problems/SnakeAndLadders):** `Game` instance (often singleton in simple apps).
