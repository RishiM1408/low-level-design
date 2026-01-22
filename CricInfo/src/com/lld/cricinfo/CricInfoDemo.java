package com.lld.cricinfo;

public class CricInfoDemo {
    public static void main(String[] args) {
        System.out.println("--- CricInfo Observer Pattern Demo ---");

        CricketMatch match = new CricketMatch();

        ScoreObserver mobile = new MobileDisplay();
        ScoreObserver web = new WebDisplay();

        match.addObserver(mobile);
        match.addObserver(web);

        // Match starts
        match.setScore(50, 0, 5.0);

        // Wicket falls
        match.setScore(50, 1, 5.1);

        // Six
        match.setScore(56, 1, 5.2);

        // Web user unsubscribes
        System.out.println("\n(Web user disconnects)");
        match.removeObserver(web);

        match.setScore(60, 1, 6.0);
    }
}
