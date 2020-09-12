package io.reflectoring.fileswithstreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class FilesWithStreams {
	public static final String fileName = "src/main/resources/paths.properties";
	static String folderPath = "src/main/resources/books";
	static String filePath = "src/main/resources/books/bookIndex.txt";
	static String jarFile = "src/main/resources/books/books.zip";

	public static void main(String[] args) throws IOException {
		// processWithStream();
		// readLineByLineUsingFiles();
		readLineByLineUsingBufferedReader();
		readFilterCountUsingFiles();
		// printJarFileContents();
		// printMatchingJarEntries();
		// readWithParallelStreamAndPrint();
	}

	static void processWithStream() {
		List<String> cities = Arrays.asList("London", "Sydney", "Colombo", "Cairo", "Beijing");
		cities.stream().filter(a -> a.startsWith("C")).map(String::toUpperCase).sorted().forEach(System.out::println);

	}

	static void readLineByLineUsingFiles() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(filePath))) {
			lines.forEach(System.out::println);
		}
	}

	static void readLineByLineUsingBufferedReader() throws IOException {
		try (Stream<String> lines = (Files.newBufferedReader(Paths.get(filePath)).lines())) {
			lines.forEach(System.out::println);
		}
	}
	
	static void readFilterCountUsingFiles() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(filePath))) {	
			long i = lines.filter(line -> line.startsWith("A")).count();
			System.out.println("Count of lines starting with 'A' is " + i);

		}
	}

	static void printJarFileContents() throws IOException {
		try (JarFile jFile = new JarFile(jarFile)) {
			jFile.stream().forEach(file -> System.out.println(file));

		}
	}

	static void printMatchingJarEntries() throws IOException {
		try (JarFile jFile = new JarFile(jarFile)) {
			Optional<JarEntry> searchResult = jFile.stream().filter(file -> file.getName().contains("Matilda"))
					.findAny();
			System.out.println(searchResult.get());

		}
	}

	static void readWithParallelStreamAndPrint() throws IOException {

		List<String> lines = Files.readAllLines(Path.of(filePath));
		try (Stream<String> stream = lines.parallelStream()) {
			stream.forEach(System.out::println);
		}
	}

}
