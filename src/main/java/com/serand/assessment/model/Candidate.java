package com.serand.assessment.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Candidate {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Map<String, Double> cvScoreMap = new HashMap<>();
    private Map<String, Double> surveyScore = new HashMap<>();
    private Map<String, Double> overallScoreMap = new HashMap<>();
    private double overallScore;

}
