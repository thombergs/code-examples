package io.reflectoring.fileswithstreams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesWithStreams {
	
	static String folderPath = "src/main/resources/books";
	static String filePath = "src/main/resources/books/bookIndex.txt";
	static String csvPath = "src/main/resources/cakes.csv";
	static String utfFilePath = "src/main/resources/input.txt";
	static String jarFile = "src/main/resources/books.zip";

	public static void main(String[] args) throws IOException {
		System.out.println("-------------------Files with Streams------------------ ");
		
		System.out.println("-------------------Introductory Example------------------ ");

		processWithStream();

		System.out.println(
				"\r\n" + "-------------------Example 1 - Reading line by line from a file------------------ " + "\r\n");

		readLineByLineUsingFiles();

		System.out.println(
				"\r\n" + "-------------------Example 2 - Reading with Buffered Reader------------------" + "\r\n");

		readLineByLineUsingBufferedReader();

		System.out.println(
				"\r\n" + "-------------------Example 3 - Reading all lines from a file------------------ " + "\r\n");

		readAllLinesUsingFiles();

		System.out.println(
				"\r\n" + "...................Example 4 - Reading with parallel streams------------------ " + "\r\n");

		readWithParallelStreamAndPrint();

		System.out.println(
				"\r\n" + "-------------------Example 5 - Reading UTF-encoded file------------------ " + "\r\n");

		readUtfEncodedFile();

		System.out.println(
				"\r\n" + "-------------------Example 6 - Reading, Filtering and Counting------------------ " + "\r\n");

		readFilterCountFromFile();

		System.out.println("\r\n" + "-------------------Example 7 - Splitting Words------------------ " + "\r\n");

		splitWordsFromFile();

		System.out.println("\r\n" + "-------------------Example 8 - Loading from CSV - ------------------ " + "\r\n");

		loadItemsFromCsvFile();

		System.out.println("\r\n" + "...................Example 9 - Listing Directories------------------ " + "\r\n");

		listDirectories();

		System.out
				.println("\r\n" + "...................Example 10 - Listing Regular Files------------------ " + "\r\n");

		listRegularFiles();

		System.out.println(
				"\r\n" + "...................Example 11 - Walking Files Recursively------------------ " + "\r\n");

		walkFilesRecursively();

		System.out.println("\r\n" + "...................Example 12 - Finding Files------------------ " + "\r\n");

		findFiles();

		System.out.println(
				"\r\n" + "...................Example 13 - Printing JAR fie contents------------------ " + "\r\n");

		printJarFileContents();

		System.out.println("...................Example 14 - Printing Matching JAR entries------------------ " + "\r\n");

		printMatchingJarEntries();

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

	static void readAllLinesUsingFiles() throws IOException {
		List<String> strList = Files.readAllLines(Path.of(filePath));
		Stream<String> lines = strList.stream();
		lines.forEach(System.out::println);
	}

	static void readUtfEncodedFile() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(utfFilePath), StandardCharsets.UTF_8)) {
			lines.forEach(System.out::println);
		}
	}

	static void loadItemsFromCsvFile() throws IOException {
		Pattern pattern = Pattern.compile(",");
		try (Stream<String> lines = Files.lines(Path.of(csvPath))) {
			List<Cake> cakes = lines.skip(1).map(line -> {
				String[] arr = pattern.split(line);
				return new Cake(Integer.parseInt(arr[0]), arr[1], Integer.parseInt(arr[2]));
			}).collect(Collectors.toList());
			cakes.forEach(System.out::println);
		}
	}

	static void readFilterCountFromFile() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(filePath))) {
			long i = lines.filter(line -> line.startsWith("A")).count();
			System.out.println("The count of lines starting with 'A' is " + i);

		}
	}

	static void splitWordsFromFile() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(filePath))) {
			Stream<String> words = lines.flatMap(line -> Stream.of(line.split("\\W+")));
			Set<String> wordSet = words.collect(Collectors.toSet());
			System.out.println(wordSet);
		}
	}

	static void listDirectories() throws IOException {
		try (Stream<Path> paths = Files.list(Path.of(folderPath))) {
			paths.filter(Files::isDirectory).forEach(System.out::println);
		}
	}

	static void listRegularFiles() throws IOException {
		try (Stream<Path> paths = Files.list(Path.of(folderPath))) {
			paths.filter(Files::isRegularFile).forEach(System.out::println);
		}
	}

	static void walkFilesRecursively() throws IOException {
		try (Stream<Path> stream = Files.walk(Path.of(folderPath))) {
			stream.filter(Files::isRegularFile).forEach(System.out::println);
		}
	}

	static void findFiles() throws IOException {
		int depth = Integer.MAX_VALUE;
		try (Stream<Path> paths = Files.find(Path.of(folderPath), depth, (path, attr) -> {
			return attr.isRegularFile() && path.toString().endsWith(".pdf");
		})) {

			paths.forEach(System.out::println);

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
		try (Stream<String> lines = (Files.lines(Path.of(filePath)).parallel())) {
			lines.forEach(System.out::println);
		}
	}

}
