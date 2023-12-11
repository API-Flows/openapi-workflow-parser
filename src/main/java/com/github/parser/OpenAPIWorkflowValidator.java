package com.github.parser;

import com.github.model.OpenAPIWorkflow;
import com.github.model.SourceDescription;
import com.github.model.Step;
import com.github.model.Workflow;

public class OpenAPIWorkflowValidator {

    public void validate(OpenAPIWorkflow openAPIWorkflow) {
        if (openAPIWorkflow.getWorkflowsSpec() == null || openAPIWorkflow.getWorkflowsSpec().isEmpty()) {
            throw new RuntimeException("'workflowsSpec' is undefined");
        }

        if (openAPIWorkflow.getInfo() == null) {
            throw new RuntimeException("'Info' is undefined");
        }
        if (openAPIWorkflow.getInfo().getTitle() == null || openAPIWorkflow.getInfo().getTitle().isEmpty()) {
            throw new RuntimeException("'Info title' is undefined");
        }
        if (openAPIWorkflow.getInfo().getVersion() == null || openAPIWorkflow.getInfo().getVersion().isEmpty()) {
            throw new RuntimeException("'Info version' is undefined");
        }

        if (openAPIWorkflow.getSourceDescriptions() == null || openAPIWorkflow.getSourceDescriptions().isEmpty()) {
            throw new RuntimeException("'SourceDescriptions' is undefined or empty");
        }

        int i = 0;
        for (SourceDescription sourceDescription : openAPIWorkflow.getSourceDescriptions()) {
            if (sourceDescription.getName() == null || sourceDescription.getName().isEmpty()) {
                throw new RuntimeException("'SourceDescription[" + i + "] name' is undefined");
            }
            if (sourceDescription.getUrl() == null || sourceDescription.getUrl().isEmpty()) {
                throw new RuntimeException("'SourceDescription[" + i + "] url' is undefined");
            }
            i++;
        }

        if (openAPIWorkflow.getWorkflows() == null || openAPIWorkflow.getWorkflows().isEmpty()) {
            throw new RuntimeException("'Workflows' is undefined or empty");
        }

        i = 0;
        for(Workflow workflow : openAPIWorkflow.getWorkflows()) {
            if (workflow.getWorkflowId() == null || workflow.getWorkflowId().isEmpty()) {
                throw new RuntimeException("'Workflow[" + i + "] workflowId' is undefined");
            }
            if (workflow.getSteps() == null) {
                throw new RuntimeException("'Workflow Steps' is undefined");
            }
            for(Step step : workflow.getSteps()) {
                if(step.getStepId() == null || step.getStepId().isEmpty()) {
                    throw new RuntimeException("'Workflow[" + workflow.getWorkflowId() + "] stepId' is undefined");
                }
                if(step.getOperationId() == null && step.getWorkflowId() == null && step.getOperationRef() == null) {
                    throw new RuntimeException("'Workflow[" + workflow.getWorkflowId() + "]' should provide at least one of the following: [operationId, operationRef, workflowId]");
                }

                int numAssignedValues = (step.getOperationId() != null ? 1 : 0) +
                        (step.getWorkflowId() != null ? 1 : 0) +
                        (step.getOperationRef() != null ? 1 : 0);

                if (numAssignedValues != 1) {
                    throw new RuntimeException("'Workflow[" + workflow.getWorkflowId() + "]' should provide only one of the following: [operationId, operationRef, workflowId]");
                }
            }
            i++;
        }

    }
}
