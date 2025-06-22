package com.serand.assessment.model;

import lombok.Data;

@Data
public class Question {

    private String id;
    private String reference;
    private String question;
    private String[] availableAnswers;
    private String[] correctAnswers;
    private boolean isGemini;

}
