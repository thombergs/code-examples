/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

/**
 * @author Pratik Das
 *
 */
@Component("FetchUsersAPI")
public class FetchUsersAPIHealthContributor 
    implements CompositeHealthContributor {
	private Map<String, HealthContributor> 
	        contributors = new LinkedHashMap<>();

	@Autowired
	public FetchUsersAPIHealthContributor(
			UrlShortenerServiceHealthIndicator 
			        urlShortenerServiceHealthContributor,
			DatabaseHealthContributor 
			        databaseHealthContributor) {
		super();
		// First check if URL shortener service is reachable with
		// Health Indicator of URL shortener service
		contributors.put("urlShortener", 
				urlShortenerServiceHealthContributor);
		// Check if USERS table used in the API can be queried with 
		//Health Indicator of Database
		contributors.put("database", 
				databaseHealthContributor);
	}

	/**
	 *  return list of health contributors
	 */
	@Override
	public Iterator<NamedContributor<HealthContributor>> 
	       iterator() {
             return contributors.entrySet().stream()
                .map((entry) -> 
                   NamedContributor.of(entry.getKey(), 
                		   entry.getValue())).iterator();
	}
	
	@Override
	public HealthContributor getContributor(String name) {
		return contributors.get(name);
	}


}
