package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workflow {

    private String workflowId;
    private String summary;
    private String description;
    private Schema inputs;

    private String dependsOn;

    private List<Parameter> parameters = new ArrayList<>();
    private List<SuccessAction> successActions = new ArrayList<>();
    private List<FailureAction> failureActions = new ArrayList<>();

    private List<Step> steps = new ArrayList<>();
    private Map<String, String> outputs = new HashMap<>();

    @JsonProperty("workflowId")
    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("inputs")
    public Schema getInputs() {
        return inputs;
    }

    public void setInputs(Schema inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Workflow addStep(Step step) {
        this.steps.add(step);
        return this;
    }

    @JsonProperty("outputs")
    public Map<String, String> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, String> outputs) {
        this.outputs = outputs;
    }

    public Workflow workflowId(String workflowId) {
        this.workflowId = workflowId;
        return this;
    }

    public Workflow summary(String summary) {
        this.summary = summary;
        return this;
    }
    public Workflow description(String description) {
        this.description = description;
        return this;
    }

    public Workflow inputs(Schema inputs) {
        this.inputs = inputs;
        return this;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<SuccessAction> getSuccessActions() {
        return successActions;
    }

    public void setSuccessActions(List<SuccessAction> successActions) {
        this.successActions = successActions;
    }

    public List<FailureAction> getFailureActions() {
        return failureActions;
    }

    public void setFailureActions(List<FailureAction> failureActions) {
        this.failureActions = failureActions;
    }
}
