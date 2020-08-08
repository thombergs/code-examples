package io.reflectoring.beanlifecycle.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Create custom {@link SchedulerFactoryBean} for Quartz
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        AutowireCapableJobFactory jobFactory = new AutowireCapableJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory);

        return factoryBean;
    }

}
