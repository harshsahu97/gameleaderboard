package com.example.gameleaderboard.service;
import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheServiceImpl implements CacheService{

    Comparator<Score> scoreComparator = new Comparator<Score>() {
        @Override
        public int compare(Score a, Score b) {
            if(b.getScore() > a.getScore())
                return -1;
            else if(b.getScore() < a.getScore())
                return 1;
            return 0;
        }
    };

    PriorityQueue<Score> minHeap = new PriorityQueue<>(scoreComparator);

    @Autowired
    ScoreRepository scoreRepository;

    public CacheServiceImpl() {
        List<Score> list = scoreRepository.findTop5Scores();
        for(Score score : list)
            minHeap.add(score);
    }

    public List<Score> getTopFive(){
        List<Score> list = new ArrayList<>();
        while(!minHeap.isEmpty()){
            list.add(minHeap.poll());
            if(list.size() == 5)
                break;
        }
        for(Score score : list)
            minHeap.add(score);
        return list;
    }


    public void addOrUpdateInCache(Score score){
        if(minHeap.isEmpty() || minHeap.size()<5){
            minHeap.add(score);
            return;
        }
        if(minHeap.peek().getScore() < score.getScore()) {
            minHeap.poll();
            minHeap.add(score);
        }
    }

}
