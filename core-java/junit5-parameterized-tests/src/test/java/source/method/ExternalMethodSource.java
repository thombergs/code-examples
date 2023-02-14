package source.method;

import java.util.stream.Stream;

public class ExternalMethodSource {

	static Stream<String> checkExternalMethodSourceArgs() {
		return Stream.of("a1", "b2");
	}

}
