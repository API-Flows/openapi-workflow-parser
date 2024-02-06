package com.apiflows.parser;

import com.apiflows.model.*;
import com.fasterxml.jackson.core.JsonPointer;
import io.swagger.v3.oas.models.media.Schema;

import java.util.*;
import java.util.regex.Pattern;

public class OpenAPIWorkflowValidator {

    private OpenAPIWorkflow openAPIWorkflow = null;
    Set<String> workflowIds = new HashSet<>();
    Map<String, Set<String>> stepIds = new HashMap<>();
    Set<String> operationIds = new HashSet<>();
    Set<Schema> components = new HashSet<>();

    OpenAPIWorkflowValidator() {
    }

    public OpenAPIWorkflowValidator(OpenAPIWorkflow openAPIWorkflow) {
        this.openAPIWorkflow = openAPIWorkflow;
    }

    public OpenAPIWorkflowValidatorResult validate() {

        if(this.openAPIWorkflow == null) {
            throw new RuntimeException("OpenAPIWorkflow is not provided");
        }

        loadWorkflowIds(this.openAPIWorkflow);
        loadStepIds(this.openAPIWorkflow.getWorkflows());
        loadOperationIds(this.openAPIWorkflow);

        OpenAPIWorkflowValidatorResult result = new OpenAPIWorkflowValidatorResult();
        
        if (openAPIWorkflow.getWorkflowsSpec() == null || openAPIWorkflow.getWorkflowsSpec().isEmpty()) {
            result.addError("'workflowsSpec' is undefined");
        }

        // Info
        result.addErrors(validateInfo(openAPIWorkflow.getInfo()));
        // SourceDescriptions
        result.addErrors(validateSourceDescriptions(openAPIWorkflow.getSourceDescriptions()));
        // Workflows
        if (openAPIWorkflow.getWorkflows() == null || openAPIWorkflow.getWorkflows().isEmpty()) {
            result.addError("'Workflows' is undefined");
        }

        if (openAPIWorkflow.getWorkflows() != null) {
            for (Workflow workflow : openAPIWorkflow.getWorkflows()) {
                int i = 0;

                result.addErrors(validateWorkflow(workflow, i));

                for (Step step : workflow.getSteps()) {
                    result.addErrors(validateStep(step, workflow.getWorkflowId()));
                }
            }
        }
        // Components
        result.addErrors(validateComponents(openAPIWorkflow.getComponents()));

        if(!result.getErrors().isEmpty()) {
            result.setValid(false);
        }

        return result;
    }

    List<String> validateInfo(Info info) {
        List<String> errors = new ArrayList<>();

        if (info == null) {
            errors.add("'Info' is undefined");
        }
        if (info != null && (info.getTitle() == null || info.getTitle().isEmpty())) {
            errors.add("'Info title' is undefined");
        }
        if (info != null && (info.getVersion() == null || info.getVersion().isEmpty())) {
            errors.add("'Info version' is undefined");
        }

        return errors;
    }

    List<String> validateSourceDescriptions(List<SourceDescription> sourceDescriptions) {
        List<String> SUPPORTED_TYPES = Arrays.asList("openapi", "workflowsSpec");

        List<String> errors = new ArrayList<>();

        if (sourceDescriptions == null) {
            errors.add("'SourceDescriptions' is undefined");
        }

        if (sourceDescriptions != null) {
            int i = 0;
            for (SourceDescription sourceDescription : sourceDescriptions) {
                if (sourceDescription.getName() == null || sourceDescription.getName().isEmpty()) {
                    errors.add("'SourceDescription[" + i + "] name' is undefined");
                }
                if (sourceDescription.getUrl() == null || sourceDescription.getUrl().isEmpty()) {
                    errors.add("'SourceDescription[" + i + "] url' is undefined");
                }
                if (sourceDescription.getType() != null) {
                    if(!SUPPORTED_TYPES.contains(sourceDescription.getType())) {
                        errors.add("'SourceDescription[" + i + "] type' is invalid");
                    }
                }
                i++;
            }

            if(i == 0) {
                errors.add("'SourceDescriptions' is empty");
            }
        }


        return errors;
    }

