package com.lld.snakeladder;

import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private Board board;
    private Dice dice;
    private Queue<Player> players;
    private boolean winnerFound;

    public Game(int boardSize, int numberOfDice) {
        this.board = new Board(boardSize);
        this.dice = new Dice(numberOfDice);
        this.players = new LinkedList<>();
        this.winnerFound = false;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addSnakeOrLadder(int start, int end) {
        board.addJump(start, end);
    }

    public void startGame() {
        if (players.isEmpty()) {
            System.out.println("No players!");
            return;
        }

        while (!winnerFound) {
            Player currentPlayer = players.poll();
            System.out.println("\nTurn: " + currentPlayer.getName() + " at " + currentPlayer.getPosition());

            int rollValue = dice.roll();
            System.out.println("   Rolled: " + rollValue);

            int nextPos = currentPlayer.getPosition() + rollValue;

            if (nextPos > board.getSize()) {
                System.out.println("   Overshot! Stay at " + currentPlayer.getPosition());
                players.add(currentPlayer);
                continue;
            }

            // Check for jumps (Snake/Ladder)
            nextPos = board.getNewPosition(nextPos);

            // Check win
            if (nextPos == board.getSize()) {
                System.out.println("   Moved to " + nextPos);
                System.out.println("WINNER: " + currentPlayer.getName() + " wins the game!");
                winnerFound = true;
                return;
            } else {
                System.out.println("   Moved to " + nextPos);
                currentPlayer.setPosition(nextPos);
                players.add(currentPlayer);
            }
        }
    }
}
