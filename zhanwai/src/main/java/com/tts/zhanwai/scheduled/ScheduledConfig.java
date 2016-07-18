package com.tts.zhanwai.scheduled;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduledConfig {
	@Value(value = "${corn.trigger}")
	private String cronString;

	@Autowired
	private ScheduledJob scheduledJob;

	@Bean(name = "schedulerFactory")
	public SchedulerFactory schedulerFactory() {
		return new StdSchedulerFactory();
	}

	@Bean(name = "cronTrigger")
	public CronTrigger cronTrigger() {
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
