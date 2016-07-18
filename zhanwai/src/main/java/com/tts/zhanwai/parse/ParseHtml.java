package com.tts.zhanwai.parse;

import java.util.List;

import com.tts.zhanwai.model.Category;

public interface ParseHtml<T> {
	public List<T> parseHtmlBody(String html);
}
