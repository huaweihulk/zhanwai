package com.tts.zhanwai.scheduled;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduledConfig {
	private static Logger logger = LoggerFactory.getLogger(ScheduledConfig.class);
	@Value(value = "${corn.trigger}")
	private String cronString;

	@Bean(name = "scheduler")
	public Scheduler scheduler() {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		try {
			scheduler = schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scheduler;
	}

	@Bean(name = "cronTrigger")
	public CronTrigger cronTrigger() {
		logger.info("Scheduler config cron :{}", cronString);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronString))
				.build();
		return cronTrigger;
	}

	@Bean(name = "jobDetail")
	public JobDetail jobDetail() {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).build();
		return jobDetail;
	}
}
