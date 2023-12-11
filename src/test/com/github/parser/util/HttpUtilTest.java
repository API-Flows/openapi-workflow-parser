package com.github.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilTest {

    HttpUtil httpUtil = new HttpUtil();

    @Test
    void call() {
        final String WORKFLOWS_SPEC_URL = "https://github.com/OAI/sig-workflows/blob/main/examples/1.0.0/pet-coupons.workflow.yaml";
        assertNotNull(httpUtil.call(WORKFLOWS_SPEC_URL));
    }

    @Test
    void isUrl() {
        final String WORKFLOWS_SPEC_URL = "https://github.com/OAI/sig-workflows/blob/main/examples/1.0.0/pet-coupons.workflow.yaml";
        assertTrue(httpUtil.isUrl(WORKFLOWS_SPEC_URL));
    }
}