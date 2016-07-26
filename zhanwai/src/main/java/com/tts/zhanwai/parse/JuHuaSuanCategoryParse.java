package com.tts.zhanwai.parse;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.model.Category;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class JuHuaSuanCategoryParse extends CategoryParse {
	private static final Logger logger = LogUtils.getLogger(CategoryParse.class);
	private List<Category> juHuaSuanCategories = new ArrayList<Category>();
	@Autowired
	private ProductListDownloader productListDownloader;
	@Autowired
	private JuHuaSuanProductListParse juHuaSuanProductListParse;

	@Override
	public void startDownloadProductList(Map<String, String> header) {
		// TODO Auto-generated method stub
		for (Category category : juHuaSuanCategories) {
			DownloadType downloadType = productDownloadType(category, header);
			downloadType.setUrl(downloadType.getUrl());
			// logger.error(downloadType.getUrl());
			juHuaSuanProductListParse.setCategory(category);
			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			juHuaSuanProductListParse.startParse(httpResponse, header);
		}
		juHuaSuanCategories.clear();// 必须清除，第二次调用的时候会重新整理类目，不清理可能会出现重复数据
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		String html = parseResponse(res, header);
		parseHtmlBody(html);
		startDownloadProductList(header);
	}

	@Override
	public List<Category> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			html = new String(html.getBytes(), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern htmlPattern = Pattern
				.compile("<a href=\".*?(\") class=\"J_cat[\\s\\S]+?(\">)[\\s]+?([\u4E00-\u9FA5]+)");
		Matcher htmlMatcher = htmlPattern.matcher(html);
		while (htmlMatcher.find()) {
			String url = null;
			String name = null;
			String body = html.substring(htmlMatcher.start(), htmlMatcher.end());
			Pattern bodyPattern = Pattern.compile("href=\".*?(\")");
			Matcher bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				url = body.substring(bodyMatcher.start() + 6, bodyMatcher.end() - 1);
			}
			bodyPattern = Pattern.compile("[\u4E00-\u9FA5]+");
			bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				name = body.substring(bodyMatcher.start(), bodyMatcher.end());
			}
			Category category = new Category(name, url);
			if (!juHuaSuanCategories.contains(category)) {
				juHuaSuanCategories.add(category);
				count++;
			}
		}
		//logger.info("JuHuaSuan parse {} categories", count);
		return super.parseHtmlBody(html);
	}
}
