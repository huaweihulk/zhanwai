package com.tts.zhanwai.model;

import java.io.Serializable;

public class DownloadType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private UrlType urlType;
	private Method method;
	private String user_agent;
	private String referer;
	private String cookie;
	private String paserclass;

	public String getPaserclass() {
		return paserclass;
	}

	public void setPaserclass(String paserclass) {
		this.paserclass = paserclass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UrlType getUrlType() {
		return urlType;
	}

	public void setUrlType(UrlType urlType) {
		this.urlType = urlType;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
