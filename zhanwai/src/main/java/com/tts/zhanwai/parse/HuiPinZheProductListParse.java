package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.ProductListDetail;

@Component
public class HuiPinZheProductListParse extends ProductListParse {
	private static String UrlHeader = "http://www.huipinzhe.com";
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
	
	public HuiPinZheProductListParse() {
		super();
	}

	public HuiPinZheProductListParse(CloseableHttpResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 单独解析界面，防止对包含多页面的获取
	 * 
	 * @param html
	 * @return
	 */
	public List<ProductListDetail> parseHtml(String html) {
		List<ProductListDetail> details = new ArrayList<ProductListDetail>();

		return details;
	}

	public Matcher pageMatcher(String html) {
		Pattern pagePattern = Pattern.compile("<a\\s+href=\"/\\w+\\?\\w+=\\d+\"\\s+class=\"\\s+\">\\d+</a>");
		Matcher matcher = pagePattern.matcher(html);
		return matcher;
	}

	public Matcher urlMatcher(String html) {
		Pattern urlMatcher = Pattern.compile("href=\"/\\w+");
		Matcher matcher = urlMatcher.matcher(html);
		return matcher;
	}

	public Matcher detailMatcher(String html) {
		Pattern pattern = Pattern.compile("");
		Matcher matcher = pattern.matcher(html);
		return null;
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		String page = null;
		Matcher pageMatcher = pageMatcher(html);
		Matcher urlMatcher = null;
		while (pageMatcher.find()) {
			page = pageMatcher.group(0);
			urlMatcher = urlMatcher(page);
			String url = page.substring(urlMatcher.start(), urlMatcher.end());
		}
		return super.parseHtmlBody(html);
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		parseResponse(res, header);
	}
}
