package com.serand.assessment.model;

import lombok.Data;

@Data
public class SurveyResponseAnswer {

    private String questionId;
    private String reference;
    private String type; // "multipleChoice", "text", "coding"
    private String[] arrayAnswer;
    private String stringAnswer;
    private int intAnswer;
    private double questionScore;
    private String scoreExplanation;

}
