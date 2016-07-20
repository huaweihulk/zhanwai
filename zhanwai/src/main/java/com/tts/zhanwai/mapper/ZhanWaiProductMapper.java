package com.tts.zhanwai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ZhanWaiProductMapper {
	@InsertProvider(type = ZhanWaiProductProvider.class, method = "insertZhanWaiProduct")
	public void insertZhanWaiProduct(@Param(value = "zhanWaiProduct") ZhanWaiProduct zhanWaiProduct);

	@InsertProvider(type = ZhanWaiProductProvider.class, method = "insertBatchZhanwaiProducts")
	public void insertBatchZhanwaiProducts(@Param(value = "zhanWaiProducts") List<ZhanWaiProduct> zhanWaiProducts);
}
