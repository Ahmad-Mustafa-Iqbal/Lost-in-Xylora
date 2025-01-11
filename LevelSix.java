package main;

import java.util.Random;
import java.util.Scanner;

public class LevelSix extends Level {
    private final Random random;
    private final Scanner scanner;
    private int score;
    private int stealthSuccessStreak = 0;

    public LevelSix(Player player) {
        super(player);
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.score = 0;
    }

    @Override
    public void play() {
        try {
            printSlowly("\nLevel 6: Return Journey", 50);
            printSlowly("Objective: Escape Xylora and return home safely while avoiding additional hazards.", 50);

            boolean missionComplete = false;

            Alien[] ambushingAliens = generateAmbushingAliens();

            while (player.getHealth() > 0 && player.getAmmo() > 0 && player.getEnergy() > 0) {
                displayStatus(ambushingAliens);

                int debrisDamage = encounterSpaceDebris();
                player.updateHealth(-debrisDamage);

                if (player.getHealth() <= 0)
                    break;

                printSlowly("\nA surprise ambush from hostile aliens occurs!", 50);
                printSlowly("Choose your action: ", 50);
                printSlowly("1. Fight the aliens", 50);
                printSlowly("2. Use stealth to avoid detection", 50);
                printSlowly("Enter your choice: ", 50);
                int actionChoice = scanner.nextInt();

                switch (actionChoice) {
                    case 1 -> handleCombat(ambushingAliens);
                    case 2 -> handleStealth();
                    default -> printSlowly("Invalid action! Please choose 1 or 2.", 50);
                }

                if (player.getHealth() <= 0)
                    break;

                if (areAllAliensDefeated(ambushingAliens)) {
                    missionComplete = true;
                    break;
                }
            }

            endMission(missionComplete);
        } catch (Exception e) {
            printSlowly("An error occurred during the mission: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void displayStatus(Alien[] aliens) {
        try {
            printSlowly("\n=== PLAYER STATUS ===", 50);
            printSlowly("Health: " + player.getHealth() + "%", 50);
            printSlowly("Ammo: " + player.getAmmo() + " rounds", 50);
            printSlowly("Energy: " + player.getEnergy() + "%", 50);

            printSlowly("\n=== AMBUSHING ALIENS ===", 50);
            for (int i = 0; i < aliens.length; i++) {
                if (aliens[i].getHealth() > 0) {
                    printSlowly("Alien " + (i + 1) + " - Health: " + aliens[i].getHealth() +
                            ", Attack Power: " + aliens[i].getAttackPower(), 50);
                }
            }
        } catch (Exception e) {
            printSlowly("An error occurred while displaying status: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private int encounterSpaceDebris() {
        try {
            if (random.nextInt(100) < 50) {
                int damage = random.nextInt(8) + 1;
                printSlowly("You collided with space debris and lost " + damage + " health!", 50);
                return damage;
            } else {
                printSlowly("You successfully navigated through the space debris without taking damage!", 50);
                return 0;
            }
        } catch (Exception e) {
            printSlowly("An error occurred while encountering space debris: " + e.getMessage(), 50);
            e.printStackTrace();
            return 0;
        }
    }

    private void handleCombat(Alien[] aliens) {
        try {
            if (player.getAmmo() >= 2 && player.getEnergy() > 0) {
                printSlowly("\nChoose which alien to attack (1-" + aliens.length + "): ", 50);
                int target = scanner.nextInt() - 1;

                if (target >= 0 && target < aliens.length) {
                    Alien alien = aliens[target];
                    int damage = random.nextInt(20) + 10;
                    alien.takeDamage(damage);
                    printSlowly("You attacked Alien " + (target + 1) + " for " + damage + " damage.", 50);
                    player.updateAmmo(-2);
                    player.updateEnergy(-2);
                    score += 10;
                } else {
                    printSlowly("Invalid choice!", 50);
                }
            } else {
                printSlowly("Not enough ammo or energy to fight!", 50);
            }

            for (Alien alien : aliens) {
                if (alien.getHealth() > 0) {
                    int alienDamage = alien.attack();
                    player.updateHealth(-alienDamage);
                    printSlowly("Alien attacked you for " + alienDamage + " damage!", 50);
                }
            }
        } catch (Exception e) {
            printSlowly("An error occurred during combat: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void handleStealth() {
        try {
            if (player.getEnergy() > 5) {
                if (random.nextInt(100) < 30) {
                    printSlowly("You successfully used stealth and avoided detection!", 50);
                    player.updateEnergy(-10);
                    score += 5;
                    stealthSuccessStreak++;

                    if (stealthSuccessStreak >= 3 && random.nextInt(100) < 10) {
                        printSlowly("Amazing! You avoided detection entirely and safely returned to Earth!", 50);
                        endMission(true);
                        return;
                    }
                } else {
                    printSlowly("Stealth failed! You were detected by the aliens.", 50);
                    int damage = random.nextInt(15) + 10;
                    player.updateHealth(-damage);
                    printSlowly("You took " + damage + " damage!", 50);
                    stealthSuccessStreak = 0;
                }
            } else {
                printSlowly("Not enough energy to use stealth!", 50);
            }
        } catch (Exception e) {
            printSlowly("An error occurred while using stealth: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private boolean areAllAliensDefeated(Alien[] aliens) {
        try {
            for (Alien alien : aliens) {
                if (alien.getHealth() > 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            printSlowly("An error occurred while checking if all aliens are defeated: " + e.getMessage(), 50);
            e.printStackTrace();
            return false;
        }
    }

    private void endMission(boolean missionComplete) {
        try {
            if (missionComplete) {
                printSlowly("Mission Complete! You successfully returned to Earth with the rescued crew!", 50);
                score += 50;
            } else {
                printSlowly("You failed to return to Earth. Game Over.", 50);
            }
            player.updateScore(score);
        } catch (Exception e) {
            printSlowly("An error occurred while ending the mission: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private Alien[] generateAmbushingAliens() {
        try {
            int count = 1 + random.nextInt(3);
            Alien[] aliens = new Alien[count];

            for (int i = 0; i < count; i++) {
                int health = 1 + random.nextInt(30);
                int attackPower = 1 + random.nextInt(10);
                aliens[i] = new Alien(health, attackPower);
            }
            return aliens;
        } catch (Exception e) {
            printSlowly("An error occurred while generating ambushing aliens: " + e.getMessage(), 50);
            e.printStackTrace();
            return new Alien[0];
        }
    }

    private void printSlowly(String text, int delay) {
        try {
            for (char ch : text.toCharArray()) {
                System.out.print(ch);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println();
        } catch (Exception e) {
            printSlowly("An error occurred while printing text: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    public int getScore() {
        return score;
    }
}
