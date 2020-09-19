/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@Component
@Slf4j
public class UrlShortenerServiceHealthIndicator implements HealthIndicator, HealthContributor {

	private static final String URL = "https://cleanuri.com/api/v1/shorten";

	@Override
	public Health health() {
		// check if url shortener service url is reachable
        try {
            URL url = new URL(URL);
            int port = url.getPort();
            if (port == -1) {
                port = url.getDefaultPort();
            }

            try (Socket socket = new Socket(url.getHost(), port)) {
            } catch (IOException e) {
                log.warn("Failed to connect to : {}", URL);
                return Health.down().withDetail("error", e.getMessage()).build();
            }
        } catch (MalformedURLException e1) {
            log.warn("Invalid URL: {}",URL);
            return Health.down().withDetail("error", e1.getMessage()).build();
        }

        return Health.up().build();
	}

}
