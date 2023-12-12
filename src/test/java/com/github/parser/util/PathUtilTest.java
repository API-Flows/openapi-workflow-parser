package com.github.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathUtilTest {

    PathUtil pathUtil = new PathUtil();

    @Test
    void getFromFile() {
        final String WORKFLOWS_SPEC_FILE = "src/test/java/resources/1.0.0/pet-coupons.workflow.yaml";
        assertFalse(pathUtil.getFromFile(WORKFLOWS_SPEC_FILE).isEmpty());
    }

}