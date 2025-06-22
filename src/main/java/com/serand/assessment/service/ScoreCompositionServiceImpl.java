package com.serand.assessment.service;

import com.serand.assessment.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoreCompositionServiceImpl implements ScoreCompositionService {

    @Override
    public Scores aggregatePillarScores(Map<String, Map<String, SurveyResponseAnswer>> answers,
                                        Company company, Pillars pillars) {
        Scores scores = new Scores();
        scores.setWeightings(company.getWeightings());

        if (pillars == null || pillars.getPillars().isEmpty()) {
            // Standard Pillars Aggregation
            double valuesScore = 0, cultureScore = 0, workLifeScore = 0;
            int valuesCount = 0, cultureCount = 0, workLifeCount = 0;
            for (Map<String, SurveyResponseAnswer> pillarAnswers : answers.values()) {
                for (SurveyResponseAnswer answer : pillarAnswers.values()) {
                    if ("values".equals(answer.getReference())) { valuesScore += answer.getQuestionScore(); valuesCount++; }
                    if ("culture".equals(answer.getReference())) { cultureScore += answer.getQuestionScore(); cultureCount++; }
                    if ("workplace".equals(answer.getReference())) { workLifeScore += answer.getQuestionScore(); workLifeCount++; }
                }
            }
            scores.setValues(valuesCount > 0 ? valuesScore / valuesCount : 0);
            scores.setCulture(cultureCount > 0 ? cultureScore / cultureCount : 0);
            WorkLife workLifeObj = new WorkLife();
            workLifeObj.setWorkLife(workLifeCount > 0 ? workLifeScore / workLifeCount : 0);
            scores.setWorkLife(workLifeObj);
            scores.setMindset(50.0); // Mock value
            // Mindset/growthMindset logic can be added as needed
        } else {
            // Dynamic Pillars Aggregation
            Map<String, Double> customPillarScores = new HashMap<>();
            Map<String, Integer> pillarCounts = new HashMap<>();
            for (String pillarName : pillars.getPillars().keySet()) {
                customPillarScores.put(pillarName, 0.0);
                pillarCounts.put(pillarName, 0);
            }
            for (Map<String, SurveyResponseAnswer> pillarAnswers : answers.values()) {
                for (SurveyResponseAnswer answer : pillarAnswers.values()) {
                    String pillarName = answer.getReference();
                    if (pillars.getPillars().containsKey(pillarName)) {
                        customPillarScores.put(pillarName, customPillarScores.get(pillarName) + answer.getQuestionScore());
                        pillarCounts.put(pillarName, pillarCounts.get(pillarName) + 1);
                    }
                }
            }
            for (String pillarName : pillars.getPillars().keySet()) {
                double score = (pillarCounts.get(pillarName) > 0) ? customPillarScores.get(pillarName) / pillarCounts.get(pillarName) : 0.0;
                scores.addCustomPillarScore(pillarName, score);
            }
        }
        return scores;
    }

    @Override
    public double calculateOverallScore(Scores scores, double cvScore) {
        scores.recalculateOverallScore(); // Assumes Scores DTO has this logic
        double overall = scores.getOverallScore();
        return cvScore > 0 ? (overall + cvScore) / 2 : overall;
    }

    @Override
    public Application updateApplicationScores(Application application, Scores scores, double cvScore) {
        application.setScores(scores);
        double finalScore = calculateOverallScore(scores, cvScore);
        application.setApplicationOverallScore(finalScore);
        return application;
    }
}