package com.serand.assessment.model;

import lombok.Data;

import java.util.Map;

@Data
public class SurveyResponse {

    private String id;
    private Survey survey;
    private Candidate candidate;
    private Application application;
    private Map<String, SurveyResponseAnswer> answerMap;
}
