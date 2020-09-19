/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@Component("FetchUsersAPI")
@Slf4j
public class FetchUsersAPIHealthIndicator  implements HealthIndicator {

	private static final String URL = "https://cleanuri.com/api/v1/shorten";

	@Autowired
    private DataSource ds;
    
	@Override
	public Health health() {
		
		Health dbHealth = dbIsHealthy();
		Health serviceHealth = externalServiceIsHealthy();
		
		if(Status.UP.equals(dbHealth.getStatus())) {
			if(Status.UP.equals(serviceHealth.getStatus())) {
			   return Health.up().build();
			}else {
				return serviceHealth;
			}
		}else {
		    return dbHealth;
		}
	}
	
	private Health dbIsHealthy() {
		try(Connection conn = ds.getConnection();){
			Statement stmt = conn.createStatement();
			stmt.execute("select FIRST_NAME,LAST_NAME,MOBILE,EMAIL from USER");
		} catch (SQLException ex) {
			return Health.outOfService().withException(ex).build();
		}
		return Health.up().build();
	}
	
	private Health externalServiceIsHealthy() {
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
