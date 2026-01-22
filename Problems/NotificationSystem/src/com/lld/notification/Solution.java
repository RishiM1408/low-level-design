package com.lld.notification;

// --- Enums / Models ---
enum ChannelType {
    EMAIL, SMS, PUSH
}

class Notification {
    String message;
    String recipient;

    public Notification(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }
}

// --- Interface ---
interface NotificationSender {
    void send(Notification notification);
}

// --- Adapters / Concrete Implementations ---
class EmailSender implements NotificationSender {
    @Override
    public void send(Notification n) {
        System.out.println("Sending EMAIL to " + n.recipient + ": " + n.message);
        // In real life: call SendGrid/AWS SES API
    }
}

class SMSSender implements NotificationSender {
    @Override
    public void send(Notification n) {
        System.out.println("Sending SMS to " + n.recipient + ": " + n.message);
        // In real life: call Twilio API
    }
}

class PushSender implements NotificationSender {
    @Override
    public void send(Notification n) {
        System.out.println("Sending PUSH to " + n.recipient + ": " + n.message);
        // In real life: call FCM/APNS
    }
}

// --- Factory ---
class NotificationFactory {
    public static NotificationSender createSender(ChannelType type) {
        switch (type) {
            case EMAIL:
                return new EmailSender();
            case SMS:
                return new SMSSender();
            case PUSH:
                return new PushSender();
            default:
                throw new IllegalArgumentException("Unknown Channel");
        }
    }
}

// --- Service ---
class NotificationService {
    public void sendNotification(String msg, String recipient, ChannelType type) {
        Notification n = new Notification(msg, recipient);
        NotificationSender sender = NotificationFactory.createSender(type);
        sender.send(n);
    }

    // SDE-3: Bulk Send
    public void sendBulk(String msg, String[] recipients, ChannelType type) {
        NotificationSender sender = NotificationFactory.createSender(type);
        for (String r : recipients) {
            sender.send(new Notification(msg, r));
        }
    }
}

// --- Demo ---
public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Notification System Demo ---");
        NotificationService service = new NotificationService();

        // 1. Send Email
        service.sendNotification("Welcome to LLD Mastery!", "user@example.com", ChannelType.EMAIL);

        // 2. Send SMS
        service.sendNotification("OTP: 1234", "+1987654321", ChannelType.SMS);

        // 3. Bulk Push
        String[] users = { "Device1", "Device2" };
        service.sendBulk("New Offer!", users, ChannelType.PUSH);
    }
}
