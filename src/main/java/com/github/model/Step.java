package com.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Step {

    private String stepId;
    private String operationId;
    private String description;
    private List<Parameter> parameters;
    private List<SuccessCriterion> successCriteria;
    private Outputs outputs;

    @JsonProperty("stepId")
    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    @JsonProperty("operationId")
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("parameters")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("successCriteria")
    public List<SuccessCriterion> getSuccessCriteria() {
        return successCriteria;
    }

    public void setSuccessCriteria(List<SuccessCriterion> successCriteria) {
        this.successCriteria = successCriteria;
    }

    @JsonProperty("outputs")
    public Outputs getOutputs() {
        return outputs;
    }

    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }
}
