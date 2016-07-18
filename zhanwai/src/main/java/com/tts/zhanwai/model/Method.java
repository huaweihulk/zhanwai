package com.tts.zhanwai.model;

public enum Method {
	GET("GET"), POST("POST");
	private String method;

	public String getMethod() {
		return method;
	}

	private Method(String method) {
		this.method = method;
	}

}
