package com.serand.assessment.model;

import lombok.Data;

@Data
public class Application {

    private String id;
    private Candidate candidate;
    private Survey survey;
    private Company company;
    private boolean complete;
    private SurveyResponse candidateResponse;
    private Scores scores;
    private double applicationOverallScore;

}
