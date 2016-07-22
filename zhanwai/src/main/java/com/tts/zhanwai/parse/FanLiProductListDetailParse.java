package com.tts.zhanwai.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.cache.annotation.Cacheable;

import com.tts.zhanwai.model.ProductListDetail;

@Cacheable
public class FanLiProductListDetailParse extends ProductListParse {
	private String url = "http://9.fanli.com/?cid=3&p=18";
	private List<ProductListDetail> productListDetails = new ArrayList<ProductListDetail>();

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

	@Override
	public List<ProductListDetail> parseHtmlBody(String html) {
		// TODO Auto-generated method stub
		List<ProductListDetail> tmpProductListDetail = new ArrayList<ProductListDetail>();
		tmpProductListDetail = parseHtml(html);

		return super.parseHtmlBody(html);
	}

	@Override
	public void startParse(CloseableHttpResponse res, Map<String, String> header) {
		// TODO Auto-generated method stub
		super.startParse(res, header);
	}
}
