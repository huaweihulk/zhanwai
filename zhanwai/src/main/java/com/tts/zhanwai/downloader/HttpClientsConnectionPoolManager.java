package com.tts.zhanwai.downloader;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

@Component
public class HttpClientsConnectionPoolManager extends PoolingHttpClientConnectionManager {
	
}
