package com.lld.parking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

// --- Enums ---
enum VehicleType {
    CAR, TRUCK, MOTORCYCLE
}

enum SpotType {
    COMPACT, LARGE, MOTORCYCLE_SPOT
}

// --- Strategy Interfaces ---
interface PricingStrategy {
    double calculateFee(Ticket ticket);
}

interface ParkingAssignmentStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType type);
}

// --- Concrete Strategies ---
class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFee(Ticket ticket) {
        long hours = ChronoUnit.HOURS.between(ticket.entryTime, LocalDateTime.now());
        if (hours == 0)
            hours = 1; // Minimum 1 hour
        return hours * 20.0; // Flat 20 per hour
    }
}

class NaturalOrderParkingStrategy implements ParkingAssignmentStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType type) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findFreeSpot(type);
            if (spot != null)
                return spot;
        }
        return null;
    }
}

// --- Entities ---
abstract class Vehicle {
    protected String plate;
    protected VehicleType type;

    public Vehicle(String plate, VehicleType type) {
        this.plate = plate;
        this.type = type;
    }

    public VehicleType getType() {
        return type;
    }
}

class Car extends Vehicle {
    public Car(String plate) {
        super(plate, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(String plate) {
        super(plate, VehicleType.TRUCK);
    }
}

class ParkingSpot {
    private int id;
    private SpotType type;
    private boolean isFree;
    private Vehicle vehicle;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingSpot(int id, SpotType type) {
        this.id = id;
        this.type = type;
        this.isFree = true;
    }

    public boolean isFree() {
        return isFree;
    }

    public SpotType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public boolean assignVehicle(Vehicle v) {
        lock.lock();
        try {
            if (!isFree)
                return false; // Double check
            this.vehicle = v;
            this.isFree = false;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void removeVehicle() {
        lock.lock();
        try {
            this.vehicle = null;
            this.isFree = true;
        } finally {
            lock.unlock();
        }
    }
}

class ParkingFloor {
    private int level;
    private List<ParkingSpot> spots;

    public ParkingFloor(int level, int numSpots) {
        this.level = level;
        this.spots = Collections.synchronizedList(new ArrayList<>());
        // Initialize spots: 50% compact, 25% large, 25% bike
        for (int i = 0; i < numSpots; i++) {
            SpotType type = SpotType.COMPACT;
            if (i < numSpots * 0.25)
                type = SpotType.MOTORCYCLE_SPOT;
            else if (i > numSpots * 0.75)
                type = SpotType.LARGE;
            spots.add(new ParkingSpot(i, type));
        }
    }

    public ParkingSpot findFreeSpot(VehicleType vType) {
        // Simple mapping
        SpotType needed = SpotType.COMPACT;
        if (vType == VehicleType.TRUCK)
            needed = SpotType.LARGE;
        if (vType == VehicleType.MOTORCYCLE)
            needed = SpotType.MOTORCYCLE_SPOT;

        for (ParkingSpot spot : spots) {
            // Optimistic check
            if (spot.isFree() && spot.getType() == needed) {
                return spot;
            }
        }
        return null;
    }
}

class Ticket {
    String id;
    LocalDateTime entryTime;
    ParkingSpot spot;

    public Ticket(ParkingSpot spot) {
        this.id = UUID.randomUUID().toString();
        this.entryTime = LocalDateTime.now();
        this.spot = spot;
    }
}

class ParkingLot {
    private List<ParkingFloor> floors;
    private ParkingAssignmentStrategy assignmentStrategy;
    private PricingStrategy pricingStrategy;
    private static ParkingLot instance;

    private ParkingLot() {
        floors = new ArrayList<>();
        assignmentStrategy = new NaturalOrderParkingStrategy();
        pricingStrategy = new HourlyPricingStrategy();
    }

    public static synchronized ParkingLot getInstance() {
        if (instance == null)
            instance = new ParkingLot();
        return instance;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public Ticket parkVehicle(Vehicle v) {
        ParkingSpot spot = assignmentStrategy.findSpot(floors, v.getType());
        if (spot != null) {
            // Concurrent Attempt: Try to lock/assign
            // The strategy returns a candidate, but between finding and assigning, it might
            // be taken.
            // Our Spot.assignVehicle has a lock. return that result.
            if (spot.assignVehicle(v)) {
                return new Ticket(spot);
            } else {
                // Retry finding? simplified -> fail
                System.out.println("Race condition hit! Spot taken.");
            }
        }
        return null;
    }

    public double exitVehicle(Ticket ticket) {
        ticket.spot.removeVehicle();
        return pricingStrategy.calculateFee(ticket);
    }
}

// --- Main Verification ---
public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Parking Lot System Demo ---");
        ParkingLot lot = ParkingLot.getInstance();
        lot.addFloor(new ParkingFloor(1, 10)); // Floor 1, 10 spots

        Vehicle v1 = new Car("KA-01-1234");
        Vehicle v2 = new Truck("KA-01-9999");

        // 1. Park Car
        Ticket t1 = lot.parkVehicle(v1);
        if (t1 != null) {
            System.out.println("Parked Car. Spot ID: " + t1.spot.getId());
        } else {
            System.out.println("Failed to park Car.");
        }

        // 2. Park Truck
        Ticket t2 = lot.parkVehicle(v2);
        if (t2 != null) {
            System.out.println("Parked Truck. Spot ID: " + t2.spot.getId());
        }

        // 3. Exit Car
        if (t1 != null) {
            double fee = lot.exitVehicle(t1);
            System.out.println("Car Exited. Fee: " + fee);
        }
    }
}
