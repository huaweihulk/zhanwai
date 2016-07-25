package com.tts.zhanwai;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.executer.TaskExecuter;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.parse.AbstractParse;
import com.tts.zhanwai.scheduled.ScheduledTasks;
import com.tts.zhanwai.utils.ClientParse;

@SpringBootApplication
@EnableScheduling
public class BootApp implements CommandLineRunner {
	@Autowired
	private ScheduledTasks scheduledTasks;
	@Autowired
	private StartJob startJob;
	@Autowired
	private TaskExecuter taskExecuter;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(BootApp.class, args);
	}

	@Scheduled(cron = "0 */5 * * * ?")
	// @Scheduled(cron = "0 18 21 * * ?")
	public void startTask() {
		List<DownloadType> downloadTypes = ClientParse.parse();
		 taskExecuter.init();
		 taskExecuter.executThreads(downloadTypes);
//		for (DownloadType downloadType : downloadTypes) {
//			try {
//				startJob.startJob(downloadType);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void run(String... arg0) throws Exception {
		//startTask();
	}

}
