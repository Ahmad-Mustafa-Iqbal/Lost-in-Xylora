package main;

import java.util.Random;
import java.util.Scanner;

public class LevelTwo extends Level {
    private int score;
    private char[][] grid;
    private int playerX, playerY;
    private int clueScoreTarget;
    private Random random;

    public LevelTwo(Player player) {
        super(player);
        this.score = 0;
        this.grid = new char[7][7];
        this.playerX = 3;
        this.playerY = 3;
        this.random = new Random();
        this.clueScoreTarget = 30 + random.nextInt(31);
        initializeGrid();
    }

    private void initializeGrid() {
        try {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = '.';
                }
            }

            placeRandomObjects('A', 5);
            placeRandomObjects('S', 2);
            placeRandomObjects('C', 1);
        } catch (Exception e) {
            printSlowly("Error initializing grid: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void placeRandomObjects(char object, int count) {
        try {
            for (int i = 0; i < count; i++) {
                int x, y;
                do {
                    x = random.nextInt(grid.length);
                    y = random.nextInt(grid[0].length);
                } while (grid[x][y] != '.' || (x == playerX && y == playerY));
                grid[x][y] = object;
            }
        } catch (Exception e) {
            printSlowly("Error placing random objects: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void printGrid() {
        try {
            System.out.println();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == playerX && j == playerY) {
                        System.out.print("^  ");
                    } else {
                        System.out.print(grid[i][j] + "  ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        } catch (Exception e) {
            printSlowly("Error printing grid: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void advanceAsteroids() {
        try {
            int newAsteroids = random.nextInt(2) + 1;
            placeRandomObjects('A', newAsteroids);
        } catch (Exception e) {
            printSlowly("Error advancing asteroids: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void updateClues() {
        try {
            placeRandomObjects('C', 1);
        } catch (Exception e) {
            printSlowly("Error updating clues: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        try {
            printSlowly("Welcome to Level 2: Asteroid Storm!", 50);
            Scanner scanner = new Scanner(System.in);

            boolean survived = false;

            printSlowly("Navigate through the asteroid field and collect clues to win!", 50);
            printSlowly("Target clue score: " + clueScoreTarget, 50);

            boolean navigationGlitch = player.hasNavigationGlitch();

            if (navigationGlitch) {
                printSlowly("Warning: Navigation glitch is active! Your moves may get misdirected.", 50);
            }

            while (true) {
                printGrid();
                printSlowly("\nOptions:", 50);
                printSlowly("W: Move Up", 50);
                printSlowly("A: Move Left", 50);
                printSlowly("S: Move Down", 50);
                printSlowly("D: Move Right", 50);
                printSlowly("Enter your move (W/A/S/D): ", 50);

                printSlowly("\n(A = Asteroid, S = Station, C = Clue, ^ = Your Ship)", 50);

                char move = scanner.next().toUpperCase().charAt(0);

                if (navigationGlitch) {
                    Random random = new Random();
                    if (random.nextInt(5) == 0) {
                        int randomMove = random.nextInt(4);
                        switch (randomMove) {
                            case 0:
                                if (playerX > 0)
                                    playerX--;
                                break;
                            case 1:
                                if (playerY > 0)
                                    playerY--;
                                break;
                            case 2:
                                if (playerX < grid.length - 1)
                                    playerX++;
                                break;
                            case 3:
                                if (playerY < grid[0].length - 1)
                                    playerY++;
                                break;
                        }
                        printSlowly("Navigation glitch caused misdirection!", 50);
                    } else {
                        switch (move) {
                            case 'W':
                                if (playerX > 0)
                                    playerX--;
                                break;
                            case 'A':
                                if (playerY > 0)
                                    playerY--;
                                break;
                            case 'S':
                                if (playerX < grid.length - 1)
                                    playerX++;
                                break;
                            case 'D':
                                if (playerY < grid[0].length - 1)
                                    playerY++;
                                break;
                            default:
                                printSlowly("Invalid move! Try again.", 50);
                                continue;
                        }
                    }
                } else {
                    switch (move) {
                        case 'W':
                            if (playerX > 0)
                                playerX--;
                            break;
                        case 'A':
                            if (playerY > 0)
                                playerY--;
                            break;
                        case 'S':
                            if (playerX < grid.length - 1)
                                playerX++;
                            break;
                        case 'D':
                            if (playerY < grid[0].length - 1)
                                playerY++;
                            break;
                        default:
                            printSlowly("Invalid move! Try again.", 50);
                            continue;
                    }
                }

                player.updateFuel(-1);
                score += 1;

                char currentCell = grid[playerX][playerY];
                grid[playerX][playerY] = '.';

                if (currentCell == 'A') {
                    int damage = random.nextInt(15) + 5;
                    player.updateHealth(-damage);
                    printSlowly("You hit an asteroid! Took " + damage + " damage.", 50);
                } else if (currentCell == 'S') {
                    player.updateHealth(10);
                    player.updateFuel(20);
                    printSlowly("You reached a recharge station. Gained health and fuel.", 50);
                    score += 10;
                } else if (currentCell == 'C') {
                    score += 30;
                    printSlowly("You found a clue!", 50);
                    updateClues();
                }

                if (player.getFuel() <= 0) {
                    printSlowly("You ran out of fuel and failed to survive.", 50);
                    break;
                } else if (player.getHealth() <= 0) {
                    printSlowly("Your spaceship was destroyed.", 50);
                    break;
                } else if (player.getEnergy() <= 0) {
                    printSlowly("You ran out of energy.", 50);
                    break;
                }

                printSlowly("\nCurrent Status:", 50);
                printSlowly("Fuel: " + player.getFuel() + " | Health: " + player.getHealth() + " | Energy: "
                        + player.getEnergy(), 50);
                printSlowly("Score: " + score, 50);

                if (score >= clueScoreTarget) {
                    survived = true;
                    break;
                }

                advanceAsteroids();
            }

            if (survived) {
                printSlowly("Congratulations! You survived the asteroid storm and completed the mission.", 50);
                score += 50;
            } else {
                printSlowly("Mission failed. Try again!", 50);
            }

            player.updateScore(score);
        } catch (Exception e) {
            printSlowly("An error occurred during Level 2: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void printSlowly(String text, int delay) {
        for (char ch : text.toCharArray()) {
            System.out.print(ch);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}
