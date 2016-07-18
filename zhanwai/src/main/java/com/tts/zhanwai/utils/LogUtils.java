package com.tts.zhanwai.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	public final static <T> Logger getLogger(Class<T> clazz) {
		return LoggerFactory.getLogger(clazz);
	}
}
