package com.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Components {

    private Map<String, InputComponent> inputs;
    private Map<String, ParameterComponent> parameters;

    // Getters and setters

    @JsonProperty("inputs")
    public Map<String, InputComponent> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, InputComponent> inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("parameters")
    public Map<String, ParameterComponent> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, ParameterComponent> parameters) {
        this.parameters = parameters;
    }
}

