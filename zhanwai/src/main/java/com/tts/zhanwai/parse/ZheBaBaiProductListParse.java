package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.xerces.impl.xpath.regex.Match;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private static String PAGEURL = "http://www.zhe800.com/ju_tag/taoxiangbao/page/";
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
		List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
		Pattern idsPattern = Pattern.compile("window.setDealIDs[\\s\\S]+?}");
		Matcher matcher = idsPattern.matcher(html);
		String ids = null;
		if (matcher.find()) {
			ids = html.substring(matcher.start() + 28, matcher.end() - 1);
		}
		String jsUrl = "http://status.tuanimg.com/n/deal_service/json_new?deal_ids=" + ids;
		String jsResult = AbstractParse.translateReponseHtml(simpleDownloader.startDownload(jsUrl));
		Pattern pattern = Pattern.compile("\"taobao_id\":\"\\d+");
		matcher = pattern.matcher(jsResult);
		String res = null;
		int totalPage = 0;
		while (matcher.find()) {
			res = jsResult.substring(matcher.start() + 13, matcher.end());
			ProductListDetail productListDetail = new ProductListDetail();
			productListDetail.setSpid(Long.valueOf(res));
			productListDetails.add(productListDetail);
		}
		totalPage = productListDetails.size();
		int count = 0;
		pattern = Pattern.compile("\"url\":\"[\\s\\S]*?url=");
		matcher = pattern.matcher(jsResult);
		while (matcher.find() && count < totalPage) {
			res = jsResult.substring(matcher.start() + 7, matcher.end());
			productListDetails.get(count++).setProductUrl(res);
		}
		pattern = Pattern.compile(",\"price\":\\d*");
		count = 0;
		matcher = pattern.matcher(jsResult);
		while (matcher.find() && count < totalPage) {
			res = jsResult.substring(matcher.start() + 9, matcher.end());
			productListDetails.get(count++).setnPrice(Integer.valueOf(res) * 100);
		}
		return productListDetails;
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
		while (pageMatcher.find()) {
			lastPageBody = html.substring(pageMatcher.start(), pageMatcher.end());
		}
		pagePattern = Pattern.compile(">[0-9]*<");
		pageMatcher = pagePattern.matcher(lastPageBody);
		int pageCount = 0;
		if (pageMatcher.find()) {
			pageCount = Integer.valueOf(lastPageBody.substring(pageMatcher.start() + 1, pageMatcher.end() - 1));
		}
		for (int i = 2; i <= pageCount && header != null; i++) {
			DownloadType downloadType = new DownloadType();
			downloadType.setCookie(header.get(Constants.COOKIE));
			downloadType.setMethod(Method.GET);
			downloadType.setUrl(PAGEURL + i);
			downloadType.setUrlType(UrlType.PRODUCTLIST);
			downloadType.setUser_agent(header.get(Constants.USER_AGETNT));
			downloadType.setReferer(header.get(Constants.REFERE));
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
		logger.error(htmlBody);
		productListDetails = parseHtmlBody(htmlBody);
		zhanWaiProductService.insertProductListDetails(productListDetails);
	}
}
