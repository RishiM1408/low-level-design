package com.lld.snakeladder;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int size;
    // Map of Start -> End for both Snakes and Ladders
    private Map<Integer, Integer> jumps;

    public Board(int size) {
        this.size = size;
        this.jumps = new HashMap<>();
    }

    public void addJump(int start, int end) {
        if (start < 0 || start > size || end < 0 || end > size) {
            throw new IllegalArgumentException("Jump positions out of bounds");
        }
        jumps.put(start, end);
    }

    public int getNewPosition(int currentPos) {
        if (currentPos > size)
            return currentPos; // Should be handled by game logic (overshoot)

        // If there is a jump at this position (Snake head or Ladder bottom)
        if (jumps.containsKey(currentPos)) {
            int target = jumps.get(currentPos);
            System.out.println("   Jump triggered! " + currentPos + " -> " + target);
            return target;
        }
        return currentPos;
    }

    public int getSize() {
        return size;
    }
}
