package com.serand.assessment.service;

import com.serand.assessment.model.*;

import java.util.Map;

public interface ScoreCompositionService {

    Scores aggregatePillarScores(Map<String, Map<String, SurveyResponseAnswer>> answers,
                                 Company company, Pillars pillars);
    double calculateOverallScore(Scores scores, double cvScore);
    Application updateApplicationScores(Application application, Scores scores,
                                        double cvScore);
}
