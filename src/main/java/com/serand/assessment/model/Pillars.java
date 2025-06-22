package com.serand.assessment.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Pillars {
    private Map<String, Double> pillars = new HashMap<>();
}
