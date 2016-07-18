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
public class CategoryParse extends AbstractParse implements ParseHtml<Category> {
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
	public List<? extends Object> parseResponse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		this.response = res;
		List<Category> category = null;
		StringBuilder stringBuilder = new StringBuilder();
		for (Header hea : res.getAllHeaders()) {
			if (!StringUtils.isEmpty(hea.getName()) && hea.equals("Set-Cookie")) {
				stringBuilder.append(hea.getValue());
			}
		}
		header.put("cookie", stringBuilder.toString());
		HttpEntity entity = response.getEntity();
		InputStream is = null;
		BufferedReader bufferedReader = null;
		StringBuilder builder = new StringBuilder();
		if (entity != null) {
			try {
				is = entity.getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(is));
				String tmp = null;
				while ((tmp = bufferedReader.readLine()) != null) {
					builder.append(tmp);
				}
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			category = parseHtmlBody(builder.toString());
		}
		try {
			if (is != null) {
				is.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return category;
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
		parseResponse(res, header);
		startDownloadProductList(header);
	}
}