    List<String> validateWorkflow(Workflow workflow, int index ){
        List<String> errors = new ArrayList<>();

        if (workflow.getWorkflowId() == null || workflow.getWorkflowId().isEmpty()) {
            errors.add("'Workflow[" + index + "] workflowId' is undefined");
        }

        if (workflow.getWorkflowId() != null && !isValidWorkflowId(workflow.getWorkflowId())) {
            errors.add("WorkflowId " + workflow.getWorkflowId() + " format is invalid (should match regex " +  getWorkflowIdRegularExpression() + ")");
        }

        if (workflow.getSteps() == null || workflow.getSteps().isEmpty()) {
            errors.add("'Workflow " + workflow.getWorkflowId() + "' no Steps are undefined");
        }

        for (String key : workflow.getOutputs().keySet()) {
            if(!isValidOutputsKey(key)) {
                errors.add("Workflow[" + workflow.getWorkflowId() + "] Outputs key is invalid (should match regex " + getOutputsKeyRegularExpression() + ")");
            }
        }


        return errors;
    }

    List<String> validateStep(Step step, String workflowId ) {
        List<String> errors = new ArrayList<>();

        String stepId = step.getStepId();

        if (stepId == null || stepId.isEmpty()) {
            errors.add("'Workflow[" + workflowId + "] stepId' is undefined");
        }

        if (stepId != null && !isValidStepId(stepId)) {
            errors.add("'Step " + stepId + " is invalid (should match regex " + getStepIdRegularExpression() + ")");
        }

        int numAssignedValues = (step.getOperationId() != null ? 1 : 0) +
                (step.getWorkflowId() != null ? 1 : 0) +
                (step.getOperationRef() != null ? 1 : 0);

        if (numAssignedValues != 1) {
            if(stepId != null) {
                errors.add("'Step " + stepId + " should provide only one of the following: [operationId, operationRef, workflowId]");
            } else {
                errors.add("'Workflow[" + workflowId + "]' should provide only one of the following: [operationId, operationRef, workflowId]");
            }
        }

        if(step.getParameters() != null) {
            for(Parameter parameter : step.getParameters()) {
                errors.addAll(validateParameter(parameter, workflowId));

                if(step.getWorkflowId() != null) {
                    // when the step in context specifies a workflowId the parameter IN must be defined
                    if(parameter.getIn() == null) {
                        errors.add("'Workflow[" + workflowId + "]' parameter IN must be defined");
                    }
                }
            }
        }

        if(step.getDependsOn() != null) {
            if(!stepExists(workflowId, step.getDependsOn())) {
                errors.add("'Step " + stepId + " 'dependsOn' is invalid (no such a step exists)");
            }

            if(step.getDependsOn().equals(stepId)) {
                errors.add("'Step " + stepId + " 'dependsOn' is invalid (same value as stepId)");
            }
        }

        for(Criterion criterion : step.getSuccessCriteria()) {
            errors.addAll(validateCriterion(criterion, stepId));
        }

        for(SuccessAction successAction: step.getOnSuccess()) {
            errors.addAll(validateSuccessAction(workflowId, stepId, successAction));
        }

        for(FailureAction failureAction : step.getOnFailure()) {
            errors.addAll(validateFailureAction(workflowId, stepId, failureAction));

        }

        return errors;
    }

    List<String> validateParameter(Parameter parameter, String workflowId ) {
        List<String> SUPPORTED_VALUES = Arrays.asList("path", "query", "header", "cookie", "body", "workflow");

        List<String> errors = new ArrayList<>();

        if(parameter.get$ref() != null) {
            // Reference object
            // check is URI
        } else {
            // Parameter object
            String name = parameter.getName();

            if(name == null) {
                errors.add("'Workflow[" + workflowId + "]' parameter has no name");
            }
            if(parameter.getIn() != null) {
                if(!SUPPORTED_VALUES.contains(parameter.getIn())) {
                    if(name != null) {
                        errors.add("Parameter '" + name + "' type (" + parameter.getIn() + ") is invalid");
                    } else {
                        errors.add("'Workflow[" + workflowId + "]' parameter type (" + parameter.getIn() + ") is invalid");
                    }
                }
            }
            if(parameter.getValue() == null) {
                if(name != null) {
                    errors.add("Parameter '" + name + "' has no value");
                } else {
                    errors.add("'Workflow[" + workflowId + "]' parameter has no value");
                }
            }
        }
        return errors;
    }

