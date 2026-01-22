# 🚀 LLD-Mastery-Pro

> **The Ultimate Low-Level Design (LLD) Roadmap for SDE-1, SDE-2, and SDE-3 Interviews.**

![Java](https://img.shields.io/badge/Language-Java-orange.svg)
![Status](https://img.shields.io/badge/Status-Active-brightgreen.svg)
![Pattern](https://img.shields.io/badge/Focus-OOD%20%26%20Concurrency-blue.svg)

This repository is a comprehensive guide to mastering **Object-Oriented Design** and **Machine Coding** for Big Tech interviews. It is structured to help you scale from writing basic clean code to designing complex, thread-safe, and extensible systems.

---

## 🎯 The Philosophy: "The Golden Rules"

To master LLD at a Senior level, every solution in this repo adheres to three core principles:

1.  **Concurrency First**: We don't just write code; we write _thread-safe_ code. We explicitly handle race conditions using Locks, Atomic variables, or Semaphores.
2.  **The Mermaid Blueprint**: Every problem includes a visual **UML Class Diagram** using Mermaid.js to help you visualize relationships (Association vs Composition).
3.  **The "Why" Section**: We document **Trade-offs**. Why Strategy over State? Why a Factory? We explain the design decisions.

---

## 🗺️ Roadmap & Expectations

| Level              | Focus Area              | Key Concepts                                                                   |
| :----------------- | :---------------------- | :----------------------------------------------------------------------------- |
| **SDE-1 (Junior)** | **Clean Code & SOLID**  | Class modeling, DRY, meaningful naming, basic modularity.                      |
| **SDE-2 (Mid)**    | **Design Patterns**     | Strategy, Factory, Observer, Decorator, State, Command.                        |
| **SDE-3 (Senior)** | **Concurrency & Scale** | Custom Locks, Optimistic Locking, Thread Pools, Handling 10k concurrent users. |

---

## 📚 Solution Index

### 🏢 Real-World Systems

| Problem                                                   | Company Tags         | Pattern / Concept                                   | Status       |
| :-------------------------------------------------------- | :------------------- | :-------------------------------------------------- | :----------- |
| **[Parking Lot System](./Problems/ParkingLot)**           | Amazon, Microsoft    | **Factory, Strategy** (Concurreny: `ReentrantLock`) | ✅ Completed |
| **[Splitwise](./Problems/Splitwise)**                     | Uber, Razorpay       | **Strategy** (Debt Simplification Graph)            | ✅ Completed |
| **[Elevator System](./Problems/ElevatorSystem)**          | Uber, Google         | **State, Dispatcher** (SCAN Algorithm)              | ✅ Completed |
| **[Movie Ticket Booking](./Problems/MovieTicketBooking)** | Flipkart, BookMyShow | **Locking Mechanism** (Concurrency)                 | ✅ Completed |
| **[Vending Machine](./Problems/VendingMachine)**          | Google, Walmart      | **State Pattern** (Complex Transitions)             | ✅ Completed |
| **[ATM Machine](./Problems/ATMMachine)**                  | Amazon, Visa         | **Chain of Responsibility** (Dispensing)            | ✅ Completed |
| **[Order Management](./Problems/OrderManagement)**        | Wayfair, Flipkart    | **Producer-Consumer** (Queue Processing)            | ✅ Completed |
| **[Notification System](./Problems/NotificationSystem)**  | Swiggy, Amazon       | **Observer, Adapter** (Pluggable Channels)          | ✅ Completed |

### 🎮 Games & Tools

| Problem                                           | Company Tags      | Pattern / Concept                          | Status       |
| :------------------------------------------------ | :---------------- | :----------------------------------------- | :----------- |
| **[Snake & Ladders](./Problems/SnakeAndLadders)** | Meta, Amazon      | **Game Loop** (Singleton)                  | ✅ Completed |
| **[Tic-Tac-Toe / Chess](./Problems/Chess)**       | Google, Microsoft | **Command, Flyweight** (Move Validation)   | ✅ Completed |
| **[Log Framework](./Problems/LogFramework)**      | Atlassian         | **Chain of Responsibility**                | ✅ Completed |
| **[Rate Limiter](./Problems/RateLimiter)**        | Stripe, Google    | **Token Bucket** (Thread-Safety)           | ✅ Completed |
| **[LRU Cache](./Problems/LRUCache)**              | Google, Amazon    | **Doubly Linked List + Map** (Concurrency) | ✅ Completed |
| **[Unix "Find" Command](./Problems/UnixFind)**    | Amazon            | **Specification / Composite** (Filtering)  | ✅ Completed |
| **[CricInfo Dashboard](./Problems/CricInfo)**     | Hotstar, Cricbuzz | **Observer** (Live Updates)                | ✅ Completed |

---

## 🏗️ Repository Structure

- `Problems/`: Contains the full source code, Design doc (`Design.md`), and UML (`UML.md`) for each problem.
- `Patterns/`: (Coming Soon) Quick reference for implementation patterns.
- `Company_wise_questions.md`: A mapping of which companies ask which questions.

---

**Author**: Rishi | **Language**: Java | **License**: MIT
