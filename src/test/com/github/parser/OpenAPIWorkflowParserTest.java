package com.github.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenAPIWorkflowParserTest {

    private OpenAPIWorkflowParser parser = new OpenAPIWorkflowParser();

    @Test
    void isUrl() {
        final String WORKFLOWS_SPEC_URL = "https://github.com/OAI/sig-workflows/blob/main/examples/1.0.0/pet-coupons.workflow.yaml";
        assertTrue(parser.isUrl(WORKFLOWS_SPEC_URL));
    }

    @Test
    void getFromFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.workflow.yaml";
        assertFalse(parser.getFromFile(WORKFLOWS_SPEC_FILE).isEmpty());
    }

    @Test
    void getFromUrl() {
        final String WORKFLOWS_SPEC_FILE = "https://raw.githubusercontent.com/OAI/sig-workflows/main/examples/1.0.0/pet-coupons.workflow.yaml";
        assertFalse(parser.getFromUrl(WORKFLOWS_SPEC_FILE).isEmpty());
    }

    @Test
    void parse() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertNotNull(result.getOpenAPIWorkflow());
        assertEquals("1.0.0", result.getOpenAPIWorkflow().getWorkflowsSpec());
        assertNotNull(result.getOpenAPIWorkflow().getInfo());
        assertEquals("Petstore - Apply Coupons", result.getOpenAPIWorkflow().getInfo().getTitle());
        assertNotNull(result.getOpenAPIWorkflow().getComponents());
    }

    @Test
    void parseFromUrl() {
        final String WORKFLOWS_SPEC_FILE = "https://raw.githubusercontent.com/OAI/sig-workflows/main/examples/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = parser.parse(WORKFLOWS_SPEC_FILE);
        assertNotNull(result.getOpenAPIWorkflow());
        assertEquals("1.0.0", result.getOpenAPIWorkflow().getWorkflowsSpec());
        assertNotNull(result.getOpenAPIWorkflow().getInfo());
        assertEquals("Petstore - Apply Coupons", result.getOpenAPIWorkflow().getInfo().getTitle());
        assertNotNull(result.getOpenAPIWorkflow().getComponents());
    }

}