    List<String> validateSuccessAction(String workflowId, String stepId, SuccessAction successAction) {
        List<String> SUPPORTED_VALUES = Arrays.asList("end", "goto");

        List<String> errors = new ArrayList<>();

        if (successAction.getType() == null) {
            errors.add("Step " + stepId + " SuccessAction has no type");
        }

        if (successAction.getType() != null) {
            if (!SUPPORTED_VALUES.contains(successAction.getType())) {
                errors.add("Step " + stepId + " SuccessAction type (" + successAction.getType() + ") is invalid");
            }
        }

        if(successAction.getWorkflowId() == null && successAction.getStepId() == null) {
            errors.add("Step " + stepId + " SuccessAction must define either workflowId or stepId");
        }

        if(successAction.getWorkflowId() != null && successAction.getStepId() != null) {
            errors.add("Step " + stepId + " SuccessAction cannot define both workflowId and stepId");
        }

        if(successAction.getStepId() != null && successAction.getType() != null && successAction.getType().equals("goto")) {
            // when type `goto` stepId must exist (if provided)
            if (!stepExists(workflowId, successAction.getStepId())) {
                errors.add("Step " + stepId + " SuccessAction stepId is invalid (no such a step exists)");
            }
        }

        return errors;
    }

    List<String> validateFailureAction(String workflowId, String stepId, FailureAction failureAction) {
        List<String> SUPPORTED_VALUES = Arrays.asList("end", "retry", "goto");

        List<String> errors = new ArrayList<>();

        if (failureAction.getType() == null) {
            errors.add("Step " + stepId + " FailureAction has no type");
        }

        if (failureAction.getType() != null) {
            if (!SUPPORTED_VALUES.contains(failureAction.getType())) {
                errors.add("Step " + stepId + " FailureAction type (" + failureAction.getType() + ") is invalid");
            }
        }

        if(failureAction.getWorkflowId() == null && failureAction.getStepId() == null) {
            errors.add("Step " + stepId + " FailureAction must define either workflowId or stepId");
        }

        if(failureAction.getWorkflowId() != null && failureAction.getStepId() != null) {
            errors.add("Step " + stepId + " FailureAction cannot define both workflowId and stepId");
        }

        if(failureAction.getRetryAfter() != null && failureAction.getRetryAfter() < 0) {
            errors.add("Step " + stepId + " FailureAction retryAfter must be non-negative");

        }

        if(failureAction.getRetryLimit() != null && failureAction.getRetryLimit() < 0) {
            errors.add("Step " + stepId + " FailureAction retryLimit must be non-negative");

        }

        if(failureAction.getStepId() != null && failureAction.getType() != null
                && (failureAction.getType().equals("goto") || failureAction.getType().equals("retry"))) {
            // when type `goto` or `retry` stepId must exist (if provided)
            if (!stepExists(workflowId, failureAction.getStepId())) {
                errors.add("Step " + stepId + " FailureAction stepId is invalid (no such a step exists)");
            }
        }


        return errors;
    }

    List<String> validateCriterion(Criterion criterion, String stepId) {
        List<String> SUPPORTED_VALUES = Arrays.asList("simple", "regex", "JSONPath");

        List<String> errors = new ArrayList<>();

        if(criterion.getCondition() == null) {
            errors.add("Step " + stepId + " has no condition");
        }

        if (criterion.getType() != null && !SUPPORTED_VALUES.contains(criterion.getType())) {
            errors.add("Step " + stepId + " SuccessCriteria type (" + criterion.getType() + ") is invalid");
        }

        if (criterion.getType() != null && criterion.getContext() == null) {
            errors.add("Step " + stepId + " SuccessCriteria type is specified but context is not provided");
        }

        return errors;
    }

    List<String> validateComponents(Components components) {
        List<String> errors = new ArrayList<>();

        if(components != null) {
            if (components.getParameters() != null) {

                for(String key : components.getParameters().keySet()) {
                    if(isValidComponentKey(key)) {
                        errors.add("'Component parameter " + key + " is invalid (should match regex " + getComponentKeyRegularExpression() + ")");
                    }
                }

                for (Parameter parameter : components.getParameters().values()) {
                    errors.addAll(validateParameter(parameter, "Components"));
                }
            }
            if (components.getInputs() != null) {

                for(String key : components.getInputs().keySet()) {
                    if(isValidComponentKey(key)) {
                        errors.add("'Component input " + key + " is invalid (should match regex " + getComponentKeyRegularExpression() + ")");
                    }
                }

            }
        }

        return errors;
    }

