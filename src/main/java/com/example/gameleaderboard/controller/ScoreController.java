package com.example.gameleaderboard.controller;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.service.CacheService;
import com.example.gameleaderboard.service.ScoreFileReaderService;
import com.example.gameleaderboard.service.ScoreService;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ScoreController {

    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);

    private final ScoreService scoreService;

    private final CacheService cacheService;

    public ScoreController(@Autowired CacheService cacheService, @Autowired ScoreService scoreService) {
        this.cacheService = cacheService;
        this.scoreService = scoreService;
    }

    @GetMapping("/get_top_scores")
    public List<Score> getTop5Scores() {
        try {
            return cacheService.getTopFive();
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            return scoreService.findTop5Scores();
        }
    }

    @PostMapping("/save_player_score")
    public void saveHighestScoreOfPlayer(@RequestBody Score score) {
        try {
            cacheService.addOrUpdateInCache(score);
            Optional<Score> oldScore = scoreService.findScoreByPlayerId(score.getPlayerId());
            if (oldScore.isPresent()) {
                if (oldScore.get().getScore() > score.getScore())
                    return;
            }
            scoreService.saveScore(score);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    @Deprecated
    @GetMapping("/all")
    public List<Score> getAllScores() {
        return scoreService.fetchAllScores();
    }

    @GetMapping("/filereader")
    public void consumeFromFile() {
        try {
            List<Score> scoreList = ScoreFileReaderService.readFromFile();
            for (Score score : scoreList) {
                saveHighestScoreOfPlayer(score);
            }
        } catch (CsvValidationException csvValidationException) {
            logger.error("Invalid CSV");
        } catch (IOException ioException) {
            logger.error("An I/O exception found");
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }


}
