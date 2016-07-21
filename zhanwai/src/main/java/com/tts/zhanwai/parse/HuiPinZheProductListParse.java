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
		Pattern pattern = Pattern.compile("<li class=\"h-goodsli id_stat_vc\"[\\s\\S]+?(</li>)");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			ProductListDetail detail = new ProductListDetail();
			String body = html.substring(matcher.start(), matcher.end());
			Pattern tmpPattern = Pattern.compile("<b>&yen;</b>\\s<em>([0-9]|\\.)+<");
			Matcher tmpMatcher = tmpPattern.matcher(body);
			// 有最新的价格就表示不是活动等
			if (tmpMatcher.find()) {
				String price = body.substring(tmpMatcher.start() + 17, tmpMatcher.end() - 1);
				detail.setnPrice(Float.valueOf(price));
				tmpPattern = Pattern.compile("<a class=\"id_stat_c\" href=\"/redirect/\\d+\"");
				tmpMatcher = tmpPattern.matcher(body);
				if (tmpMatcher.find()) {
					String detailUrl = body.substring(tmpMatcher.start() + 37, tmpMatcher.end() - 1);
					detail.setDetailUrl(UrlHeader + "/redirect/" + detailUrl);
				}

				tmpPattern = Pattern.compile("data-link=\".+\"\\s");
				tmpMatcher = tmpPattern.matcher(body);
				String data_link = tmpMatcher.find() ? body.substring(tmpMatcher.start() + 11, tmpMatcher.end() - 2)
						: "";
				// 排除京东等干扰
				if (data_link.contains("taobao") || data_link.contains("tmall")) {
					tmpPattern = Pattern.compile("\\?id=\\d+");
					tmpMatcher = tmpPattern.matcher(data_link);
					if (tmpMatcher.find()) {
						detail.setSpid(Long.valueOf(data_link.substring(tmpMatcher.start() + 4, tmpMatcher.end())));
					} else {
						String redrictUrl = detail.getDetailUrl();
						// logger.info(redrictUrl);
						if (data_link.contains("taobao")) {
							String page = simpleParseTaobaoId.parseResponse(simpleDownloader.startDownload(redrictUrl));
							detail.setSpid(Long.valueOf(simpleParseTaobaoId.parseHuiPinZheRedictTaoBaoId(page)));
						} else {
							String page1 = simpleParseTaobaoId
									.parseResponse(simpleDownloader.startDownload(redrictUrl));
							detail.setSpid(Long.valueOf(simpleParseTaobaoId.parseHuiPinZheRedictTaoBaoId(page1)));
						}
					}
					details.add(detail);
				}
			}
		}
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
		logger.error(productListDetails.size() + "");
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
		productListDetails.clear();
	}
}
