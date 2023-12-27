package com.apiflows.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PathUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

    public String getFromFile(String filepath) {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
