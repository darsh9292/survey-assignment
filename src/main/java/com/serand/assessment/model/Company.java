package com.serand.assessment.model;

import lombok.Data;

import java.util.List;

@Data
public class Company {
    private String id;
    private String name;
    private List<ValuesAnswer> valuesAnswers;
    private String[] culture;
    private WorkLifeBenefitsImpactDTO workLifeBenefitsImpact;
    private Weightings weightings;
    private Pillars pillars;

}
