package com.reflectoring.lombok.processor;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileDataProcessor implements DataProcessor {

    public static final Logger log = LoggerFactory.getLogger(FileDataProcessor.class);

    @Override
    @SneakyThrows
    public void dataProcess() {
        processFile();
    }

    @SneakyThrows(IOException.class)
    public List<String> readFromFile(Path path) {
        return Files.readAllLines(path);
    }

    private void processFile() throws IOException {
        File file = new File("sample.txt");
        log.info("Check if file exists: {}", file.exists());
        throw new IOException();
    }

}
