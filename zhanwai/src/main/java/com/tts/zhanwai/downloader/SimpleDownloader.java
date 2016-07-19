package com.tts.zhanwai.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.DownloadType;

@Component
public class SimpleDownloader extends AbstractDownloader {
	public CloseableHttpResponse getHttpRespon(DownloadType downloadType) {
		return parseBody(downloadType);
	}

	@Override
	public CloseableHttpResponse parseBody(DownloadType downloadType) {
		// TODO Auto-generated method stub
		return productBody(downloadType);
	}

}
