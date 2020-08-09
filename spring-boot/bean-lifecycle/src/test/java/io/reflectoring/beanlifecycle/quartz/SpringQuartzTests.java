package io.reflectoring.beanlifecycle.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringQuartzTests {

    @Autowired
    Scheduler quartzScheduler;

    @Test
    public void testQuartzJobHasCapabaleOfAutowireSpringBeans() throws SchedulerException, InterruptedException {

        JobDetail jobDetail = JobBuilder.newJob(SampleJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
                .startNow().build();
        quartzScheduler.scheduleJob(jobDetail, trigger);

        Thread.sleep(5000);

    }

}