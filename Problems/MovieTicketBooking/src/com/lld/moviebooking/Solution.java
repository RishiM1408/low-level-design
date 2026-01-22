package com.lld.moviebooking;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

// --- Enums ---
enum SeatStatus {
    AVAILABLE, LOCKED, BOOKED
}

enum SeatType {
    NORMAL, PREMIUM
}

// --- Entity Classes ---
class Seat {
    String id;
    SeatType type;
    SeatStatus status;
    double price;

    public Seat(String id, SeatType type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }
}

class User {
    String id;

    public User(String id) {
        this.id = id;
    }
}

class Show {
    String id;
    String movieName;
    Map<String, Seat> seats;

    public Show(String id, String movieName, int numSeats) {
        this.id = id;
        this.movieName = movieName;
        this.seats = new ConcurrentHashMap<>();
        for (int i = 1; i <= numSeats; i++) {
            String seatId = "S" + i;
            SeatType type = (i <= 2) ? SeatType.PREMIUM : SeatType.NORMAL;
            double price = (type == SeatType.PREMIUM) ? 200.0 : 100.0;
            seats.put(seatId, new Seat(seatId, type, price));
        }
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    // CRITICAL SECTION: Lock specific seats to prevent double booking.
    // SDE-3 focus: Synchronize this method to ensure atomicity of checking +
    // locking.
    // Or use synchronized block on "this".
    public synchronized boolean lockSeats(List<String> seatIds) {
        // 1. Check if ALL requested seats are AVAILABLE
        for (String seatId : seatIds) {
            Seat seat = seats.get(seatId);
            if (seat == null || seat.status != SeatStatus.AVAILABLE) {
                return false; // Fail fast if any seat is taken
            }
        }

        // 2. All are available, LOCK them
        for (String seatId : seatIds) {
            Seat seat = seats.get(seatId);
            seat.status = SeatStatus.LOCKED;
        }
        System.out.println("Seats locked successfully: " + seatIds);
        return true;
    }

    public void unlockSeats(List<Seat> seatsToUnlock) {
        for (Seat seat : seatsToUnlock) {
            seat.status = SeatStatus.AVAILABLE;
        }
    }

    public void confirmSeats(List<Seat> seatsToConfirm) {
        for (Seat seat : seatsToConfirm) {
            seat.status = SeatStatus.BOOKED;
        }
    }
}

class Booking {
    String id;
    Show show;
    List<Seat> seats;
    User user;
    double amount;
    LocalDateTime bookingTime;

    public Booking(Show show, List<Seat> seats, User user) {
        this.id = UUID.randomUUID().toString();
        this.show = show;
        this.seats = seats;
        this.user = user;
        this.bookingTime = LocalDateTime.now();
        this.amount = seats.stream().mapToDouble(s -> s.price).sum();
    }
}

// --- Controller / System ---
class BookingSystem {

    public Booking bookTickets(User user, Show show, List<String> seatIds) {
        // 1. Attempt lock
        boolean locked = show.lockSeats(seatIds);
        if (!locked) {
            System.out.println("User " + user.id + ": Booking Failed. Seats already taken.");
            return null;
        }

        // 2. Create Booking (Simulate Payment processing)
        List<Seat> bookedSeats = new ArrayList<>();
        for (String id : seatIds)
            bookedSeats.add(show.getSeat(id));

        // Simulating Payment Success
        try {
            // System.out.println("Processing payment for User " + user.id + "...");
            Thread.sleep(50); // Simulate network delay
        } catch (InterruptedException e) {
        }

        // 3. Confirm
        show.confirmSeats(bookedSeats);
        Booking booking = new Booking(show, bookedSeats, user);
        System.out.println("User " + user.id + ": Booking Confirmed! ID: " + booking.id);
        return booking;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Movie Ticket Booking Concurrency Demo ---");

        Show show = new Show("SHOW1", "Avengers", 5); // 5 Seats: S1..S5
        BookingSystem system = new BookingSystem();

        User user1 = new User("Alice");
        User user2 = new User("Bob");

        // Concurrency Test: Alice and Bob try to book same seats [S1, S2] at same time
        Runnable task1 = () -> {
            system.bookTickets(user1, show, Arrays.asList("S1", "S2"));
        };

        Runnable task2 = () -> {
            system.bookTickets(user2, show, Arrays.asList("S1", "S3")); // S1 conflict
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verification: Check status of S1
        Seat s1 = show.getSeat("S1");
        System.out.println("Final Status of S1: " + s1.status);
    }
}
