package com.tts.zhanwai.parse;

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
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class ZheBaBaiCategoryParse extends CategoryParse {
	private List<Category> zheBaBaiCategories = new ArrayList<Category>();
	private static Logger logger = LogUtils.getLogger(ZheBaBaiCategoryParse.class);
	private static String showTaoBaoOnly = "?taobao_only=1";
	@Autowired
	private ZheBaBaiProductListParse zheBaBaiProductListParse;
	@Autowired
	private ProductListDownloader productListDownloader;

	@Override
	public List<Category> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile(
				"<a href=\".+\"\\s*class=\"\\w*\"\\s*add_params=\"utm_content=float_top_nav\"><em>[\u4E00-\u9FA5]+</em></a>");
		Matcher matcher = pattern.matcher(html);
		String categoryName = null;
		String categoryUrl = null;
		while (matcher.find()) {
			String urlString = html.substring(matcher.start(), matcher.end());
			if (!urlString.contains("全部")) {
				Pattern categoryPattern = Pattern.compile("[\u4E00-\u9FA5]+");
				Matcher categorymatcher = categoryPattern.matcher(urlString);
				if (categorymatcher.find()) {
					categoryName = urlString.substring(categorymatcher.start(), categorymatcher.end());
				}
				categoryPattern = Pattern.compile("<a href=\".*?\"");
				categorymatcher = categoryPattern.matcher(urlString);
				if (categorymatcher.find()) {
					categoryUrl = urlString.substring(categorymatcher.start() + 9, categorymatcher.end() - 1);
					// slogger.info(categoryUrl);
				}
				zheBaBaiCategories.add(new Category(categoryName, categoryUrl));
			}
		}
		// logger.info("zhe800 parse {} catogry", zheBaBaiCategories.size());
		return zheBaBaiCategories;
	}

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
		for (Category category : zheBaBaiCategories) {
			DownloadType downloadType = productDownloadType(category, header);
			downloadType.setUrl(downloadType.getUrl() + showTaoBaoOnly);
			// logger.error(downloadType.getUrl());
			zheBaBaiProductListParse.setCategory(category);
			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			zheBaBaiProductListParse.startParse(httpResponse, header);
		}
		zheBaBaiCategories.clear();//必须清除，第二次调用的时候会重新整理类目，不清理可能会出现重复数据
	}
}
