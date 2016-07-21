package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mysql.cj.mysqlx.protobuf.MysqlxCrud.Find;
import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.model.UrlType;
import com.tts.zhanwai.service.ZhanWaiProductService;
import com.tts.zhanwai.utils.Constants;
import com.tts.zhanwai.utils.LogUtils;
import com.tts.zhanwai.utils.TaoBaoJumpUrlUtils;

@Component
public class HuiPinZheProductListParse extends ProductListParse {
	private static String UrlHeader = "http://www.huipinzhe.com";
	private static Logger logger = LogUtils.getLogger(HuiPinZheProductListParse.class);
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
	private Map<String, String> header;
	@Autowired
	private ProductListDownloader productListDownloader;
	@Autowired
	private SimpleDownloader simpleDownloader;
	@Autowired
	private SimpleParseTaobaoId simpleParseTaobaoId;
	@Autowired
	private ZhanWaiProductService zhanWaiProductService;

	public HuiPinZheProductListParse() {
		super();
	}

	public HuiPinZheProductListParse(CloseableHttpResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 单独解析界面，防止对包含多页面的获取 因为有些链接是活动页面，所以解析出来的url中如果有就抛弃
	 * 
	 * @param html
	 * @return
	 */
	@Override
	public List<ProductListDetail> parseHtml(String html) {
		List<ProductListDetail> details = new ArrayList<ProductListDetail>();
		ProductListDetail detail = null;
		String patterUrl = "<li class=\"h-goodsli id_stat_vc\".*";
		Pattern pattern = Pattern.compile(patterUrl);
		Matcher matcher = pattern.matcher(html);
		int count = 0;
		while (matcher.find()) {
			String data = html.substring(matcher.start(), matcher.end());
			// Pattern dataPattern =
			// Pattern.compile("data-link=\"(\\w|\\?|\\.|/|&|:|\\s|=|\\+|-|;|')+\"");
			Pattern dataPattern = Pattern.compile("data-link=\".+\"\\s");
			Matcher dataMatcher = dataPattern.matcher(data);
			String data_link = dataMatcher.find() ? data.substring(dataMatcher.start() + 11, dataMatcher.end() - 2)
					: "";
			if (!data_link.contains("activity")) {
				detail = new ProductListDetail();
				if (data_link.contains("taobao")) {
					detail.setShopType(Constants.TMALL);
				} else if (data_link.contains("tmall")) {
					detail.setShopType(Constants.TAOBAO);
				} else if (data_link.contains("jd.com")) {
					detail.setShopType(Constants.JD);
				} else if (data_link.contains("huipinzhe")) {
					detail.setShopType(Constants.HUIPIZHE);
				} else {
					detail.setShopType(Constants.OTHERSHOP);
				}
				detail.setCategory(getCategoryName());
				detail.setProductUrl(data_link);
				// 解析淘宝id，有些链接是乱码
				if (detail.getShopType().equals(Constants.TMALL) || detail.getShopType().equals(Constants.TAOBAO)) {
					dataPattern = Pattern.compile("\\?id=\\d+");
					dataMatcher = dataPattern.matcher(data_link);
					if (dataMatcher.find()) {
						detail.setSpid(Long.valueOf(data_link.substring(dataMatcher.start() + 4, dataMatcher.end())));
					}
				}
				dataPattern = Pattern.compile("data-title=\"(\\w|[\u4E00-\u9FA5]|\\s)+\"");
				dataMatcher = dataPattern.matcher(data);
				detail.setTitle(
						dataMatcher.find() ? data.substring(dataMatcher.start() + 12, dataMatcher.end() - 1) : "");
				dataPattern = Pattern.compile("data-imgurl=\".+\"");
				dataMatcher = dataPattern.matcher(data);
				detail.setPictureUrl(
						dataMatcher.find() ? data.substring(dataMatcher.start() + 13, dataMatcher.end() - 1) : "");
				details.add(detail);
				count++;
			}
		}
		logger.info("details count:{}", count);
		count = 0;
		pattern = Pattern.compile("<b>&yen;</b>\\s<em>([0-9]|\\.)+<");
		matcher = pattern.matcher(html);
		while (matcher.find() && count < details.size()) {
			String price = html.substring(matcher.start() + 17, matcher.end() - 1);
			details.get(count++).setnPrice(Float.valueOf(price));
		}
		logger.info("nPrice:{}", count);
		count = 0;
		pattern = Pattern.compile("<del>&yen;([0-9]|\\.)+<");
		matcher = pattern.matcher(html);
		while (matcher.find() && count < details.size()) {
			String price = html.substring(matcher.start() + 10, matcher.end() - 1);
			details.get(count++).setoPrice(Float.valueOf(price));
		}
		logger.info("oPrice:{}", count);
		count = 0;
		pattern = Pattern.compile("<a class=\"gn id_stat_c\" href=\"/p/detail\\?id=\\d+\"");
		matcher = pattern.matcher(html);
		while (matcher.find() && count < details.size()) {
			String detailUrl = html.substring(matcher.start() + 43, matcher.end() - 1);
			details.get(count++).setDetailUrl(UrlHeader + "/p/detail?id=" + detailUrl);
		}
		logger.info("detailurl:{}", count);
		count = 0;
		Map<Integer, Integer> spidZero = new HashMap<Integer, Integer>();
		for (int i = 0; i < details.size(); i++) {
			if (details.get(i).getSpid() == 0L) {
				spidZero.put(i, i);
			}
		}
		pattern = Pattern.compile(
				"<a class=\"id_stat_c\" href=\"(\\w|/)+\" target=\"_blank\" rel=\"nofollow\">[\u4E00-\u9FA5]+</a>");
		matcher = pattern.matcher(html);
		count = 0;
		while (matcher.find()) {
			if (spidZero.containsKey(count)) {
				detail = details.get(spidZero.get(count));
				String href = html.substring(matcher.start(), matcher.end());
				Pattern tmpPattern = Pattern.compile("href=\"/.+/.+?\"");
				Matcher tmpMatcher = tmpPattern.matcher(href);
				if (tmpMatcher.find()) {
					String redrictUrl = UrlHeader + href.substring(tmpMatcher.start() + 6, tmpMatcher.end() - 1);
					// logger.error(redrictUrl);
					if (detail.getProductUrl().contains("taobao")) {
						String page = simpleParseTaobaoId.parseResponse(simpleDownloader.startDownload(redrictUrl));
						detail.setSpid(Long.valueOf(simpleParseTaobaoId.parseHuiPinZheRedictTaoBaoId(page)));
						// logger.error(detail.getSpid() + "");
					} else if (detail.getProductUrl().contains("tmall")) {
						String page1 = simpleParseTaobaoId.parseResponse(simpleDownloader.startDownload(redrictUrl));
						detail.setSpid(Long.valueOf(simpleParseTaobaoId.parseHuiPinZheRedictTaoBaoId(page1)));
					}
					// logger.error(detail.getSpid() + "");
				}
			}
			count++;
		}
		logger.info("redirect:{}", count);
		return details;
	}

	public Matcher pageMatcher(String html) {
		Pattern pagePattern = Pattern.compile("<a\\s+href=\"/\\w+\\?\\w+=\\d+\"\\s+class=\"\\s+\">\\d+</a>");
		Matcher matcher = pagePattern.matcher(html);
		return matcher;
	}

	public Matcher urlMatcher(String html) {
		Pattern urlMatcher = Pattern.compile("href=\"/\\w+\\?\\w+=\\d+");
		Matcher matcher = urlMatcher.matcher(html);
		return matcher;
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> tmpDetails = parseHtml(html);
		if (tmpDetails != null) {
			productListDetails.addAll(tmpDetails);
		}

		String page = null;
		Matcher pageMatcher = pageMatcher(html);
		Matcher urlMatcher = null;
		while (pageMatcher.find()) {
			page = pageMatcher.group();
			urlMatcher = urlMatcher(page);
			if (urlMatcher.find()) {
				String url = page.substring(urlMatcher.start() + 6, urlMatcher.end());
				url = UrlHeader + url;
				DownloadType downloadType = new DownloadType();
				if (header != null) {
					downloadType.setCookie(header.get(Constants.COOKIE));
					downloadType.setMethod(Method.GET);
					downloadType.setUrl(url);
					downloadType.setUrlType(UrlType.PRODUCTLIST);
					downloadType.setUser_agent(header.get(Constants.USER_AGETNT));
					downloadType.setReferer(header.get(Constants.REFERE));
				}
				CloseableHttpResponse httpResponse = productListDownloader.startDownload(downloadType);
				List<ProductListDetail> tmProductListDetails = parseHtml(parseResponse(httpResponse, header));
				productListDetails.addAll(tmProductListDetails);
				tmProductListDetails.clear();
				tmProductListDetails = null;
			}
		}
		for (ProductListDetail detail : productListDetails) {
			detail.setSales((detail.getnPrice() * 10) / detail.getoPrice());
			detail.setWebsite("www.huipinzhe.com");
		}
		return this.productListDetails;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		this.header = header;
		String htmlBody = parseResponse(res, header);
		productListDetails = parseHtmlBody(htmlBody);
		// logger.error(productListDetails.get(productListDetails.size() -
		// 1).toString());
		// zhanWaiProductService.insertProductListDetails(productListDetails);
		zhanWaiProductService.insertProductListDetails(productListDetails);
	}
}
