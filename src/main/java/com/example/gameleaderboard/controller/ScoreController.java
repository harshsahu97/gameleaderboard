package com.example.gameleaderboard.controller;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import com.example.gameleaderboard.service.CacheService;
import com.example.gameleaderboard.service.ScoreFileReaderService;
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

    private final ScoreRepository scoreRepository;

    private final CacheService cacheService;

    public ScoreController(@Autowired CacheService cacheService, @Autowired ScoreRepository scoreRepository) {
        this.cacheService = cacheService;
        this.scoreRepository = scoreRepository;
    }

    @GetMapping("/")
    public List<Score> getTop5Scores() {
        try {
            return cacheService.getTopFive();
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            return scoreRepository.findTop5Scores();
            //return null;
        }
    }

    @PostMapping("/save")
    public void saveScore(@RequestBody Score score) {
        try {
            cacheService.addOrUpdateInCache(score);
            Optional<Score> oldScore = scoreRepository.findById(score.getId());
            if (oldScore.isPresent()) {
                if (oldScore.get().getScore() > score.getScore())
                    return;
            }
            scoreRepository.save(score);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    @GetMapping("/all")
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    @GetMapping("/filereader")
    public void consumeFromFile() {
        try {
            List<Score> scoreList = ScoreFileReaderService.readFromFile();
            for (Score score : scoreList) {
                saveScore(score);
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
