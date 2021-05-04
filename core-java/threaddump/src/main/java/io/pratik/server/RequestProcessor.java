/**
 * 
 */
package io.pratik.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author pratikdas
 *
 */
public class RequestProcessor  {
	private static final Logger logger = Logger.getLogger(RequestProcessor.class.getName());

	private ServerSocket serverSocket = null;

	public RequestProcessor(final ServerSocket ssock) {
		super();
		this.serverSocket = ssock;
	}


	public void handleClientRequest() throws IOException {
		try (Socket client = serverSocket.accept()) {
			logger.info("Processing request from client " + client.getInetAddress().getHostAddress());
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

			StringBuilder requestBuilder = new StringBuilder();
			String line;
			while (!(line = br.readLine()).isBlank()) {
				requestBuilder.append(line + "\r\n");
			}

			String request = requestBuilder.toString();
			String[] requestsLines = request.split("\r\n");
			String[] requestLine = requestsLines[0].split(" ");
			String method = requestLine[0];
			String path = requestLine[1];
			String version = requestLine[2];
			String host = requestsLines[1].split(" ")[1];

			List<String> headers = new ArrayList<>();
			for (int h = 2; h < requestsLines.length; h++) {
				String header = requestsLines[h];
				headers.add(header);
			}

			String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
					client.toString(), method, path, version, host, headers.toString());
			System.out.println(accessLog);

			String contentType = "application/json";
			String dummyContent = "{\"message\":\"This is a dummy server. I am healthy\"}";
			sendResponse(client, "200 OK", contentType, dummyContent.getBytes());
		} catch (Exception e) {
			logger.info("Error in processing "+e.getMessage());
    	}

	}

	private void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
		OutputStream clientOutput = client.getOutputStream();
		clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
		clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
		clientOutput.write("\r\n".getBytes());
		clientOutput.write(content);
		clientOutput.write("\r\n\r\n".getBytes());
		clientOutput.flush();
		client.close();
	}

}
