package com.tts.zhanwai.mapper;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.utils.LogUtils;

@Component
public class ZhanWaiProductProvider {
	private static Logger logger = LogUtils.getLogger(ZhanWaiProductProvider.class);

	public String insertZhanWaiProduct(ZhanWaiProduct product) {
			return new SQL().INSERT_INTO("t_shop_product_zhanwai").VALUES("md5", product.getMd5())
					.VALUES("website", product.getWebsite()).VALUES("url", product.getUrl())
					.VALUES("spid", String.valueOf(product.getSpid()))
					.VALUES("price", String.valueOf(product.getPrice())).VALUES("ctime", product.getCtime().toString())
					.toString();
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
}
