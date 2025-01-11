package main;

public abstract class Level {
    protected Player player;

    public Level(Player player) {
        this.player = player;
    }

    public abstract void play();

    protected void updatePlayerScore(int points) {
        player.updateScore(points);
    }
}
