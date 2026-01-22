package com.lld.chess;

// --- Entities ---
abstract class Piece {
    boolean white;

    public Piece(boolean white) {
        this.white = white;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    public boolean isWhite() {
        return white;
    }
}

class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // L-shape: 2+1 or 1+2
        int x = Math.abs(start.x - end.x);
        int y = Math.abs(start.y - end.y);
        return x * y == 2;
    }
}

class King extends Piece {
    public King(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // 1 step any direction
        int x = Math.abs(start.x - end.x);
        int y = Math.abs(start.y - end.y);
        return x <= 1 && y <= 1;
    }
}

class Spot {
    int x, y;
    Piece piece;

    public Spot(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
}

class Board {
    Spot[][] spots = new Spot[8][8];

    public Board() {
        resetBoard();
    }

    public void resetBoard() {
        // Initialize empty
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = new Spot(i, j, null);
            }
        }
        // Demo: Place White King at (0,0), Black Knight at (2,1)
        spots[0][0].piece = new King(true);
        spots[2][1].piece = new Knight(false);
    }

    public Spot getSpot(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return null;
        return spots[x][y];
    }
}

class Move {
    Spot start;
    Spot end;
    Piece pieceMoved;

    public Move(Spot start, Spot end) {
        this.start = start;
        this.end = end;
        this.pieceMoved = start.piece;
    }

    public boolean isValid() {
        // Source must have piece
        if (pieceMoved == null)
            return false;

        // Dest must not have friendly piece
        if (end.piece != null && end.piece.white == pieceMoved.white)
            return false;

        return true;
    }
}

class Game {
    Board board;

    public Game() {
        board = new Board();
    }

    public boolean playerMove(int startX, int startY, int endX, int endY) {
        Spot start = board.getSpot(startX, startY);
        Spot end = board.getSpot(endX, endY);

        Move move = new Move(start, end);
        if (!move.isValid()) {
            System.out.println("Invalid Move: Basic validation failed.");
            return false;
        }

        if (start.piece.canMove(board, start, end)) {
            // Execute Move
            end.piece = start.piece;
            start.piece = null;
            System.out.println(
                    "Move Successful: " + end.piece.getClass().getSimpleName() + " to (" + endX + "," + endY + ")");
            return true;
        } else {
            System.out.println("Invalid Move: Piece logic failed.");
            return false;
        }
    }
}

// --- Demo ---
// --- Interactive Demo ---
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        System.out.println("--- Chess Game CLI (Interactive) ---");
        System.out.println("Commands: 'move x1 y1 x2 y2', 'reset', 'exit'");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            String command = parts[0].toLowerCase();

            // Java 14 Switch Expression
            String result = switch (command) {
                case "exit" -> {
                    running = false;
                    yield "Exiting...";
                }
                case "reset" -> {
                    game = new Game();
                    yield "Board Reset.";
                }
                case "move" -> {
                    if (parts.length < 5)
                        yield "Usage: move x1 y1 x2 y2";
                    try {
                        int x1 = Integer.parseInt(parts[1]);
                        int y1 = Integer.parseInt(parts[2]);
                        int x2 = Integer.parseInt(parts[3]);
                        int y2 = Integer.parseInt(parts[4]);
                        boolean success = game.playerMove(x1, y1, x2, y2);
                        yield success ? "Valid Move! Board updated." : "Invalid Move.";
                    } catch (NumberFormatException e) {
                        yield "Error: Coordinates must be integers.";
                    }
                }
                default -> "Unknown command: " + command;
            };

            System.out.println(result);
        }
        scanner.close();
    }
}
