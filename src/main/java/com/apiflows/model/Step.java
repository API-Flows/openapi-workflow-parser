package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step {

    private String stepId;
    private String operationId;
    private String operationRef;
    private Operation operation;
    private String workflowId;
    private Workflow workflow;
    private String description;
    private String dependsOn;
    private List<Parameter> parameters = new ArrayList<>();
    private List<Criterion> successCriteria = new ArrayList<>();
    private Map<String, String> outputs = new HashMap<>();
    private List<SuccessAction> onSuccess = new ArrayList<>();
    private List<FailureAction> onFailure = new ArrayList<>();

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

    public String getOperationRef() {
        return operationRef;
    }

    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    @JsonProperty("parameters")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("successCriteria")
    public List<Criterion> getSuccessCriteria() {
        return successCriteria;
    }

    public void setSuccessCriteria(List<Criterion> successCriteria) {
        this.successCriteria = successCriteria;
    }

    public Map<String, String> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, String> outputs) {
        this.outputs = outputs;
    }

    public List<SuccessAction> getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(List<SuccessAction> onSuccess) {
        this.onSuccess = onSuccess;
    }

    public List<FailureAction> getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(List<FailureAction> onFailure) {
        this.onFailure = onFailure;
    }
}
