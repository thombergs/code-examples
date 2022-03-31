package com.reflectoring.lombok.service;

import com.reflectoring.lombok.processor.DataProcessor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SneakyThrowsService {

    private final DataProcessor fileDataProcessor;

    public SneakyThrowsService(DataProcessor fileDataProcessor) {
        this.fileDataProcessor = fileDataProcessor;
    }

    public void getFileData() {
        fileDataProcessor.dataProcess();
    }

    // @SneakyThrows simplifies lambda usage
    public List<List<String>> readFileData() {
        List<String> paths = List.of("/file1", "/file2");
        return paths.stream()
                .map(Paths::get)
                .map(fileDataProcessor::readFromFile)
                .collect(Collectors.toList());
    }


}
