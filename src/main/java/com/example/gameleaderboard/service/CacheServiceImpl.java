package com.example.gameleaderboard.service;

import com.example.gameleaderboard.controller.ScoreController;
import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheServiceImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
    Comparator<Score> scoreComparator = new Comparator<Score>() {
        @Override
        public int compare(Score a, Score b) {
            if (b.getScore() > a.getScore())
                return -1;
            else if (b.getScore() < a.getScore())
                return 1;
            return 0;
        }
    };
    private final PriorityQueue<Score> minHeap = new PriorityQueue<>(scoreComparator);
    private final HashMap<Long, Score> hashMap = new HashMap<>();

    public CacheServiceImpl(@Autowired ScoreRepository scoreRepository) {
        List<Score> list = scoreRepository.findTop5Scores();
        for (Score score : list)
            addInCache(score);
    }

    public List<Score> getTopFive() {
        List<Score> list = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            list.add(minHeap.poll());
            if (list.size() == 5)
                break;
        }
        minHeap.addAll(list);
        Collections.reverse(list);
        return list;
    }


    public void addOrUpdateInCache(Score score) {
        if(hashMap.containsKey(score.getId())){
            Score old_score = hashMap.get(score.getId());
            if(old_score.getScore() < score.getScore()){
                List<Score> scoreList = new LinkedList<>();
                while(!minHeap.isEmpty()){
                    Score heapTop = minHeap.poll();
                    if(heapTop.getId().equals(score.getId()))
                        scoreList.add(score);
                    else
                        scoreList.add(heapTop);
                }
                minHeap.addAll(scoreList);
                hashMap.replace(score.getId(), score);
            }
        }
        else if (minHeap.isEmpty() || minHeap.size() < 5) {
            addInCache(score);
        }
        else if (minHeap.peek().getScore() < score.getScore()) {
            hashMap.remove(minHeap.poll().getId());
            addInCache(score);
        }
    }

    private void addInCache(Score score){
        minHeap.add(score);
        hashMap.put(score.getId(), score);
    }

}
