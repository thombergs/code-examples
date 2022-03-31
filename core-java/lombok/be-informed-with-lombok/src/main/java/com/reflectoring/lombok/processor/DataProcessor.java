package com.reflectoring.lombok.processor;

import java.nio.file.Path;
import java.util.List;

public interface DataProcessor {
    void dataProcess();
    List<String> readFromFile(Path path);
}
