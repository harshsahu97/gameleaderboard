package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;
import java.util.List;
import java.util.Optional;


public interface ScoreService {
    void saveScore(Score score);

    List<Score> findTop5Scores();

    Optional<Score> findScoreByPlayerId(Long playerId);

    List<Score> fetchAllScores();
}
