package main;

import java.util.Random;
import java.util.Scanner;

public class LevelFive {
    private Player player;
    private Scanner scanner;
    private Random random;
    private int score;

    public LevelFive(Player player) {
        this.player = player;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.score = 0;
    }

    public void play() {
        try {
            printSlowly("\nLevel 5: Rescue Operation", 50);
            printSlowly("Objective: Defend the stranded crew from alien creatures and escape back to the spaceship.",
                    50);

            boolean missionComplete = false;
            Alien[] aliens = generateAliens();

            while (player.getHealth() > 0 && player.getAmmo() > 0 && player.getEnergy() > 0) {
                displayStatus(aliens);

                printSlowly("\nChoose your action:", 50);
                printSlowly("1. Attack", 50);
                printSlowly("2. Defend", 50);
                printSlowly("Enter your choice: ", 50);
                int actionChoice = scanner.nextInt();

                if (actionChoice == 1) {
                    handleAttack(aliens);
                } else if (actionChoice == 2) {
                    handleDefend(aliens);
                } else {
                    printSlowly("Invalid action! Please choose 1 or 2.", 50);
                    continue;
                }

                processAlienAttacks(aliens);
                missionComplete = checkMissionCompletion(aliens);
                if (missionComplete)
                    break;
            }

            if (missionComplete) {
                printSlowly("\nMission Complete! You successfully defended the crew and escaped the planet!", 50);
                score += 50;
                player.updateScore(score);
            } else {
                printSlowly("\nGame Over! You failed to complete the rescue operation.", 50);
            }
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

            printSlowly("\n=== ALIEN STATS ===", 50);
            for (int i = 0; i < aliens.length; i++) {
                if (aliens[i].getHealth() > 0) {
                    printSlowly("Alien " + (i + 1) + " - Health: " + aliens[i].getHealth()
                            + ", Attack Power: " + aliens[i].getAttackPower(), 50);
                }
            }
        } catch (Exception e) {
            printSlowly("An error occurred while displaying status: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void handleAttack(Alien[] aliens) {
        try {
            if (player.getAmmo() > 0 && player.getEnergy() > 0) {
                printSlowly("\nChoose which alien to attack (1-" + aliens.length + "): ", 50);
                int targetAlienIndex = scanner.nextInt() - 1;

                if (targetAlienIndex >= 0 && targetAlienIndex < aliens.length) {
                    Alien targetAlien = aliens[targetAlienIndex];

                    if (targetAlien.getHealth() > 0) {
                        int damage = random.nextInt(15) + 10;
                        targetAlien.takeDamage(damage);
                        player.updateAmmo(-1);
                        player.updateEnergy(-5);

                        printSlowly("You attacked Alien " + (targetAlienIndex + 1) + " for " + damage + " damage.", 50);
                        score += 10;

                        if (targetAlien.getHealth() <= 0) {
                            printSlowly("Alien " + (targetAlienIndex + 1) + " has been defeated!", 50);
                            score += 20;
                        }
                    } else {
                        printSlowly("Alien " + (targetAlienIndex + 1) + " is already defeated!", 50);
                    }
                } else {
                    printSlowly("Invalid alien choice!", 50);
                }
            } else {
                printSlowly("Not enough ammo or energy to attack!", 50);
            }
        } catch (Exception e) {
            printSlowly("An error occurred while handling the attack: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void handleDefend(Alien[] aliens) {
        try {
            int defense = random.nextInt(10) + 5;
            player.updateEnergy(-3);

            printSlowly("You defended and blocked up to " + defense + " damage.", 50);

            for (Alien alien : aliens) {
                if (alien.getHealth() > 0) {
                    int counterDamage = random.nextInt(5) + 3;
                    alien.takeDamage(counterDamage);
                    printSlowly("You counterattacked Alien for " + counterDamage + " damage!", 50);

                    if (alien.getHealth() <= 0) {
                        printSlowly("Alien has been defeated by your counterattack!", 50);
                        score += 20;
                    }
                }
            }

            score += 10;
        } catch (Exception e) {
            printSlowly("An error occurred while defending: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void processAlienAttacks(Alien[] aliens) {
        try {
            for (Alien alien : aliens) {
                if (alien.getHealth() > 0) {
                    int damageTaken = random.nextInt(alien.getAttackPower());
                    player.updateHealth(-damageTaken);
                    printSlowly("Alien attacked you for " + damageTaken + " damage!", 50);
                }
            }
        } catch (Exception e) {
            printSlowly("An error occurred while processing alien attacks: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private boolean checkMissionCompletion(Alien[] aliens) {
        try {
            for (Alien alien : aliens) {
                if (alien.getHealth() > 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            printSlowly("An error occurred while checking mission completion: " + e.getMessage(), 50);
            e.printStackTrace();
            return false;
        }
    }

    private Alien[] generateAliens() {
        try {
            int numberOfAliens = 1 + random.nextInt(3);
            Alien[] aliens = new Alien[numberOfAliens];

            for (int i = 0; i < numberOfAliens; i++) {
                int health = 5 + random.nextInt(20);
                int attackPower = 2 + random.nextInt(5);
                aliens[i] = new Alien(health, attackPower);
            }

            return aliens;
        } catch (Exception e) {
            printSlowly("An error occurred while generating aliens: " + e.getMessage(), 50);
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
}
