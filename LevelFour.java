package main;

import java.util.Random;
import java.util.Scanner;

public class LevelFour extends Level {
    private int score;
    private Scanner scanner;
    private Random random;
    private String[][] map;
    private String[][] hazards;

    public LevelFour(Player player) {
        super(player);
        this.score = 0;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    @Override
    public void play() {
        try {
            printSlowly("\nLevel 4: Planet Exploration", 50);
            printSlowly("Objective: Land on Xylora and locate the stranded crew.", 50);

            map = generateMap();
            hazards = generateHazards(map.length, map[0].length);
            System.out.println("The Hazards Grid will disappear soon memorize it.");
            displayHazards(hazards);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            clearHazards();

            boolean crewFound = false;

            while (!crewFound && player.getHealth() > 0 && player.getOxygen() > 0) {
                displayMap(map);

                printSlowly("\nCurrent Status:", 50);
                printSlowly("Health: " + player.getHealth() + "% | Oxygen: " + player.getOxygen() + "%", 50);
                printSlowly("Score: " + score, 50);
                printSlowly("Enter your move (W/A/S/D): ", 50);

                String move = scanner.next().toUpperCase();
                int[] position = findPlayerPosition();
                int x = position[0];
                int y = position[1];

                switch (move) {
                    case "W":
                        if (x > 0)
                            x--;
                        else
                            printSlowly("Invalid move!", 50);
                        break;
                    case "S":
                        if (x < map.length - 1)
                            x++;
                        else
                            printSlowly("Invalid move!", 50);
                        break;
                    case "A":
                        if (y > 0)
                            y--;
                        else
                            printSlowly("Invalid move!", 50);
                        break;
                    case "D":
                        if (y < map[0].length - 1)
                            y++;
                        else
                            printSlowly("Invalid move!", 50);
                        break;
                    default:
                        printSlowly("Invalid input! Use W/A/S/D.", 50);
                        continue;
                }

                handleEncounter(x, y);

                if (map[x][y].equals("C")) {
                    crewFound = true;
                    score += 50;
                    printSlowly("You found the stranded crew!", 50);
                    player.updateScore(score);
                    break;
                }

                updatePlayerPosition(position, x, y);

                if (player.getHealth() <= 0) {
                    printSlowly("You have died! Game Over.", 50);
                    break;
                }
                if (player.getOxygen() <= 0) {
                    printSlowly("You ran out of oxygen! Game Over.", 50);
                    break;
                }

                score += 1;
            }

            if (crewFound) {
                printSlowly("Level 4 Complete!", 50);
            } else {
                printSlowly("Mission failed.", 50);
            }

            if (player.getHealth() <= 20) {
                score -= 10;
                printSlowly("Warning! Health is critically low. Score penalized.", 50);
            }
            if (player.getOxygen() <= 20) {
                score -= 10;
                printSlowly("Warning! Oxygen is critically low. Score penalized.", 50);
            }
        } catch (Exception e) {
            printSlowly("An error occurred during Level 4: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void clearConsole() {
        try {

            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {

                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            printSlowly("Unable to clear console screen.", 50);
        }
    }

    private void clearHazards() {
        try {
            clearConsole();
            printSlowly("Hazards grid has been removed from the console.", 50);
        } catch (Exception e) {
            printSlowly("An error occurred while clearing hazards: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void handleEncounter(int x, int y) {
        try {
            if (hazards[x][y].equals("H")) {
                int encounter = random.nextInt(3);
                switch (encounter) {
                    case 0:
                        player.updateHealth(-10);
                        score -= 5;
                        printSlowly("You encountered a hazard! Health reduced by 10.", 50);
                        break;
                    case 1:
                        player.updateOxygen(-10);
                        score -= 5;
                        printSlowly("You entered a toxic zone! Oxygen reduced by 10.", 50);
                        break;
                    case 2:
                        printSlowly("You encountered an energy barrier! Solve the puzzle to proceed.", 50);
                        if (!solvePuzzle()) {
                            player.updateHealth(-20);
                            score -= 10;
                            printSlowly("Puzzle failed! Health reduced by 20.", 50);
                        } else {
                            score += 10;
                            printSlowly("Puzzle solved! Score increased.", 50);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            printSlowly("An error occurred during the encounter: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void updatePlayerPosition(int[] currentPosition, int newX, int newY) {
        try {
            map[currentPosition[0]][currentPosition[1]] = ".";
            map[newX][newY] = "^";
        } catch (Exception e) {
            printSlowly("An error occurred while updating player position: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private boolean solvePuzzle() {
        try {
            int num1 = random.nextInt(10) + 1;
            int num2 = random.nextInt(10) + 1;
            String[] operations = { "+", "-", "*" };
            String operation = operations[random.nextInt(operations.length)];
            int correctAnswer = switch (operation) {
                case "+" -> num1 + num2;
                case "-" -> num1 - num2;
                case "*" -> num1 * num2;
                default -> 0;
            };

            printSlowly("Solve: " + num1 + " " + operation + " " + num2, 50);
            int answer = scanner.nextInt();
            return answer == correctAnswer;
        } catch (Exception e) {
            printSlowly("An error occurred while solving the puzzle: " + e.getMessage(), 50);
            e.printStackTrace();
            return false;
        }
    }

    private String[][] generateMap() {
        try {
            String[][] map = new String[7][7];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = ".";
                }
            }

            int crewX = random.nextInt(map.length);
            int crewY = random.nextInt(map[0].length);
            map[crewX][crewY] = "C";

            int shipX, shipY;
            do {
                shipX = random.nextInt(map.length);
                shipY = random.nextInt(map[0].length);
            } while (shipX == crewX && shipY == crewY);
            map[shipX][shipY] = "^";

            return map;
        } catch (Exception e) {
            printSlowly("An error occurred while generating the map: " + e.getMessage(), 50);
            e.printStackTrace();
            return new String[7][7];
        }
    }

    private String[][] generateHazards(int rows, int cols) {
        try {
            String[][] hazards = new String[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    hazards[i][j] = ".";
                }
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (random.nextInt(100) < 20 && !map[i][j].equals("C") && !map[i][j].equals("^")) {
                        hazards[i][j] = "H";
                    }
                }
            }
            return hazards;
        } catch (Exception e) {
            printSlowly("An error occurred while generating hazards: " + e.getMessage(), 50);
            e.printStackTrace();
            return new String[rows][cols];
        }
    }

    private void displayHazards(String[][] hazards) {
        try {
            printSlowly("\nDisplaying hazards:", 50);
            for (int i = 0; i < hazards.length; i++) {
                for (int j = 0; j < hazards[i].length; j++) {
                    System.out.print(hazards[i][j].equals("H") ? "H " : ". ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            printSlowly("An error occurred while displaying hazards: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private int[] findPlayerPosition() {
        try {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j].equals("^")) {
                        return new int[] { i, j };
                    }
                }
            }
            return new int[] { 0, 0 };
        } catch (Exception e) {
            printSlowly("An error occurred while finding player position: " + e.getMessage(), 50);
            e.printStackTrace();
            return new int[] { 0, 0 };
        }
    }

    private void displayMap(String[][] map) {
        try {
            printSlowly("\nMap:", 50);
            for (String[] row : map) {
                for (String cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            printSlowly("An error occurred while displaying the map: " + e.getMessage(), 50);
            e.printStackTrace();
        }
    }

    private void printSlowly(String text, int delay) {
        for (char ch : text.toCharArray()) {
            System.out.print(ch);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    public int getScore() {
        return score;
    }
}
