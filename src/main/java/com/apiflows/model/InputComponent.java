package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class InputComponent {

    private String type;
    private Map<String, Object> properties;

    // Getters and setters

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("properties")
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

