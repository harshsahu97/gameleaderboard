package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;

import java.util.List;

public interface CacheService {
    List<Score> getTopFive();

    void addOrUpdateInCache(Score score);
}
