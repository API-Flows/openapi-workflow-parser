package com.apiflows.parser;

import com.apiflows.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OpenAPIWorkflowValidatorTest {

    OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();

    @Test
    void validate() {
        OpenAPIWorkflow openAPIWorkflow = new OpenAPIWorkflow();
        OpenAPIWorkflowValidatorResult result = new OpenAPIWorkflowValidator(openAPIWorkflow).validate();

        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
        assertEquals("'workflowsSpec' is undefined", result.getErrors().get(0));
    }

    @Test
    void validateInfoVersion() {
        Info info = new Info();
        info.setTitle("title");

        assertEquals(1, validator.validateInfo(info).size());
    }

    @Test
    void validateSourceDescriptions() {
        List<SourceDescription> sourceDescriptions = null;

        assertEquals(1, validator.validateSourceDescriptions(sourceDescriptions).size());
    }

    @Test
    void validateSourceDescriptionsWithoutUrl() {
        List<SourceDescription> sourceDescriptions = new ArrayList<>();
        sourceDescriptions.add(new SourceDescription()
                .name("Source one")
                .type("openapi")
                .url(null));
        assertEquals(1, validator.validateSourceDescriptions(sourceDescriptions).size());
    }

    @Test
    void validateSourceDescriptionsInvalidType() {
        List<SourceDescription> sourceDescriptions = new ArrayList<>();
        sourceDescriptions.add(new SourceDescription()
                .name("Source one")
                .type("unkwown")
                .url("https://example.com/spec.json"));
        assertEquals(1, validator.validateSourceDescriptions(sourceDescriptions).size());
    }

    @Test
    void validateWorkflowWithoutWorkflowId() {
        Workflow workflow = new Workflow()
                .workflowId(null)
                .addStep(new Step()
                        .stepId("step-one"));
        int index = 0;

        assertEquals(1, validator.validateWorkflow(workflow, index).size());
    }

    @Test
    void validateWorkflowWithoutSteps() {
        Workflow workflow = new Workflow()
                .workflowId("workflow-id-1");
        int index = 0;

        assertEquals(1, validator.validateWorkflow(workflow, index).size());
    }

    @Test
    void validateStep() {
        Step step = new Step()
                .stepId("step-one")
                .description("First step in the workflow")
                .operationId("openapi-endpoint");
        String worklowId = "q1";

        assertEquals(0, validator.validateStep(step, worklowId).size());
    }

    @Test
    void validateStepMissingStepId() {
        Step step = new Step()
                .stepId(null)
                .description("First step in the workflow")
                .operationId("openapi-endpoint");
        String worklowId = "q1";

        assertEquals(1, validator.validateStep(step, worklowId).size());
    }

    @Test
    void validateStepMissingEntity() {
        Step step = new Step()
                .stepId("step-one")
                .description("First step in the workflow")
                .operationId(null)
                .workflowId(null)
                .operationRef(null);
        String worklowId = "q1";

        assertEquals(1, validator.validateStep(step, worklowId).size());
    }

    @Test
    void validateStepDependsOn() {
        final String WORKFLOW_ID = "q1";

        List<Workflow> workflows = List.of(
                new Workflow()
                        .workflowId(WORKFLOW_ID)
                        .addStep(new Step()
                                .stepId("step-one"))
                        .addStep(new Step()
                                .stepId("step-two"))
        );

        validator.loadStepIds(workflows);

        Step step = new Step()
                .stepId("step-two")
                .description("Second step in the workflow")
                .operationId("openapi-endpoint")
                .dependsOn("step-one");

        assertEquals(0, validator.validateStep(step, WORKFLOW_ID).size());
    }

    @Test
    void validateStepDependsOnMissingStep() {
        final String WORKFLOW_ID = "q1";

        List<Workflow> workflows = List.of(
                new Workflow()
                        .workflowId(WORKFLOW_ID)
                        .addStep(new Step()
                                .stepId("step-one"))
                        .addStep(new Step()
                                .stepId("step-two"))
        );

        validator.loadStepIds(workflows);

        Step step = new Step()
                .stepId("step-two")
                .description("Second step in the workflow")
                .operationId("openapi-endpoint")
                .dependsOn("step-three");

        assertEquals(1, validator.validateStep(step, WORKFLOW_ID).size());
    }

    @Test
    void validateStepDependsOnSelf() {
        final String WORKFLOW_ID = "q1";

        List<Workflow> workflows = List.of(
                new Workflow()
                        .workflowId(WORKFLOW_ID)
                        .addStep(new Step()
                                .stepId("step-one"))
                        .addStep(new Step()
                                .stepId("step-two"))
        );

        validator.loadStepIds(workflows);

        Step step = new Step()
                .stepId("step-one")
                .description("Second step in the workflow")
                .operationId("openapi-endpoint")
                .dependsOn("step-one");

        assertEquals(1, validator.validateStep(step, WORKFLOW_ID).size());
    }

    @Test
    void validateStepWithoutInAttribute() {
        Step step = new Step()
                .stepId("step-one")
                .description("First step in the workflow")
                .workflowId("workflow-id-2");
        step.addParameter(new Parameter()
                .name("param")
                .value("value"));

        String worklowId = "q1";

        assertEquals(1, validator.validateStep(step, worklowId).size());
    }


    @Test
    void validateParameter() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("path");
        String worklowId = "q1";

        assertEquals(0, validator.validateParameter(parameter, worklowId).size());
    }

    @Test
    void validateParameterInvalidIn() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("dummy");
        String worklowId = "q1";

        assertEquals(1, validator.validateParameter(parameter, worklowId).size());
    }

    @Test
    void validateParameterWithoutValue() {
        Parameter parameter = new Parameter()
                .name("param")
                .value(null)
                .in("query");
        String worklowId = "q1";

        assertEquals(1, validator.validateParameter(parameter, worklowId).size());
    }

    @Test
    void validateSuccessAction() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .type("end")
                .stepId("step-one");

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(0, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionInvalidType() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .type("invalid-type")
                .stepId("step-one");

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionMissingEntity() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .type("end")
                .stepId(null)
                .workflowId(null);

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionInvalidEntity() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .type("end")
                .stepId("step-one")
                .workflowId("workflow-id");

        successAction.addCriteria(
                new Criterion()
                        .condition("$statusCode == 200"));

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionInvalidStepId() {
        String workflowId = "w1";

        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();

        Map<String, Set<String>> stepIds = new HashMap<>();
        stepIds.put("w1", Set.of("step-one", "step-two", "step-three"));

        validator.stepIds = stepIds;

        String stepId = "step-one";
        SuccessAction successAction = new SuccessAction()
                .type("goto")
                .stepId("step-dummy");

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }


    @Test
    void validateCriterion() {
        String stepId = "step-one";

        Criterion criterion = new Criterion()
                .condition("$statusCode == 200")
                .type("simple")
                .context("$response.body");

        assertEquals(0, validator.validateCriterion(criterion, stepId).size());
    }

    @Test
    void validateCriterionWithoutType() {
        String stepId = "step-one";

        Criterion criterion = new Criterion()
                .condition("$statusCode == 200");

        assertEquals(0, validator.validateCriterion(criterion, stepId).size());
    }
    @Test
    void validateCriterionInvalidType() {
        String stepId = "step-one";

        Criterion criterion = new Criterion()
                .condition("$statusCode == 200")
                .type("dummy")
                .context("$response.body");

        assertEquals(1, validator.validateCriterion(criterion, stepId).size());
    }

    @Test
    void validateCriterionMissingContext() {
        String stepId = "step-one";

        Criterion criterion = new Criterion()
                .condition("$statusCode == 200")
                .type("simple")
                .context(null);

        assertEquals(1, validator.validateCriterion(criterion, stepId).size());
    }

    @Test
    void validateFailureAction() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .type("end")
                .stepId("step-one")
                .retryAfter(1000L)
                .retryLimit(3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(0, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionInvalidType() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .type("dummy")
                .stepId("step-one")
                .retryAfter(1000L)
                .retryLimit(3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionInvalidRetrySettings() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .type("end")
                .stepId("step-one")
                .retryAfter(-1000L)
                .retryLimit(-3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(2, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionMissingEntity() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .type("retry")
                .stepId(null)
                .workflowId(null)
                .retryAfter(1000L)
                .retryLimit(3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionInvalidEntity() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .type("end")
                .stepId("step-one")
                .workflowId("workflow-test")
                .retryAfter(1000L)
                .retryLimit(3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionInvalidStepId() {
        String workflowId = "w1";
        String stepId = "step-one";

        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();

        Map<String, Set<String>> stepIds = new HashMap<>();
        stepIds.put("w1", Set.of("step-one", "step-two", "step-three"));

        validator.stepIds = stepIds;

        FailureAction failureAction = new FailureAction()
                .type("retry")
                .stepId("step-dummy")
                .retryAfter(1000L)
                .retryLimit(3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }


    @Test
    void validateWorkflowIdsUniqueness() {
        List<Workflow> list = List.of(
                new Workflow()
                        .workflowId("one"),
                new Workflow()
                        .workflowId("one"));

        assertEquals(1, validator.validateWorkflowIdsUniqueness(list).size());
    }

    @Test
    void validateComponentsParameterInvalidIn() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("dummy");
        String worklowId = "q1";

        Components components = new Components();
        components.addParameter("param1", parameter);

        assertEquals(1, validator.validateParameter(parameter, worklowId).size());
    }

    @Test
    void validateComponentsParameter() {
        Parameter parameter = new Parameter()
                .name("page")
                .value("1")
                .in("query");
        String worklowId = "q1";

        Components components = new Components();
        components.addParameter("page", parameter);

        assertEquals(0, validator.validateParameter(parameter, worklowId).size());
    }

    @Test
    void loadStepsWithDuplicateIds() {
        List<Workflow> list = List.of(
                new Workflow()
                        .workflowId("one")
                        .addStep(new Step()
                                .stepId("step-ABC"))
                        .addStep(new Step()
                                .stepId("step-ABC"))
        );

        assertEquals(1, validator.loadStepIds(list).size());
    }

    @Test
    public void getNumOpenApiSourceDescriptions() {
        List<SourceDescription> sourceDescriptions = List.of(
                new SourceDescription()
                        .name("openapifile-1")
                        .type("openapi"),
                new SourceDescription()
                        .name("openapifile-2")
                        .type("openapi"),
                new SourceDescription()
                        .name("workflowspec-1")
                        .type("workflowsSpec")
        );

        assertEquals(2, new OpenAPIWorkflowValidator().getNumOpenApiSourceDescriptions(sourceDescriptions));
    }

    @Test
    public void getNumWorkflowsSpecSourceDescriptions() {
        List<SourceDescription> sourceDescriptions = List.of(
                new SourceDescription()
                        .name("openapifile-1")
                        .type("openapi"),
                new SourceDescription()
                        .name("openapifile-2")
                        .type("openapi"),
                new SourceDescription()
                        .name("workflowspec-1")
                        .type("workflowsSpec")
        );

        assertEquals(1, new OpenAPIWorkflowValidator().getNumWorkflowsSpecSourceDescriptions(sourceDescriptions));
    }

    @Test
    public void validateStepsOperationIds() {
        boolean multipleOpenApiFiles = true;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .operationId("post-operation")
        );

        assertEquals(1, new OpenAPIWorkflowValidator().validateStepsOperationIds(steps, multipleOpenApiFiles).size());
    }

    @Test
    public void validateStepsOperationIdsWithoutRuntimeExpression() {
        boolean multipleOpenApiFiles = false;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .operationId("post-operation")
        );

        assertEquals(0, new OpenAPIWorkflowValidator().validateStepsOperationIds(steps, multipleOpenApiFiles).size());
    }

    @Test
    public void validateStepsWorkflowIds() {
        boolean multipleWorkflowsSpecFiles = true;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .workflowId("w2")
        );

        assertEquals(1, new OpenAPIWorkflowValidator().validateStepsWorkflowIds(steps, multipleWorkflowsSpecFiles).size());
    }

    @Test
    public void validateStepsWorkflowIdsWithoutRuntimeExpression() {
        boolean multipleWorkflowsSpecFiles = false;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .workflowId("w2")
        );

        assertEquals(0, new OpenAPIWorkflowValidator().validateStepsWorkflowIds(steps, multipleWorkflowsSpecFiles).size());
    }

    @Test
    void stepExists() {
        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();
        Map<String, Set<String>> stepIds = new HashMap<>();
        stepIds.put("w1", Set.of("step-one", "step-two", "step-three"));

        validator.stepIds = stepIds;

        assertTrue(validator.stepExists("w1", "step-one"));
    }

    @Test
    void stepNotFound() {
        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();
        Map<String, Set<String>> stepIds = new HashMap<>();
        stepIds.put("w1", Set.of("step-one", "step-two", "step-three"));

        validator.stepIds = stepIds;

        assertFalse(validator.stepExists("w1", "step-dummy"));
    }

    @Test
    void workflowExists() {
        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();
        validator.workflowIds.add("w1");

        assertTrue(validator.workflowExists("w1"));
    }

    @Test
    void workflowNotFound() {
        OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();
        validator.workflowIds.add("w1");

        assertFalse(validator.workflowExists("dummy"));
    }

    @Test
    void validWorkflowId() {
        assertTrue(new OpenAPIWorkflowValidator().isValidWorkflowId("idOfTheWorkflow_1"));
    }

    @Test
    void invalidWorkflowId() {
        assertFalse(new OpenAPIWorkflowValidator().isValidWorkflowId("workflow id"));
    }

    @Test
    void validOutputsKey() {
        assertTrue(new OpenAPIWorkflowValidator().isValidOutputsKey("tokenExpires"));
    }

    @Test
    void invalidOutputsKey() {
        assertFalse(new OpenAPIWorkflowValidator().isValidOutputsKey("$tokenExpires"));
    }

    @Test
    void invalidOutputsKeyWithSpace() {
        assertFalse(new OpenAPIWorkflowValidator().isValidOutputsKey("$token Expires"));
    }

    @Test
    void validComponentKey() {
        assertTrue(new OpenAPIWorkflowValidator().isValidComponentKey("pagination"));
    }

    @Test
    void invalidComponentKey() {
        assertFalse(new OpenAPIWorkflowValidator().isValidComponentKey("pagination order"));
    }


    @Test
    void isValidJsonPointer() {
        assertTrue(new OpenAPIWorkflowValidator().isValidJsonPointer("/user/id"));
    }

    @Test
    void invalidJsonPointer() {
        assertFalse(new OpenAPIWorkflowValidator().isValidJsonPointer("user/id"));
    }

//    @Test
//    void isValidJsonPointer2() {
//        assertTrue(new OpenAPIWorkflowValidator().isValidJsonPointer("#/petId"));
//    }

}