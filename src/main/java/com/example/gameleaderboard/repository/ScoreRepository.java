package com.example.gameleaderboard.repository;

import com.example.gameleaderboard.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAll();

    Optional<Score> findById(Long id);

    @Query(value = "SELECT * FROM Score s ORDER BY s.score DESC LIMIT 5", nativeQuery = true)
    List<Score> findTop5Scores();
}
