package main;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        playBackgroundSound();

        boolean isRunning = true;

        while (isRunning) {
            printSlowly("Welcome to Lost in Xylora!", 50);
            printSlowly("1. Start New Game", 50);
            printSlowly("2. View High Scores", 50);
            printSlowly("3. Exit", 50);
            printSlowly("Enter your choice: ", 50);

            int choice = -1;

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                printSlowly("Invalid input! Please enter a number between 1 and 3.", 50);
                scanner.nextLine();
                continue;
            }

            Player player;

            switch (choice) {
                case 1:
                    startNewGame(scanner);
                    break;

                case 2:
                    Player.displayHighScores();
                    printSlowly("Would you like to start a new game? (Y/N): ", 50);
                    String response = scanner.next().toUpperCase();
                    if (response.equals("Y")) {
                        startNewGame(scanner);
                    }
                    break;

                case 3:
                    printSlowly("Exiting the game...", 50);
                    isRunning = false;
                    break;

                default:
                    printSlowly("Invalid choice! Please enter a valid option.", 50);
            }

            if (isRunning) {
                printSlowly("Would you like to return to the main menu? (Y/N): ", 50);
                String mainMenuResponse = scanner.next().toUpperCase();
                if (!mainMenuResponse.equals("Y")) {
                    isRunning = false;
                    printSlowly("Exiting the game...", 50);
                }
            }
        }

        scanner.close();
    }

    private static void startNewGame(Scanner scanner) {
        printSlowly("Enter your name: ", 50);
        String name = scanner.next();
        Player player = new Player(name);

        try {
            LevelOne levelOne = new LevelOne(player);
            levelOne.play();

            LevelTwo levelTwo = new LevelTwo(player);
            levelTwo.play();

            LevelThree levelThree = new LevelThree(player);
            levelThree.play();

            LevelFour levelFour = new LevelFour(player);
            levelFour.play();

            LevelFive levelFive = new LevelFive(player);
            levelFive.play();

            LevelSix levelSix = new LevelSix(player);
            levelSix.play();

            player.saveLeaderboard();

            printSlowly(player.getName() + "'s Final Score: " + player.getScore(), 50);
        } catch (Exception e) {
            printSlowly("An error occurred while playing the game: " + e.getMessage(), 50);
        }
    }

    private static void printSlowly(String text, int delay) {
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

    private static void playBackgroundSound() {
        try {
            File soundFile = new File("src/main/XyloraSound.wav");

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
            printSlowly("Error playing background sound. Continuing without sound.", 50);
        }
    }
}