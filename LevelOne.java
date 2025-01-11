package main;

import java.util.Random;
import java.util.Scanner;

public class LevelOne extends Level {
    public LevelOne(Player player) {
        super(player);
    }

    @Override
    public void play() {
        printSlowly("Welcome to Level 1: Spaceship Preparation!", 50);
        Scanner scanner = new Scanner(System.in);

        try {
            printSlowly("Allocate resources (Fuel, Oxygen, Energy):", 50);
            printSlowly(
                    "Each of fuel, oxygen, or energy values can range between 0 and 100, but their combined value must not exceed 100.",
                    50);
            printSlowly("Enter Fuel (0-100): ", 50);
            int fuel = scanner.nextInt();
            printSlowly("Enter Oxygen (0-100): ", 50);
            int oxygen = scanner.nextInt();
            printSlowly("Enter Energy (0-100): ", 50);
            int energy = scanner.nextInt();

            if (fuel + oxygen + energy > 100) {
                printSlowly("Resource allocation exceeds the limit. Try again.", 50);
                play();
            } else {
                player.updateFuel(-fuel);
                player.updateOxygen(-oxygen);
                player.updateEnergy(-energy);
                updatePlayerScore(10);
                printSlowly("Resources allocated successfully!", 50);

                Random random = new Random();
                int malfunctionChance = random.nextInt(100);

                if (malfunctionChance < 70) {
                    printSlowly("Warning: A random malfunction has occurred!", 50);
                    int malfunctionType = random.nextInt(3);

                    switch (malfunctionType) {
                        case 0:
                            printSlowly("Minor Fuel Leak detected!", 50);
                            printSlowly("1. Fix it immediately (Costs 15 Energy).", 50);
                            printSlowly("2. Ignore it (Lose 10 Fuel).", 50);
                            handleDecision(scanner, -15, -10, "Energy", "Fuel");
                            updatePlayerScore(5);
                            break;

                        case 1:
                            printSlowly("Oxygen System Malfunction!", 50);
                            printSlowly("1. Fix it immediately (Costs 10 Energy).", 50);
                            printSlowly("2. Ignore it (Lose 15 Oxygen).", 50);
                            handleDecision(scanner, -10, -15, "Energy", "Oxygen");
                            updatePlayerScore(5);
                            break;

                        case 2:
                            printSlowly("Navigation System Glitch!", 50);
                            printSlowly("1. Fix it immediately (Costs 20 Energy).", 50);
                            printSlowly("2. Ignore it (Risk navigation issues in the next level).", 50);
                            handleNavigationGlitch(scanner);
                            updatePlayerScore(5);
                            break;
                    }
                } else {
                    printSlowly("No malfunctions detected. Proceeding safely.", 50);
                    updatePlayerScore(10);
                }

                updatePlayerScore(50);

                printSlowly("Level 1 complete! Prepare for Level 2.", 50);
            }
        } catch (Exception e) {
            printSlowly("An error occurred during resource allocation or malfunction handling: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void handleNavigationGlitch(Scanner scanner) {
        try {
            printSlowly("Enter your choice: ", 50);
            int choice = scanner.nextInt();

            if (choice == 1) {
                if (player.getEnergy() >= 20) {
                    player.updateEnergy(-20);
                    printSlowly("The navigation system glitch has been fixed! 20 Energy used.", 50);
                } else {
                    printSlowly("Not enough Energy to fix the issue. Moving forward with risks.", 50);
                }
            } else if (choice == 2) {
                player.setNavigationGlitch(true);
                printSlowly("You ignored the navigation glitch. It may affect you in the next level.", 50);
            } else {
                printSlowly("Invalid choice. Proceeding without action.", 50);
            }
        } catch (Exception e) {
            printSlowly("Invalid input detected. Please enter a valid choice.", 50);
            e.printStackTrace();
        }
    }

    private void handleDecision(Scanner scanner, int fixCost, int ignoreCost, String fixResource,
            String ignoreResource) {
        try {
            printSlowly("Enter your choice: ", 50);
            int choice = scanner.nextInt();

            if (choice == 1) {
                if (fixResource.equals("Energy") && player.getEnergy() >= Math.abs(fixCost)) {
                    player.updateEnergy(fixCost);
                    printSlowly("The issue has been fixed! " + Math.abs(fixCost) + " " + fixResource + " used.", 50);
                } else {
                    printSlowly("Not enough " + fixResource + " to fix the issue. Moving forward with risks.", 50);
                }
            } else if (choice == 2) {
                if (ignoreResource != null) {
                    switch (ignoreResource) {
                        case "Fuel":
                            player.updateFuel(ignoreCost);
                            break;
                        case "Oxygen":
                            player.updateOxygen(ignoreCost);
                            break;
                    }
                    printSlowly("The issue was ignored. " + Math.abs(ignoreCost) + " " + ignoreResource + " lost.", 50);
                } else {
                    printSlowly("You ignored the issue. Future consequences may arise.", 50);
                }
            } else {
                printSlowly("Invalid choice. Proceeding without action.", 50);
            }
        } catch (Exception e) {
            printSlowly("Invalid input detected. Please enter a valid choice.", 50);
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
