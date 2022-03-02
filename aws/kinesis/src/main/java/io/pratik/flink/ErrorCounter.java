package io.pratik.flink;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import io.pratik.Constants;
import io.pratik.models.LogRecord;
import software.amazon.kinesis.connectors.flink.FlinkKinesisProducer;
import software.amazon.kinesis.connectors.flink.config.ConsumerConfigConstants;

public class ErrorCounter {
	private final static Logger logger = Logger.getLogger(ErrorCounter.class.getName());
	private static final String FILE_PATH = "/Users/pratikdas/eclipse-workspace/kinesisexamples/src/main/resources/apache_access_log";

	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<String> inputStream = createSource(env);

		DataStream<LogRecord> logRecords = inputStream.flatMap(new FlatMapFunction<String, LogRecord>() {

			@Override
			public void flatMap(String value, Collector<LogRecord> out) throws Exception {

				String[] parts = value.split("\\s+");

				LogRecord record = new LogRecord();
				record.setIp(parts[0]);
				record.setHttpStatus(parts[8]);
				record.setUrl(parts[6]);

				out.collect(record);

			}

		});

		DataStream<LogRecord> errorRecords = logRecords.filter(new FilterFunction<LogRecord>() {

			@Override
			public boolean filter(LogRecord value) throws Exception {
				boolean matched = !value.getHttpStatus().equalsIgnoreCase("200");

				return matched;
			}
		});

		DataStream<String> keyedStream = errorRecords.keyBy(value -> value.getIp()).flatMap(new FlatMapFunction<LogRecord, String>() {

			@Override
			public void flatMap(LogRecord value, Collector<String> out) throws Exception {
				out.collect(value.getUrl()+"::" + value.getHttpStatus());
			}
		});

		//TODO Uncomment this code for deploying to Kinesis Data Analytics
		
		// keyedStream.addSink(createSink());
		
		keyedStream.print();

		env.execute("Error alerts");

	}

	/*private static void createSink(final StreamExecutionEnvironment env, DataStream<LogRecord> input) {
		input.print();
	}*/

	private static DataStream<String> createSource(final StreamExecutionEnvironment env) {
		return env.readTextFile(
				FILE_PATH);
	}

	//TODO Uncomment this code for deploying to Kinesis Data Analytics
	/*private static DataStream<String> createSource(final StreamExecutionEnvironment env) {
		Properties inputProperties = new Properties();
		inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Constants.AWS_REGION.toString());
		inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");

		String inputStreamName = "in-app-log-stream";
		return env.addSource(new FlinkKinesisConsumer<>(inputStreamName, new SimpleStringSchema(), inputProperties));
	}*/

	private static FlinkKinesisProducer<String> createSink() {
		Properties outputProperties = new Properties();
		outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Constants.AWS_REGION.toString());

		FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<>(new SimpleStringSchema(), outputProperties);
		String outputStreamName = "log_data_stream";
		sink.setDefaultStream(outputStreamName);
		sink.setDefaultPartition("0");

		return sink;
	}



	private File getFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);

		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}
	}
}
