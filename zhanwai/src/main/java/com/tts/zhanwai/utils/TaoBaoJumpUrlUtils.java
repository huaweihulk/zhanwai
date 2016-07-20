package com.tts.zhanwai.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.parse.AbstractParse;

public class TaoBaoJumpUrlUtils {
	public static String transLateHtmlSpecialChars(String str) {
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&#039");
		return str;
	}

	/**
	 * 消除有些url链接中的特殊字符
	 * 
	 * @param url
	 * @return
	 */
	public static String decodeUrl(String url) {
		String realUrl = "";
		realUrl = URLDecoder.decode(url);
		return realUrl;
	}

}
