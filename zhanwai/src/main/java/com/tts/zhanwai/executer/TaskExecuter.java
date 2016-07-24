package com.tts.zhanwai.executer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.StartJob;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class TaskExecuter {
	class TaskThread implements Runnable {
		private DownloadType downloadType;

		public TaskThread(DownloadType downloadType) {
			super();
			this.downloadType = downloadType;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				startJob.startJob(downloadType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Value(value = "${executerCount}")
	private String executerCount;
	private ThreadPoolExecutor threadPoolExecutor = null;
	private static final Logger logger = LogUtils.getLogger(TaskExecuter.class);
	@Autowired
	private StartJob startJob;

	public void init() {
		this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.valueOf(executerCount));
	}

	public void executThreads(List<DownloadType> downloadTypes) {
		int threadCount = 1;
		for (DownloadType downloadType : downloadTypes) {
			logger.info("Executer{} start!", threadCount);
			threadPoolExecutor.execute(new TaskThread(downloadType));
			threadCount++;
		}
		threadPoolExecutor.shutdown();
	}
}