    boolean isValidWorkflowId(String workflowId) {
        return Pattern.matches(getWorkflowIdRegularExpression(), workflowId);
    }

    boolean isValidStepId(String stepId) {
        return Pattern.matches(getStepIdRegularExpression(), stepId);
    }

    boolean isValidComponentKey(String key) {
        return Pattern.matches(getComponentKeyRegularExpression(), key);
    }

    String getStepIdRegularExpression() {
        return "[A-Za-z0-9_\\-]+";
    }

    String getWorkflowIdRegularExpression() {
        return "[A-Za-z0-9_\\\\-]++";
    }
    String getComponentKeyRegularExpression() {
        return "^[a-zA-Z0-9\\.\\-_]+$";
    }

    boolean isValidOutputsKey(String key) {
        return Pattern.matches(getOutputsKeyRegularExpression(), key);
    }

    String getOutputsKeyRegularExpression() {
        return "^[a-zA-Z0-9\\.\\-_]+$";
    }

    List<String> loadWorkflowIds(List<Workflow> workflows) {
        List<String> errors = new ArrayList<>();

        if(workflows != null) {
            for(Workflow workflow : workflows) {
                if(!this.workflowIds.add(workflow.getWorkflowId())) {
                    // id already exists
                    errors.add("WorkflowId is not unique: " + workflow.getWorkflowId());
                }
            }
        }

        return errors;
    }

    List<String> loadStepIds(List<Workflow> workflows) {
        List<String> errors = new ArrayList<>();

        if(workflows != null) {
            for(Workflow workflow : workflows) {
                Set<String> steps = this.stepIds.get(workflow.getWorkflowId());

                if(steps == null) {
                    steps = new HashSet<>();
                }

                for(Step step : workflow.getSteps()) {
                    if(!steps.add(step.getStepId())) {
                        // id already exists
                        errors.add("StepId is not unique: " + step.getStepId());
                    }
                }

                this.stepIds.put(workflow.getWorkflowId(), steps);
            }
        }

        return errors;
    }

    List<String> loadOperationIds(OpenAPIWorkflow openAPIWorkflow) {
        List<String> errors = new ArrayList<>();

        boolean multipleOpenApiFiles = getNumOpenApiSourceDescriptions(openAPIWorkflow.getSourceDescriptions()) > 1 ? true : false;

        for(Workflow workflow : openAPIWorkflow.getWorkflows()) {
            errors.addAll(validateStepsOperationIds(workflow.getSteps(), multipleOpenApiFiles));

            for(Step step : workflow.getSteps()) {
                if(step.getOperationId() != null) {
                    this.operationIds.add(step.getOperationId());
                }
            }
        }

        return errors;
    }

    public List<String> validateStepsOperationIds(List<Step> steps, boolean multipleOpenApiFiles) {
        List<String> errors = new ArrayList<>();

        for(Step step : steps) {
            if(multipleOpenApiFiles) {
                // must use runtime expression to map applicable SourceDescription
                if(step.getOperationId() != null && !step.getOperationId().startsWith("$sourceDescriptions.")) {
                    errors.add("Operation " + step.getOperationId() + " must be specified using a runtime expression (e.g., $sourceDescriptions.<name>.<operationId>)");
                }
            }
        }

        return errors;
    }

    // num of SourceDescriptions with type 'openapi'
    int getNumOpenApiSourceDescriptions(List<SourceDescription> sourceDescriptions) {
        return (int) sourceDescriptions.stream().filter(p -> p.isOpenApi()).count();
    }

    // num of SourceDescriptions with type 'workflowsSpec'
    int getNumWorkflowsSpecSourceDescriptions(List<SourceDescription> sourceDescriptions) {
        return (int) sourceDescriptions.stream().filter(p -> p.isWorkflowsSpec()).count();
    }

    boolean stepExists(String workflowId, String stepId) {
        return this.stepIds.get(workflowId) != null && this.stepIds.get(workflowId).contains(stepId);
    }

    public boolean isValidJsonPointer(String jsonPointerString) {

        boolean ret;

        try {
            JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
            ret = true;
        } catch (IllegalArgumentException e) {
            ret = false;
        }

        return ret;
    }

}
