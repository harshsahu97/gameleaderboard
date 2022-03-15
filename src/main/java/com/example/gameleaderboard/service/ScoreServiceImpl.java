package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService{
    @Autowired
    private ScoreRepository scoreRepository;

    public void saveScore(Score score){
        scoreRepository.save(score);
    }

    public List<Score> findTop5Scores() {
        return scoreRepository.findTop5Scores();
    }

    public Optional<Score> findScoreByPlayerId(Long playerId) {
        return scoreRepository.findById(playerId);
    }

    public List<Score> fetchAllScores() {
        return scoreRepository.findAll();
    }
}
