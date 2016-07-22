package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.xerces.impl.xpath.regex.Match;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.model.UrlType;
import com.tts.zhanwai.service.ZhanWaiProductService;
import com.tts.zhanwai.utils.Constants;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class ZheBaBaiProductListParse extends ProductListParse {
	private static String PAGEURL = "/page/";
	private static String showTaoBaoOnly = "?taobao_only=1";
	private static String website = "www.zhe800.com";
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
	private static Logger logger = LogUtils.getLogger(ZheBaBaiProductListParse.class);
	private Map<String, String> header;
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
		Pattern idsPattern = Pattern.compile("window.setDealIDs[\\s\\S]+?}");
		Matcher matcher = idsPattern.matcher(html);
		String ids = null;
		if (matcher.find()) {
			ids = html.substring(matcher.start() + 28, matcher.end() - 3);
		}
		String jsUrl = "http://status.tuanimg.com/n/deal_service/json_new?deal_ids=" + ids;
		// logger.info(jsUrl);
		String jsResult = AbstractParse.translateReponseHtml(simpleDownloader.startDownload(jsUrl));
		Pattern patternall = Pattern.compile("\\{\"begin_time\"[\\S\\s]*?(\"title\")");
		Matcher matcherall = patternall.matcher(jsResult);
		while (matcherall.find()) {
			ProductListDetail detail = new ProductListDetail();
			detail.setWebsite(website);
			String result = null;
			String body = jsResult.substring(matcherall.start(), matcherall.end());
			Pattern bodyPattern = Pattern.compile("\"taobao_id\":\"\\d+");
			Matcher bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				result = body.substring(bodyMatcher.start() + 13, bodyMatcher.end());
				detail.setSpid(Long.valueOf(result));
			}
			bodyPattern = Pattern.compile("\"url\":\"[\\s\\S]*?(\\?dealId)");
			bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				result = body.substring(bodyMatcher.start() + 7, bodyMatcher.end() - 7);
				detail.setDetailUrl(result);
			} else {
				detail.setDetailUrl("");
			}
			bodyPattern = Pattern.compile(",\"price\":[\\d|.]*");
			bodyMatcher = bodyPattern.matcher(body);
			if (bodyMatcher.find()) {
				result = body.substring(bodyMatcher.start() + 9, bodyMatcher.end());
				detail.setnPrice(Float.valueOf(result));
			}
			if (detail.getSpid() != 0) {
				pageProductListDetails.add(detail);
			}
		}
		// logger.error("productListDetails:{}", pageProductListDetails.size());
		return pageProductListDetails;
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> fristPageList;
		fristPageList = parseHtml(html);
		if (fristPageList != null) {
			productListDetails.addAll(fristPageList);
			fristPageList.clear();
		}
		fristPageList = null;
		// 获取页码数
		Pattern pagePattern = Pattern.compile("<a href=\".+?\".+?>\\d+</a>");
		Matcher pageMatcher = pagePattern.matcher(html);
		String lastPageBody = null;
		int pageCount = 1;
		while (pageMatcher.find()) {
			lastPageBody = html.substring(pageMatcher.start(), pageMatcher.end());
		}
		if (!StringUtils.isEmpty(lastPageBody)) {
			pagePattern = Pattern.compile(">[0-9]*<");
			pageMatcher = pagePattern.matcher(lastPageBody);

			if (pageMatcher.find()) {
				pageCount = Integer.valueOf(lastPageBody.substring(pageMatcher.start() + 1, pageMatcher.end() - 1));
			}
		}
		logger.error("pageCount{}", pageCount);
		for (int i = 2; i <= pageCount && header != null; i++) {
			DownloadType downloadType = new DownloadType();
			downloadType.setCookie(header.get(Constants.COOKIE));
			downloadType.setMethod(Method.GET);
			downloadType.setUrl(getCategory().getCategoryUrl() + PAGEURL + i + showTaoBaoOnly);
			downloadType.setUrlType(UrlType.PRODUCTLIST);
			downloadType.setUser_agent(header.get(Constants.USER_AGETNT));
			downloadType.setReferer(header.get(Constants.REFERE));
			// logger.error(downloadType.getUrl());
			if (getCategory().getCategoryUrl().contains("ertong")) {
				System.out.println("..");
			}
			CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
			List<ProductListDetail> tmProductListDetails = parseHtml(parseResponse(httpResponse, header));
			productListDetails.addAll(tmProductListDetails);
			tmProductListDetails.clear();
			tmProductListDetails = null;
		}
		return productListDetails;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		this.header = header;
		String htmlBody = parseResponse(res, header);
		productListDetails = parseHtmlBody(htmlBody);
		zhanWaiProductService.insertProductListDetails(productListDetails);
		productListDetails.clear();
	}
}
