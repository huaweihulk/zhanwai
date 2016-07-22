package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.model.UrlType;
import com.tts.zhanwai.service.ZhanWaiProductService;
import com.tts.zhanwai.utils.Constants;
import com.tts.zhanwai.utils.LogUtils;

@Cacheable
public class FanLiProductListDetailParse extends ProductListParse {
	private String url = "http://9.fanli.com/?cid=3&p=18";
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
	private static final String pageQuery = "&page=";
	private Map<String, String> header;
	private static final Logger loger = LogUtils.getLogger(FanLiProductListDetailParse.class);
	@Autowired
	private ProductListDownloader productListDownloader;
	@Autowired
	private SimpleDownloader simpleDownloader;
	@Autowired
	private SimpleParseTaobaoId simpleParseTaobaoId;
	@Autowired
	private ZhanWaiProductService zhanWaiProductService;

	@Override
	public List<ProductListDetail> parseHtml(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> pageProductListDetails = new ArrayList<ProductListDetail>();
		Pattern htmlPattern = Pattern.compile("<div class=\"nine-item[\\s\\S]*?(<div class=\"amount\">)");
		Matcher htmlMatcher = htmlPattern.matcher(html);
		while (htmlMatcher.find()) {
			ProductListDetail productListDetail = new ProductListDetail();
			String body = html.substring(htmlMatcher.start(), htmlMatcher.end());
			Pattern bodyPatter = Pattern.compile("<a href=\"[\\s\\S]*?(\")");
			Matcher bodyMatcher = bodyPatter.matcher(body);
			if (bodyMatcher.find()) {
				String detailUrl = body.substring(bodyMatcher.start() + 9, bodyMatcher.end() - 1);
				productListDetail.setDetailUrl(detailUrl);
			}
			bodyPatter = Pattern.compile("&pid=\\d+");
			bodyMatcher = bodyPatter.matcher(body);
			if (bodyMatcher.find()) {
				String spid = body.substring(bodyMatcher.start() + 5, bodyMatcher.end());
				productListDetail.setSpid(Long.valueOf(spid));
			}
			bodyPatter = Pattern.compile("<strong>\\d*<em>(\\.)*\\d*</em></strong>");
			bodyMatcher = bodyPatter.matcher(body);
			if (bodyMatcher.find()) {
				String price = body.substring(bodyMatcher.start(), bodyMatcher.end());
				bodyPatter = Pattern.compile("\\.*\\d*");
				bodyMatcher = bodyPatter.matcher(price);
				StringBuilder priceBuilder = new StringBuilder();
				while (bodyMatcher.find()) {
					priceBuilder.append(price.substring(bodyMatcher.start(), bodyMatcher.end()));
				}
				productListDetail.setnPrice(Float.valueOf(priceBuilder.toString()));
			}
			if (productListDetail.getSpid() != 0) {
				pageProductListDetails.add(productListDetail);
			}
		}
		return pageProductListDetails;
	}

	public boolean hasDataShow(String html) {
		Pattern htmlPattern = Pattern.compile("<div class=\"nine-item[\\s\\S]*?(<div class=\"amount\">)");
		Matcher htmlMatcher = htmlPattern.matcher(html);
		return htmlMatcher.find();
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> tmpProductListDetail = new ArrayList<ProductListDetail>();
		tmpProductListDetail = parseHtml(html);
		int pageCount = 2;
		while (hasDataShow(html)) {
			DownloadType downloadType = new DownloadType();
			downloadType.setCookie(header.get(Constants.COOKIE));
			downloadType.setMethod(Method.GET);
			downloadType.setUrl(getCategory().getCategoryUrl() + pageQuery + pageCount);
			downloadType.setUrlType(UrlType.PRODUCTLIST);
			downloadType.setUser_agent(header.get(Constants.USER_AGETNT));
			downloadType.setReferer(header.get(Constants.REFERE));
			// logger.error(downloadType.getUrl());

			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			html = parseResponse(httpResponse, header);
			List<ProductListDetail> tmProductListDetails = parseHtml(html);
			productListDetails.addAll(tmProductListDetails);
			tmProductListDetails.clear();
			tmProductListDetails = null;
			pageCount++;
		}
		return productListDetails;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		super.startParse(res, header);
	}
}
