package com.tts.zhanwai.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.utils.LogUtils;

@Component
public class SimpleParseTaobaoId {
	private static Logger logger = LogUtils.getLogger(SimpleParseTaobaoId.class);

	public String parseResponse(CloseableHttpResponse response) {
		// logger.error(response.getStatusLine().toString());
		String htmlBody = AbstractParse.translateReponseHtml(response);
		return htmlBody;
	}

	public String parseTaobaoId(String htmlBody) {
		// logger.error("html length:", htmlBody.length());
		String taobaoId = "";
		Pattern pattern = Pattern.compile("itemId:\"\\d+");
		Matcher matcher = pattern.matcher(htmlBody);
		if (matcher.find()) {
			taobaoId = htmlBody.substring(matcher.start() + 8, matcher.end());
		}
		// logger.error("taobaoId:{}", taobaoId);
		return taobaoId;
	}

	public String parseTmallId(String htmlBody) {
		// logger.error("html length:", htmlBody.length());
		String tmallId = "";
		Pattern pattern = Pattern.compile("itemId\\s+:\\s{0,}'\\d+");
		Matcher matcher = pattern.matcher(htmlBody);
		if (matcher.find()) {
			tmallId = htmlBody.substring(matcher.start() + 8, matcher.end());
		}
		pattern = Pattern.compile("\\d+");
		matcher = pattern.matcher(tmallId);
		if (matcher.find()) {
			tmallId = tmallId.substring(matcher.start(), matcher.end());
		}
		// logger.error("taobaoId:{}", tmallId);
		return tmallId;
	}

	public String parseHuiPinZheRedictTaoBaoId(String html) {
		String result = "";
		Pattern pattern = Pattern.compile("itemid=\"\\d+\"");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			result = html.substring(matcher.start() + 8, matcher.end() - 1);
		}
		return result;
	}
}
