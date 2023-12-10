package com.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Inputs {

    private Map<String, Object> properties;

    @JsonProperty("properties")
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
