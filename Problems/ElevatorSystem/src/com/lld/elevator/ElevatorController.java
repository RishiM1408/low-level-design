package com.lld.elevator;

import java.util.PriorityQueue;
import java.util.Collections;

public class ElevatorController {
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    private int currentFloor;
    private Direction direction;

    public ElevatorController(int startFloor) {
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.upQueue = new PriorityQueue<>(); // Default: Min-Heap (1, 2, 3...)
        this.downQueue = new PriorityQueue<>(Collections.reverseOrder()); // Max-Heap (10, 9, 8...)
    }

    public synchronized void addRequest(int floor) {
        if (floor == currentFloor) {
            System.out.println("Elevator is already at floor " + floor);
            return;
        }

        if (floor > currentFloor) {
            upQueue.add(floor);
            System.out.println("Added to UP Queue: " + floor);
        } else {
            downQueue.add(floor);
            System.out.println("Added to DOWN Queue: " + floor);
        }

        // Wake up if idle
        if (direction == Direction.IDLE) {
            if (floor > currentFloor)
                direction = Direction.UP;
            else
                direction = Direction.DOWN;
        }
    }

    public void run() {
        while (!upQueue.isEmpty() || !downQueue.isEmpty()) {
            processRequests();
        }
        direction = Direction.IDLE;
        System.out.println("Elevator IDLE at floor " + currentFloor);
    }

    private void processRequests() {
        if (direction == Direction.UP || direction == Direction.IDLE) {
            processUpRequests();
            if (!downQueue.isEmpty()) {
                direction = Direction.DOWN;
                processDownRequests();
            }
        } else {
            processDownRequests();
            if (!upQueue.isEmpty()) {
                direction = Direction.UP;
                processUpRequests();
            }
        }
    }

    private void processUpRequests() {
        while (!upQueue.isEmpty()) {
            int target = upQueue.poll();
            // Move up
            if (target >= currentFloor) {
                currentFloor = target;
                System.out.println("Moving UP... Reached Floor " + currentFloor);
            }
        }
    }

    private void processDownRequests() {
        while (!downQueue.isEmpty()) {
            int target = downQueue.poll();
            // Move down
            if (target <= currentFloor) {
                currentFloor = target;
                System.out.println("Moving DOWN... Reached Floor " + currentFloor);
            }
        }
    }
}
