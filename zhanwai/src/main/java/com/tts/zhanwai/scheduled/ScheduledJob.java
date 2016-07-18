package com.tts.zhanwai.scheduled;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.utils.LogUtils;

@Component
public class ScheduledJob implements Job {
	private static Logger logger = LogUtils.getLogger(ScheduledJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.error(new Date().toString());
		System.out.println(new Date().toString());
	}

}
