package com.serand.assessment.service;

import com.serand.assessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CentralScoringEngineTest {

    private GeminiService geminiService;
    private CentralScoringEngineImpl scoringEngine;

    @BeforeEach
    void setUp() {
        geminiService = mock(GeminiService.class);
        scoringEngine = new CentralScoringEngineImpl(geminiService);
    }

    @Test
    void testCalculateQuestionScore_McqTypeOne() {
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setType("multipleChoice");
        answer.setArrayAnswer(new String[]{"always", "sometimes"});

        Question question = new Question();
        question.setCorrectAnswers(new String[]{"always", "often"});

        double score = scoringEngine.calculateQuestionScore(answer, question, new Company(), "TestSurvey");
        assertTrue(score > 0);
    }

    @Test
    void testCalculateQuestionScore_McqVariable() {
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setType("multipleChoice");
        answer.setArrayAnswer(new String[]{"A", "B"});

        Question question = new Question();
        question.setCorrectAnswers(new String[]{"A", "C"});
        question.setReference("ref");

        double score = scoringEngine.calculateQuestionScore(answer, question, new Company(), "TestSurvey");
        assertTrue(score > 0);
    }

    @Test
    void testCalculateQuestionScore_Text() {
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setType("text");
        answer.setStringAnswer("Some answer");

        Question question = new Question();
        question.setQuestion("What is your value?");

        when(geminiService.getSurveyAnswerRelevanceScore(anyString(), anyString(), anyString()))
                .thenReturn("0.8-Explanation");

        double score = scoringEngine.calculateQuestionScore(answer, question, new Company(), "TestSurvey");
        assertEquals(80.0, score);
    }

    @Test
    void testCalculateAllQuestionScores() {
        // Prepare answer and question
        SurveyResponseAnswer answer = new SurveyResponseAnswer();
        answer.setType("multipleChoice");
        answer.setArrayAnswer(new String[]{"always"});

        Question question = new Question();
        question.setCorrectAnswers(new String[]{"always"});
        question.setId("q1");

        // Prepare survey and response
        Survey survey = new Survey();
        survey.setQuestions(Collections.singletonList(question));

        Company company = new Company();

        // Call method
        Map<String, Double> scores = scoringEngine.calculateAllQuestionScores(new SurveyResponse(), survey, company);

        // Assert
        assertNotNull(scores);
        assertTrue(scores.containsKey("q1"));
        assertTrue(scores.get("q1") >= 0);
    }
}