package com.serand.assessment.dto;

import com.serand.assessment.model.Scores;
import lombok.Data;

@Data
public class SurveyProcessingResponse {

    private boolean success;
    private double overallScore;
    private Scores scores;
    private String message;

    private SurveyProcessingResponse(boolean success, double overallScore, Scores scores, String message) {
        this.success = success;
        this.overallScore = overallScore;
        this.scores = scores;
        this.message = message;
    }

    public static SurveyProcessingResponse success(double overallScore, Scores scores, String message) {
        return new SurveyProcessingResponse(true, overallScore, scores, message);
    }

    public static SurveyProcessingResponse error(String message) {
        return new SurveyProcessingResponse(false, 0, null, message);
    }

}
