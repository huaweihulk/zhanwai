package com.tts.zhanwai.model;

public enum UrlType {
	CATEGORY("category"), PRODUCTLIST("productlist"), PRODUCTDETAIL("productdetail");
	private String urltype;

	private UrlType(String urltype) {
		this.urltype = urltype;
	}

	public String getUrltype() {
		return urltype;
	}

}
