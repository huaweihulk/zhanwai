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
import org.springframework.util.StringUtils;

public abstract class AbstractParse {

	public abstract String parseResponse(CloseableHttpResponse response, Map<String, String> header);

	public abstract List<? extends Object> parseHtmlBody(String htmlbody);

	public abstract void startParse(CloseableHttpResponse res, Map<String, String> header);
}
