package com.example.a4200_project;

public class HighScore {
    private int id;
    private String playerName;
    private int score;

    public HighScore(int id, String playerName, int score) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public String toString() {
        return playerName + " " + score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
