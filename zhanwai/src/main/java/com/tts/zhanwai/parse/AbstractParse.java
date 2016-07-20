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

import com.mysql.cj.api.log.Log;

public abstract class AbstractParse {

	public abstract String parseResponse(CloseableHttpResponse response, Map<String, String> header);

	public abstract List<? extends Object> parseHtmlBody(String htmlbody);

	public abstract void startParse(CloseableHttpResponse res, Map<String, String> header);

	public static String translateReponseHtml(CloseableHttpResponse response) {
		HttpEntity entity = response.getEntity();
		InputStream is = null;
		BufferedReader bufferedReader = null;
		StringBuilder htmlStringBuilder = new StringBuilder();
		if (entity != null) {
			try {
				is = entity.getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(is));
				String tmp = null;
				while ((tmp = bufferedReader.readLine()) != null) {
					htmlStringBuilder.append(tmp);
					htmlStringBuilder.append("\n");
				}
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		return htmlStringBuilder.toString();
	}
}
