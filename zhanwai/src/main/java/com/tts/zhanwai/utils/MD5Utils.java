package com.tts.zhanwai.utils;

import org.springframework.util.DigestUtils;

public class MD5Utils {

	public static final String EncodeMd5Hex(String md5Code) {
		return DigestUtils.md5DigestAsHex(md5Code.getBytes());
	}
}
