package com.tts.zhanwai.mapper;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

@Component
public class ZhanWaiProductProvider {
	public String insertZhanWaiProduct(ZhanWaiProduct product) {
		return new SQL().INSERT_INTO("t_shop_product_zhanwai").VALUES("md5", product.getMd5())
				.VALUES("website", product.getWebsite()).VALUES("url", product.getUrl())
				.VALUES("spid", String.valueOf(product.getSpid())).VALUES("price", String.valueOf(product.getPrice()))
				.VALUES("ctime", product.getCtime().toString()).toString();
	}

	public String insertBatchZhanwaiProducts(List<ZhanWaiProduct> zhanWaiProducts) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into t_shop_product_zhanwai(md5,website,url,spid,price,ctime) values ");
		for (ZhanWaiProduct product : zhanWaiProducts) {
			sb.append("('" + product.getMd5() + "','" + product.getWebsite() + "','" + product.getUrl() + "','"
					+ product.getSpid() + "','" + product.getPrice() + "','" + product.getCtime() + "'),");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
