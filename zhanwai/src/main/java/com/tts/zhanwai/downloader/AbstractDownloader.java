package com.tts.zhanwai.downloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.utils.Constants;
import com.tts.zhanwai.utils.LogUtils;

@Component
public abstract class AbstractDownloader {
	@Autowired
	private HttpClientsConnectionPoolManager poolManager;
	@Autowired
	private HttpClientRequestExceptionHandler requestHandler;
	private DownloadType dowload;
	private Logger logger = LogUtils.getLogger(AbstractDownloader.class);
	public Map<String, String> header = new HashMap<String, String>();

	protected CloseableHttpResponse productBody(DownloadType downloadType) {
		// TODO Auto-generated method stub
		this.dowload = downloadType;
		header.put("cookie", downloadType.getCookie());
		header.put("referer", downloadType.getReferer());
		header.put("user_agent", downloadType.getUser_agent());
		poolManager.setDefaultMaxPerRoute(30);
		poolManager.setMaxTotal(200);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolManager)
				.setRetryHandler(requestHandler).build();
		HttpRequestBase request;
		switch (dowload.getMethod()) {
		case GET:
			request = new HttpGet(dowload.getUrl());
			break;
		case POST:
			request = new HttpGet(dowload.getUrl());
			break;
		default:
			request = new HttpGet(dowload.getUrl());
			break;
		}
		request.setHeader(Constants.COOKIE, dowload.getCookie());
		request.setHeader(Constants.REFERE, dowload.getReferer());
		request.setHeader(Constants.USER_AGETNT, dowload.getUser_agent());
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public abstract CloseableHttpResponse startDownload(DownloadType downloadType);
}
