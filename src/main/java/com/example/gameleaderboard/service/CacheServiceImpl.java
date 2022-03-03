package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;
import com.example.gameleaderboard.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheServiceImpl implements CacheService {

    private final ScoreRepository scoreRepository;
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
    private PriorityQueue<Score> minHeap = new PriorityQueue<>(scoreComparator);
    private HashMap<Long, Score> hashMap = new HashMap<>();

    public CacheServiceImpl(@Autowired ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
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
        for (Score score : list)
            minHeap.add(score);
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
                    if(heapTop.getId() == score.getId()) {
                        scoreList.add(score);
                        continue;
                    }
                    scoreList.add(heapTop);
                }
                for(Score scoreItr : scoreList)
                    minHeap.add(scoreItr);
                hashMap.replace(score.getId(), score);
            }
        }
        if (minHeap.isEmpty() || minHeap.size() < 5) {
            addInCache(score);
            return;
        }
        if (minHeap.peek().getScore() < score.getScore()) {
            hashMap.remove(minHeap.poll().getId());
            addInCache(score);
        }
    }

    private void addInCache(Score score){
        minHeap.add(score);
        hashMap.put(score.getId(), score);
    }

}
