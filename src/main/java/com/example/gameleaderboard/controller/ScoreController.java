package com.example.gameleaderboard.controller;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import com.example.gameleaderboard.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ScoreController {
    @Autowired
    ScoreRepository scoreRepository;

    //@Autowired
    //CacheService cacheService;

    @GetMapping("/")
    public List<Score> getAllScores(){
        return scoreRepository.findAll();
    }

    @PostMapping("/save")
    public Score saveScore(@RequestBody Score score) {
        //cacheService.addOrUpdateInCache(score);
        Optional<Score> oldScore = scoreRepository.findById(score.getId());
        if(oldScore.isPresent()){
            if(oldScore.get().getScore() > score.getScore())
                return oldScore.get();
        }
        return scoreRepository.save(score);
    }

    @GetMapping("/top5")
    public List<Score> getTopFiveScores(){
        return scoreRepository.findTop5Scores();
        //return cacheService.getTopFive();
    }
/*
    @GetMapping("/cache")
    public List<Score> getTopFiveScoresCache(){
        return cacheService.getTopFive();
    }

 */
}
