package com.tts.zhanwai.model;

import java.io.Serializable;

public class ProductListDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;// 标题
	private float oPrice;// 原价
	private float nPrice;// 优惠价
	private float sales;// 折扣
	private String shopType;// 商店类型，天猫，淘宝等
	private String activity;// 站外显示活动
	private String pictureUrl;// 图像地址
	private String detailUrl;// 网站内的地址
	private String productUrl;// 天猫淘宝山品地址
	private String category;// 站外分类
	private long spid;//产品ID
	private String website;//产品的网站信息；
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public long getSpid() {
		return spid;
	}

	public void setSpid(long spid) {
		this.spid = spid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

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
				+ detailUrl + ", productUrl=" + productUrl + ", spid=" + spid + "]";
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
}
