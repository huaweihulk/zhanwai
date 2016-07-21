package com.tts.zhanwai.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tts.zhanwai.model.Category;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.ProductListDetail;

@Component
public class CategoryParse extends AbstractParse {
	private CloseableHttpResponse response;

	public CategoryParse() {

	}

	public CategoryParse(CloseableHttpResponse response) {
		super();
		this.response = response;
	}

	public CloseableHttpResponse getResponse() {
		return response;
	}

	public void setResponse(CloseableHttpResponse response) {
		this.response = response;
	}

	@Override
	public String parseResponse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		this.response = res;
		StringBuilder headerStringBuilder = new StringBuilder();
		for (Header hea : res.getAllHeaders()) {
			if (!StringUtils.isEmpty(hea.getName()) && hea.equals("Set-Cookie")) {
				headerStringBuilder.append(hea.getValue());
			}
		}
		header.put("cookie", headerStringBuilder.toString());
		String htmlBody = translateReponseHtml(res);
		System.out.println(htmlBody.length());
		return htmlBody;
	}

	@Override
	public List<Category> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProductListDetail> startDownloadProductList(Map<String, String> header) {
		return null;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
	}

	protected DownloadType productDownloadType(Category category, Map<String, String> header) {
		DownloadType downloadType = new DownloadType();
		downloadType.setMethod(Method.GET);
		downloadType.setUrl(category.getCategoryUrl());
		downloadType.setUser_agent(header.get("user_agent"));
		downloadType.setCookie(header.get("cookie"));
		downloadType.setReferer(header.get("referer"));
		return downloadType;
	}
}
