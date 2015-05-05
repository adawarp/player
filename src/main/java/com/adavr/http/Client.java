/*
 * Copyright (C) 2015 Shotaro Uchida <fantom@xmaker.mx>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.adavr.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Client {

	public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0";
	public static final int DEFAULT_TIMEOUT = 10000;
	public static final int DEFAULT_RETRY = 5;
	public static final String DEFAULT_CHARSET = "Shift_JIS";
	private DefaultHttpClient httpClient;
	private HttpContext localContext;
	private ByteArrayOutputStream byteStream;
	private String contentType;
	private String charset;
	private URL currentURL;

	public Client() {
		httpClient = new DefaultHttpClient(new BasicClientConnectionManager());
		localContext = new BasicHttpContext();
		byteStream = new ByteArrayOutputStream();

		CookieStore cookieStore = new BasicCookieStore();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		httpClient.addRequestInterceptor(new HttpRequestInterceptor() {

			@Override
			public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});

		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

			@Override
			public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					HeaderElement[] codecs = ceheader.getElements();
					for (HeaderElement codec : codecs) {
						if (codec.getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GzipDecompressingEntity(response.getEntity()));
							return;
						}
					}
				}
			}
		});

		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_TIMEOUT);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, DEFAULT_USER_AGENT);
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= DEFAULT_RETRY) {
					// Do not retry if over max retry count
					return false;
				}
				System.out.println(exception.getClass().getName());
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// Do not retry on SSL handshake exception
					return false;
				}
				HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}
		};

		httpClient.setHttpRequestRetryHandler(myRetryHandler);
	}

	private void parseURL(String url) throws IOException {
		if (currentURL == null) {
			currentURL = new URL(url);
		} else {
			currentURL = new URL(currentURL, url);
		}
	}

	public URL getCurrentURL() {
		return currentURL;
	}

	public static String format(String s) {
		return format("", s);
	}

	public static String format(String p, String s) {
		return s.replaceAll("<.*>| |　|	|\n" + p, "");
	}

	public void setUserAgent(String ua) {
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, ua);
	}

	public void setProxy(String host, int port) {
		HttpHost proxy = new HttpHost(host, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	public void setCredentials(AuthScope scope, Credentials c) {
		httpClient.getCredentialsProvider().setCredentials(scope, c);
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void clearCookie() {
		CookieStore store = (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
		store.clear();
	}

	public CookieStore getCookieStore() {
		return (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
	}

	private void executeRequest(HttpUriRequest request) throws ClientException, IOException {
		HttpResponse response = httpClient.execute(request, localContext);
		int code = response.getStatusLine().getStatusCode();
		if (code != HttpStatus.SC_OK) {
			EntityUtils.consume(response.getEntity());
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY) {
				Header header = response.getHeaders("Location")[0];
				String loc = header.getValue();
				System.out.println("Redirect Location: " + loc);
				get(loc);
			} else {
				throw new ClientException("Unknown Response: " + code);
			}
		} else {
			handleResponse(response);
		}
	}

	public void post(String action, UrlEncodedFormEntity entity) throws ClientException {
		try {
			parseURL(action);
			HttpPost post = new HttpPost(currentURL.toURI());
			post.setEntity(entity);
			executeRequest(post);
		} catch (IOException | URISyntaxException ex) {
			throw new ClientException(ex);
		}
	}

	public void get(String url) throws ClientException {
		try {
			parseURL(url);
			HttpGet get = new HttpGet(currentURL.toURI());
			executeRequest(get);
		} catch (IOException | URISyntaxException ex) {
			throw new ClientException(ex);
		}
	}

	private void handleResponse(HttpResponse response) throws IOException {
		byteStream.reset();
		response.getEntity().writeTo(byteStream);
		Header ct = response.getEntity().getContentType();
		setContentType(ct.getValue());
		Matcher m = Pattern.compile("charset=(.+)").matcher(getContentType());
		if (m.find()) {
			setCharset(m.group(1));
		} else {
			setCharset(DEFAULT_CHARSET);
		}
	}

	public Reader getContentAsReader() {
		try {
			return new StringReader(byteStream.toString(charset));
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	public byte[] getContentAsByteArray() {
		return byteStream.toByteArray();
	}

	public Document getJsoupDocument() {
		try {
			return Jsoup.parse(byteStream.toString(charset));
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	public void writeContent(File file) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			byteStream.writeTo(bos);
		}
	}
}