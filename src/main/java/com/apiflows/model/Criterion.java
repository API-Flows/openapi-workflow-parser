package com.apiflows.model;

import com.apiflows.parser.util.CriterionTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Criterion {

    private String condition;
    private String context;

    @JsonDeserialize(using = CriterionTypeDeserializer.class)
    private Object type;

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

    /** Returns the simple string type (e.g. "simple", "regex") or null if an expression type object is used. */
    @JsonProperty("type")
    public String getType() {
        return type instanceof String ? (String) type : null;
    }

    public void setType(String type) {
        this.type = type;
    }

    /** Returns the expression type object when type is "jsonpath" or "xpath" with a version, or null otherwise. */
    public CriterionExpressionType getExpressionType() {
        return type instanceof CriterionExpressionType ? (CriterionExpressionType) type : null;
    }

    public void setExpressionType(CriterionExpressionType expressionType) {
        this.type = expressionType;
    }

    public Criterion condition(String condition) {
        this.condition = condition;
        return this;
    }

    public Criterion context(String context) {
        this.context = context;
        return this;
    }

    public Criterion type(String type) {
        this.type = type;
        return this;
    }

    public Criterion expressionType(CriterionExpressionType expressionType) {
        this.type = expressionType;
        return this;
    }
}

