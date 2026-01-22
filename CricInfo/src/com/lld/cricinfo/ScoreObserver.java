package com.lld.cricinfo;

public interface ScoreObserver {
    void update(int runs, int wickets, double overs);
}
