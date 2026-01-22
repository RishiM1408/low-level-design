package com.lld.elevator;

public class Request {
    int floor;

    public Request(int floor) {
        this.floor = floor;
    }

    // We could add Direction for external request, but for simplicity we just treat
    // all as "Go to Floor X".
    // In real SCAN, we treat internal and external roughly same: Just stops to
    // make.
    public int getFloor() {
        return floor;
    }
}
