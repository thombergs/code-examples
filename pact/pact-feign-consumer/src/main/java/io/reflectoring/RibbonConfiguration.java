package io.reflectoring;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;

public class RibbonConfiguration {

	@Bean
	public IRule ribbonRule(IClientConfig config) {
		return new RandomRule();
	}

}
