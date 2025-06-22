package com.serand.assessment.service;

import com.serand.assessment.model.Candidate;
import com.serand.assessment.model.Survey;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    public String getSurveyAnswerRelevanceScore(String answer, String question, String surveyName) {
        // Mock implementation - returns score (0-1) and explanation separated by hyphen
        double score = 0.7 + (Math.random() * 0.3); // Random score 0.7-1.0
        String explanation = "AI evaluated the answer as highly relevant to the question.";
        return String.format("%.2f-%s", score, explanation);
    }

    public void generateCandidateFeedback(Candidate candidate, Survey survey) {
        // Mock implementation
        System.out.println("Generating AI feedback for candidate: " + candidate.getId());
    }

}