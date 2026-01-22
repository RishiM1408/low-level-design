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
public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Chess Game Demo ---");
        Game game = new Game();

        // 1. Valid Knight Move (2,1) -> (0,2) (L-Shape)
        // Black Knight is at (2,1)
        System.out.println("Test 1: Moving Knight (2,1) -> (0,2)");
        boolean r1 = game.playerMove(2, 1, 0, 2);
        System.out.println("Result: " + r1);

        // 2. Invalid King Move (0,0) -> (0,2) (Too far)
        System.out.println("\nTest 2: Moving King (0,0) -> (0,2)");
        boolean r2 = game.playerMove(0, 0, 0, 2);
        System.out.println("Result: " + r2);
    }
}
