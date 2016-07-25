package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.generic.NEW;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.model.Category;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class FanLiCategoryParse extends CategoryParse {
	private List<Category> fanLiCategories = new ArrayList<Category>();
	private static Logger logger = LogUtils.getLogger(FanLiCategoryParse.class);
	@Autowired
	private FanLiProductListDetailParse fanLiProductListDetailParse;
	@Autowired
	private ProductListDownloader productListDownloader;

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		String html = parseResponse(res, header);
		parseHtmlBody(html);
		startDownloadProductList(header);
	}

	@Override
	public void startDownloadProductList(Map<String, String> header) {
		// TODO Auto-generated method stub
		for (Category category : fanLiCategories) {
			DownloadType downloadType = productDownloadType(category, header);
			downloadType.setUrl(downloadType.getUrl());
			// logger.error(downloadType.getUrl());
			fanLiProductListDetailParse.setCategory(category);
			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			fanLiProductListDetailParse.startParse(httpResponse, header);
		}
		fanLiCategories.clear();
	}

	@Override
	public List<Category> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		Pattern categoryPattern = Pattern
				.compile("<a href=\"http://9.fanli.com/\\?cid=\\d+[\\s\\S]*?([\u4E00-\u9FA5]</a>)");
		Matcher categoryMatcher = categoryPattern.matcher(html);
		String categoryUrl = null;
		String categoryName = null;
		while (categoryMatcher.find()) {
			String body = html.substring(categoryMatcher.start(), categoryMatcher.end());
			Pattern bodyPattern = Pattern.compile("href=\"http.*?(\")");
			Matcher bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				categoryUrl = body.substring(bodyMatcher.start() + 6, bodyMatcher.end() - 1);
			}
			bodyPattern = Pattern.compile("[\u4E00-\u9FA5]+");
			bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				categoryName = body.substring(bodyMatcher.start(), bodyMatcher.end());
			}
			Category category = new Category(categoryName, categoryUrl);
			if (!category.getCategoryName().contains("全部") && !category.getCategoryName().contains("包邮")) {
				fanLiCategories.add(category);
			}
		}
		// logger.info("fanli parse {} catogry", fanLiCategories.size());
		return fanLiCategories;
	}

}
