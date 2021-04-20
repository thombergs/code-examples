package io.pratik.server;

import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * @author pratikdas
 *
 */
public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) throws Exception {
	      ServerSocket ssock = new ServerSocket(8080);
	      logger.info("Server Started. Listening on port 8080");
	      
	      while (true) {
	         new RequestProcessor(ssock).handleClientRequest();;
	      }		
	}
}
