package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.generic.NEW;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mysql.cj.api.log.Log;
import com.tts.zhanwai.downloader.SimpleDownloader;
import com.tts.zhanwai.model.ProductListDetail;
import com.tts.zhanwai.service.ZhanWaiProductService;
import com.tts.zhanwai.utils.LogUtils;

@Component
public class JuHuaSuanProductListParse extends ProductListParse {
	private List<ProductListDetail> juHuaSuanProductListDetails = new ArrayList<ProductListDetail>();
	private String stringFormater = "https://ju.taobao.com/json/jusp2/ajaxGetTpFloor.json?urlKey=%s&floorIndex=%d&pc=true";
	private static final Logger logger = LogUtils.getLogger(JuHuaSuanProductListParse.class);
	private static final String websit = "ju.taobao.com";
	@Autowired
	private SimpleParseTaobaoId simpleParseTaobaoId;
	@Autowired
	private SimpleDownloader simpleDownloader;
	@Autowired
	private ZhanWaiProductService zhanWaiProductService;
	private Map<String, String> header;

	private boolean hasDateShow(String html) {
		return !html.contains("\"code\":\"301\"");
	}

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		String categoryUrl = getCategory().getCategoryUrl();
		String category = null;
		int floorIndex = 1;
		Pattern categoryPattern = Pattern.compile("jusp/\\w+?(/)");
		Matcher categoryMatcher = categoryPattern.matcher(categoryUrl);
		if (categoryMatcher.find()) {
			category = categoryUrl.substring(categoryMatcher.start() + 5, categoryMatcher.end() - 1);
		}
		String jsonUrl = String.format(stringFormater, category, floorIndex++);
		String htmlBody = simpleParseTaobaoId.parseResponse(simpleDownloader.startDownload(jsonUrl));
		while (hasDateShow(htmlBody)) {
			//logger.info(jsonUrl);
			juHuaSuanProductListDetails.addAll(parseHtml(htmlBody));
			jsonUrl = String.format(stringFormater, category, floorIndex++);
			htmlBody = simpleParseTaobaoId.parseResponse(simpleDownloader.startDownload(jsonUrl));
		}
		// logger.info("juHuaSuanProductListDetails size{}",
		// juHuaSuanProductListDetails.size());
		return juHuaSuanProductListDetails;
	}

	@Override
	public List<ProductListDetail> parseHtml(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> pageProductListDetails = new ArrayList<ProductListDetail>();
		Pattern htmlPattern = Pattern.compile("\"baseinfo\"[\\s\\S]*?(\"remind\")");
		Matcher htmlMatcher = htmlPattern.matcher(html);
		while (htmlMatcher.find()) {
			ProductListDetail productListDetail = new ProductListDetail();
			productListDetail.setWebsite(websit);
			String body = html.substring(htmlMatcher.start(), htmlMatcher.end());
			String spid = null;
			String url = null;
			String price = null;
			Pattern bodyPattern = Pattern.compile("actPrice\":\"\\d*\\.*\\d*?(\")");
			Matcher bodymatcher = bodyPattern.matcher(body);
			if (bodymatcher.find()) {
				price = body.substring(bodymatcher.start() + 11, bodymatcher.end() - 1);
				productListDetail.setnPrice(Float.valueOf(price));
			}
			bodyPattern = Pattern.compile("itemUrl\":\"[\\s\\S]*?(\")");
			bodymatcher = bodyPattern.matcher(body);
			if (bodymatcher.find()) {
				url = "http:" + body.substring(bodymatcher.start() + 11, bodymatcher.end() - 1);
				productListDetail.setDetailUrl(url);
			}
			bodyPattern = Pattern.compile("item_id=\\d+");
			bodymatcher = bodyPattern.matcher(url);
			if (bodymatcher.find()) {
				spid = url.substring(bodymatcher.start() + 8, bodymatcher.end());
				productListDetail.setSpid(Long.valueOf(spid));
			}
			if (productListDetail.getSpid() != 0) {
				pageProductListDetails.add(productListDetail);
			}
		}
		// logger.info("pageProductListDetails size{}",
		// pageProductListDetails.size());
		return pageProductListDetails;
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub

		this.header = header;
		String htmlBody = parseResponse(res, header);
		parseHtmlBody(htmlBody);
		zhanWaiProductService.insertProductListDetails(juHuaSuanProductListDetails);
		juHuaSuanProductListDetails.clear();
	}

}
