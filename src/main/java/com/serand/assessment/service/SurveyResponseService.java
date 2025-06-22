package com.serand.assessment.service;

import com.serand.assessment.model.*;

import java.util.Arrays;
import java.util.Map;

/**
 * THIS IS A SIMPLIFIED, INTENTIONALLY "MESSY" VERSION FOR THE REFACTORING ASSIGNMENT.
 * The candidate's task is to refactor this into a CentralScoringEngine and ScoreCompositionService.
 */
public class SurveyResponseService {

    private CentralScoringEngine centralScoringEngine;

    private ScoreCompositionService scoreCompositionService;

    public SurveyResponseService(CentralScoringEngine centralScoringEngine,
                                 ScoreCompositionService scoreCompositionService) {
        this.centralScoringEngine = centralScoringEngine;
        this.scoreCompositionService = scoreCompositionService;
    }

    public void processSurveyResponse(SurveyResponse response, Survey survey, Company company, Pillars pillars, Application application, double cvScore) {
        // Calculate individual question scores
        Map<String, Double> questionScores = centralScoringEngine.calculateAllQuestionScores(response, survey, company);

        // Prepare answers map for aggregation
        Map<String, Map<String, SurveyResponseAnswer>> answers = /* logic to build answers map from response */ null;

        // Aggregate pillar scores
        Scores pillarScores = scoreCompositionService.aggregatePillarScores(answers, company, pillars);

        // Calculate overall score
        double overallScore = scoreCompositionService.calculateOverallScore(pillarScores, cvScore);

        // Update application scores if needed
         Application updatedApp = scoreCompositionService.updateApplicationScores(application, pillarScores, cvScore);

        // Persist response, scores, and trigger further actions as needed
    }

    // --- Private Helper Methods (THESE SHOULD BE MOVED TO A SCORING ENGINE) ---
    private double calculateMatchScore(Mindset candidateMindset, double weight) {
        return 50.0 * weight; // Simplified logic
    }

    private double getVariableMcqQuestionScore(String[] candidateAnswer, String[] correctAnswers , String questionReference, Company company){
        long matches = Arrays.stream(candidateAnswer)
                .filter(ca -> Arrays.stream(correctAnswers).anyMatch(cca -> ca.equalsIgnoreCase(cca)))
                .count();
        return correctAnswers.length == 0 ? 0 : (double)matches / correctAnswers.length * 100;
    }

    private double getFixedMcqQuestionScore(String[] candidateAnswer, String[] correctAnswers){
        double scorePoints = 0;
        for (String ans : candidateAnswer) {
            if ("always".equalsIgnoreCase(ans)) scorePoints += 1.0;
            if ("often".equalsIgnoreCase(ans)) scorePoints += 0.75;
            if ("sometimes".equalsIgnoreCase(ans)) scorePoints += 0.5;
        }
        return (scorePoints / correctAnswers.length) * 100;
    }

    private boolean isTypeOneQuestion(String[] correctAnswers) {
        return Arrays.stream(correctAnswers).anyMatch(ans -> "always".equalsIgnoreCase(ans) || "often".equalsIgnoreCase(ans));
    }
}