package com.serand.assessment.service;

import com.serand.assessment.model.*;

import java.util.Map;

public interface CentralScoringEngine {
    double calculateQuestionScore(SurveyResponseAnswer answer, Question question,
                                  Company company, String surveyName);

    Map<String, Double> calculateAllQuestionScores(SurveyResponse response,
                                                   Survey survey, Company company);
}
