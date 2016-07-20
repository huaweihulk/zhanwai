package com.tts.zhanwai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tts.zhanwai.mapper.ZhanWaiProduct;
import com.tts.zhanwai.mapper.ZhanWaiProductMapper;
import com.tts.zhanwai.model.ProductListDetail;

@Component
public class ZhanWaiProductService {
	@Autowired
	private ZhanWaiProductMapper zhanWaiProductMapper;

	private ZhanWaiProduct zhanWaiProductTranslate(ProductListDetail productListDetail) {
		ZhanWaiProduct zhanWaiProduct = new ZhanWaiProduct();
		zhanWaiProduct.setPrice(String.valueOf(productListDetail.getnPrice()));
		// zhanWaiProduct.setMd5();
		return zhanWaiProduct;
	};

	public void insertProductListDetails(List<ProductListDetail> productListDetails) {
		if (productListDetails != null && productListDetails.size() > 0) {

		}

	}

	public void insertProductListDetail(ProductListDetail productListDetail) {
		if (productListDetail != null) {
			ZhanWaiProduct zhanWaiProduct = new ZhanWaiProduct();

			zhanWaiProductMapper.insertZhanWaiProduct(zhanWaiProduct);
		}
	}
}
