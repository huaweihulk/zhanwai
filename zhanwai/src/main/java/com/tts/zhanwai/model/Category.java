package com.tts.zhanwai.model;

import java.io.Serializable;

public class Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String categoryName;
	private String categoryUrl;

	public Category(String categoryName, String categoryUrl) {
		super();
		this.categoryName = categoryName;
		this.categoryUrl = categoryUrl;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryUrl() {
		return categoryUrl;
	}

	public void setCategoryUr(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}

	@Override
	public String toString() {
		return "Category [categoryName=" + categoryName + ", categoryUrl=" + categoryUrl + "]";
	};

}
