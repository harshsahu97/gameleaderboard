package com.example.gameleaderboard.service;

import com.example.gameleaderboard.model.Score;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ScoreFileReaderService {

    private static final String FILE_PATH = "C:\\Users\\harsh\\Desktop\\scores.csv";

    private static final Logger logger = LoggerFactory.getLogger(ScoreFileReaderService.class);

    public static List<Score> readFromFile() throws IOException, CsvValidationException {
        List<Score> scoreList = new LinkedList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(FILE_PATH));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    scoreList.add(convertToScore(nextLine));
                } catch (NumberFormatException e) {
                    logger.error("Couldnt parse a line in CSV");
                    continue;
                }
            }
        } catch (Exception e) {
            logger.error("Couldn't read the CSV file");
            e.printStackTrace();
        }
        return scoreList;
    }

    private static Score convertToScore(String[] line) throws NumberFormatException {
        Score score = new Score();
        score.setId(Long.parseLong(line[0]));
        score.setPlayerName(line[1]);
        score.setScore(Long.parseLong(line[2]));
        return score;
    }
}
