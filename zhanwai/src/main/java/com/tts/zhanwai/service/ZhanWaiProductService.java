package com.tts.zhanwai.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.cj.mysqlx.io.MessageWriter;
import com.tts.zhanwai.mapper.ZhanWaiProduct;
import com.tts.zhanwai.mapper.ZhanWaiProductMapper;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.MD5Utils;

@Component
public class ZhanWaiProductService {
	@Autowired
	private ZhanWaiProductMapper zhanWaiProductMapper;
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private ZhanWaiProduct zhanWaiProductTranslate(ProductListDetail productListDetail) {
		Date now = new Date();
		String date = simpleDateFormat.format(now);
		ZhanWaiProduct zhanWaiProduct = new ZhanWaiProduct();
		zhanWaiProduct.setPrice(String.valueOf(productListDetail.getnPrice()));
		zhanWaiProduct.setCtime(date);
		zhanWaiProduct.setSpid(productListDetail.getSpid());
		zhanWaiProduct.setUrl(productListDetail.getProductUrl());
		zhanWaiProduct.setWebsite(productListDetail.getWebsite());
		StringBuilder sb = new StringBuilder();
		sb.append(zhanWaiProduct.getWebsite()).append(zhanWaiProduct.getSpid()).append(date);
		zhanWaiProduct.setMd5(MD5Utils.EncodeMd5Hex(sb.toString()));
		// zhanWaiProduct.setMd5();
		return zhanWaiProduct;
	};

	public void insertProductListDetails(List<ProductListDetail> productListDetails) {
		if (productListDetails != null && productListDetails.size() > 0) {
			int start = 0;
			int max = productListDetails.size();
			while (start + 200 < max) {
				zhanWaiProductMapper.insertBatchProductListDetails(productListDetails, start, start + 200);
				start += 200;
			}
			zhanWaiProductMapper.insertBatchProductListDetails(productListDetails, start, max);
		}
		// productListDetails.clear();
		// productListDetails = null;
	}

	public void insertProductListDetail(ProductListDetail productListDetail) {
		if (productListDetail != null) {
			zhanWaiProductMapper.insertZhanWaiProduct(zhanWaiProductTranslate(productListDetail));
		}
	}
}
