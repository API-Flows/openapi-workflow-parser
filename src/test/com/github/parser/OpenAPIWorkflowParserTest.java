package com.github.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenAPIWorkflowParserTest {

    private OpenAPIWorkflowParser parser = new OpenAPIWorkflowParser();

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