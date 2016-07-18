package com.tts.zhanwai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.utils.ClientParse;

@SpringBootApplication
public class BootApp implements CommandLineRunner {
	@Autowired
	private StartJob startJob;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(BootApp.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub

		List<DownloadType> downloadTypes = ClientParse.parse();
		for (DownloadType downloadType : downloadTypes) {
			startJob.startJob(downloadType);
		}
	}

}
