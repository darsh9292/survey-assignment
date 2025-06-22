package com.serand.assessment.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Scores {
    private double values;
    private double culture;
    private double mindset;
    private double growthMindset;
    private WorkLife workLife;
    private Map<String, Double> customPillarScores = new HashMap<>();
    private Map<String, String[]> customPillarAnswers = new HashMap<>();
    private Weightings weightings;
    private Pillars pillars;

    public double getOverallScore() {
        if (pillars != null && !pillars.getPillars().isEmpty()) {
            // Dynamic pillars calculation
            double totalScore = 0;
            double totalWeight = 0;
            for (Map.Entry<String, Double> pillar : pillars.getPillars().entrySet()) {
                String pillarName = pillar.getKey();
                Double weight = pillar.getValue();
                Double score = customPillarScores.get(pillarName);
                if (score != null) {
                    totalScore += score * weight;
                    totalWeight += weight;
                }
            }
            return totalWeight > 0 ? totalScore / totalWeight : 0;
        } else if (weightings != null) {
            // Standard pillars calculation
            double workLifeScore = workLife != null ? workLife.getWorkLife() : 0;
            return (values * weightings.getValues() +
                    culture * weightings.getCulture() +
                    mindset * weightings.getMindset() +
                    workLifeScore * weightings.getWorkLife()) /
                    (weightings.getValues() + weightings.getCulture() +
                            weightings.getMindset() + weightings.getWorkLife());
        }
        return 0;
    }

    public void recalculateOverallScore() {
        // Triggers getOverallScore() logic
    }

    public void addCustomPillarScore(String pillarName, double score) {
        customPillarScores.put(pillarName, score);
    }

    public void addCustomPillarAnswers(String pillarName, String[] answers) {
        customPillarAnswers.put(pillarName, answers);
    }

}
