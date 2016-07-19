package com.tts.zhanwai.scheduled;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.utils.LogUtils;

@Component
public class ScheduledTasks {
	public static Logger logger = LogUtils.getLogger(ScheduledTasks.class);
	@Autowired
	public Scheduler scheduler;
	@Autowired
	public CronTrigger cronTrigger;
	@Autowired
	public JobDetail jobDetail;

	public void startScheduledTask() {
		try {
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
			logger.error(cronTrigger.getNextFireTime().toString());
			logger.error("scheduler:{}", scheduler.getSchedulerName());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
