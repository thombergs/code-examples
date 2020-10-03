package io.reflectoring.componentscan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BeanViewer {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@EventListener
	public void showBeansRegistered(ApplicationReadyEvent event) {
		String[] beanNames = event.getApplicationContext().getBeanDefinitionNames();
		for(String beanName: beanNames) {
			LOG.info("{}", beanName);
		}
	}
}
