package com.apiflows.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilTest {

    HttpUtil httpUtil = new HttpUtil();

    @Test
    void call() {
        final String WORKFLOWS_SPEC_URL = "https://raw.githubusercontent.com/API-Flows/openapi-workflow-parser/main/src/test/resources/1.0.0/pet-coupons.arazzo.yaml";
        assertNotNull(httpUtil.call(WORKFLOWS_SPEC_URL));
    }

    @Test
    void isUrl() {
        final String WORKFLOWS_SPEC_URL = "https://raw.githubusercontent.com/API-Flows/openapi-workflow-parser/main/src/test/resources/1.0.0/pet-coupons.arazzo.yaml";
        assertTrue(httpUtil.isUrl(WORKFLOWS_SPEC_URL));
    }
}