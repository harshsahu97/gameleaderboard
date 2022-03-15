package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.example.gameleaderboard.constant.ScoreLeaderBoardConstants.SCORE_COUNT;

@Service
public class CacheServiceImpl implements CacheService {
    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
    private final PriorityQueue<Score> scorePriorityQueue;
    private final HashMap<Long, Score> playerScoreMap;

    Comparator<Score> scoreComparator = (a, b) -> {
        if (b.getScore() > a.getScore())
            return -1;
        else if (b.getScore() < a.getScore())
            return 1;
        return 0;
    };
    private final ScoreRepository scoreRepository;

    public CacheServiceImpl(@Autowired ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
        scorePriorityQueue = new PriorityQueue<>(scoreComparator);
        playerScoreMap = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        List<Score> list = scoreRepository.findTop5Scores();
        for (Score score : list)
            addInCache(score);
    }

    public List<Score> getTopFive() {
        List<Score> list = new ArrayList<>();
        while (!scorePriorityQueue.isEmpty()) {
            list.add(scorePriorityQueue.poll());
            if (list.size() == SCORE_COUNT)
                break;
        }
        scorePriorityQueue.addAll(list);
        Collections.reverse(list);
        return list;
    }


    public void addOrUpdateInCache(Score score) {
        try {
            if (playerScoreMap.containsKey(score.getPlayerId())) {
                refreshHigherScoreInCache(score);
            } else if (shouldAddToCache()) {
                addInCache(score);
            } else if (scorePriorityQueue.peek().getScore() < score.getScore()) {
                playerScoreMap.remove(scorePriorityQueue.poll().getPlayerId());
                addInCache(score);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    private boolean shouldAddToCache() {
        return scorePriorityQueue.isEmpty() || scorePriorityQueue.size() < SCORE_COUNT; //parameter const
    }

    private void refreshHigherScoreInCache(Score score) {
        Score old_score = playerScoreMap.get(score.getPlayerId());
        if (old_score.getScore() < score.getScore()) {
            List<Score> scoreList = new LinkedList<>();
            while (!scorePriorityQueue.isEmpty()) {
                Score heapTop = scorePriorityQueue.poll();
                if (heapTop.getPlayerId().equals(score.getPlayerId()))
                    scoreList.add(score);
                else
                    scoreList.add(heapTop);
            }
            scorePriorityQueue.addAll(scoreList);
            playerScoreMap.replace(score.getPlayerId(), score);
        }
    }

    private void addInCache(Score score) {
        scorePriorityQueue.add(score);
        playerScoreMap.put(score.getPlayerId(), score);
    }

}
