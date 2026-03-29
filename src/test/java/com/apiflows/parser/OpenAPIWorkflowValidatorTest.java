package com.apiflows.parser;

import com.apiflows.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OpenAPIWorkflowValidatorTest {

    OpenAPIWorkflowValidator validator = new OpenAPIWorkflowValidator();

    private OpenAPIWorkflowValidator validatorWithStepIds(String workflowId, String... steps) {
        OpenAPIWorkflowValidator v = new OpenAPIWorkflowValidator();
        Map<String, Set<String>> ids = new HashMap<>();
        ids.put(workflowId, new HashSet<>(Arrays.asList(steps)));
        v.stepIds = ids;
        return v;
    }

    private OpenAPIWorkflowValidator validatorWithWorkflowIds(String... workflowIds) {
        OpenAPIWorkflowValidator v = new OpenAPIWorkflowValidator();
        v.workflowIds.addAll(Arrays.asList(workflowIds));
        return v;
    }

    @Test
    void validate() {
        OpenAPIWorkflow openAPIWorkflow = new OpenAPIWorkflow();
        OpenAPIWorkflowValidatorResult result = new OpenAPIWorkflowValidator(openAPIWorkflow).validate();

        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
        assertEquals("'arazzo' is undefined", result.getErrors().get(0));
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
                .name("source-one")
                .type("openapi")
                .url(null));
        assertEquals(1, validator.validateSourceDescriptions(sourceDescriptions).size());
    }

    @Test
    void validateSourceDescriptionsInvalidType() {
        List<SourceDescription> sourceDescriptions = new ArrayList<>();
        sourceDescriptions.add(new SourceDescription()
                .name("source-one")
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
                .operationPath(null);
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
                .operationId("op-id-2");
        step.addParameter(new Parameter()
                .name("param")
                .value("value"));

        String worklowId = "q1";

        assertEquals(1, validator.validateStep(step, worklowId).size());
    }

    @Test
    void validateReusableParameter() {
        Parameter parameter = new Parameter()
                .reference("$components.parameters.pageSize")
                .value("1");
        String worklowId = "q1";

        Components components = new Components()
                .parameter("pageSize", parameter);

        validator.loadComponents(components);

        assertEquals(0, validator.validateReusableParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateReusableParameterNotFound() {
        Parameter parameter = new Parameter()
                .reference("$components.parameters.pageSize")
                .value("1");
        Parameter anotherParameter = new Parameter()
                .reference("$components.parameters.pageCounter")
                .value("1");
        String worklowId = "q1";

        Components components = new Components()
                .parameter("anotherParameter", anotherParameter);

        validator.loadComponents(components);

        assertEquals(1, validator.validateReusableParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateParameter() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("path");
        String worklowId = "q1";

        assertEquals(0, validator.validateParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateParameterInvalidIn() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("dummy");
        String worklowId = "q1";

        assertEquals(1, validator.validateParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateParameterBodyInIsInvalid() {
        Parameter parameter = new Parameter()
                .name("param")
                .value("1")
                .in("body");
        String worklowId = "q1";

        assertEquals(1, validator.validateParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateParameterWithoutValue() {
        Parameter parameter = new Parameter()
                .name("param")
                .value(null)
                .in("query");
        String worklowId = "q1";

        assertEquals(1, validator.validateParameter(parameter, worklowId, null).size());
    }

    @Test
    void validateSuccessAction() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("end");

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(0, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionEndTypeDoesNotRequireTarget() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("end")
                .stepId(null)
                .workflowId(null);

        assertEquals(0, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionInvalidType() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("invalid-type")
                .stepId("step-one");

        successAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionGotoMissingTarget() {
        String workflowId = "w1";
        String stepId = "step-one";

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("goto")
                .stepId(null)
                .workflowId(null);

        assertEquals(1, validator.validateSuccessAction(workflowId, stepId, successAction).size());
    }

    @Test
    void validateSuccessActionGotoInvalidEntity() {
        OpenAPIWorkflowValidator v = validatorWithStepIds("w1", "step-one", "step-two");
        v.workflowIds.add("target-workflow");

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("goto")
                .stepId("step-one")
                .workflowId("target-workflow");

        assertEquals(1, v.validateSuccessAction("w1", "step-one", successAction).size());
    }

    @Test
    void validateSuccessActionGotoValidWorkflowId() {
        OpenAPIWorkflowValidator v = validatorWithWorkflowIds("target-workflow");

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("goto")
                .workflowId("target-workflow");

        assertEquals(0, v.validateSuccessAction("w1", "step-one", successAction).size());
    }

    @Test
    void validateSuccessActionGotoInvalidWorkflowId() {
        OpenAPIWorkflowValidator v = validatorWithWorkflowIds("w1");

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("goto")
                .workflowId("non-existent-workflow");

        assertEquals(1, v.validateSuccessAction("w1", "step-one", successAction).size());
    }

    @Test
    void validateSuccessActionInvalidStepId() {
        OpenAPIWorkflowValidator v = validatorWithStepIds("w1", "step-one", "step-two", "step-three");

        SuccessAction successAction = new SuccessAction()
                .name("on-success")
                .type("goto")
                .stepId("step-dummy");

        assertEquals(1, v.validateSuccessAction("w1", "step-one", successAction).size());
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
                .name("on-failure")
                .type("end")
                .retryAfter(1.5)
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
                .name("on-failure")
                .type("dummy")
                .retryAfter(1.5)
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
                .name("on-failure")
                .type("end")
                .retryAfter(-1.5)
                .retryLimit(-3);

        failureAction.addCriteria(
                new Criterion()
                        .context("$statusCode == 200"));

        assertEquals(2, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionRetryTypeDoesNotRequireTarget() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .name("on-failure")
                .type("retry")
                .stepId(null)
                .workflowId(null)
                .retryAfter(1.5)
                .retryLimit(3);

        assertEquals(0, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionGotoMissingTarget() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .name("on-failure")
                .type("goto")
                .stepId(null)
                .workflowId(null);

        assertEquals(1, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionGotoInvalidEntity() {
        OpenAPIWorkflowValidator v = validatorWithStepIds("w1", "step-one", "step-two");

        FailureAction failureAction = new FailureAction()
                .name("on-failure")
                .type("goto")
                .stepId("step-one")
                .workflowId("workflow-test");

        assertEquals(1, v.validateFailureAction("w1", "step-one", failureAction).size());
    }

    @Test
    void validateFailureActionRetryAfterDecimalValue() {
        String workflowId = "w1";
        String stepId = "step-one";

        FailureAction failureAction = new FailureAction()
                .name("on-failure")
                .type("retry")
                .retryAfter(0.5)
                .retryLimit(3);

        assertEquals(0, validator.validateFailureAction(workflowId, stepId, failureAction).size());
    }

    @Test
    void validateFailureActionInvalidStepId() {
        OpenAPIWorkflowValidator v = validatorWithStepIds("w1", "step-one", "step-two", "step-three");

        FailureAction failureAction = new FailureAction()
                .name("on-failure")
                .type("retry")
                .stepId("step-dummy")
                .retryAfter(1.5)
                .retryLimit(3);

        assertEquals(1, v.validateFailureAction("w1", "step-one", failureAction).size());
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
        String componentName = "user";

        Components components = new Components();
        components.addParameter("param1", parameter);

        assertEquals(1, validator.validateParameter(parameter, null, componentName).size());
    }

    @Test
    void validateComponentsParameter() {
        Parameter parameter = new Parameter()
                .name("page")
                .value("1")
                .in("query");
        String componentName = "user";

        Components components = new Components();
        components.addParameter("page", parameter);

        assertEquals(0, validator.validateParameter(parameter, null, componentName).size());
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
                        .type("arazzo")
        );

        assertEquals(2, new OpenAPIWorkflowValidator().getNumOpenApiSourceDescriptions(sourceDescriptions));
    }

    @Test
    public void getNumArazzoTypeSourceDescriptions() {
        List<SourceDescription> sourceDescriptions = List.of(
                new SourceDescription()
                        .name("openapifile-1")
                        .type("openapi"),
                new SourceDescription()
                        .name("openapifile-2")
                        .type("openapi"),
                new SourceDescription()
                        .name("workflowspec-1")
                        .type("arazzo")
        );

        assertEquals(1, new OpenAPIWorkflowValidator().getNumArazzoTypeSourceDescriptions(sourceDescriptions));
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
        boolean multipleArazzoTypeFiles = true;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .workflowId("w2")
        );

        assertEquals(1, new OpenAPIWorkflowValidator().validateStepsWorkflowIds(steps, multipleArazzoTypeFiles).size());
    }

    @Test
    public void validateStepsWorkflowIdsWithoutRuntimeExpression() {
        boolean multipleArazzoTypeFiles = false;

        List<Step> steps = List.of(
                new Step()
                        .stepId("step-one")
                        .workflowId("w2")
        );

        assertEquals(0, new OpenAPIWorkflowValidator().validateStepsWorkflowIds(steps, multipleArazzoTypeFiles).size());
    }

    @Test
    void stepExists() {
        assertTrue(validatorWithStepIds("w1", "step-one", "step-two", "step-three").stepExists("w1", "step-one"));
    }

    @Test
    void stepNotFound() {
        assertFalse(validatorWithStepIds("w1", "step-one", "step-two", "step-three").stepExists("w1", "step-dummy"));
    }

    @Test
    void workflowExists() {
        assertTrue(validatorWithWorkflowIds("w1").workflowExists("w1"));
    }

    @Test
    void workflowNotFound() {
        assertFalse(validatorWithWorkflowIds("w1").workflowExists("dummy"));
    }

    @Test
    void validWorkflowId() {
        assertTrue(new OpenAPIWorkflowValidator().isValidWorkflowId("idOfTheWorkflow_1"));
    }

    @Test
    void validWorkflowIdWithHyphen() {
        assertTrue(new OpenAPIWorkflowValidator().isValidWorkflowId("workflow-id-1"));
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


    // --- New feature tests ---

    @Test
    void validateArazzoVersionValid() {
        assertTrue(new OpenAPIWorkflowValidator().isValidArazzoVersion("1.0.0"));
    }

    @Test
    void validateArazzoVersionValidWithPrerelease() {
        assertTrue(new OpenAPIWorkflowValidator().isValidArazzoVersion("1.0.1-beta"));
    }

    @Test
    void validateArazzoVersionInvalid() {
        assertFalse(new OpenAPIWorkflowValidator().isValidArazzoVersion("2.0.0"));
    }

    @Test
    void validateArazzoVersionInvalidFormat() {
        assertFalse(new OpenAPIWorkflowValidator().isValidArazzoVersion("1.0"));
    }

    @Test
    void validateSourceDescriptionNameValid() {
        assertTrue(new OpenAPIWorkflowValidator().isValidSourceDescriptionName("my-source_1"));
    }

    @Test
    void validateSourceDescriptionNameInvalidWithSpace() {
        assertFalse(new OpenAPIWorkflowValidator().isValidSourceDescriptionName("my source"));
    }

    @Test
    void validateSourceDescriptionNameInvalidWithDot() {
        assertFalse(new OpenAPIWorkflowValidator().isValidSourceDescriptionName("my.source"));
    }

    @Test
    void validateSourceDescriptionInvalidName() {
        List<SourceDescription> sourceDescriptions = new ArrayList<>();
        sourceDescriptions.add(new SourceDescription()
                .name("invalid name")
                .type("openapi")
                .url("https://example.com/spec.json"));
        assertEquals(1, validator.validateSourceDescriptions(sourceDescriptions).size());
    }

    @Test
    void validateSuccessActionMissingName() {
        SuccessAction successAction = new SuccessAction()
                .type("end");

        assertEquals(1, validator.validateSuccessAction("w1", "step-one", successAction).size());
    }

    @Test
    void validateFailureActionMissingName() {
        FailureAction failureAction = new FailureAction()
                .type("end");

        assertEquals(1, validator.validateFailureAction("w1", "step-one", failureAction).size());
    }

    @Test
    void validateWorkflowDependsOnValid() {
        OpenAPIWorkflowValidator v = new OpenAPIWorkflowValidator();
        v.workflowIds.add("w1");
        v.workflowIds.add("w2");

        Workflow workflow = new Workflow()
                .workflowId("w2")
                .addStep(new Step().stepId("step-one").operationId("op"));
        workflow.setDependsOn(List.of("w1"));

        assertEquals(0, v.validateWorkflow(workflow, 0).size());
    }

    @Test
    void validateWorkflowDependsOnInvalid() {
        OpenAPIWorkflowValidator v = new OpenAPIWorkflowValidator();
        v.workflowIds.add("w1");

        Workflow workflow = new Workflow()
                .workflowId("w1")
                .addStep(new Step().stepId("step-one").operationId("op"));
        workflow.setDependsOn(List.of("non-existent-workflow"));

        assertEquals(1, v.validateWorkflow(workflow, 0).size());
    }

    @Test
    void validateCriterionWithExpressionType() {
        Criterion criterion = new Criterion()
                .condition("$.petId")
                .context("$response.body")
                .expressionType(new CriterionExpressionType()
                        .type("jsonpath")
                        .version("draft-goessner-dispatch-jsonpath-00"));

        assertEquals(0, validator.validateCriterion(criterion, "step-one").size());
    }

    @Test
    void validateCriterionExpressionTypeMissingVersion() {
        Criterion criterion = new Criterion()
                .condition("$.petId")
                .context("$response.body")
                .expressionType(new CriterionExpressionType()
                        .type("jsonpath"));

        assertEquals(1, validator.validateCriterion(criterion, "step-one").size());
    }

    @Test
    void validateCriterionExpressionTypeInvalidType() {
        Criterion criterion = new Criterion()
                .condition("$.petId")
                .context("$response.body")
                .expressionType(new CriterionExpressionType()
                        .type("simple")
                        .version("draft-goessner-dispatch-jsonpath-00"));

        assertEquals(1, validator.validateCriterion(criterion, "step-one").size());
    }

    @Test
    void validateCriterionExpressionTypeMissingContext() {
        Criterion criterion = new Criterion()
                .condition("$.petId")
                .expressionType(new CriterionExpressionType()
                        .type("jsonpath")
                        .version("draft-goessner-dispatch-jsonpath-00"));

        assertEquals(1, validator.validateCriterion(criterion, "step-one").size());
    }

    @Test
    void validateComponentsSuccessActions() {
        Components components = new Components();
        components.getSuccessActions().put("onSuccess", new SuccessAction().name("onSuccess").type("end"));

        assertEquals(0, validator.validateComponents(components).size());
    }

    @Test
    void validateComponentsSuccessActionsInvalidKey() {
        Components components = new Components();
        components.getSuccessActions().put("on success", new SuccessAction().name("onSuccess").type("end"));

        assertEquals(1, validator.validateComponents(components).size());
    }

    @Test
    void validateComponentsFailureActions() {
        Components components = new Components();
        components.getFailureActions().put("onFailure", new FailureAction().name("onFailure").type("end"));

        assertEquals(0, validator.validateComponents(components).size());
    }

    @Test
    void validateComponentsFailureActionsInvalidKey() {
        Components components = new Components();
        components.getFailureActions().put("on failure", new FailureAction().name("onFailure").type("end"));

        assertEquals(1, validator.validateComponents(components).size());
    }

    @Test
    void parameterValueBoolean() {
        Parameter parameter = new Parameter()
                .name("flag")
                .value(true)
                .in("query");

        assertEquals(0, validator.validateParameter(parameter, "w1", null).size());
    }

    @Test
    void parameterValueNumber() {
        Parameter parameter = new Parameter()
                .name("page")
                .value(42)
                .in("query");

        assertEquals(0, validator.validateParameter(parameter, "w1", null).size());
    }

    @Test
    void isValidJsonPointer() {
        assertTrue(new OpenAPIWorkflowValidator().isValidJsonPointer("/user/id"));
    }

    @Test
    void invalidJsonPointer() {
        assertFalse(new OpenAPIWorkflowValidator().isValidJsonPointer("user/id"));
    }

    @Test
    void invalidJsonPointerSyntax() {
        assertFalse(new OpenAPIWorkflowValidator().isValidJsonPointer("#/petId"));
    }

}