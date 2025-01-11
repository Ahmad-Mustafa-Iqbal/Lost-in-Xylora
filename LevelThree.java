package main;

import java.util.Random;
import java.util.Scanner;

public class LevelThree extends Level {
    private int puzzleAttempts;
    private final String[] puzzles = {
            "I speak without a mouth and hear without ears. What am I?",
            "The more of this you take, the more you leave behind. What is it?",
            "I have keys but no locks. What am I?"
    };
    private final String[] answers = { "echo", "footsteps", "keyboard" };

    public LevelThree(Player player) {
        super(player);
        this.puzzleAttempts = 3;
    }

    @Override
    public void play() {
        try {
            printSlowly("Welcome to Level 3: Alien Encounter!", 50);

            Scanner scanner = new Scanner(System.in);

            printSlowly("You have encountered an alien civilization.", 50);
            printSlowly("Do you want to trade resources or engage in combat?", 50);
            printSlowly("1. Trade Resources", 50);
            printSlowly("2. Fight the Alien", 50);
            printSlowly("Enter your choice: ", 50);

            int choice = scanner.nextInt();

            if (choice == 1) {
                tradeResources(scanner);
            } else if (choice == 2) {
                fightAlien(scanner);
            } else {
                printSlowly("Invalid choice! You chose to trade resources by default.", 50);
                tradeResources(scanner);
            }
        } catch (Exception e) {
            printSlowly("An error occurred during Level 3: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void tradeResources(Scanner scanner) {
        try {
            Random random = new Random();
            int fuelCost = 10 + random.nextInt(11);
            int energyCost = 5 + random.nextInt(6);

            printSlowly("The alien demands " + fuelCost + " Fuel and " + energyCost
                    + " Energy in exchange for the location of your crew.", 50);

            if (player.getFuel() >= fuelCost && player.getEnergy() >= energyCost) {
                player.updateFuel(-fuelCost);
                player.updateEnergy(-energyCost);
                player.updateScore(20);
                printSlowly("You successfully traded and obtained the location!", 50);
            } else {
                printSlowly("You don't have enough resources to trade. The alien becomes suspicious and hostile!", 50);
                fightAlien(scanner);
            }
        } catch (Exception e) {
            printSlowly("An error occurred during trade: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void fightAlien(Scanner scanner) {
        try {
            printSlowly("You decided to fight the alien!", 50);

            while (puzzleAttempts > 0) {
                printSlowly("The alien challenges you to a puzzle fight. Solve the riddle to weaken the alien.", 50);
                if (solvePuzzle(scanner)) {
                    player.updateScore(30);
                    printSlowly("You solved the puzzle! The alien is now vulnerable.", 50);
                    printSlowly("Congratulations! You have decoded the location of the stranded crew.", 50);
                    return;
                } else {
                    Random random = new Random();
                    int damage = 10 + random.nextInt(21);
                    player.updateHealth(-damage);
                    printSlowly("You took " + damage + " damage. Remaining health: " + player.getHealth(), 50);
                    if (player.getHealth() <= 0) {
                        printSlowly("You have been defeated by the alien. Game Over!", 50);
                        return;
                    }
                }
            }

            printSlowly("You failed to solve the puzzle after multiple attempts. You are trapped in space forever!",
                    50);
        } catch (Exception e) {
            printSlowly("An error occurred during the alien fight: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private boolean solvePuzzle(Scanner scanner) {
        try {
            Random random = new Random();
            int puzzleIndex = random.nextInt(puzzles.length);

            printSlowly("Puzzle: " + puzzles[puzzleIndex], 50);
            printSlowly("Enter your answer: ", 50);

            String answer = scanner.next().toLowerCase();
            boolean isCorrect = answer.equals(answers[puzzleIndex]);

            if (isCorrect) {
                printSlowly("Correct! The alien is impressed by your wisdom.", 50);
            } else {
                puzzleAttempts--;
                player.updateScore(-10);
                if (puzzleAttempts > 0) {
                    printSlowly("Incorrect answer. You have " + puzzleAttempts + " attempts remaining.", 50);
                } else {
                    printSlowly("You have no attempts left.", 50);
                }
            }

            return isCorrect;
        } catch (Exception e) {
            printSlowly("An error occurred while solving the puzzle: " + e.getMessage(), 50);
            e.printStackTrace();
            return false;
        }
    }

    private void printSlowly(String text, int delay) {
        try {
            for (char ch : text.toCharArray()) {
                System.out.print(ch);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        } catch (Exception e) {
            printSlowly("An error occurred while printing text: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }
}
