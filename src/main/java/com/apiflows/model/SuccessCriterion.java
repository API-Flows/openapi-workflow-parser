package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessCriterion {

    private String condition;

    // Getters and setters

    @JsonProperty("condition")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

