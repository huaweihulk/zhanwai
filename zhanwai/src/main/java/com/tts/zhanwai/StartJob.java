package com.tts.zhanwai;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.downloader.AbstractDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.UrlType;
import com.tts.zhanwai.parse.AbstractParse;
import com.tts.zhanwai.utils.SpringBeanUtils;

@Component
public class StartJob {
	private AbstractDownloader downloader;
	private AbstractParse parse;
	private DownloadType downloadType;
	private static final String paserClassPath = "com.tts.zhanwai.parse.";
	private static final String downloaderClassPath = "com.tts.zhanwai.downloader.";
	@Autowired
	private SpringBeanUtils springBeanUtils;

	public DownloadType getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(DownloadType downloadType) {
		this.downloadType = downloadType;
	}

	public AbstractDownloader getDownloader() {
		return downloader;
	}

	public void setDownloader(AbstractDownloader downloader) {
		this.downloader = downloader;
	}

	public AbstractParse getParse() {
		return parse;
	}

	public void setParse(AbstractParse parse) {
		this.parse = parse;
	}

	public void initParam() throws ClassNotFoundException {
		if (downloadType != null) {
			UrlType urlType = downloadType.getUrlType();
			String parseclass = downloadType.getPaserclass();
			switch (urlType) {
			case CATEGORY:
				this.downloader = (AbstractDownloader) springBeanUtils
						.getBean(Class.forName(downloaderClassPath + "CategoryDownloader"));
				break;
			default:
				this.downloader = (AbstractDownloader) springBeanUtils
						.getBean(Class.forName(downloaderClassPath + "CategoryDownloader"));
				break;
			}
			this.parse = (AbstractParse) springBeanUtils.getBean(Class.forName(paserClassPath + parseclass));
		}
	}

	public void startJob(DownloadType downloadType) throws ClassNotFoundException {
		setDownloadType(downloadType);
		initParam();
		CloseableHttpResponse response = downloader.parseBody(downloadType);
		parse.startParse(response, downloader.header);
	}
}
