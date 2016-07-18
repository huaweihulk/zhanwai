package com.tts.zhanwai.scheduled;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
	@Autowired
	public SchedulerFactory schedulerFactory;
	@Autowired
	public CronTrigger cronTrigger;
	@Autowired
	public JobDetail jobDetail;

	public void startScheduledTask() {
		try {
			System.out.println(jobDetail.getDescription());
			schedulerFactory.getScheduler().scheduleJob(jobDetail, cronTrigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
