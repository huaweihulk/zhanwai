package com.tts.zhanwai.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.DownloadType;

@Component
public class CategoryDownloader extends AbstractDownloader {

	@Override
	public CloseableHttpResponse startDownload(DownloadType downloadType) {
		// TODO Auto-generated method stub
		return productBody(downloadType);
	}

}
