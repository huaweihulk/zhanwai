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
import com.tts.zhanwai.model.ProductListDetail;

@Component
public class ProductListParse extends AbstractParse {
	private CloseableHttpResponse response;
	private String categoryName;
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ProductListParse() {
		super();
	}

	public ProductListParse(CloseableHttpResponse response) {
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
	public String parseResponse(CloseableHttpResponse response, Map<String, String> header) {
		// TODO Auto-generated method stub
		this.response = response;
		StringBuilder headerStringBuilder = new StringBuilder();
		for (Header hea : response.getAllHeaders()) {
			if (!StringUtils.isEmpty(hea.getName()) && hea.equals("Set-Cookie")) {
				headerStringBuilder.append(hea.getValue());
			}
		}
		header.put("cookie", headerStringBuilder.toString());
		return translateReponseHtml(response);
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		String html = parseResponse(res, header);
		parseHtmlBody(html);
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProductListDetail> parseHtml(String html) {
		return null;
	}
}
