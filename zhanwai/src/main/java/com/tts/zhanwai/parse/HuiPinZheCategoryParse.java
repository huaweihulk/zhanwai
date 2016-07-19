package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.cj.core.util.StringUtils;
import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.model.Category;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class HuiPinZheCategoryParse extends CategoryParse {
	private static String UrlHeader = "http://www.huipinzhe.com";
	private static Logger logger = LogUtils.getLogger(HuiPinZheCategoryParse.class);
	private List<Category> categories = new ArrayList<Category>();
	@Autowired
	public ProductListDownloader productListDownloader;
	@Autowired
	public HuiPinZheProductListParse huiPinZheProductListParse;

	public HuiPinZheCategoryParse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 覆写类完成对应的解析,这个方案必须被复写完成对应的解析
	 */
	@Override
	public List<Category> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		Pattern htmlPattern = Pattern
				.compile("\\s*<li><a href=\"(/\\w+)+\"\\s*target=\"\\w+\"\\s*>[\u4E00-\u9FA5]{2}</a></li>");
		Matcher htmlMatcher = htmlPattern.matcher(html);
		Pattern categoryPattern = Pattern.compile("[\u4E00-\u9FA5]{2}");
		Pattern categoryUrl = Pattern.compile("href=\"/\\w+");
		Matcher lineMatcher = null;
		while (htmlMatcher.find()) {
			String line = html.substring(htmlMatcher.start(), htmlMatcher.end());
			// logger.warn(line);
			if (!StringUtils.isNullOrEmpty(line) && !line.contains("全部")) {
				lineMatcher = categoryPattern.matcher(line);
				String categoryStr = null;
				String categoryStrUrl = null;
				if (lineMatcher.find()) {
					categoryStr = line.substring(lineMatcher.start(), lineMatcher.end());
				}
				lineMatcher = categoryUrl.matcher(line);
				if (lineMatcher.find()) {
					categoryStrUrl = UrlHeader + line.substring(lineMatcher.start() + 6, lineMatcher.end());
				}
				Category category = new Category(categoryStr, categoryStrUrl);
				categories.add(category);
			}
		}
		logger.info("huipinzhe parse {} catogry", categories.size());
		return categories;
	}

	/**
	 * 开始从分类解析产品列表
	 */
	@Override
	public List<ProductListDetail> startDownloadProductList(Map<String, String> header) {
		// TODO Auto-generated method stub
		List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
		for (Category category : categories) {
			DownloadType downloadType = new DownloadType();
			downloadType.setMethod(Method.GET);
			downloadType.setUrl(category.getCategoryUrl());
			downloadType.setUser_agent(header.get("user_agent"));
			downloadType.setCookie(header.get("cookie"));
			downloadType.setReferer(header.get("referer"));
			System.out.println(downloadType.getUrl());
			huiPinZheProductListParse.setCategory(category.getCategoryName());
			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			huiPinZheProductListParse.startParse(httpResponse, header);
		}
		return productListDetails;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		super.startParse(res, header);
	}
}
