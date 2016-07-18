package com.tts.zhanwai.parse;

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

public abstract class AbstractParse {

	public abstract List<? extends Object> parseResponse(CloseableHttpResponse response, Map<String, String> header);

	public abstract void startParse(CloseableHttpResponse res, Map<String, String> header);
}
