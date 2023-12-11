package com.github.parser.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    public String getFolderPath(String filepath) {
        Path filePath = Paths.get(filepath);

        return filePath.getParent().toString();
    }
}
