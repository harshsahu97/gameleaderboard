package com.example.gameleaderboard.controller;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import com.example.gameleaderboard.service.CacheServiceImpl;
import com.example.gameleaderboard.service.ScoreServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ScoreControllerTest {
    @InjectMocks
    ScoreController scoreController;

    @Mock
    ScoreRepository scoreRepository;

    @Mock
    CacheServiceImpl cacheService;

    @Mock
    ScoreServiceImpl scoreService;

    private Score score;

    @Before
    public void setup(){
        score = new Score();
        score.setPlayerId(1L);
        score.setPlayerName("drassel");
        score.setScore(123L);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void consumeFromFileTest(){
        when(scoreRepository.findById(anyLong())).thenReturn(Optional.empty());
        scoreController.consumeFromFile();
        verify(cacheService, atLeast(1)).addOrUpdateInCache(any(Score.class));
    }

    @Test
    public void getTop5ScoresTestWhenCacheCall(){
        scoreController.getTop5Scores();
        verify(cacheService, times(1)).getTopFive();
    }

    @Test
    public void getTop5ScoresTestWhenDBCall(){
        when(cacheService.getTopFive()).thenThrow(new IllegalStateException("ISE"));
        when(scoreService.findTop5Scores()).thenReturn(Arrays.asList(score));
        List<Score> scoreList = scoreController.getTop5Scores();
        verify(scoreService, times(1)).findTop5Scores();
        assertEquals(scoreList.size(), 1);
        assertEquals(scoreList.get(0).getPlayerName(), "drassel");
    }
}
