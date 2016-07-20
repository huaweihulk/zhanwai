package com.tts.zhanwai.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.utils.Constants;

@Component
public class SimpleDownloader extends AbstractDownloader {
	public CloseableHttpResponse getHttpRespon(DownloadType downloadType) {
		return startDownload(downloadType);
	}

	@Override
	public CloseableHttpResponse startDownload(DownloadType downloadType) {
		// TODO Auto-generated method stub
		return productBody(downloadType);
	}

	public CloseableHttpResponse startDownload(String url) {
		DownloadType downloadType = new DownloadType();
		downloadType.setMethod(Method.GET);
		downloadType.setUrl(url);
		downloadType.setUser_agent(Constants.USER_AGETNT);
		return startDownload(downloadType);
	}
}
