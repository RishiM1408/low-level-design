package com.lld.cricinfo;

public class MobileDisplay implements ScoreObserver {
    @Override
    public void update(int runs, int wickets, double overs) {
        System.out.println("MOBILE APP: " + runs + "/" + wickets + " Over: " + overs);
    }
}

class WebDisplay implements ScoreObserver {
    @Override
    public void update(int runs, int wickets, double overs) {
        System.out.println("WEBSITE: Score is " + runs + " for " + wickets + " wickets. Current Over: " + overs);
    }
}
