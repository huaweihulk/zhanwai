package com.tts.zhanwai.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.LogUtils;
import com.tts.zhanwai.utils.MD5Utils;

@Component
public class ZhanWaiProductProvider {
	private static Logger logger = LogUtils.getLogger(ZhanWaiProductProvider.class);
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		logger.info(sb.toString());
		return sb.toString();
	}

	public String insertBatchProductListDetails(List<ProductListDetail> productListDetails, int start, int end) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into t_shop_product_zhanwai(md5,website,url,spid,price,ctime) values ");
		ProductListDetail product = null;
		for (int i = start; i < end; i++) {
			String date = simpleDateFormat.format(new Date());
			StringBuilder sbuilder = new StringBuilder();
			product = productListDetails.get(i);
			sbuilder.append(product.getWebsite()).append(product.getSpid()).append(date);
			sb.append("('" + MD5Utils.EncodeMd5Hex(date) + "','" + product.getWebsite() + "','" + product.getDetailUrl()
					+ "','" + product.getSpid() + "','" + product.getnPrice() * 100 + "','" + date + "'),");
		}
		sb.deleteCharAt(sb.length() - 1);
		// logger.info(sb.toString());
		return sb.toString();
	}
}
