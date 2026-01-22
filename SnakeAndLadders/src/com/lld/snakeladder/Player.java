package com.lld.snakeladder;

public class Player {
    private String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0; // Start at 0 or 1? Often 0, moving to 1..100
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
