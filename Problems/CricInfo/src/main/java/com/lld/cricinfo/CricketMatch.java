package com.lld.cricinfo;

import java.util.ArrayList;
import java.util.List;

public class CricketMatch implements MatchSubject {
    private int runs;
    private int wickets;
    private double overs;
    private List<ScoreObserver> observers;

    public CricketMatch() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(ScoreObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ScoreObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (ScoreObserver o : observers) {
            o.update(runs, wickets, overs);
        }
    }

    public void setScore(int runs, int wickets, double overs) {
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overs;
        System.out.println("\n-- Score Update: " + runs + "/" + wickets + " (" + overs + ") --");
        notifyObservers();
    }
}
