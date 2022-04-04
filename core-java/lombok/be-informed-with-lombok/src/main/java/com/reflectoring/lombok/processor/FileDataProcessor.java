package com.reflectoring.lombok.processor;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FileDataProcessor implements DataProcessor {

    public static final Logger log = LoggerFactory.getLogger(FileDataProcessor.class);

    @Override
    public void dataProcess() {
        String data = processFile();
        log.info("File data: {}", data);
        processData(data);
    }

    @SneakyThrows(IOException.class)
    public List<String> readFromFile(Path path) {
        return Files.readAllLines(path);
    }

    @SneakyThrows(IOException.class)
    private String processFile() {
        File file = new ClassPathResource("sample1.txt").getFile();
        log.info("Check if file exists: {}", file.exists());
        return FileUtils.readFileToString(file, "UTF-8");
    }

    @SneakyThrows(DateTimeParseException.class)
    private void processData(String data) {
        LocalDate localDt = LocalDate.parse(data);
        log.info("Date: {}", localDt);
    }

}
