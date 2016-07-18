package com.tts.zhanwai.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tts.zhanwai.model.DownloadType;
import com.tts.zhanwai.model.Method;
import com.tts.zhanwai.model.UrlType;

@Component
public class ClientParse {
	private static SAXReader saxReader = new SAXReader();
	private static Logger logger = LogUtils.getLogger(ClientParse.class);

	public static List<DownloadType> parse() {
		List<DownloadType> types = new ArrayList<DownloadType>();
		InputStream is = ClientParse.class.getClassLoader().getResourceAsStream("downloadClients.xml");
		try {
			Document document = saxReader.read(is);
			Element clients = document.getRootElement();
			for (@SuppressWarnings("unchecked")
			Iterator<Element> client = clients.elementIterator(); client.hasNext();) {
				Element param = client.next();
				DownloadType type = new DownloadType();
				for (@SuppressWarnings("unchecked")
				Iterator<Element> pIt = param.elementIterator(); pIt.hasNext();) {
					Element el = pIt.next();
					String name = el.getName();
					String text = el.getText();
					if (!StringUtils.isEmpty(name)) {
						if (name.equals("url")) {
							type.setUrl(text);
						} else if (name.equals("urltype")) {
							if (!StringUtils.isEmpty(text)) {
								if (text.equals("category")) {
									type.setUrlType(UrlType.CATEGORY);
								} else if (text.equals("productlist")) {
									type.setUrlType(UrlType.PRODUCTLIST);
								} else if (text.equals("productdetail")) {
									type.setUrlType(UrlType.PRODUCTDETAIL);
								} else {

								}
							}
						} else if (name.equals("cookie")) {
							type.setCookie(text);
						} else if (name.equals("method")) {
							if (!StringUtils.isEmpty(text) && text.equals("GET")) {
								type.setMethod(Method.GET);
							} else if (!StringUtils.isEmpty(text) && text.equals("POST")) {
								type.setMethod(Method.POST);
							}
						} else if (name.equals("user_agent")) {
							type.setUser_agent(text);
						} else if (name.equals("referer")) {
							type.setReferer(text);
						} else if (name.equals("paserclass")) {
							type.setPaserclass(text);
						} else {

						}
					}
				}
				types.add(type);
				logger.info("Import {} site for spider", types.size());
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return types;
	}
}
