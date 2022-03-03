package com.example.gameleaderboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "Score")
public class Score {
    @Id
    @Column(name = "id", unique = true ,nullable = false)
    private Long id;

    @Column(name = "score", nullable = false)
    private Long score;

    @Column(name = "player_name", nullable = false)
    @JsonProperty("player_name")
    private String playerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}



