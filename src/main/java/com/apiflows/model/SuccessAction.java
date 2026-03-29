package com.apiflows.model;

public class SuccessAction extends Action {

    public SuccessAction name(String name) {
        setName(name);
        return this;
    }

    public SuccessAction type(String type) {
        setType(type);
        return this;
    }

    public SuccessAction workflowId(String workflowId) {
        setWorkflowId(workflowId);
        return this;
    }

    public SuccessAction stepId(String stepId) {
        setStepId(stepId);
        return this;
    }
}
