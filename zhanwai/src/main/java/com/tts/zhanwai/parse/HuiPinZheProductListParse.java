package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.cj.mysqlx.protobuf.MysqlxCrud.Find;
import com.tts.zhanwai.downloader.ProductListDownloader;
import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.utils.Constants;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class HuiPinZheProductListParse extends ProductListParse {
	private static String UrlHeader = "http://www.huipinzhe.com";
	private static Logger logger = LogUtils.getLogger(HuiPinZheProductListParse.class);
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();
	private String category;
	@Autowired
	private ProductListDownloader productListDownloader;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

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
	public List<ProductListDetail> parseHtml(String html) {
		List<ProductListDetail> details = new ArrayList<ProductListDetail>();
		ProductListDetail detail = null;
		String patterUrl = "<li class=\"h-goodsli id_stat_vc\"(\\w|\\s|-|=|\"|\\?|/|:|[\u4E00-\u9FA5]|\\.|!|_|%|\\&|\\^|\\*|\\$|(|)|;)+jpg\">";
		Pattern pattern = Pattern.compile(patterUrl);
		Matcher matcher = pattern.matcher(html);
		int count = 0;
		while (matcher.find()) {
			String data = html.substring(matcher.start(), matcher.end());
			// Pattern dataPattern =
			// Pattern.compile("data-link=\"(\\w|\\?|\\.|/|&|:|\\s|=|\\+|-|;|')+\"");
			Pattern dataPattern = Pattern.compile("data-link=\".+\"");
			Matcher dataMatcher = dataPattern.matcher(data);
			String data_link = dataMatcher.find() ? data.substring(dataMatcher.start() + 11, dataMatcher.end() - 1)
					: "";
			if (!data_link.contains("activity")) {
				detail = new ProductListDetail();
				if (data_link.contains("taobao")) {
					detail.setShopType(Constants.TMALL);
				} else if (data_link.contains("tmall")) {
					detail.setShopType(Constants.TAOBAO);
				}
				detail.setCategory(category);
				detail.setProductUrl(data_link);
				dataPattern = Pattern.compile("data-title=\"(\\w|[\u4E00-\u9FA5]|\\s)+\"");
				dataMatcher = dataPattern.matcher(data);
				detail.setTitle(
						dataMatcher.find() ? data.substring(dataMatcher.start() + 12, dataMatcher.end() - 1) : "");
				// dataPattern =
				// Pattern.compile("data-imgurl=\"(\\w|\\?|\\.|/|&|:|\\s|=|!)+\"");
				dataPattern = Pattern.compile("data-imgurl=\".+\"");
				dataMatcher = dataPattern.matcher(data);
				detail.setPictureUrl(
						dataMatcher.find() ? data.substring(dataMatcher.start() + 13, dataMatcher.end() - 1) : "");
				details.add(detail);
			}
			count++;
		}
		logger.error("details count:{}", count);
		count = 0;
		pattern = Pattern.compile("<b>&yen;</b>\\s{0,}<em>([0-9]|\\.)+\\s{0,}</em>");
		matcher = pattern.matcher(html);
		matcher.find();
		while (matcher.find()) {
			count++;
			String price = html.substring(matcher.start() + 17, matcher.end() - 1);
			// details.get(count++).setnPrice(Float.valueOf(price));
		}
		logger.error("nPrice:{}", count);
		count = 0;
		pattern = Pattern.compile("<del>&yen;([0-9]|\\.)+</del>");
		matcher = pattern.matcher(html);
		while (matcher.find()) {
			String price = html.substring(matcher.start() + 10, matcher.end() - 1);
			// details.get(count++).setoPrice(Float.valueOf(price));
			count++;
		}
		logger.error("oPrice:{}", count);
		count = 0;
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

	public Matcher detailMatcher(String html) {
		Pattern pattern = Pattern.compile("<li class=\"h-goodsli id_stat_vc\"(\\w|\\s|-|=|\"|\\?|/|:|汉字|\\.|!)+jpg\"");
		Matcher matcher = pattern.matcher(html);
		return null;
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
			page = pageMatcher.group(0);
			urlMatcher = urlMatcher(page);
			if (urlMatcher.find()) {
				String url = page.substring(urlMatcher.start() + 6, urlMatcher.end());
				url = UrlHeader + url;
				DownloadType downloadType = new DownloadType();
				// productListDownloader.parseBody()
			}
		}
		return super.parseHtmlBody(html);
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		parseResponse(res, header);
	}
}
