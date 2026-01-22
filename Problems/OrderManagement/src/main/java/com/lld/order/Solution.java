package com.lld.order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// --- Entities ---
enum OrderStatus {
    CREATED, PROCESSED, SHIPPED
}

class Order {
    int id;
    String user;
    OrderStatus status;

    public Order(int id, String user) {
        this.id = id;
        this.user = user;
        this.status = OrderStatus.CREATED;
    }

    @Override
    public String toString() {
        return "Order[ID=" + id + ", User=" + user + ", Status=" + status + "]";
    }
}

// --- Producer: Order Service ---
class OrderService {
    private final BlockingQueue<Order> orderQueue;

    public OrderService(BlockingQueue<Order> queue) {
        this.orderQueue = queue;
    }

    public void placeOrder(Order order) {
        try {
            System.out.println("Placing Order: " + order.id);
            orderQueue.put(order); // Blocks if full
            System.out.println("Order " + order.id + " added to queue.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// --- Consumer: Order Processor ---
class OrderProcessor implements Runnable {
    private final BlockingQueue<Order> orderQueue;

    public OrderProcessor(BlockingQueue<Order> queue) {
        this.orderQueue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderQueue.take(); // Blocks if empty
                process(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Processor stopped.");
        }
    }

    private void process(Order order) {
        System.out.println("Processing Order: " + order.id);
        // Simulate heavy work (Payment, Inventory)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        order.status = OrderStatus.PROCESSED;
        System.out.println("Order Completed: " + order);
    }
}

// --- Demo ---
public class Solution {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Order Management System (Producer-Consumer) ---");

        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(5); // Limit 5
        OrderService service = new OrderService(queue);

        // Start Consumers
        Thread worker1 = new Thread(new OrderProcessor(queue));
        Thread worker2 = new Thread(new OrderProcessor(queue));
        worker1.start();
        worker2.start();

        // Produce Orders
        service.placeOrder(new Order(101, "Alice"));
        service.placeOrder(new Order(102, "Bob"));
        service.placeOrder(new Order(103, "Charlie"));

        // Allow time for processing
        Thread.sleep(2000);

        System.out.println("Stopping System...");
        worker1.interrupt();
        worker2.interrupt();
    }
}
