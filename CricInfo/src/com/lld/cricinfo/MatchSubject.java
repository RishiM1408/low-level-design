package com.lld.cricinfo;

public interface MatchSubject {
    void addObserver(ScoreObserver o);

    void removeObserver(ScoreObserver o);

    void notifyObservers();
}
