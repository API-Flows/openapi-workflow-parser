package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriterionExpressionType {

    private String type;
    private String version;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public CriterionExpressionType type(String type) {
        this.type = type;
        return this;
    }

    public CriterionExpressionType version(String version) {
        this.version = version;
        return this;
    }
}
