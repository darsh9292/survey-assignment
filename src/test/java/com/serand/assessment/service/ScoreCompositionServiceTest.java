package com.serand.assessment.service;

import com.serand.assessment.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCompositionServiceTest {

    private final ScoreCompositionService service = new ScoreCompositionServiceImpl();

    @Test
    void testAggregatePillarScores_Standard() {
        Map<String, Map<String, SurveyResponseAnswer>> answers = new HashMap<>();
        Map<String, SurveyResponseAnswer> pillarAnswers = new HashMap<>();
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setReference("values");
        answer.setQuestionScore(80.0);
        pillarAnswers.put("q1", answer);
        answers.put("pillar1", pillarAnswers);

        Company company = new Company();
        company.setWeightings(new Weightings());

        Scores scores = service.aggregatePillarScores(answers, company, null);
        assertNotNull(scores);
        assertTrue(scores.getValues() > 0);
    }

    @Test
    void testAggregatePillarScores_Dynamic() {
        Map<String, Map<String, SurveyResponseAnswer>> answers = new HashMap<>();
        Map<String, SurveyResponseAnswer> pillarAnswers = new HashMap<>();
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setReference("customPillar");
        answer.setQuestionScore(70.0);
        pillarAnswers.put("q1", answer);
        answers.put("pillar1", pillarAnswers);

        Company company = new Company();
        company.setWeightings(new Weightings());

        Pillars pillars = new Pillars();
        Map<String, Double> pillarMap = new HashMap<>();
        pillarMap.put("customPillar", 1d);
        pillars.setPillars(pillarMap);

        Scores scores = service.aggregatePillarScores(answers, company, pillars);
        assertNotNull(scores);
        assertTrue(scores.getCustomPillarScores().containsKey("customPillar"));
    }

    @Test
    void testCalculateOverallScore() {
        Scores scores = new Scores();
        double result = service.calculateOverallScore(scores, 80.0);
        assertEquals(70.0, result);
    }

    @Test
    void testUpdateApplicationScores() {
        Application app = new Application();
        Scores scores = new Scores();
        double cvScore = 70.0;

        Application updated = service.updateApplicationScores(app, scores, cvScore);
        assertNotNull(updated.getScores());
        assertEquals(60.0, updated.getApplicationOverallScore());
    }
}