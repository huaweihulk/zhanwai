package com.tts.zhanwai;

import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.parse.AbstractParse;
import com.tts.zhanwai.scheduled.ScheduledTasks;
import com.tts.zhanwai.utils.ClientParse;

@SpringBootApplication
@EnableScheduling
public class BootApp implements CommandLineRunner {
	@Autowired
	private StartJob startJob;
	@Autowired
	private ScheduledTasks scheduledTasks;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(BootApp.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		scheduledTasks.startScheduledTask();
		List<DownloadType> downloadTypes = ClientParse.parse();
		for (DownloadType downloadType : downloadTypes) {
			startJob.startJob(downloadType);
		}
	}

}
