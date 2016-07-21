package com.tts.zhanwai.scheduled;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.StartJob;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.utils.ClientParse;
import com.tts.zhanwai.utils.LogUtils;

@Component

public class ScheduledJob implements Job {
	private static Logger logger = LogUtils.getLogger(ScheduledJob.class);
	//private static final StartJob startJob = new StartJob();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
//		List<DownloadType> downloadTypes = ClientParse.parse();
//		for (DownloadType downloadType : downloadTypes) {
//			try {
//				startJob.startJob(downloadType);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

}
