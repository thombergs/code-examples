package io.reflectoring.beanlifecycle.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SampleJob implements Job {

    @Autowired
    private SampleServiceBean service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.hello();
    }
}