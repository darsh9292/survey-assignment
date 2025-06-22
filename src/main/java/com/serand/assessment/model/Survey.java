package com.serand.assessment.model;

import lombok.Data;

import java.util.List;

@Data
public class Survey {

    private String id;
    private String name;
    private List<Question> questions;
    private Company company;
    private PersonalityProfile personalityProfile;

}
