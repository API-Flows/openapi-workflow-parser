package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.media.Schema;

import java.util.HashMap;
import java.util.Map;

public class Components {

    private Map<String, Schema> inputs = new HashMap<>();
    private Map<String, Parameter> parameters = new HashMap<>();
    private Map<String, SuccessAction> successActions = new HashMap<>();
    private Map<String, FailureAction> failureActions = new HashMap<>();

    // Getters and setters

    @JsonProperty("inputs")
    public Map<String, Schema> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Schema> inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("parameters")
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, Parameter parameter) {
        this.parameters.put(key, parameter);
    }

    public void addInput(String key, Schema input) {
        this.inputs.put(key, input);
    }

    @JsonProperty("successActions")
    public Map<String, SuccessAction> getSuccessActions() {
        return successActions;
    }

    public void setSuccessActions(Map<String, SuccessAction> successActions) {
        this.successActions = successActions;
    }

    @JsonProperty("failureActions")
    public Map<String, FailureAction> getFailureActions() {
        return failureActions;
    }

    public void setFailureActions(Map<String, FailureAction> failureActions) {
        this.failureActions = failureActions;
    }

    public Components parameter(String key, Parameter parameter) {
        this.addParameter(key, parameter);
        return this;
    }
}

