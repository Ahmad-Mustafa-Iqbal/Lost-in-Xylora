package main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player {
    private String name;
    private int health;
    private int fuel;
    private int oxygen;
    private int energy;
    private int ammo;
    private int score;
    private boolean navigationGlitch = false;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.fuel = 100;
        this.oxygen = 100;
        this.energy = 100;
        this.ammo = 50;
        this.score = 0;
    }

    public boolean hasNavigationGlitch() {
        return navigationGlitch;
    }

    public void setNavigationGlitch(boolean navigationGlitch) {
        this.navigationGlitch = navigationGlitch;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, 100));
    }

    public int getFuel() {
        return this.fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = Math.max(0, Math.min(fuel, 100));
    }

    public int getOxygen() {
        return this.oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = Math.max(0, Math.min(oxygen, 100));
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, 100));
    }

    public int getAmmo() {
        return this.ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = Math.max(0, ammo);
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = Math.max(0, score);
    }

    public void updateScore(int points) {
        this.score += points;
    }

    public void updateHealth(int amount) {
        this.setHealth(this.health + amount);
    }

    public void updateFuel(int amount) {
        this.setFuel(this.fuel + amount);
    }

    public void updateOxygen(int amount) {
        this.setOxygen(this.oxygen + amount);
    }

    public void updateEnergy(int amount) {
        this.setEnergy(this.energy + amount);
    }

    public void updateAmmo(int amount) {
        this.setAmmo(this.ammo + amount);
    }

    public void saveLeaderboard() {
        List<ScoreEntry> scores = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        String playerName = parts[0];
                        int playerScore = Integer.parseInt(parts[1]);
                        String date = parts.length >= 3 ? parts[2] : "Unknown Date";
                        scores.add(new ScoreEntry(playerName, playerScore, date));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
            br.close();
            scores.add(new ScoreEntry(this.name, this.score,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

            scores.sort((a, b) -> b.getScore() - a.getScore());

            if (scores.size() > 10) {
                scores = scores.subList(0, 10);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("leaderboard.txt"));
            for (ScoreEntry entry : scores) {
                bw.write(entry.getName() + "," + entry.getScore() + "," + entry.getDate() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving to the leaderboard.");
            e.printStackTrace();
        }
    }

    public static void displayHighScores() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
            String line;
            List<ScoreEntry> scores = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        String playerName = parts[0];
                        int playerScore = Integer.parseInt(parts[1]);
                        String date = parts.length >= 3 ? parts[2] : "Unknown Date";
                        scores.add(new ScoreEntry(playerName, playerScore, date));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
            br.close();

            scores.sort((a, b) -> b.getScore() - a.getScore());

            System.out.println("\n--- High Scores ---");
            for (int i = 0; i < Math.min(10, scores.size()); i++) {
                ScoreEntry entry = scores.get(i);
                System.out.println((i + 1) + ". " + entry.getName() + " - Score: " + entry.getScore() + " - Date: "
                        + entry.getDate());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the leaderboard.");
            e.printStackTrace();
        }
    }

    private static class ScoreEntry {
        private final String name;
        private final int score;
        private final String date;

        public ScoreEntry(String name, int score, String date) {
            this.name = name;
            this.score = score;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public String getDate() {
            return date;
        }
    }
}
