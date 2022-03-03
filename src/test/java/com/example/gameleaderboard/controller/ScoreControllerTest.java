package com.example.gameleaderboard.controller;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import com.example.gameleaderboard.service.CacheServiceImpl;
import com.example.gameleaderboard.service.ScoreFileReaderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ScoreControllerTest {
    @InjectMocks
    ScoreController scoreController;

    @Mock
    ScoreRepository scoreRepository;

    @Mock
    CacheServiceImpl cacheService;

    private Score score;

    @Before
    public void setup(){
        score = new Score();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void comsumeFromFileTest(){
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
        when(scoreRepository.findTop5Scores()).thenReturn(Arrays.asList(score));
        scoreController.getTop5Scores();
        verify(scoreRepository, times(1)).findTop5Scores();
    }

    @Test
    public void getAllTest(){
        scoreController.getAllScores();
        verify(scoreRepository, times(1)).findAll();
    }

}
