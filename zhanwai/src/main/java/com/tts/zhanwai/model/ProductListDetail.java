package com.tts.zhanwai.model;

import java.io.Serializable;

public class ProductListDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private float oPrice;
	private float nPrice;
	private float sales;
	private String shopType;
	private String activity;
	private String pictureUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getoPrice() {
		return oPrice;
	}

	public void setoPrice(float oPrice) {
		this.oPrice = oPrice;
	}

	public float getnPrice() {
		return nPrice;
	}

	public void setnPrice(float nPrice) {
		this.nPrice = nPrice;
	}

	public float getSales() {
		return sales;
	}

	public void setSales(float sales) {
		this.sales = sales;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String toString() {
		return "ProductListDetail [title=" + title + ", oPrice=" + oPrice + ", nPrice=" + nPrice + ", sales=" + sales
				+ ", shopType=" + shopType + ", activity=" + activity + ", pictureUrl=" + pictureUrl + ", detailUrl="
				+ detailUrl + ", productUrl=" + productUrl + "]";
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	private String detailUrl;
	private String productUrl;
}
