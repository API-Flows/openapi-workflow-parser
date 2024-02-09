package com.apiflows.parser;

import com.apiflows.model.Step;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenAPIWorkflowParserTest {

    private OpenAPIWorkflowParser parser = new OpenAPIWorkflowParser();

    @Test
    void parseFromFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertNotNull(result.getOpenAPIWorkflow());
        assertEquals("1.0.0-prerelease", result.getOpenAPIWorkflow().getWorkflowsSpec());
        assertNotNull(result.getOpenAPIWorkflow().getInfo());
        assertEquals("Petstore - Apply Coupons", result.getOpenAPIWorkflow().getInfo().getTitle());
        assertNotNull(result.getOpenAPIWorkflow().getComponents());

        assertNotNull(result.getOpenAPIWorkflow().getWorkflows());
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0));
        // inputs
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getInputs());
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getInputs().get$ref());
        assertNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getInputs().getProperties());

    }

    @Test
    void parseFromUrl() {
        final String WORKFLOWS_SPEC_FILE = "https://raw.githubusercontent.com/API-Flows/openapi-workflow-parser/main/src/test/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertNotNull(result.getOpenAPIWorkflow());
        assertEquals("1.0.0", result.getOpenAPIWorkflow().getWorkflowsSpec());
        assertNotNull(result.getOpenAPIWorkflow().getInfo());
        assertEquals("Petstore - Apply Coupons", result.getOpenAPIWorkflow().getInfo().getTitle());
    }

    @Test
    void simpleWorkflow() {
        final String WORKFLOWS_SPEC_FILE = "./src/test/resources/1.0.0/simple.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertNotNull(result.getOpenAPIWorkflow());
        assertNotNull(result.getOpenAPIWorkflow().getInfo());
        assertEquals("simple", result.getOpenAPIWorkflow().getInfo().getTitle());

        assertNotNull(result.getOpenAPIWorkflow().getWorkflows());
        assertEquals(1, result.getOpenAPIWorkflow().getWorkflows().size());
        // 2 inputs
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getInputs());
        assertEquals(2, result.getOpenAPIWorkflow().getWorkflows().get(0).getInputs().getProperties().size());
        // 1 output
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getOutputs());
        assertEquals(1, result.getOpenAPIWorkflow().getWorkflows().get(0).getOutputs().size());
        // 1 step
        assertNotNull(result.getOpenAPIWorkflow().getWorkflows().get(0).getSteps());
        assertEquals(1, result.getOpenAPIWorkflow().getWorkflows().get(0).getSteps().size());
        Step step = result.getOpenAPIWorkflow().getWorkflows().get(0).getSteps().get(0);
        assertNotNull(step);
        assertEquals(2, step.getOutputs().size());
    }

    @Test
    void parseFromJsonFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/workflow.json";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertTrue(result.isJson());
        assertNotNull(result.getOpenAPIWorkflow());
        assertEquals("1.0.0", result.getOpenAPIWorkflow().getWorkflowsSpec());
    }

    @Test
    public void isYaml() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertTrue(result.isYaml());
        assertFalse(result.isJson());
    }

    @Test
    public void getJsonFormat() {
        final String CONTENT = "{" +
                "\"workflowsSpec\" : \"1.0.0\"" +
                "}";

        OpenAPIWorkflowParserResult.Format format = parser.getFormat(CONTENT);
        assertEquals(OpenAPIWorkflowParserResult.Format.JSON, format);
    }

    @Test
    public void getYamlFormat() {
        final String CONTENT = "" +
                "workflowsSpec : 1.0.0" +
                "info:" +
                "  title: simple\n";

        OpenAPIWorkflowParserResult.Format format = parser.getFormat(CONTENT);
        assertEquals(OpenAPIWorkflowParserResult.Format.YAML, format);
    }
}