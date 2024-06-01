package com.apiflows.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathUtilTest {

    PathUtil pathUtil = new PathUtil();

    @Test
    void getFromFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.arazzo.yaml";
        assertFalse(pathUtil.getFromFile(WORKFLOWS_SPEC_FILE).isEmpty());
    }

    @Test
    void isFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/resources/1.0.0/pet-coupons.arazzo.yaml";
        assertTrue(pathUtil.isFile(WORKFLOWS_SPEC_FILE));
    }

    @Test
    void isFileFalse() {
        final String WORKFLOWS_SPEC_FILE = "{text}";
        assertFalse(pathUtil.isFile(WORKFLOWS_SPEC_FILE));
    }

}