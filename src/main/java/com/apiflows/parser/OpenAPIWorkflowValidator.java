package com.apiflows.parser;

import com.apiflows.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class OpenAPIWorkflowValidator {

    public OpenAPIWorkflowValidatorResult validate(OpenAPIWorkflow openAPIWorkflow) {
        OpenAPIWorkflowValidatorResult result = new OpenAPIWorkflowValidatorResult();
        
        if (openAPIWorkflow.getWorkflowsSpec() == null || openAPIWorkflow.getWorkflowsSpec().isEmpty()) {
            result.addError("'workflowsSpec' is undefined");
        }

        if (openAPIWorkflow.getInfo() == null) {
            result.addError("'Info' is undefined");
        }
        if (openAPIWorkflow.getInfo() != null && (openAPIWorkflow.getInfo().getTitle() == null || openAPIWorkflow.getInfo().getTitle().isEmpty())) {
            result.addError("'Info title' is undefined");
        }
        if (openAPIWorkflow.getInfo() != null && (openAPIWorkflow.getInfo().getVersion() == null || openAPIWorkflow.getInfo().getVersion().isEmpty())) {
            result.addError("'Info version' is undefined");
        }

        if (openAPIWorkflow.getSourceDescriptions() == null || openAPIWorkflow.getSourceDescriptions().isEmpty()) {
            result.addError("'SourceDescriptions' is undefined");
        }

        if (openAPIWorkflow.getSourceDescriptions() != null) {
            int i = 0;
            for (SourceDescription sourceDescription : openAPIWorkflow.getSourceDescriptions()) {
                if (sourceDescription.getName() == null || sourceDescription.getName().isEmpty()) {
                    result.addError("'SourceDescription[" + i + "] name' is undefined");
                }
                if (sourceDescription.getUrl() == null || sourceDescription.getUrl().isEmpty()) {
                    result.addError("'SourceDescription[" + i + "] url' is undefined");
                }
                if (sourceDescription.getType() != null) {
                    List<String> supportedValues = Arrays.asList("openapi", "workflowsSpec");
                    if(!supportedValues.contains(sourceDescription.getType())) {
                        result.addError("'SourceDescription[" + i + "] type' is invalid");
                    }
                }
                i++;
            }

            if(i == 0) {
                result.addError("'SourceDescriptions' is empty");
            }
        }

        if (openAPIWorkflow.getWorkflows() == null || openAPIWorkflow.getWorkflows().isEmpty()) {
            result.addError("'Workflows' is undefined");
        }

        if (openAPIWorkflow.getWorkflows() != null) {
            for (Workflow workflow : openAPIWorkflow.getWorkflows()) {
                int i = 0;

                if (workflow.getWorkflowId() == null || workflow.getWorkflowId().isEmpty()) {
                    result.addError("'Workflow[" + i + "] workflowId' is undefined");
                }
                if (workflow.getSteps() == null) {
                    result.addError("'Workflow " + workflow.getWorkflowId() + "' no Steps are undefined");
                }

                int j = 0;
                HashSet<String> stepIds = new HashSet<>();
                for (Step step : workflow.getSteps()) {
                    if (step.getStepId() == null || step.getStepId().isEmpty()) {
                        result.addError("'Workflow[" + workflow.getWorkflowId() + "] stepId' is undefined");
                    } else {
                        if(stepIds.contains(step.getStepId())) {
                            result.addError("'Workflow[" + workflow.getWorkflowId() + "] stepId' " + step.getStepId() + " is not unique");
                        } else {
                            stepIds.add(step.getStepId());
                        }
                    }
                    if (!isValidStepId(step.getStepId())) {
                        result.addError("'Workflow[" + workflow.getWorkflowId() + "] stepId' is invalid (should match regex " + getStepIdRegularExpression() + ")");
                    }
                    if (step.getOperationId() == null && step.getWorkflowId() == null && step.getOperationRef() == null) {
                        result.addError("'Workflow[" + workflow.getWorkflowId() + "]' should provide at least one of the following: [operationId, operationRef, workflowId]");
                    }

                    int numAssignedValues = (step.getOperationId() != null ? 1 : 0) +
                            (step.getWorkflowId() != null ? 1 : 0) +
                            (step.getOperationRef() != null ? 1 : 0);

                    if (numAssignedValues != 1) {
                        result.addError("'Workflow[" + workflow.getWorkflowId() + "]' should provide only one of the following: [operationId, operationRef, workflowId]");
                    }

                    if(step.getParameters() != null) {
                        for(Parameter parameter : step.getParameters()) {
                            if(parameter.get$ref() != null) {
                                // Reference object
                                // check is URI
                            } else {
                                // Parameter object
                                if(parameter.getName() == null) {
                                    result.addError("'Workflow[" + workflow.getWorkflowId() + "]' parameter has no name");
                                }
                                if(parameter.getIn() == null) {
                                    result.addError("'Workflow[" + workflow.getWorkflowId() + "]' parameter has no type");
                                }
                                if(parameter.getIn() != null) {
                                    List<String> supportedValues = Arrays.asList("path", "query", "header", "cookie", "body", "workflow");
                                    if(!supportedValues.contains(parameter.getIn())) {
                                        result.addError("'Workflow[" + workflow.getWorkflowId() + "]' parameter type (" + parameter.getIn() + ") is invalid");
                                    }
                                }
                                if(parameter.getValue() == null) {
                                    result.addError("'Workflow[" + workflow.getWorkflowId() + "]' parameter has no value");
                                }
                            }
                        }
                    }
                    j++;

                    if(step.getDependsOn() != null) {
                        boolean matchingStep = false;
                        for (Step s : workflow.getSteps()) {
                            if(s.getStepId().equals(step.getDependsOn())) {
                               matchingStep = true;
                               break;
                            }
                        }
                        if(!matchingStep) {
                            result.addError("'Workflow[" + workflow.getWorkflowId() + "] stepId' " + step.getStepId() + " 'dependsOn' is invalid (no such a step exists)");
                        }
                    }

                    for(SuccessAction successAction : step.getOnSuccess()) {
                        if(successAction.getType() == null) {
                            result.addError("'Workflow[" + workflow.getWorkflowId() + "]' step SuccessAction has no type");
                        }
                        if(successAction.getType() != null) {
                            List<String> supportedValues = Arrays.asList("end", "goto");
                            if(!supportedValues.contains(successAction.getType())) {
                                result.addError("'Workflow[" + workflow.getWorkflowId() + "]' step SuccessAction  type (" + successAction.getType() + ") is invalid");
                            }
                        }
                    }
                }
                if(j == 0) {
                    result.addError("'Workflow " + workflow.getWorkflowId() + "' no Steps are undefined");
                }

                for (String key : workflow.getOutputs().keySet()) {
                    if(!isValidOutputsKey(key)) {
                        result.addError("Workflow[" + workflow.getWorkflowId() + "] Outputs key is invalid (should match regex " + getOutputsKeyRegularExpression() + ")");
                    }
                }
            }
        }
        
        return result;
    }

    boolean isValidStepId(String stepId) {
        return Pattern.matches(getStepIdRegularExpression(), stepId);
    }

    String getStepIdRegularExpression() {
        return "[A-Za-z0-9_\\-]+";
    }

    boolean isValidOutputsKey(String key) {
        return Pattern.matches(getOutputsKeyRegularExpression(), key);
    }

    String getOutputsKeyRegularExpression() {
        return "^[a-zA-Z0-9\\.\\-_]+$";
    }

}
