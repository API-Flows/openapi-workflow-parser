package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Criterion {

    private String condition;
    private String context;
    private String type;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

