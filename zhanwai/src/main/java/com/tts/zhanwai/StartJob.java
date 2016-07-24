package com.tts.zhanwai;

import java.util.List;

import org.apache.bcel.generic.NEW;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.downloader.AbstractDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.UrlType;
import com.tts.zhanwai.parse.AbstractParse;
import com.tts.zhanwai.utils.ClientParse;
import com.tts.zhanwai.utils.SpringBeanUtils;

@Component
public class StartJob {
	// private AbstractDownloader downloader;
	// private AbstractParse parse;
	private DownloadType downloadType;
	private static final String paserClassPath = "com.tts.zhanwai.parse.";
	private static final String downloaderClassPath = "com.tts.zhanwai.downloader.";

	class DownloaderParse {
		private AbstractDownloader downloader;
		private AbstractParse parse;

		public DownloaderParse(AbstractDownloader downloader, AbstractParse parse) {
			super();
			this.downloader = downloader;
			this.parse = parse;
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

	}

	ThreadLocal<DownloaderParse> downloaderParseThreadLocal = new ThreadLocal<StartJob.DownloaderParse>();
	@Autowired
	private SpringBeanUtils springBeanUtils;

	public DownloadType getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(DownloadType downloadType) {
		this.downloadType = downloadType;
	}

	// public AbstractDownloader getDownloader() {
	// return downloader;
	// }
	//
	// public void setDownloader(AbstractDownloader downloader) {
	// this.downloader = downloader;
	// }
	//
	// public AbstractParse getParse() {
	// return parse;
	// }
	//
	// public void setParse(AbstractParse parse) {
	// this.parse = parse;
	// }

	public synchronized void initParam(DownloadType downloadType) throws ClassNotFoundException {
		AbstractDownloader downloader = null;
		AbstractParse parse = null;
		if (downloadType != null) {
			UrlType urlType = downloadType.getUrlType();
			String parseclass = downloadType.getPaserclass();
			switch (urlType) {
			case CATEGORY:
				downloader = (AbstractDownloader) springBeanUtils
						.getBean(Class.forName(downloaderClassPath + "CategoryDownloader"));
				break;
			case PRODUCTLIST:
				downloader = (AbstractDownloader) springBeanUtils
						.getBean(Class.forName(downloaderClassPath + "ProductListDownloader"));
			default:
				downloader = (AbstractDownloader) springBeanUtils
						.getBean(Class.forName(downloaderClassPath + "CategoryDownloader"));
				break;
			}
			parse = (AbstractParse) springBeanUtils.getBean(Class.forName(paserClassPath + parseclass));
			downloaderParseThreadLocal.set(new DownloaderParse(downloader, parse));
		}
	}

	public void startJob(DownloadType downloadType) throws ClassNotFoundException {
		initParam(downloadType);
		CloseableHttpResponse response = downloaderParseThreadLocal.get().getDownloader().startDownload(downloadType);
		downloaderParseThreadLocal.get().getParse().startParse(response,
				downloaderParseThreadLocal.get().getDownloader().header);
	}
}
