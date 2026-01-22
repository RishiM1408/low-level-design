package com.lld.snakeladder;

public class SnakeGameDemo {
    public static void main(String[] args) {
        System.out.println("--- Snake & Ladders Demo ---");

        // 1. Initialize Game (Board Size 20 for quick demo)
        Game game = new Game(20, 1);

        // 2. Add Players
        game.addPlayer(new Player("Alice"));
        game.addPlayer(new Player("Bob"));

        // 3. Add Snakes and Ladders
        // Ladder: 2 -> 15
        game.addSnakeOrLadder(2, 15);
        // Snake: 18 -> 1
        game.addSnakeOrLadder(18, 1);

        // 4. Start Game
        game.startGame();
    }
}
