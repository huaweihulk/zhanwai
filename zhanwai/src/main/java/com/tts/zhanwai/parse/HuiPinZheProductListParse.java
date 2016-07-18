package com.tts.zhanwai.parse;

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.tts.zhanwai.model.ProductListDetail;

public class HuiPinZheProductListParse extends ProductListParse {

	public HuiPinZheProductListParse(CloseableHttpResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		return super.parseHtmlBody(html);
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		super.startParse(res, header);
	}
	@Override
	public List<? extends Object> parseResponse(CloseableHttpResponse response, Map<String, String> header) {
		// TODO Auto-generated method stub
		return super.parseResponse(response, header);
	}
}
