package com.serand.assessment.service;

import com.serand.assessment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CentralScoringEngineImpl implements CentralScoringEngine {

    private final GeminiService geminiService;

    @Autowired
    public CentralScoringEngineImpl(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @Override
    public double calculateQuestionScore(SurveyResponseAnswer answer, Question question, Company company, String surveyName) {
        if (answer == null || question == null) return 0.0;

        double currentQuestionScore = 0.0;

        if ("multipleChoice".equals(answer.getType()) || "coding".equals(answer.getType())) {
            if (isTypeOneQuestion(question.getCorrectAnswers())) {
                currentQuestionScore = getFixedMcqQuestionScore(answer.getArrayAnswer(), question.getCorrectAnswers());
            } else {
                currentQuestionScore = getVariableMcqQuestionScore(answer.getArrayAnswer(), question.getCorrectAnswers(), question.getReference(), company);
            }
        } else { // Text-based answers
            String scoreExplanationAndScore = geminiService.getSurveyAnswerRelevanceScore(answer.getStringAnswer(), question.getQuestion(), surveyName);
            currentQuestionScore = Double.parseDouble(scoreExplanationAndScore.split("-")[0]);
            currentQuestionScore *= 100; // Convert 0-1 to 0-100
        }
        return currentQuestionScore;
    }

    @Override
    public Map<String, Double> calculateAllQuestionScores(SurveyResponse response, Survey survey, Company company) {
        if (response == null || survey == null || company == null) {
            return Map.of();
        }

        Map<String, Double> map = new HashMap<>();

        response.getAnswerMap().forEach(
            (questionReference, answer) -> {
                List<Question> question = survey.getQuestions();
                if (question != null) {
                    question.stream()
                        .filter(q -> q.getReference().equals(questionReference))
                        .findFirst()
                        .ifPresent(q -> {
                            double score = calculateQuestionScore(answer, q, company, survey.getName());
                            map.put(questionReference, score);
                        });
                }
            }
        );
        return map;
    }

    private boolean isTypeOneQuestion(String[] correctAnswers) {
        return Arrays.stream(correctAnswers).anyMatch(ans -> "always".equalsIgnoreCase(ans) || "often".equalsIgnoreCase(ans));
    }

    private double getFixedMcqQuestionScore(String[] candidateAnswer, String[] correctAnswers) {
        double scorePoints = 0;
        for (String ans : candidateAnswer) {
            if ("always".equalsIgnoreCase(ans)) scorePoints += 1.0;
            if ("often".equalsIgnoreCase(ans)) scorePoints += 0.75;
            if ("sometimes".equalsIgnoreCase(ans)) scorePoints += 0.5;
        }
        return (scorePoints / correctAnswers.length) * 100;
    }

    private double getVariableMcqQuestionScore(String[] candidateAnswer, String[] correctAnswers, String questionReference, Company company) {
        long matches = Arrays.stream(candidateAnswer)
                .filter(ca -> Arrays.stream(correctAnswers).anyMatch(cca -> ca.equalsIgnoreCase(cca)))
                .count();
        return correctAnswers.length == 0 ? 0 : (double) matches / correctAnswers.length * 100;
    }
}