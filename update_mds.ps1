$antiPatterns = @{
    "ATMMachine" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Giant If-Else for States
*   **Bad:** `if (state == HAS_CARD) ... else if (state == IDLE) ...`
*   **Why:** Hard to maintain. Adding a new state touches all methods.
*   **Fix:** **State Pattern**. `IdleState`, `HasCardState` classes.

### ❌ 2. Handling Cash as Integers
*   **Bad:** `int balance = 100;`
*   **Why:** Integer overflow or lack of precision for currencies.
*   **Fix:** Use `BigDecimal` or a dedicated `Money` class.
"@

    "Chess" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. God Class 'Board'
*   **Bad:** The Board class validating every move for every piece type.
*   **Why:** Board becomes 2000 lines long.
*   **Fix:** **Polymorphism**. `Piece.validateMove()` handles its own logic.

### ❌ 2. Storing State in strings
*   **Bad:** `String board[][] = new String[8][8];` "K", "Q", "p".
*   **Why:** String parsing is slow and error-prone.
*   **Fix:** Use Object-Oriented `Cell` and `Piece` objects.
"@

    "Splitwise" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Floating Point Money
*   **Bad:** `double amount = 10.0 / 3;`
*   **Why:** Precision errors ($3.33333). Money vanishes.
*   **Fix:** Use `BigDecimal` and rounding modes.

### ❌ 2. Graph Complexity
*   **Bad:** Solving 'Simplify Debt' with N! brute force.
*   **Why:** Crashes with >10 users.
*   **Fix:** Use greedy algorithms or Flow Network (Max Flow/Min Cut) approximations.
"@

    "ElevatorSystem" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. FCFS Scheduling
*   **Bad:** Servicing requests strictly in arrival order.
*   **Why:** Elevator zig-zags (1 -> 10 -> 2 -> 9). 
*   **Fix:** **SCAN/LOOK Algorithm**. Continues in one direction before switching.

### ❌ 2. Polling for Requests
*   **Bad:** `while(true) { checkButtons(); sleep(100); }`
*   **Why:** CPU intensive or latent.
*   **Fix:** Use **Observer Pattern** or Event Listeners.
"@

    "LogFramework" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Blocking UI for Logs
*   **Bad:** Writing to disk on the main thread.
*   **Why:** App freezes while waiting for I/O.
*   **Fix:** asynchronous logging (Producer-Consumer queue).

### ❌ 2. Hardcoded Log Levels
*   **Bad:** `if (level == 1)` checks everywhere.
*   **Fix:** **Chain of Responsibility**. `InfoLogger` passes to `ErrorLogger`.
"@

    "LRUCache" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. O(N) Scans
*   **Bad:** Using `ArrayList` for the cache and scanning to find the oldest.
*   **Why:** Operations become O(N). Cache is slow.
*   **Fix:** `HashMap` + `DoublyLinkedList` for O(1).

### ❌ 2. Global Lock
*   **Bad:** `synchronized(map)` for both reads and writes.
*   **Why:** Reads block writes.
*   **Fix:** `ReadWriteLock` or `ConcurrentHashMap`.
"@

    "MovieTicketBooking" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. The 'Double Booking' Bug
*   **Bad:** Checking `isSeatFree` and `bookSeat` as separate DB calls.
*   **Why:** Race condition. Two users book the same seat.
*   **Fix:** **Transactions** (ACID) or `SELECT ... FOR UPDATE` locking.

### ❌ 2. Polling for Payment
*   **Bad:** Client polling server 'is payment done?'.
*   **Fix:** Webhooks or Async Messaging (Kafka/SQS).
"@

    "NotificationSystem" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Synchronous Third-Party Calls
*   **Bad:** Calling SendGrid/Twilio API on the user request thread.
*   **Why:** If Twilio is down, your API hangs.
*   **Fix:** **Message Queue** (RabbitMQ). Fire and forget.

### ❌ 2. Hard Dependencies
*   **Bad:** `new EmailSender()` inside the service.
*   **Fix:** **Dependency Injection** & **Adapter Pattern** to swap providers easily.
"@

    "OrderManagement" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Inventory Oversell
*   **Bad:** Decrementing inventory after payment success.
*   **Why:** User pays, but item is gone. Bad UX.
*   **Fix:** Reserve inventory *temporarily* during checkout (TTL).

### ❌ 2. God 'Order' Object
*   **Bad:** Order object handling payment, shipping, and notification logic.
*   **Fix:** **Saga Pattern** or Event Driven Architecture.
"@

    "SnakeAndLadders" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Hardcoded Board
*   **Bad:** `if (pos == 99) goto 10`
*   **Why:** Can't change board layout.
*   **Fix:** Map of Jump Entities (Start -> End).

### ❌ 2. Singleton Game
*   **Bad:** Formatting the game as a static class.
*   **Why:** Cannot run multiple games (e.g., tournament).
*   **Fix:** Game instance per session.
"@

    "UnixFind" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Recursion without Limit
*   **Bad:** Naive DFS on a circular symlink (folder inside itself).
*   **Why:** StackOverflowError.
*   **Fix:** Track visited inodes or set max depth.

### ❌ 2. Hardcoded Filters
*   **Bad:** `if (file == "txt" && size > 5MB)`
*   **Why:** Can't add dynamic AND/OR logic.
*   **Fix:** **Specification Pattern** or **Composite Pattern**.
"@

    "CricInfo" = @"

---
## 6. Anti-Patterns (What NOT to do)
### ❌ 1. Polling for Scores
*   **Bad:** Client refreshing every second.
*   **Why:** Server load.
*   **Fix:** **Observer Pattern** or WebSockets for Push updates.

### ❌ 2. Tightly Coupled Displays
*   **Bad:** Match object calling `mobileDisplay.update()`.
*   **Fix:** Decouple via `Subject` interface. Match doesn't know who is watching.
"@
}

foreach ($key in $antiPatterns.Keys) {
    if (Test-Path "e:\low-level-design\Problems\$key\README.md") {
        Add-Content -Path "e:\low-level-design\Problems\$key\README.md" -Value $antiPatterns[$key]
        Write-Host "Updated $key README"
    }
}
