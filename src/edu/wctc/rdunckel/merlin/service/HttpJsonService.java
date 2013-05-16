package edu.wctc.rdunckel.merlin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpJsonService {

	private HttpHost targetHost;
	private DefaultHttpClient httpClient;

	public HttpJsonService(String host) {
		httpClient = new DefaultHttpClient();
		targetHost = new HttpHost(host);
	}

	public String getJson(HttpGet httpGet) throws ClientProtocolException,
			IOException {
		httpGet.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		HttpResponse response = httpClient.execute(targetHost, httpGet);
		HttpEntity entity = response.getEntity();

		inputStream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		result = sb.toString();

		return result;
	}

}